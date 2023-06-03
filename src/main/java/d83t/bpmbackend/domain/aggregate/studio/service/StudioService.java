package d83t.bpmbackend.domain.aggregate.studio.service;

import d83t.bpmbackend.domain.aggregate.studio.dto.StudioRequestDto;
import d83t.bpmbackend.domain.aggregate.studio.dto.StudioResponseDto;
import d83t.bpmbackend.domain.aggregate.user.entity.User;

import java.util.List;

public interface StudioService {
    StudioResponseDto createStudio(StudioRequestDto requestDto);

    StudioResponseDto findById(Long studioId, User user);

    List<StudioResponseDto> findStudioAll(Integer limit, Integer offset, String condition, String q, User user);
}
