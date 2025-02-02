package d83t.bpmbackend.domain.aggregate.studio.repository;

import d83t.bpmbackend.domain.aggregate.studio.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewFavoriteRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByReviewIdAndUserId(Long reviewId, Long userId);
    boolean existsByReviewIdAndUserId(Long reviewId, Long userId);
    Long countByReviewId(Long reviewId);
}
