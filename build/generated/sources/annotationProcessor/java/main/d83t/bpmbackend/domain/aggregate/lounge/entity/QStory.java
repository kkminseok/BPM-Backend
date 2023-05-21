package d83t.bpmbackend.domain.aggregate.lounge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStory is a Querydsl query type for Story
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStory extends EntityPathBase<Story> {

    private static final long serialVersionUID = -908076696L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStory story = new QStory("story");

    public final d83t.bpmbackend.base.entity.QDateEntity _super = new d83t.bpmbackend.base.entity.QDateEntity(this);

    public final d83t.bpmbackend.domain.aggregate.profile.entity.QProfile author;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<StoryImage, QStoryImage> images = this.<StoryImage, QStoryImage>createList("images", StoryImage.class, QStoryImage.class, PathInits.DIRECT2);

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final ListPath<StoryLike, QStoryLike> likes = this.<StoryLike, QStoryLike>createList("likes", StoryLike.class, QStoryLike.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> modifiedDate = _super.modifiedDate;

    public QStory(String variable) {
        this(Story.class, forVariable(variable), INITS);
    }

    public QStory(Path<? extends Story> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStory(PathMetadata metadata, PathInits inits) {
        this(Story.class, metadata, inits);
    }

    public QStory(Class<? extends Story> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new d83t.bpmbackend.domain.aggregate.profile.entity.QProfile(forProperty("author"), inits.get("author")) : null;
    }

}

