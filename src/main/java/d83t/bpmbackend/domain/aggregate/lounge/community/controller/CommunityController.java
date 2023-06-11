package d83t.bpmbackend.domain.aggregate.lounge.community.controller;

import d83t.bpmbackend.base.report.dto.ReportDto;
import d83t.bpmbackend.domain.aggregate.lounge.community.dto.CommunityCommentDto;
import d83t.bpmbackend.domain.aggregate.lounge.community.dto.CommunityCommentResponse;
import d83t.bpmbackend.domain.aggregate.lounge.community.dto.CommunityRequestDto;
import d83t.bpmbackend.domain.aggregate.lounge.community.dto.CommunityResponseDto;
import d83t.bpmbackend.domain.aggregate.lounge.community.service.CommunityCommentService;
import d83t.bpmbackend.domain.aggregate.lounge.community.service.CommunityFavoriteService;
import d83t.bpmbackend.domain.aggregate.lounge.community.service.CommunityService;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import d83t.bpmbackend.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lounge/community")
@Slf4j
public class CommunityController {

    private final CommunityService communityService;
    private final CommunityFavoriteService communityFavoriteService;
    private final CommunityCommentService communityCommentService;

    @Operation(summary = "커뮤니티 글 등록 API", description = "커뮤니티 스토리 게시판에 글을 등록합니다")
    @ApiResponse(responseCode = "201", description = "커뮤니티 등록 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "이미지가 5개 넘게 들어왔습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "잘못된 유저가 들어왔습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public CommunityResponseDto createCommunity(
            @ModelAttribute CommunityRequestDto requestDto,
            @Nullable @RequestPart List<MultipartFile> files,
            @AuthenticationPrincipal User user) {
        log.info("story create request : {}", requestDto.toString());
        return communityService.createCommunity(requestDto, files, user);
    }

    @Operation(summary = "커뮤니티 글 리스트 조회 API", description = "page, size, sort 를 넘겨주시면 됩니다. sort 는 최신순(createdDate)과 같이 넘겨주세요.")
    @GetMapping()
    public CommunityResponseDto.MultiCommunity getAllCommunity(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "createdDate") String sort,
            @AuthenticationPrincipal User user) {
        log.info("page : " + page + " size : " + size + " sort : " + sort);
        List<CommunityResponseDto> result = communityService.getAllCommunity(page, size, sort, user);
        return CommunityResponseDto.MultiCommunity.builder().communities(result).communityCount(result.size()).build();
    }

    @Operation(summary = "커뮤니티 글 상세조회 API", description = "커뮤니티 스토리 글을 상세 조회합니")
    @ApiResponse(responseCode = "200", description = "스토리 상세조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "스토리를 찾을 수 없습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{communityId}")
    public CommunityResponseDto getCommunity(
            @PathVariable Long communityId,
            @AuthenticationPrincipal User user) {
        return communityService.getCommunity(communityId, user);
    }

    @Operation(summary = "커뮤니티 글 수정 API")
    @PutMapping("/{communityId}")
    public CommunityResponseDto updateStory(
            @PathVariable Long communityId,
            @ModelAttribute CommunityRequestDto requestDto,
            @Nullable @RequestPart List<MultipartFile> files,
            @AuthenticationPrincipal User user) {
        log.info("community story update : {}", requestDto.toString());
        return communityService.updateCommunity(communityId, requestDto, files, user);
    }

    @Operation(summary = "커뮤니티 글 삭제 API")
    @DeleteMapping("/{communityId}")
    public void deleteCommunity(
            @PathVariable Long communityId,
            @AuthenticationPrincipal User user) {
        communityService.deleteCommunity(communityId, user);
    }

    @Operation(summary = "커뮤니티 글 좋아요 생성 API")
    @PostMapping("/{communityId}/favorite")
    public void createStoryFavorite(
            @PathVariable Long communityId,
            @AuthenticationPrincipal User user) {
        communityFavoriteService.createCommunityFavorite(communityId, user);
    }

    @Operation(summary = "커뮤니티 글 좋아요 삭제 API")
    @DeleteMapping("/{communityId}/favorite")
    public void deleteCommunityFavorite(
            @PathVariable Long communityId,
            @AuthenticationPrincipal User user) {
        communityFavoriteService.deleteCommunityFavorite(communityId, user);
    }

    @Operation(summary = "커뮤니티 게시판 게시글 신고하기 API", description = "사용자가 커뮤니티 게시판 게시글을 신고합니다.")
    @ApiResponse(responseCode = "200", description = "커뮤니티 게시판 게시글 신고 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{communityId}/report")
    public void reportCommunityArticle(
            @AuthenticationPrincipal User user,
            @PathVariable Long communityId,
            @RequestBody ReportDto reportDto
            ){
        log.info("community board report input : {}, dto: {}", communityId, reportDto.toString());
        communityService.report(user, communityId, reportDto);
    }

    /*
     * 댓글 CRUD
     */
    @Operation(summary = "커뮤니티 글 댓글 작성하기")
    @ApiResponse(responseCode = "200", description = "커뮤니티 댓글 작성 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{communityId}/comments")
    public CommunityCommentResponse communityCreateComment(
            @AuthenticationPrincipal User user,
            @PathVariable Long communityId,
            @RequestBody CommunityCommentDto commentDto) {
        log.info("커뮤니티 글 댓글 작성 input : {} 번째 글 {}", communityId, commentDto.toString());
        return communityCommentService.createComment(user, communityId, commentDto);
    }

    @Operation(summary = "커뮤니티 글 댓글들 조회하기")
    @ApiResponse(responseCode = "200", description = "커뮤니티 댓글들 조회 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{communityId}/comments")
    public CommunityCommentResponse.MultiComments communityGetComments(
            @AuthenticationPrincipal User user,
            @PathVariable Long communityId) {
        log.info("커뮤니티 글 댓글 조회 input : {} 번째 글 ", communityId);
        List<CommunityCommentResponse> comments = communityCommentService.communityGetComments(user, communityId);
        return CommunityCommentResponse.MultiComments.builder().comments(comments).commentsCount(comments.size()).build();
    }

    @Operation(summary = "커뮤니티 글 댓글 수정하기")
    @ApiResponse(responseCode = "200", description = "커뮤니티 댓글 수정 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{communityId}/comments/{commentId}")
    public CommunityCommentResponse communityUpdateComment(
            @AuthenticationPrincipal User user,
            @PathVariable Long communityId,
            @PathVariable Long commentId,
            @RequestBody CommunityCommentDto commentDto) {
        log.info("커뮤니티 글 댓글 수정 input : {} 번째 글 {} 번째 댓글 {} ", communityId, commentId, commentDto.toString());
        return communityCommentService.updateComment(user, communityId, commentId, commentDto);
    }

    @Operation(summary = "커뮤니티 글 댓글 삭제하기")
    @ApiResponse(responseCode = "200", description = "커뮤니티 댓글 삭제 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{communityId}/comments/{commentId}")
    public void communityDeleteComment(
            @AuthenticationPrincipal User user,
            @PathVariable Long communityId,
            @PathVariable Long commentId) {
        log.info("커뮤니티 글 댓글 삭제 input : {} 번째 글 {} 번째 댓글 {} ", communityId, commentId);
        communityCommentService.deleteComment(user, communityId, commentId);
    }


    @Operation(summary = "커뮤니티 글 댓글 좋아요")
    @ApiResponse(responseCode = "200", description = "커뮤니티 댓글 좋아요.")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{communityId}/comments/{commentId}/favorite")
    public void favoriteCommunityComment(
            @AuthenticationPrincipal User user,
            @PathVariable Long communityId,
            @PathVariable Long commentId) {
        log.info("커뮤니티 글 댓글 좋아요 input : {} 번째 글 {} 번째 댓글 {} ", communityId, commentId);
        communityCommentService.favoriteComment(user, communityId, commentId);
    }

    @Operation(summary = "커뮤니티 글 댓글 좋아요 취소")
    @ApiResponse(responseCode = "200", description = "커뮤니티 댓글 좋아요 취소.")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{communityId}/comments/{commentId}/favorite")
    public void unfavoriteCommunityComment(
            @AuthenticationPrincipal User user,
            @PathVariable Long communityId,
            @PathVariable Long commentId) {
        log.info("커뮤니티 글 댓글 좋아요 취소 input : {} 번째 글 {} 번째 댓글 {} ", communityId, commentId);
        communityCommentService.unfavoriteComment(user, communityId, commentId);
    }

    @Operation(summary = "커뮤니티 글 댓글 신고하기", description = "사용자가 커뮤니티 게시판의 댓글을 신고합니다.")
    @ApiResponse(responseCode = "200", description = "커뮤니티 게시판 댓글 신고 성공")
    @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{communityId}/comments/{commentId}/report")
    public void reportCommunityComment(
            @AuthenticationPrincipal User user,
            @PathVariable Long communityId,
            @PathVariable Long commentId,
            @RequestBody ReportDto reportDto){
        log.info("community board report comment input : board id {} comment id {} reportDto", communityId, commentId, reportDto.toString());
        communityCommentService.report(user, communityId, commentId, reportDto);
    }

}
