package d83t.bpmbackend.domain.aggregate.lounge.questionBoard.repository;

import d83t.bpmbackend.domain.aggregate.lounge.questionBoard.entity.QuestionBoardCommentFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionBoardCommentFavoriteRepository extends JpaRepository<QuestionBoardCommentFavorite,Long> {
    Optional<QuestionBoardCommentFavorite> findByQuestionBoardCommentIdAndUserId(Long questionBoardCommentId, Long userId);
    Long countByQuestionBoardCommentId(Long questionBoardCommentId);
}
