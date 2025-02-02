package d83t.bpmbackend.domain.aggregate.lounge.community.repository;

import d83t.bpmbackend.domain.aggregate.lounge.community.entity.CommunityReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityReportRepository extends JpaRepository<CommunityReport, Long> {
    Optional<CommunityReport> findByCommunityIdAndUserId(Long communityId, Long userId);
}
