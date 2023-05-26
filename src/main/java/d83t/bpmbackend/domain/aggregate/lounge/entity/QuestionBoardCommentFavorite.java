package d83t.bpmbackend.domain.aggregate.lounge.entity;

import d83t.bpmbackend.domain.aggregate.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "questionBoardCommentFavorite")
public class QuestionBoardCommentFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionBoardComment_id")
    private QuestionBoardComment questionBoardComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
