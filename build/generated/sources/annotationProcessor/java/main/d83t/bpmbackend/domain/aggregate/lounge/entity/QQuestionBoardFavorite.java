package d83t.bpmbackend.domain.aggregate.lounge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestionBoardFavorite is a Querydsl query type for QuestionBoardFavorite
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionBoardFavorite extends EntityPathBase<QuestionBoardFavorite> {

    private static final long serialVersionUID = -1950463796L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestionBoardFavorite questionBoardFavorite = new QQuestionBoardFavorite("questionBoardFavorite");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QQuestionBoard questionBoard;

    public final d83t.bpmbackend.domain.aggregate.user.entity.QUser user;

    public QQuestionBoardFavorite(String variable) {
        this(QuestionBoardFavorite.class, forVariable(variable), INITS);
    }

    public QQuestionBoardFavorite(Path<? extends QuestionBoardFavorite> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestionBoardFavorite(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestionBoardFavorite(PathMetadata metadata, PathInits inits) {
        this(QuestionBoardFavorite.class, metadata, inits);
    }

    public QQuestionBoardFavorite(Class<? extends QuestionBoardFavorite> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.questionBoard = inits.isInitialized("questionBoard") ? new QQuestionBoard(forProperty("questionBoard"), inits.get("questionBoard")) : null;
        this.user = inits.isInitialized("user") ? new d83t.bpmbackend.domain.aggregate.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

