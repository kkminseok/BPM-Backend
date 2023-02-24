package d83t.bpmbackend.domain.aggregate.community.service;

import d83t.bpmbackend.domain.aggregate.community.dto.BodyShapeRequest;
import d83t.bpmbackend.domain.aggregate.community.dto.BodyShapeResponse;
import d83t.bpmbackend.domain.aggregate.community.entity.BodyShape;
import d83t.bpmbackend.domain.aggregate.community.entity.BodyShapeImage;
import d83t.bpmbackend.domain.aggregate.community.repository.BodyShapeRepository;
import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import d83t.bpmbackend.domain.aggregate.user.repository.UserRepository;
import d83t.bpmbackend.exception.CustomException;
import d83t.bpmbackend.exception.Error;
import d83t.bpmbackend.s3.S3UploaderService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BodyShapeServiceImpl implements BodyShapeService {

    private final UserRepository userRepository;
    private final S3UploaderService uploaderService;
    private final BodyShapeRepository bodyShapeRepository;

    @Value("${bpm.s3.bucket.bodyshape.path}")
    private String bodyShapePath;

    @Value("${spring.environment}")
    private String env;

    private String fileDir;

    @PostConstruct
    private void init() {
        if (env.equals("local")) {
            this.fileDir = getUploadPath();
        } else if (env.equals("prod")) {
            this.fileDir = this.bodyShapePath;
        }
    }

    @Override
    @Transactional
    public BodyShapeResponse createBoastArticle(User user, List<MultipartFile> files, BodyShapeRequest bodyShapeRequest) {
        //file은 최대 5개만 들어올 수 있다.
        if(files.size() > 5){
            throw new CustomException(Error.FILE_SIZE_MAX);
        }

        Optional<User> findUser = userRepository.findByKakaoId(user.getKakaoId());
        if (findUser.isEmpty()) {
            throw new CustomException(Error.NOT_FOUND_USER_ID);
        }
        List<String> filePaths = new ArrayList<>();

        Profile profile = findUser.get().getProfile();
        BodyShape bodyshape = BodyShape.builder()
                .author(profile)
                .content(bodyShapeRequest.getContent())
                .build();

        for (MultipartFile file : files) {
            String newName = createNewFileName(file.getOriginalFilename());
            String filePath = fileDir + newName;
            bodyshape.addBodyShapeImage(BodyShapeImage.builder()
                    .originFileName(newName)
                    .storagePathName(filePath)
                    .build());
            filePaths.add(filePath);
            if (env.equals("prod")) {
                uploaderService.putS3(file, bodyShapePath, newName);
            } else if (env.equals("local")) {
                try {
                    File localFile = new File(filePath);
                    file.transferTo(localFile);
                    removeNewFile(localFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        bodyShapeRepository.save(bodyshape);

        return BodyShapeResponse.builder()
                .createdAt(bodyshape.getCreatedDate())
                .author(BodyShapeResponse.Author.builder()
                        .nickname(profile.getNickName())
                        .profilePath(profile.getStoragePathName())
                        .build())
                .updatedAt(bodyshape.getModifiedDate())
                .filesPath(filePaths)
                .content(bodyshape.getContent())
                .build();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("file delete success");
            return;
        }
        log.info("file delete fail");
    }

    private String getUploadPath() {
        String path = new File("").getAbsolutePath() + "/" + "bodyShapes/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    private String createNewFileName(String originalName) {
        return UUID.randomUUID() + "." + originalName.substring(originalName.lastIndexOf("."));
    }
}
