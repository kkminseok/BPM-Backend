package d83t.bpmbackend.domain.aggregate.community.controller;

import d83t.bpmbackend.domain.aggregate.community.dto.StoryRequestDto;
import d83t.bpmbackend.domain.aggregate.community.dto.StoryResponseDto;
import d83t.bpmbackend.domain.aggregate.community.service.StoryService;
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
@RequestMapping("/api/community/story")
@Slf4j
public class StoryController {

    private final StoryService storyService;

    @Operation(summary = "커뮤니티 글 등록 API", description = "커뮤니티 스토리 게시판에 글을 등록합니다")
    @ApiResponse(responseCode = "201", description = "스토리 등록 성공", content = @Content(schema = @Schema(implementation = StoryResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "이미지가 5개 넘게 들어왔습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "잘못된 유저가 들어왔습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public StoryResponseDto createStory(
            @ModelAttribute StoryRequestDto requestDto,
            @Nullable @RequestPart List<MultipartFile> files,
            @AuthenticationPrincipal User user) {
        log.info("story create request : {}", requestDto.toString());
        return storyService.createStory(requestDto, files, user);
    }

    @Operation(summary = "커뮤니티 글 상세조회 API", description = "커뮤니티 스토리 글을 상세 조회합니")
    @ApiResponse(responseCode = "200", description = "스토리 상세조회 성공", content = @Content(schema = @Schema(implementation = StoryResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "스토리를 찾을 수 없습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{storyId}")
    public StoryResponseDto getStory(
            @PathVariable Long storyId,
            @AuthenticationPrincipal User user) {
        return storyService.getStory(storyId, user);
    }

    @Operation(summary = "커뮤니티 글 수정 API")
    @PutMapping("/{storyId}")
    public StoryResponseDto updateStory(
            @PathVariable Long storyId,
            @ModelAttribute StoryRequestDto requestDto,
            @Nullable @RequestPart List<MultipartFile> files,
            @AuthenticationPrincipal User user) {
        log.info("community story update : {}", requestDto.toString());
        return storyService.updateStory(storyId, requestDto, files, user);
    }
}
