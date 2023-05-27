package d83t.bpmbackend.domain.aggregate.lounge.repository;

import d83t.bpmbackend.domain.aggregate.lounge.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}
