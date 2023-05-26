package d83t.bpmbackend.domain.aggregate.lounge.controller;

import d83t.bpmbackend.domain.aggregate.lounge.dto.*;
import d83t.bpmbackend.domain.aggregate.lounge.service.QuestionBoardCommentService;
import d83t.bpmbackend.domain.aggregate.lounge.service.QuestionBoardService;
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
@RequestMapping("/api/community/question-board")
@Slf4j
public class QuestionBoardController {
    private final QuestionBoardService questionBoardService;
    private final QuestionBoardCommentService questionBoardCommentService;

    @Operation(summary = "질문하기 게시판 게시글 등록 API", description = "사용자가 질문하기 게시판에 질문을 등록합니다. token을 넘겨야합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 등록 성공", content = @Content(schema = @Schema(implementation = QuestionBoardResponse.SingleQuestionBoard.class)))
    @ApiResponse(responseCode = "400", description = "이미지가 5개 넘게 들어왔습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "잘못된 유저가 들어왔습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public QuestionBoardResponse createQuestionBoard(
            @AuthenticationPrincipal User user,
            @Nullable @RequestPart List<MultipartFile> files,
            @ModelAttribute QuestionBoardRequest questionBoardRequest) {
        log.info("Data input : {}", questionBoardRequest.toString());
        return questionBoardService.createQuestionBoardArticle(user, files, questionBoardRequest);
    }

    @Operation(summary = "질문하기 게시판 리스트 조회 API", description = "사용자가 질문하기 게시판 리스트 조회합니다. token을 넘겨야합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 리스트 조회 성공", content = @Content(schema = @Schema(implementation = QuestionBoardResponse.MultiQuestionBoard.class)))
    @GetMapping
    public QuestionBoardResponse.MultiQuestionBoard getQuestionBoardArticles(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset,
            @ModelAttribute QuestionBoardParam questionBoardParam) {
        log.info("query: {}", questionBoardParam.toString());
        List<QuestionBoardResponse> questionArticles = questionBoardService.getQuestionBoardArticles(user, limit, offset, questionBoardParam);
        return QuestionBoardResponse.MultiQuestionBoard.builder().questionBoardResponseList(questionArticles).questionBoardCount(questionArticles.size()).build();
    }

    @Operation(summary = "질문하기 게시판 상세 조회 API", description = "사용자가 질문하기 게시판 중 하나의 게시글을 클릭해서 상세 조회합니다. token을 넘겨야합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 상세조회 성공", content = @Content(schema = @Schema(implementation = QuestionBoardResponse.SingleQuestionBoard.class)))
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{questionBoardArticleId}")
    public QuestionBoardResponse getQuestionBoardArticle(
            @AuthenticationPrincipal User user,
            @PathVariable Long questionBoardArticleId) {
        return questionBoardService.getQuestionBoardArticle(user, questionBoardArticleId);
    }


    @Operation(summary = "질문하기 게시판 게시글 수정 API", description = "사용자가 질문하기 게시판 중 하나의 게시글을 클릭해서  수정합니다. token을 넘겨야합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 상세조회 성공", content = @Content(schema = @Schema(implementation = QuestionBoardResponse.SingleQuestionBoard.class)))
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{questionBoardArticleId}")
    public QuestionBoardResponse updateQuestionBoardArticle(
            @AuthenticationPrincipal User user,
            @Nullable @RequestPart List<MultipartFile> files,
            @Nullable @ModelAttribute QuestionBoardRequest questionBoardRequest,
            @PathVariable Long questionBoardArticleId) {
        log.info("data input: {}", questionBoardRequest.toString());
        return questionBoardService.updateQuestionBoardArticle(user, files, questionBoardRequest, questionBoardArticleId);
    }

    @Operation(summary = "질문하기 게시판 게시글 삭제 API", description = "사용자가 질문하기 게시판 중 하나의 게시글을 클릭해서 삭제합니다. token을 넘겨야합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 게시글 삭제 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{questionBoardArticleId}")
    public void deleteQuestionBoardArticle(
            @AuthenticationPrincipal User user,
            @PathVariable Long questionBoardArticleId) {
        log.info("question board delete input : {}", questionBoardArticleId);
        questionBoardService.deleteQuestionBoardArticle(user, questionBoardArticleId);
    }

    @Operation(summary = "질문하기 게시판 게시글 좋아요 API", description = "사용자가 질문하기 게시판 중 하나의 게시글을 클릭해서 좋아요를 누릅니다. token을 넘겨야합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 게시글 좋아요 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{questionBoardArticleId}/favorite")
    public QuestionBoardResponse favoriteQuestionBoardArticle(
            @AuthenticationPrincipal User user,
            @PathVariable Long questionBoardArticleId) {
        log.info("question board favorite input : {}", questionBoardArticleId);
        return questionBoardService.favoriteQuestionBoardArticle(user, questionBoardArticleId);
    }

    @Operation(summary = "질문하기 게시판 게시글 좋아요 취소 API", description = "사용자가 질문하기 게시판 중 하나의 게시글을 클릭해서 좋아요를 취소합니다. token을 넘겨야합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 게시글 좋아요 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{questionBoardArticleId}/unfavorite")
    public QuestionBoardResponse unfavoriteQuestionBoardArticle(
            @AuthenticationPrincipal User user,
            @PathVariable Long questionBoardArticleId) {
        log.info("question board favorite input : {}", questionBoardArticleId);
        return questionBoardService.unfavoriteQuestionBoardArticle(user, questionBoardArticleId);
    }

    /**
     * 댓글  CRUD
     */
    @Operation(summary = "질문하기 게시판 댓글 작성 API", description = "사용자가 질문하기 게시판 중 하나의 게시글을 클릭해서 댓글을 작성합니다. token을 넘겨야합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 게시글 댓글작성 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{questionBoardArticleId}/comments")
    public QuestionBoardCommentResponse questionBoardArticleCreateComment(
            @AuthenticationPrincipal User user,
            @PathVariable Long questionBoardArticleId,
            @RequestBody QuestionBoardCommentDto commentDto) {
        log.info("question board create comment input : {}", commentDto.toString());
        return questionBoardCommentService.createComment(user, questionBoardArticleId, commentDto);
    }

    @Operation(summary = "질문하기 게시판 댓글들 조회 API", description = "사용자가 질문하기 게시판 중 하나의 게시글을 클릭하면 댓글들이 조회됩니다. token을 넘겨야합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 게시글 댓글조회 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{questionBoardArticleId}/comments")
    public QuestionBoardCommentResponse.MultiComments questionBoardArticleGetComments(
            @AuthenticationPrincipal User user,
            @PathVariable Long questionBoardArticleId) {
        log.info("question board get comments input : {}", questionBoardArticleId);
        List<QuestionBoardCommentResponse> comments = questionBoardCommentService.getComments(user, questionBoardArticleId);
        return QuestionBoardCommentResponse.MultiComments.builder().comments(comments).commentsCount(comments.size()).build();
    }

    @Operation(summary = "질문하기 게시판 댓글들 수정 API", description = "사용자가 질문하기 게시판 댓글을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 게시글 댓글수정 성공")
    @ApiResponse(responseCode = "404", description = "게시글이나 댓글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{questionBoardArticleId}/comments/{commentId}")
    public QuestionBoardCommentResponse questionBoardArticleUpdateComments(
            @AuthenticationPrincipal User user,
            @PathVariable Long questionBoardArticleId,
            @PathVariable Long commentId,
            @RequestBody QuestionBoardCommentUpdateDto questionBoardCommentUpdateDto) {
        log.info("question board update comments input : {}", questionBoardCommentUpdateDto);
        return questionBoardCommentService.updateComment(user, questionBoardArticleId, commentId, questionBoardCommentUpdateDto);
    }

    @Operation(summary = "질문하기 게시판 댓글들 삭제 API", description = "사용자가 질문하기 게시판 중 게시글의 댓글이 삭제됩니다. token을 넘겨야합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 게시글 댓글삭제 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{questionBoardArticleId}/comments/{commentId}")
    public void questionBoardArticleDeleteComments(
            @AuthenticationPrincipal User user,
            @PathVariable Long questionBoardArticleId,
            @PathVariable Long commentId) {
        log.info("question board delete comments input : questionBoardArticleId {}, commentId {}", questionBoardArticleId, commentId);
        questionBoardCommentService.deleteComment(user, questionBoardArticleId, commentId);
    }

    @Operation(summary = "질문하기 게시판 댓글 신고하기 API", description = "사용자가 질문하기 게시판의 댓글을 신고합니다. token을 넘겨야합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 댓글 신고 성공")
    @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{questionBoardArticleId}/comments/{commentId}")
    public void questionBoardArticleReportComments(
            @AuthenticationPrincipal User user,
            @PathVariable Long questionBoardArticleId,
            @PathVariable Long commentId,
            @RequestBody QuestionBoardCommentReportDto reportDto) {
        log.info("question board report comments input : questionBoardArticleId {}, commentId {}, body : {}", questionBoardArticleId, commentId, reportDto.getReason());
        questionBoardCommentService.reportComment(user, questionBoardArticleId, commentId, reportDto);
    }

    @Operation(summary = "질문하기 게시판 게시글 댓글 좋아요 API", description = "사용자가 질문하기 게시판 중 하나의 게시글을 클릭해서 댓글 좋아요를 누릅니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 게시글 댓글 좋아요 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{questionBoardArticleId}/comments/{commentId}/favorite")
    public QuestionBoardCommentResponse favoriteQuestionBoardArticleComment(
            @AuthenticationPrincipal User user,
            @PathVariable Long questionBoardArticleId,
            @PathVariable Long commentId) {
        log.info("question board comment favorite input article: {}, comment : {}", questionBoardArticleId, commentId);
        return questionBoardCommentService.favoriteQuestionBoardArticleComment(user, questionBoardArticleId, commentId);
    }

    @Operation(summary = "질문하기 게시판 게시글 좋아요 취소 API", description = "사용자가 질문하기 게시판 중 하나의 게시글을 클릭해서 댓글 좋아요를 취소합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 게시글 댓글 좋아요 취소 성공")
    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/{questionBoardArticleId}/comments/{commentId}/unfavorite")
    public QuestionBoardCommentResponse unfavoriteQuestionBoardArticle(
            @AuthenticationPrincipal User user,
            @PathVariable Long questionBoardArticleId,
            @PathVariable Long commentId) {
        log.info("question board comment unfavorite input article: {}, comment : {}", questionBoardArticleId, commentId);
        return questionBoardCommentService.unfavoriteQuestionBoardArticleComment(user, questionBoardArticleId, commentId);
    }

}
