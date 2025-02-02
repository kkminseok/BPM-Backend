package d83t.bpmbackend.domain.aggregate.lounge.bodyShape.service;

import d83t.bpmbackend.domain.aggregate.lounge.bodyShape.dto.BodyShapeRequest;
import d83t.bpmbackend.domain.aggregate.lounge.bodyShape.dto.BodyShapeResponse;
import d83t.bpmbackend.domain.aggregate.lounge.bodyShape.entity.BodyShape;
import d83t.bpmbackend.domain.aggregate.lounge.bodyShape.entity.BodyShapeImage;
import d83t.bpmbackend.domain.aggregate.lounge.bodyShape.repository.BodyShapeRepository;
import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import d83t.bpmbackend.domain.aggregate.profile.repository.ProfileRepository;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import d83t.bpmbackend.domain.aggregate.user.repository.UserRepository;
import d83t.bpmbackend.exception.CustomException;
import d83t.bpmbackend.exception.Error;
import d83t.bpmbackend.s3.S3UploaderService;
import d83t.bpmbackend.utils.FileUtils;
import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BodyShapeServiceImpl implements BodyShapeService {

    private final UserRepository userRepository;
    private final S3UploaderService uploaderService;
    private final BodyShapeRepository bodyShapeRepository;
    private final ProfileRepository profileRepository;

    @Value("${bpm.s3.bucket.bodyshape.path}")
    private String bodyShapePath;

    @Value("${bpm.s3.bucket.base}")
    private String basePath;

    @Value("${spring.environment}")
    private String env;

    private String fileDir;

    @PostConstruct
    private void init() {
        if (env.equals("local")) {
            this.fileDir = FileUtils.getUploadPath();
        } else if (env.equals("prod")) {
            this.fileDir = this.basePath + this.bodyShapePath;
        }
    }

    @Override
    public BodyShapeResponse createBoastArticle(User user, List<MultipartFile> files, BodyShapeRequest bodyShapeRequest) {
        //file은 최대 5개만 들어올 수 있다.
        if (files != null && files.size() > 5) {
            throw new CustomException(Error.FILE_SIZE_MAX);
        }

        User findUser = userRepository.findByKakaoId(user.getKakaoId()).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_USER_ID);
        });

        Profile profile = findUser.getProfile();
        BodyShape bodyshape = BodyShape.builder()
                .author(profile)
                .content(bodyShapeRequest.getContent())
                .build();

        List<String> filePaths = new ArrayList<>();
        if(files != null && files.size() != 0) {
            for (MultipartFile file : files) {
                String newName = FileUtils.createNewFileName(file.getOriginalFilename());
                String filePath = fileDir + newName;
                bodyshape.addBodyShapeImage(BodyShapeImage.builder()
                        .originFileName(newName)
                        .storagePathName(filePath)
                        .bodyShape(bodyshape)
                        .build());
                filePaths.add(filePath);
                if (env.equals("prod")) {
                    uploaderService.putS3(file, bodyShapePath, newName);
                } else if (env.equals("local")) {
                    try {
                        File localFile = new File(filePath);
                        file.transferTo(localFile);
                        FileUtils.removeNewFile(localFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        bodyShapeRepository.save(bodyshape);

        return BodyShapeResponse.builder()
                .id(bodyshape.getId())
                .createdAt(bodyshape.getCreatedDate())
                .author(BodyShapeResponse.Author.builder()
                        .id(profile.getId())
                        .nickname(profile.getNickName())
                        .profilePath(profile.getStoragePathName())
                        .build())
                .updatedAt(bodyshape.getModifiedDate())
                .filesPath(filePaths)
                .content(bodyshape.getContent())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BodyShapeResponse> getBodyShapes(User user, Integer limit, Integer offset) {
        List<BodyShape> bodyShapes = new ArrayList<>();
        limit = limit == null ? 20 : limit;
        offset = offset == null ? 0 : offset;
        Pageable pageable = PageRequest.of(offset, limit);
        Long profileId = user.getProfile().getId();

        Optional<Profile> findProfile = profileRepository.findById(profileId);
        Profile profile = findProfile.get();
        bodyShapes = bodyShapeRepository.findByNickName(pageable, profile.getNickName());

        return bodyShapes.stream().map(bodyShape -> {
            return BodyShapeResponse.builder()
                    .id(bodyShape.getId())
                    .content(bodyShape.getContent())
                    .createdAt(bodyShape.getCreatedDate())
                    .updatedAt(bodyShape.getModifiedDate())
                    .filesPath(bodyShape.getImages().stream().map(images -> {
                        return images.getStoragePathName();
                    }).collect(Collectors.toList()))
                    .author(BodyShapeResponse.Author.builder()
                            .id(bodyShape.getAuthor().getId())
                            .nickname(bodyShape.getAuthor().getNickName())
                            .profilePath(bodyShape.getAuthor().getStoragePathName())
                            .build())
                    .build();
        }).collect(Collectors.toList());
    }

    //TODO: 향후 public, private 구분이 되는 기능이 추가될 시 Response를 바꿔야함.
    @Override
    public BodyShapeResponse getBodyShape(User user, Long bodyShapeId) {
        BodyShape bodyShape = bodyShapeRepository.findById(bodyShapeId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_BODY_SHAPE);
        });
        List<String> filePaths = new ArrayList<>();
        Profile author = bodyShape.getAuthor();
        List<BodyShapeImage> images = bodyShape.getImages();
        for (BodyShapeImage image : images) {
            filePaths.add(image.getStoragePathName());
        }
        return BodyShapeResponse.builder()
                .id(bodyShape.getId())
                .createdAt(bodyShape.getCreatedDate())
                .author(BodyShapeResponse.Author.builder()
                        .id(author.getId())
                        .nickname(author.getNickName())
                        .profilePath(author.getStoragePathName())
                        .build())
                .updatedAt(bodyShape.getModifiedDate())
                .filesPath(filePaths)
                .content(bodyShape.getContent())
                .build();
    }

    //TODO: 향후 public에 대한 대처가 필요.
    @Override
    public BodyShapeResponse updateBodyShape(User user, List<MultipartFile> files, BodyShapeRequest bodyShapeRequest, Long bodyShapeId) {
        if (files != null && files.size() > 5) {
            throw new CustomException(Error.FILE_SIZE_MAX);
        }
        User findUser = userRepository.findByKakaoId(user.getKakaoId()).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_USER_ID);
        });

        BodyShape bodyShape = bodyShapeRepository.findById(bodyShapeId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_BODY_SHAPE);
        });

        if (bodyShapeRequest.getContent() != null) {
            bodyShape.changeContent(bodyShapeRequest.getContent());
        }

        List<BodyShapeImage> bodyShapeImages = bodyShape.getImages();
        List<String> filePaths = bodyShapeImages.stream()
                .map(BodyShapeImage::getStoragePathName)
                .collect(Collectors.toList());

        //파일 수정
        if (files != null && files.size() != 0) {
            filePaths = new ArrayList<String>();
            List<BodyShapeImage> updateBodyShapeImages = new ArrayList<>();
            for (MultipartFile file : files) {
                String newName = FileUtils.createNewFileName(file.getOriginalFilename());
                String filePath = fileDir + newName;

                updateBodyShapeImages.add(BodyShapeImage.builder()
                        .originFileName(newName)
                        .storagePathName(filePath)
                        .bodyShape(bodyShape)
                        .build());

                filePaths.add(filePath);
                if (env.equals("prod")) {
                    uploaderService.putS3(file, bodyShapePath, newName);
                } else if (env.equals("local")) {
                    try {
                        File localFile = new File(filePath);
                        file.transferTo(localFile);
                        FileUtils.removeNewFile(localFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            bodyShape.changeImage(updateBodyShapeImages);
        }

        Profile profile = findUser.getProfile();

        return BodyShapeResponse.builder()
                .id(bodyShape.getId())
                .createdAt(bodyShape.getCreatedDate())
                .updatedAt(bodyShape.getModifiedDate())
                .author(BodyShapeResponse.Author.builder()
                        .id(profile.getId())
                        .nickname(profile.getNickName())
                        .profilePath(profile.getStoragePathName())
                        .build())
                .filesPath(filePaths)
                .content(bodyShape.getContent())
                .build();
    }

    //TODO: 마찬가지로 향후 public이 생길시 작성 유저인지 판단하는 로직 추가
    @Override
    public void deleteBodyShape(User user, Long bodyShapeId) {
        Optional<BodyShape> findBodyShape = bodyShapeRepository.findById(bodyShapeId);
        if (findBodyShape.isEmpty()) {
            throw new CustomException(Error.NOT_FOUND_BODY_SHAPE);
        }
        BodyShape bodyShape = findBodyShape.get();
        bodyShapeRepository.delete(bodyShape);
    }

}
