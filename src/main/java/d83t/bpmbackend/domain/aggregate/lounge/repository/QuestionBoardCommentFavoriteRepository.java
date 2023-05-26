package d83t.bpmbackend.domain.aggregate.lounge.repository;

import d83t.bpmbackend.domain.aggregate.lounge.entity.QuestionBoardCommentFavorite;
import d83t.bpmbackend.domain.aggregate.lounge.entity.QuestionBoardFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionBoardCommentFavoriteRepository extends JpaRepository<QuestionBoardCommentFavorite,Long> {
    Optional<QuestionBoardCommentFavorite> findByQuestionBoardCommentIdAndUserId(Long questionBoardCommentId, Long userId);
    Long countByQuestionBoardCommentId(Long questionBoardCommentId);
}
