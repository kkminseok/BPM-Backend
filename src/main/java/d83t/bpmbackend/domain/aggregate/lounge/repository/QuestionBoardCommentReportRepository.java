package d83t.bpmbackend.domain.aggregate.lounge.repository;

import d83t.bpmbackend.domain.aggregate.lounge.entity.QuestionBoardCommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionBoardCommentReportRepository extends JpaRepository<QuestionBoardCommentReport, Long > {
}
