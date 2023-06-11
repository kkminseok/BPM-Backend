package d83t.bpmbackend.domain.aggregate.lounge.questionBoard.repository;

import d83t.bpmbackend.domain.aggregate.lounge.questionBoard.entity.QuestionBoardFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionBoardFavoriteRepository extends JpaRepository<QuestionBoardFavorite, Long> {
    Optional<QuestionBoardFavorite> findByQuestionBoardIdAndUserId(Long questionBoardId, Long userId);
    Long countByQuestionBoardId(Long questionBoardId);
}
