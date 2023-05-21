package d83t.bpmbackend.domain.aggregate.lounge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestionBoardImage is a Querydsl query type for QuestionBoardImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionBoardImage extends EntityPathBase<QuestionBoardImage> {

    private static final long serialVersionUID = -1991116757L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestionBoardImage questionBoardImage = new QQuestionBoardImage("questionBoardImage");

    public final d83t.bpmbackend.base.entity.QDateEntity _super = new d83t.bpmbackend.base.entity.QDateEntity(this);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath originFileName = createString("originFileName");

    public final QQuestionBoard questionBoard;

    public final StringPath storagePathName = createString("storagePathName");

    public QQuestionBoardImage(String variable) {
        this(QuestionBoardImage.class, forVariable(variable), INITS);
    }

    public QQuestionBoardImage(Path<? extends QuestionBoardImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestionBoardImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestionBoardImage(PathMetadata metadata, PathInits inits) {
        this(QuestionBoardImage.class, metadata, inits);
    }

    public QQuestionBoardImage(Class<? extends QuestionBoardImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.questionBoard = inits.isInitialized("questionBoard") ? new QQuestionBoard(forProperty("questionBoard"), inits.get("questionBoard")) : null;
    }

}

