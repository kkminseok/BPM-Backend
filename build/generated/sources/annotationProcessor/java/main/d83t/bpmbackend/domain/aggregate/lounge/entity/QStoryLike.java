package d83t.bpmbackend.domain.aggregate.lounge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoryLike is a Querydsl query type for StoryLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoryLike extends EntityPathBase<StoryLike> {

    private static final long serialVersionUID = -1171715809L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoryLike storyLike = new QStoryLike("storyLike");

    public final d83t.bpmbackend.base.entity.QDateEntity _super = new d83t.bpmbackend.base.entity.QDateEntity(this);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> modifiedDate = _super.modifiedDate;

    public final QStory story;

    public final d83t.bpmbackend.domain.aggregate.user.entity.QUser user;

    public QStoryLike(String variable) {
        this(StoryLike.class, forVariable(variable), INITS);
    }

    public QStoryLike(Path<? extends StoryLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoryLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoryLike(PathMetadata metadata, PathInits inits) {
        this(StoryLike.class, metadata, inits);
    }

    public QStoryLike(Class<? extends StoryLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.story = inits.isInitialized("story") ? new QStory(forProperty("story"), inits.get("story")) : null;
        this.user = inits.isInitialized("user") ? new d83t.bpmbackend.domain.aggregate.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

