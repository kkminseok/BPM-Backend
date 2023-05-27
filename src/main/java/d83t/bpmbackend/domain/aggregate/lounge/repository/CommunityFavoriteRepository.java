package d83t.bpmbackend.domain.aggregate.lounge.repository;

import d83t.bpmbackend.domain.aggregate.lounge.entity.CommunityFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityFavoriteRepository extends JpaRepository<CommunityFavorite, Long> {
    Optional<CommunityFavorite> findByStoryIdAndUserId(Long storyId, Long userId);
    boolean existsByStoryIdAndUserId(Long storyId, Long userId);
}
