package d83t.bpmbackend.domain.aggregate.lounge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestionBoardComment is a Querydsl query type for QuestionBoardComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionBoardComment extends EntityPathBase<QuestionBoardComment> {

    private static final long serialVersionUID = 1130678575L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestionBoardComment questionBoardComment = new QQuestionBoardComment("questionBoardComment");

    public final d83t.bpmbackend.base.entity.QDateEntity _super = new d83t.bpmbackend.base.entity.QDateEntity(this);

    public final d83t.bpmbackend.domain.aggregate.profile.entity.QProfile author;

    public final StringPath body = createString("body");

    public final ListPath<QuestionBoardComment, QQuestionBoardComment> children = this.<QuestionBoardComment, QQuestionBoardComment>createList("children", QuestionBoardComment.class, QQuestionBoardComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> modifiedDate = _super.modifiedDate;

    public final QQuestionBoardComment parent;

    public final QQuestionBoard questionBoard;

    public final NumberPath<Integer> reportCount = createNumber("reportCount", Integer.class);

    public QQuestionBoardComment(String variable) {
        this(QuestionBoardComment.class, forVariable(variable), INITS);
    }

    public QQuestionBoardComment(Path<? extends QuestionBoardComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestionBoardComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestionBoardComment(PathMetadata metadata, PathInits inits) {
        this(QuestionBoardComment.class, metadata, inits);
    }

    public QQuestionBoardComment(Class<? extends QuestionBoardComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new d83t.bpmbackend.domain.aggregate.profile.entity.QProfile(forProperty("author"), inits.get("author")) : null;
        this.parent = inits.isInitialized("parent") ? new QQuestionBoardComment(forProperty("parent"), inits.get("parent")) : null;
        this.questionBoard = inits.isInitialized("questionBoard") ? new QQuestionBoard(forProperty("questionBoard"), inits.get("questionBoard")) : null;
    }

}

