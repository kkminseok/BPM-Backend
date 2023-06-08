package d83t.bpmbackend.domain.aggregate.studio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class StudioFilterDto {
    List<Integer> keyword;
}
