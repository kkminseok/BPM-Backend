package d83t.bpmbackend.domain.aggregate.community.controller;

import d83t.bpmbackend.domain.aggregate.community.dto.BodyShapeRequest;
import d83t.bpmbackend.domain.aggregate.community.dto.BodyShapeResponse;
import d83t.bpmbackend.domain.aggregate.community.dto.QuestionBoardRequest;
import d83t.bpmbackend.domain.aggregate.community.dto.QuestionBoardResponse;
import d83t.bpmbackend.domain.aggregate.community.service.BodyShapeService;
import d83t.bpmbackend.domain.aggregate.community.service.QuestionBoardService;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import d83t.bpmbackend.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "질문하기 게시판 게시글 등록 API", description = "사용자가 질문하기 게시판에 질문을 등록합니다. token을 넘겨야합니다.")
    @ApiResponse(responseCode = "200", description = "질문하기 게시판 등록 성공", content = @Content(schema = @Schema(implementation = QuestionBoardResponse.SingleQuestionBoard.class)))
    @ApiResponse(responseCode = "400", description = "이미지가 5개 넘게 들어왔습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "잘못된 유저가 들어왔습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public QuestionBoardResponse.SingleQuestionBoard createQuestionBoard(
            @AuthenticationPrincipal User user,
            @RequestPart List<MultipartFile> files,
            @ModelAttribute QuestionBoardRequest questionBoardRequest) {
        log.info("Data input : {}", questionBoardRequest.toString());
        return QuestionBoardResponse.SingleQuestionBoard.builder().questionBoardResponse(questionBoardService.createQuestionBoardArticle(user, files, questionBoardRequest)).build();
    }

}
