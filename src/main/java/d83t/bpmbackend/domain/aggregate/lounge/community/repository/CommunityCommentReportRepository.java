package d83t.bpmbackend.domain.aggregate.lounge.community.repository;

import d83t.bpmbackend.domain.aggregate.lounge.community.entity.CommunityCommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityCommentReportRepository extends JpaRepository<CommunityCommentReport,Long> {
    Optional<CommunityCommentReport> findByCommunityCommentIdAndUserId(Long communityCommentId, Long userId);
}
