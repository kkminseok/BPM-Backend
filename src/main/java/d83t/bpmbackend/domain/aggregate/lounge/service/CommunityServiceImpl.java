package d83t.bpmbackend.domain.aggregate.lounge.service;

import d83t.bpmbackend.base.report.dto.ReportDto;
import d83t.bpmbackend.base.report.repository.ReportRepository;
import d83t.bpmbackend.domain.aggregate.lounge.dto.CommunityRequestDto;
import d83t.bpmbackend.domain.aggregate.lounge.dto.CommunityResponseDto;
import d83t.bpmbackend.domain.aggregate.lounge.entity.Community;
import d83t.bpmbackend.domain.aggregate.lounge.entity.CommunityImage;
import d83t.bpmbackend.domain.aggregate.lounge.entity.Report;
import d83t.bpmbackend.domain.aggregate.lounge.repository.CommunityFavoriteRepository;
import d83t.bpmbackend.domain.aggregate.lounge.repository.CommunityRepository;
import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import d83t.bpmbackend.domain.aggregate.user.repository.UserRepository;
import d83t.bpmbackend.exception.CustomException;
import d83t.bpmbackend.exception.Error;
import d83t.bpmbackend.s3.S3UploaderService;
import d83t.bpmbackend.utils.FileUtils;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final S3UploaderService uploaderService;
    private final CommunityFavoriteRepository communityFavoriteRepository;
    private final ReportRepository reportRepository;

    @Value("${bpm.s3.bucket.story.path}")
    private String storyPath;

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
            this.fileDir = this.basePath + this.storyPath;
        }
    }

    @Override
    @Transactional
    public CommunityResponseDto createCommunity(CommunityRequestDto requestDto, List<MultipartFile> files, User user) {
        if (files == null || files.isEmpty()) {
            throw new CustomException(Error.FILE_REQUIRED);
        }
        if (files.size() > 5) {
            throw new CustomException(Error.FILE_SIZE_MAX);
        }

        List<String> filePaths = new ArrayList<>();
        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));
        Profile profile = findUser.getProfile();

        Community community = requestDto.toEntity(profile);

        for (MultipartFile file : files) {
            String newName = FileUtils.createNewFileName(file.getOriginalFilename());
            String filePath = fileDir + newName;

            community.addCommunityImage(CommunityImage.builder()
                    .originFileName(newName)
                    .storagePathName(filePath)
                    .community(community)
                    .build());
            filePaths.add(filePath);

            if (env.equals("prod")) {
                uploaderService.putS3(file, storyPath, newName);
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

        Community savedStory = communityRepository.save(community);

        return new CommunityResponseDto(savedStory, false);
    }

    @Override
    public List<CommunityResponseDto> getAllCommunity(int page, int size, String sort, User user) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<Community> stories = communityRepository.findAll(pageable);

        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));

        return stories.stream().map(story -> new CommunityResponseDto(story, checkStoryLiked(story.getId(), findUser))).collect(Collectors.toList());
    }

    @Override
    public CommunityResponseDto getCommunity(Long communityId, User user) {
        Community story = communityRepository.findById(communityId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_COMMUNITY));

        boolean isLiked = checkStoryLiked(communityId, user);
        return new CommunityResponseDto(story, isLiked);
    }

    @Override
    @Transactional
    public CommunityResponseDto updateCommunity(Long communityId, CommunityRequestDto communityRequestDto, List<MultipartFile> files, User user) {
        if (files.size() > 5) {
            throw new CustomException(Error.FILE_SIZE_MAX);
        }

        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_COMMUNITY));

        // 작성자 검증
        if (!community.getAuthor().getId().equals(findUser.getProfile().getId())) {
            throw new CustomException(Error.NOT_MATCH_USER);
        }

        if (communityRequestDto.getContent() != null) {
            community.setContent(communityRequestDto.getContent());
        }

        List<String> filePaths = new ArrayList<>();
        List<CommunityImage> communityImages = new ArrayList<>();
        for (MultipartFile file : files) {
            String newName = FileUtils.createNewFileName(file.getOriginalFilename());
            String filePath = fileDir + newName;

            communityImages.add(CommunityImage.builder()
                    .originFileName(newName)
                    .storagePathName(filePath)
                    .community(community)
                    .build());
            filePaths.add(filePath);

            if (env.equals("prod")) {
                uploaderService.putS3(file, storyPath, newName);
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
        community.updateCommunityImage(communityImages);

        Community savedStory = communityRepository.save(community);
        boolean isLiked = checkStoryLiked(communityId, findUser);
        return new CommunityResponseDto(savedStory, isLiked);
    }

    @Override
    @Transactional
    public void deleteCommunity(Long communityId, User user) {
        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));
        Community story = communityRepository.findById(communityId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_COMMUNITY));

        if (story.getAuthor().getId().equals(findUser.getProfile().getId())) {
            communityRepository.delete(story);
        } else {
            throw new CustomException(Error.NOT_MATCH_USER);
        }
    }


    @Override
    @Transactional
    public void report(User user, Long communityId, ReportDto reportDto) {
        Community community = communityRepository.findById(communityId).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_COMMUNITY);
        });

        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> {
            throw new CustomException(Error.NOT_FOUND_USER_ID);
        });

        //신고 3회 삭제
        if (community.getReportCount() >= 2) {
            communityRepository.delete(community);
        } else {
            community.plusReport();
            communityRepository.save(community);
        }

        //로그성 테이블에 남기기
        Report report = Report.builder()
                .commentAuthor(community.getAuthor().getNickName())
                .commentBody(community.getContent())
                .commentId(community.getId())
                .commentCreatedAt(community.getCreatedDate())
                .commentUpdatedAt(community.getModifiedDate())
                .reportReason(reportDto.getReason())
                .type("community")
                .reporter(findUser.getProfile().getId())
                .build();

        reportRepository.save(report);
    }

    private boolean checkStoryLiked(Long storyId, User user) {
        boolean isLiked = false;
        if (communityFavoriteRepository.existsByCommunityIdAndUserId(storyId, user.getId())) {
            isLiked = true;
        }
        return isLiked;
    }
}
