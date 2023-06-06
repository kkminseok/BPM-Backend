package d83t.bpmbackend.domain.aggregate.user.repository;

import d83t.bpmbackend.domain.aggregate.user.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByUserId(long userId);
}
