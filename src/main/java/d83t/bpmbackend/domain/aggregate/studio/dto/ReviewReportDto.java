package d83t.bpmbackend.domain.aggregate.studio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewReportDto {
    @Schema(description = "신고사유", defaultValue = "불쾌한 언어를 사용하였습니다.")
    private String reason;
}