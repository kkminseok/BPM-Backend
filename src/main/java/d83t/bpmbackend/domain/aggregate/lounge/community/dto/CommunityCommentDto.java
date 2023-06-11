package d83t.bpmbackend.domain.aggregate.lounge.community.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CommunityCommentDto {

    @Schema(description = "댓글 내용", defaultValue = "댓글 내용입니다.")
    private String body;
}
