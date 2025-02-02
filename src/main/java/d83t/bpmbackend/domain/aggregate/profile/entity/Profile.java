package d83t.bpmbackend.domain.aggregate.profile.entity;

import d83t.bpmbackend.domain.aggregate.lounge.bodyShape.entity.BodyShape;
import d83t.bpmbackend.domain.aggregate.lounge.community.entity.Community;
import d83t.bpmbackend.domain.aggregate.lounge.questionBoard.entity.QuestionBoard;
import d83t.bpmbackend.domain.aggregate.lounge.questionBoard.entity.QuestionBoardComment;
import d83t.bpmbackend.domain.aggregate.studio.entity.Like;
import d83t.bpmbackend.domain.aggregate.studio.entity.Review;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false)
    private String nickName;

    @Column(name = "bio", nullable = false)
    private String bio;

    @Column(name = "filename", nullable = false)
    private String originFileName;

    @Column(name = "path", nullable = false)
    private String storagePathName;

    @OneToOne(mappedBy = "profile")
    private User user;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BodyShape> myBodyShapes;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionBoard> myQuestionBoard;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionBoardComment> myQuestionBoardComments;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Community> stories;
}
