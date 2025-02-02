package d83t.bpmbackend.domain.aggregate.lounge.questionBoard.repository;

import d83t.bpmbackend.domain.aggregate.lounge.questionBoard.entity.QuestionBoardReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionBoardReportRepository extends JpaRepository<QuestionBoardReport, Long> {
    Optional<QuestionBoardReport> findByQuestionBoardIdAndUserId(Long questionBoardId, Long userId);
}
