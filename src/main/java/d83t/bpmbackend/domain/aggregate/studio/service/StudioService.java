package d83t.bpmbackend.domain.aggregate.studio.service;

import d83t.bpmbackend.domain.aggregate.studio.dto.StudioFilterDto;
import d83t.bpmbackend.domain.aggregate.studio.dto.StudioRequestDto;
import d83t.bpmbackend.domain.aggregate.studio.dto.StudioResponseDto;
import d83t.bpmbackend.domain.aggregate.studio.entity.Review;
import d83t.bpmbackend.domain.aggregate.studio.entity.Studio;
import d83t.bpmbackend.domain.aggregate.user.entity.User;

import java.util.List;

public interface StudioService {
    StudioResponseDto createStudio(StudioRequestDto requestDto);

    StudioResponseDto findById(Long studioId, User user);

    List<StudioResponseDto> findStudioAll(Integer limit, Integer offset, String condition, String q, User user);

    List<StudioResponseDto> getFilterStudio(User user, StudioFilterDto studioFilterDto);

    void plusKeyword(Studio studio, List<Long> keywords);

    void minusKeyword(Studio studio, Review review);

    void plusRating(Studio studio, Double rating);

    void minusRating(Studio studio, Double rating);
}
