package d83t.bpmbackend.domain.aggregate.studio.service;

import d83t.bpmbackend.domain.aggregate.studio.dto.ReviewReportDto;
import d83t.bpmbackend.domain.aggregate.studio.dto.ReviewRequestDto;
import d83t.bpmbackend.domain.aggregate.studio.dto.ReviewResponseDto;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto createReview(Long studioId, User user, List<MultipartFile> files, ReviewRequestDto requestDto);
    List<ReviewResponseDto> findAll(User user, Long studioId, int page, int size, String sort);
    ReviewResponseDto findById(User user, Long reviewId);
    //ReviewResponseDto updateReview(User user, Long studioId, Long reviewId, List<MultipartFile> files, ReviewRequestDto requestDto);
    void deleteReview(User user, Long studioId, Long reviewId);
    void reportReview(User user, Long studioId, Long reviewId, ReviewReportDto reviewReportDto);
}
