package d83t.bpmbackend.domain.aggregate.lounge.repository;

import d83t.bpmbackend.domain.aggregate.lounge.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
}
