package d83t.bpmbackend.domain.aggregate.lounge.community.repository;

import d83t.bpmbackend.domain.aggregate.lounge.community.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
}
