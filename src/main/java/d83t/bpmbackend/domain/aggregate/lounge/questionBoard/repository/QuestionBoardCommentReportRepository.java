package d83t.bpmbackend.domain.aggregate.lounge.questionBoard.repository;

import d83t.bpmbackend.domain.aggregate.lounge.questionBoard.entity.QuestionBoardCommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionBoardCommentReportRepository extends JpaRepository<QuestionBoardCommentReport, Long> {
    Optional<QuestionBoardCommentReport> findByQuestionBoardCommentIdAndUserId(Long questionBoardCommentId, Long userId);

}
