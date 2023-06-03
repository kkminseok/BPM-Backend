package d83t.bpmbackend.base.report.repository;

import d83t.bpmbackend.domain.aggregate.lounge.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {
}
