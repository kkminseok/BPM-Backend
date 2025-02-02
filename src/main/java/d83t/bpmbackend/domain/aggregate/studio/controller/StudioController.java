package d83t.bpmbackend.domain.aggregate.studio.controller;

import d83t.bpmbackend.domain.aggregate.studio.dto.*;
import d83t.bpmbackend.domain.aggregate.studio.service.LikeService;
import d83t.bpmbackend.domain.aggregate.studio.service.ReviewService;
import d83t.bpmbackend.domain.aggregate.studio.service.ScrapService;
import d83t.bpmbackend.domain.aggregate.studio.service.StudioService;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import d83t.bpmbackend.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studio")
@Slf4j
public class StudioController {

    private final StudioService studioService;
    private final ReviewService reviewService;
    private final LikeService likeService;
    private final ScrapService scrapService;

    @Operation(summary = "스튜디오 등록 API", description = "스튜디오 필수, 추가 정보를 받아 등록")
    @ApiResponse(responseCode = "201", description = "스튜디오 등록 성공", content = @Content(schema = @Schema(implementation = StudioResponseDto.class)))
    @PostMapping
    public StudioResponseDto createStudio(
            @ModelAttribute @Valid StudioRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        log.info("studio name : " + requestDto.getName());
        return studioService.createStudio(requestDto);
    }

    @Operation(summary = "스튜디오 조회 API", description = "스튜디오 정보를 조회합니다")
    @ApiResponse(responseCode = "200", description = "스튜디오 조회 성공", content = @Content(schema = @Schema(implementation = StudioResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "스튜디오를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{studioId}")
    public StudioResponseDto findStudioById(
            @PathVariable Long studioId,
            @AuthenticationPrincipal User user) {
        log.info("studio id : " + studioId);
        return studioService.findById(studioId, user);
    }

    // TODO: 쿼리 스트링으로 필터를 받아 조회
    @Operation(summary = "스튜디오 조회 API", description = "스튜디오 정보를 전체 조회합니다")
    @ApiResponse(responseCode = "200", description = "스튜디오 전체 조회 성공", content = @Content(schema = @Schema(implementation = StudioResponseDto.MultiStudios.class)))
    @GetMapping("")
    public StudioResponseDto.MultiStudios findStudioAll(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "condition", required = false) String condition,
            @RequestParam(value = "q", required = false) String q) {
        List<StudioResponseDto> findStudios = studioService.findStudioAll(limit, offset, condition, q, user);
        return StudioResponseDto.MultiStudios.builder().studios(findStudios).studiosCount(findStudios.size()).build();
    }

    @Operation(summary = "스튜디오 스크랩 생성 API")
    @PostMapping("/{studioId}/scrap")
    public void createScrap(
            @PathVariable Long studioId,
            @AuthenticationPrincipal User user) {
        scrapService.createScrap(studioId, user);
    }

    @Operation(summary = "스튜디오 스크랩 취소 API")
    @DeleteMapping("/{studioId}/scrap")
    public void deleteScrap(
            @PathVariable Long studioId,
            @AuthenticationPrincipal User user) {
        scrapService.deleteScrap(studioId, user);
    }

    /* review */
    @Operation(summary = "리뷰 등록 API")
    @PostMapping("/{studioId}/review")
    public ReviewResponseDto createReview(
            @PathVariable Long studioId,
            @AuthenticationPrincipal User user,
            @Nullable @RequestPart List<MultipartFile> files,
            @ModelAttribute ReviewRequestDto requestDto) {
        log.info("studio id : " + studioId);
        log.info("requestDto : {}", requestDto.toString());
        return reviewService.createReview(studioId, user, files, requestDto);
    }

    @Operation(summary = "리뷰 리스트 조회 API", description = "sort 는 최신순(createdDate) / 좋아요순(likeCount) 와 같이 넘겨주시면 됩니다.")
    @GetMapping("/{studioId}/review")
    public ReviewResponseDto.MultiReviews findAllReviews(
            @PathVariable Long studioId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "createdDate") String sort,
            @AuthenticationPrincipal User user) {
        log.info("page : " + page + " / size : " + size + " / sort : " + sort);
        List<ReviewResponseDto> reviews = reviewService.findAll(user, studioId, page, size, sort);
        return ReviewResponseDto.MultiReviews.builder().reviews(reviews).reviewCount(reviews.size()).build();
    }

    @Operation(summary = "리뷰 상세 조회 API")
    @GetMapping("/{studioId}/review/{reviewId}")
    public ReviewResponseDto findReviewDetail(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal User user) {
        log.info("review id : " + reviewId);
        return reviewService.findById(user, reviewId);
    }

    /*
    @Operation(summary = "리뷰 업데이트 API")
    @PutMapping("/{studioId}/review/{reviewId}")
    public ReviewResponseDto updateReview(
            @PathVariable Long studioId,
            @PathVariable Long reviewId,
            @Nullable @RequestPart List<MultipartFile> files,
            @ModelAttribute ReviewRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        log.info("review update : {}", requestDto.toString());
        return reviewService.updateReview(user, studioId, reviewId, files, requestDto);
    }
    */

    @Operation(summary = "리뷰 삭제 API")
    @DeleteMapping("/{studioId}/review/{reviewId}")
    public void deleteReview(
            @PathVariable Long studioId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal User user) {
        log.info("review id : " + reviewId);
        reviewService.deleteReview(user, studioId, reviewId);
    }

    @Operation(summary = "리뷰 신고하기 API")
    @ApiResponse(responseCode = "200", description = "리뷰 신고 성공")
    @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{studioId}/review/{reviewId}/report")
    public void reviewReport(
            @AuthenticationPrincipal User user,
            @PathVariable Long studioId,
            @PathVariable Long reviewId,
            @RequestBody ReviewReportDto reviewReportDto
    ) {
        log.info("review report input studioId: {} review Id {} reviewReportDto {}", studioId, reviewId, reviewReportDto.getReason());
        reviewService.reportReview(user, studioId, reviewId, reviewReportDto);
    }


    @Operation(summary = "좋아요 등록 API", description = "리뷰에 대한 좋아요 등록하기")
    @PostMapping("/{studioId}/review/{reviewId}/like")
    public void createLike(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal User user) {
        log.info("review id : " + reviewId);
        likeService.createLike(user, reviewId);
    }

    @Operation(summary = "좋아요 삭제 API", description = "리뷰에 대한 좋아요 삭제하기")
    @DeleteMapping("/{studioId}/review/{reviewId}/like")
    public void deleteLike(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal User user) {
        log.info("review id : " + reviewId);
        likeService.deleteLike(user, reviewId);
    }

    /*
     * 필터 검색
     * TODO: 장소와 연계
     */
    @Operation(summary = "필터 검색")
    @GetMapping("/filter")
    public StudioResponseDto.MultiStudios getFilterStudio(
            @AuthenticationPrincipal User user,
            @RequestBody StudioFilterDto studioFilterDto) {
        log.info("filter input: {}", studioFilterDto.toString());
        List<StudioResponseDto> studios = studioService.getFilterStudio(user, studioFilterDto);
        return StudioResponseDto.MultiStudios.builder().studios(studios).studiosCount(studios.size()).build();
    }
}
