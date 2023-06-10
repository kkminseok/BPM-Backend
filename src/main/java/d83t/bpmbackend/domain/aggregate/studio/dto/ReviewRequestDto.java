package d83t.bpmbackend.domain.aggregate.studio.dto;

import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import d83t.bpmbackend.domain.aggregate.studio.entity.Review;
import d83t.bpmbackend.domain.aggregate.studio.entity.Studio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "리뷰 작성 요청 DTO")
public class ReviewRequestDto {
    private Double rating;
    @Nullable
    private List<Long> recommends;
    private String content;

    /*
    public Review toEntity(Studio studio, Profile profile) {
        //임시
        if(keywordIds == null){
            return Review.builder()
                    .studio(studio)
                    .author(profile)
                    .rating(rating)
                    .content(content)
                    .build();
        }else {
            return Review.builder()
                    .studio(studio)
                    .author(profile)
                    .rating(rating)
                    .recommends(keywordIds)
                    .content(content)
                    .build();
        }
    }
     */
}
