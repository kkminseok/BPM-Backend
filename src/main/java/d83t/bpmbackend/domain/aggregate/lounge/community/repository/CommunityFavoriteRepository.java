package d83t.bpmbackend.domain.aggregate.lounge.community.repository;

import d83t.bpmbackend.domain.aggregate.lounge.community.entity.CommunityFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityFavoriteRepository extends JpaRepository<CommunityFavorite, Long> {
    Optional<CommunityFavorite> findByCommunityIdAndUserId(Long communityId, Long userId);
    boolean existsByCommunityIdAndUserId(Long communityId, Long userId);
    Long countByCommunityId(Long communityId);

}
