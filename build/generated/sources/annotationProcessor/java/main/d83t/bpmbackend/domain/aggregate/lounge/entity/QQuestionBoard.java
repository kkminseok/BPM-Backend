package d83t.bpmbackend.domain.aggregate.lounge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;
import d83t.bpmbackend.domain.aggregate.lounge.questionBoard.entity.QuestionBoard;
import d83t.bpmbackend.domain.aggregate.lounge.questionBoard.entity.QuestionBoardComment;
import d83t.bpmbackend.domain.aggregate.lounge.questionBoard.entity.QuestionBoardFavorite;
import d83t.bpmbackend.domain.aggregate.lounge.questionBoard.entity.QuestionBoardImage;


/**
 * QQuestionBoard is a Querydsl query type for QuestionBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionBoard extends EntityPathBase<QuestionBoard> {

    private static final long serialVersionUID = 1012280080L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestionBoard questionBoard = new QQuestionBoard("questionBoard");

    public final d83t.bpmbackend.base.entity.QDateEntity _super = new d83t.bpmbackend.base.entity.QDateEntity(this);

    public final d83t.bpmbackend.domain.aggregate.profile.entity.QProfile author;

    public final ListPath<QuestionBoardComment, QQuestionBoardComment> comments = this.<QuestionBoardComment, QQuestionBoardComment>createList("comments", QuestionBoardComment.class, QQuestionBoardComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createdDate = _super.createdDate;

    public final ListPath<QuestionBoardFavorite, QQuestionBoardFavorite> favorites = this.<QuestionBoardFavorite, QQuestionBoardFavorite>createList("favorites", QuestionBoardFavorite.class, QQuestionBoardFavorite.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<QuestionBoardImage, QQuestionBoardImage> image = this.<QuestionBoardImage, QQuestionBoardImage>createList("image", QuestionBoardImage.class, QQuestionBoardImage.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Integer> reportCount = createNumber("reportCount", Integer.class);

    public final StringPath slug = createString("slug");

    public QQuestionBoard(String variable) {
        this(QuestionBoard.class, forVariable(variable), INITS);
    }

    public QQuestionBoard(Path<? extends QuestionBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestionBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestionBoard(PathMetadata metadata, PathInits inits) {
        this(QuestionBoard.class, metadata, inits);
    }

    public QQuestionBoard(Class<? extends QuestionBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new d83t.bpmbackend.domain.aggregate.profile.entity.QProfile(forProperty("author"), inits.get("author")) : null;
    }

}

