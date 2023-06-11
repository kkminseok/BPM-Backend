package d83t.bpmbackend.domain.aggregate.studio.service;

import d83t.bpmbackend.base.report.repository.ReportRepository;
import d83t.bpmbackend.domain.aggregate.lounge.entity.Report;
import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import d83t.bpmbackend.domain.aggregate.studio.dto.ReviewReportDto;
import d83t.bpmbackend.domain.aggregate.studio.dto.ReviewRequestDto;
import d83t.bpmbackend.domain.aggregate.studio.dto.ReviewResponseDto;
import d83t.bpmbackend.domain.aggregate.studio.entity.Review;
import d83t.bpmbackend.domain.aggregate.studio.entity.ReviewImage;
import d83t.bpmbackend.domain.aggregate.studio.entity.Studio;
import d83t.bpmbackend.domain.aggregate.studio.repository.LikeRepository;
import d83t.bpmbackend.domain.aggregate.studio.repository.ReviewRepository;
import d83t.bpmbackend.domain.aggregate.studio.repository.StudioRepository;
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

import static d83t.bpmbackend.domain.aggregate.keyword.service.KeywordServiceImpl.keywordSymbolMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final StudioRepository studioRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final S3UploaderService uploaderService;
    private final ReportRepository reportRepository;
    private final StudioService studioService;

    @Value("${bpm.s3.bucket.review.path}")
    private String reviewPath;

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
            this.fileDir = this.basePath + this.reviewPath;
        }
    }

    @Override
    @Transactional
    public ReviewResponseDto createReview(Long studioId, User user, List<MultipartFile> files, ReviewRequestDto requestDto) {
        if (files != null && files.size() > 5) {
            throw new CustomException(Error.FILE_SIZE_MAX);
        }

        List<String> filePaths = new ArrayList<>();
        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));
        Studio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_STUDIO));
        Profile profile = findUser.getProfile();

        //Review review = requestDto.toEntity(studio, profile);
        //review 들어온 id 들을 키워드로 반환 이는 반환용이다.
        List<String> keyword = requestDto.getRecommends().stream()
                .map(ids -> keywordSymbolMap.get(ids))
                .collect(Collectors.toList());

        // 스튜디오 키워드를 증가시킨다.
        studioService.plusKeyword(studio, requestDto.getRecommends());

        // 스튜디오 평점을 다시 맞춘다.
        studioService.plusRating(studio, requestDto.getRating());

        //새 리뷰객체를 만든다.
        Review review = Review.builder()
                .studio(studio)
                .author(profile)
                .rating(requestDto.getRating())
                .content(requestDto.getContent())
                .keywords(keyword.stream().collect(Collectors.joining(",")))
                .favoriteCount(0)
                .build();

        if (files != null && files.size() != 0) {
            for (MultipartFile file : files) {
                String newName = FileUtils.createNewFileName(file.getOriginalFilename());
                String filePath = fileDir + newName;

                review.addReviewImage(ReviewImage.builder()
                        .originFileName(newName)
                        .storagePathName(filePath)
                        .review(review)
                        .build());
                filePaths.add(filePath);

                if (env.equals("prod")) {
                    uploaderService.putS3(file, reviewPath, newName);
                } else if (env.equals("local")) {
                    try {
                        File localFile = new File(filePath);
                        file.transferTo(localFile);
                        FileUtils.removeNewFile(localFile);
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }

        Review savedReview = reviewRepository.save(review);
        studio.addReview(savedReview);
        Studio updatedStudio = studioRepository.save(studio);

        return convertDto(savedReview, updatedStudio, false);
    }

    @Override
    public List<ReviewResponseDto> findAll(User user, Long studioId, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));
        Page<Review> reviews = reviewRepository.findByStudioId(studioId, pageable);

        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));
        Profile profile = findUser.getProfile();

        return reviews.stream().map(review -> {
            boolean isLiked = checkReviewLiked(review.getId(), profile.getId());
            return convertDto(review, review.getStudio(), isLiked);
        }).collect(Collectors.toList());
    }

    @Override
    public ReviewResponseDto findById(User user, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_REVIEW));

        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));
        Profile profile = findUser.getProfile();

        boolean isLiked = checkReviewLiked(review.getId(), profile.getId());
        return convertDto(review, review.getStudio(), isLiked);
    }

    /*
    @Override
    @Transactional
    public ReviewResponseDto updateReview(User user, Long studioId, Long reviewId, List<MultipartFile> files, ReviewRequestDto requestDto) {
        if (files == null || files.isEmpty()) {
            throw new CustomException(Error.FILE_REQUIRED);
        }
        if (files.size() > 5) {
            throw new CustomException(Error.FILE_SIZE_MAX);
        }

        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));
        Studio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_STUDIO));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_REVIEW));

        // 작성자 검증
        Long profileId = findUser.getProfile().getId();
        if (!review.getAuthor().getId().equals(profileId)) {
            throw new CustomException(Error.NOT_MATCH_USER);
        }

        if (requestDto.getRating() != null) {
            review.setRating(requestDto.getRating());
        }
        if (requestDto.getKeywordIds() != null) {
            review.setRecommends(requestDto.getKeywordIds());
        }
        if (requestDto.getContent() != null) {
            review.setContent(requestDto.getContent());
        }
        boolean isLiked = checkReviewLiked(review.getId(), profileId);

        List<String> filePaths = new ArrayList<>();
        List<ReviewImage> reviewImages = new ArrayList<>();
        for (MultipartFile file : files) {
            String newName = FileUtils.createNewFileName(file.getOriginalFilename());
            String filePath = fileDir + newName;

            reviewImages.add(ReviewImage.builder()
                    .originFileName(newName)
                    .storagePathName(filePath)
                    .review(review)
                    .build());
            filePaths.add(filePath);

            if (env.equals("prod")) {
                uploaderService.putS3(file, reviewPath, newName);
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
        review.updateReviewImage(reviewImages);

        Review savedReview = reviewRepository.save(review);
        return new ReviewResponseDto(savedReview, isLiked);
    }

     */

    @Override
    @Transactional
    public void deleteReview(User user, Long studioId, Long reviewId) {
        Studio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_STUDIO));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_REVIEW));
        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));

        if (review.getAuthor().getId().equals(findUser.getProfile().getId())) {
            //키워드 갯수 감소
            studioService.minusKeyword(studio, review);
            log.info("키워드 감소 성공");
            //평점 셋팅
            studioService.minusRating(studio, review.getRating());
            log.info("평점 감소 성공");
            studioRepository.save(studio);
            //리뷰 삭제
            reviewRepository.delete(review);
        } else {
            throw new CustomException(Error.NOT_MATCH_USER);
        }
    }

    @Override
    public void reportReview(User user, Long studioId, Long reviewId, ReviewReportDto reviewReportDto) {
        Studio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_STUDIO));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_REVIEW));
        User findUser = userRepository.findByKakaoId(user.getKakaoId())
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_USER_ID));

        //신고 3회 삭제
        if (review.getReportCount() >= 2) {
            reviewRepository.delete(review);
        } else {
            review.plusReport();
            reviewRepository.save(review);
        }

        //로그성 테이블에 남기기
        Report report = Report.builder()
                .commentAuthor(review.getAuthor().getNickName())
                .commentBody(review.getContent())
                .commentId(review.getId())
                .commentCreatedAt(review.getCreatedDate())
                .commentUpdatedAt(review.getModifiedDate())
                .reportReason(reviewReportDto.getReason())
                .type("review")
                .reporter(findUser.getProfile().getId())
                .build();

        reportRepository.save(report);


    }

    private boolean checkReviewLiked(Long reviewId, Long profileId) {
        boolean isLiked = false;
        if (likeRepository.existsByReviewIdAndUserId(reviewId, profileId)) {
            isLiked = true;
        }
        return isLiked;
    }

    private ReviewResponseDto convertDto(Review review, Studio studio, Boolean favorite) {
        List<String> filePaths = new ArrayList<>();
        if(review.getImages() != null) {
            for (ReviewImage image : review.getImages()) {
                filePaths.add(image.getStoragePathName());
            }
        }
        Profile author = review.getAuthor();

        //TODO: favorite 추후 업데이트
        return ReviewResponseDto.builder()
                .id(review.getId())
                .rating(review.getRating())
                .recommends(review.getKeywords())
                .content(review.getContent())
                .favorite(favorite)
                .favoriteCount(0)
                .createdAt(review.getCreatedDate())
                .updatedAt(review.getModifiedDate())
                .studio(ReviewResponseDto.StudioDto.builder()
                        .id(studio.getId())
                        .content(studio.getContent())
                        .rating(studio.getRating())
                        .name(studio.getName())
                        .build())
                .author(ReviewResponseDto.AuthorDto.builder()
                        .id(author.getId())
                        .nickname(author.getNickName())
                        .profilePath(author.getStoragePathName())
                        .build())
                .filesPath(filePaths)
                .build();

    }
}
