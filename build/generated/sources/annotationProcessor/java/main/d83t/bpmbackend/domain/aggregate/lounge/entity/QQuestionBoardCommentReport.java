package d83t.bpmbackend.domain.aggregate.lounge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQuestionBoardCommentReport is a Querydsl query type for QuestionBoardCommentReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionBoardCommentReport extends EntityPathBase<QuestionBoardCommentReport> {

    private static final long serialVersionUID = -389819616L;

    public static final QQuestionBoardCommentReport questionBoardCommentReport = new QQuestionBoardCommentReport("questionBoardCommentReport");

    public final d83t.bpmbackend.base.entity.QDateEntity _super = new d83t.bpmbackend.base.entity.QDateEntity(this);

    public final StringPath commentAuthor = createString("commentAuthor");

    public final StringPath commentBody = createString("commentBody");

    public final DateTimePath<java.time.ZonedDateTime> commentCreatedAt = createDateTime("commentCreatedAt", java.time.ZonedDateTime.class);

    public final NumberPath<Long> commentId = createNumber("commentId", Long.class);

    public final DateTimePath<java.time.ZonedDateTime> commentUpdatedAt = createDateTime("commentUpdatedAt", java.time.ZonedDateTime.class);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Long> reporter = createNumber("reporter", Long.class);

    public final StringPath reportReason = createString("reportReason");

    public QQuestionBoardCommentReport(String variable) {
        super(QuestionBoardCommentReport.class, forVariable(variable));
    }

    public QQuestionBoardCommentReport(Path<? extends QuestionBoardCommentReport> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestionBoardCommentReport(PathMetadata metadata) {
        super(QuestionBoardCommentReport.class, metadata);
    }

}

