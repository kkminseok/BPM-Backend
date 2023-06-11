package d83t.bpmbackend.domain.aggregate.lounge.community.repository;

import d83t.bpmbackend.domain.aggregate.lounge.community.entity.CommunityCommentFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityCommentFavoriteRepository extends JpaRepository<CommunityCommentFavorite, Long> {
    Optional<CommunityCommentFavorite> findByCommunityCommentIdAndUserId(Long communityCommentId, Long userId);
    Long countByCommunityCommentId(Long communityCommentId);
}
