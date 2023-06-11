package d83t.bpmbackend.domain.aggregate.lounge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class QuestionBoardCommentResponse {
    private Long id;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private String body;

    private Author author;

    private Long parentId;

    private List<QuestionBoardCommentResponse> children;

    @Schema(description = "좋아요 여부")
    private Boolean favorite;

    @Schema(description = "게시글 좋아요 갯수")
    private Long favoriteCount;

    @Schema(description = "신고하기 여부")
    private Boolean reported;

    private int reportCount;

    @Builder
    @Getter
    public static class Author {
        @Schema(description = "작성자 id", defaultValue = "0")
        private Long id;
        @Schema(description = "작성자 닉네임", defaultValue = "nickname")
        private String nickname;
        @Schema(description = "작성자 프로필 경로", defaultValue = "https://s3이미지경로")
        private String profilePath;
    }

    @Builder
    @Getter
    public static class SingleComment {
        QuestionBoardCommentResponse comment;
    }

    @Builder
    @Getter
    public static class MultiComments {
        List<QuestionBoardCommentResponse> comments;
        Integer commentsCount;
    }

    public void addChildren(QuestionBoardCommentResponse commentResponse){
        if(this.children == null){
            this.children = new ArrayList<>();
        }
        this.children.add(commentResponse);
    }
}
