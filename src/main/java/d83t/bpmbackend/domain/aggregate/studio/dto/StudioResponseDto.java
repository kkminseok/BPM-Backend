package d83t.bpmbackend.domain.aggregate.studio.dto;

import d83t.bpmbackend.domain.aggregate.studio.entity.Studio;
import d83t.bpmbackend.domain.aggregate.studio.entity.StudioImage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@Schema(description = "스튜디오 응답 DTO")
public class StudioResponseDto {
    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String firstTag;
    private String secondTag;
    private Map<String, Integer> topRecommends;
    private String phone;
    private String sns;
    private String openHours;
    private String price;
    private List<String> filesPath;
    private String content;
    private Double rating;
    private int reviewCount;
    private int scrapCount;

    private boolean isScrapped;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    @Builder
    @Getter
    public static class MultiStudios{
        List<StudioResponseDto> studios;
        Integer studiosCount;
    }
}
