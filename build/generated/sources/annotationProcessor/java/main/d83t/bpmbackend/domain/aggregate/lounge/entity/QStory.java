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
public class QStory extends EntityPathBase<Community> {

    private static final long serialVersionUID = 2144162149L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStory story = new QStory("story");

    public final d83t.bpmbackend.base.entity.QDateEntity _super = new d83t.bpmbackend.base.entity.QDateEntity(this);

    public final d83t.bpmbackend.domain.aggregate.profile.entity.QProfile author;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<CommunityImage, QStoryImage> images = this.<CommunityImage, QStoryImage>createList("images", CommunityImage.class, QStoryImage.class, PathInits.DIRECT2);

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final ListPath<CommunityFavorite, QStoryLike> likes = this.<CommunityFavorite, QStoryLike>createList("likes", CommunityFavorite.class, QStoryLike.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> modifiedDate = _super.modifiedDate;

    public QStory(String variable) {
        this(Community.class, forVariable(variable), INITS);
    }

    public QStory(Path<? extends Community> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStory(PathMetadata metadata, PathInits inits) {
        this(Community.class, metadata, inits);
    }

    public QStory(Class<? extends Community> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new d83t.bpmbackend.domain.aggregate.profile.entity.QProfile(forProperty("author"), inits.get("author")) : null;
    }

}

