package d83t.bpmbackend.domain.aggregate.lounge.dto;

import d83t.bpmbackend.domain.aggregate.lounge.entity.Community;
import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "커뮤니티 글 작성 요청 DTO")
public class CommunityRequestDto {

    private String content;

    public Community toEntity(Profile profile) {
        return Community.builder()
                .content(content)
                .author(profile)
                .build();
    }
}
