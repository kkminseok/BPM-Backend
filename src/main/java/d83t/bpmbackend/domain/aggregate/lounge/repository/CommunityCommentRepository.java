package d83t.bpmbackend.domain.aggregate.lounge.repository;

import d83t.bpmbackend.domain.aggregate.lounge.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
}
