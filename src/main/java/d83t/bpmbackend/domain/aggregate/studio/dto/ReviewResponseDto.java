package d83t.bpmbackend.domain.aggregate.studio.dto;

import d83t.bpmbackend.domain.aggregate.profile.entity.Profile;
import d83t.bpmbackend.domain.aggregate.studio.entity.Review;
import d83t.bpmbackend.domain.aggregate.studio.entity.ReviewImage;
import d83t.bpmbackend.domain.aggregate.studio.entity.Studio;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Schema(description = "리뷰 응답 DTO")
public class ReviewResponseDto {
    private Long id;
    private StudioDto studio;
    private AuthorDto author;
    private Double rating;
    private String recommends;
    private List<String> filesPath;
    private String content;
    private int favoriteCount;

    private boolean favorite;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    @Builder
    @Getter
    public static class StudioDto {
        private Long id;
        private String name;
        private Double rating;
        private String content;
    }

    @Builder
    @Getter
    public static class AuthorDto {
        private Long id;
        private String nickname;
        private String profilePath;
    }

    @Builder
    @Getter
    public static class MultiReviews {
        List<ReviewResponseDto> reviews;
        Integer reviewCount;
    }
}
