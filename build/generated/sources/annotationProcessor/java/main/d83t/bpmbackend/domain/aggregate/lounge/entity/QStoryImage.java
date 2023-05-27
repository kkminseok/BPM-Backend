package d83t.bpmbackend.domain.aggregate.lounge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoryImage is a Querydsl query type for StoryImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoryImage extends EntityPathBase<CommunityImage> {

    private static final long serialVersionUID = 1097760502L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoryImage storyImage = new QStoryImage("storyImage");

    public final d83t.bpmbackend.base.entity.QDateEntity _super = new d83t.bpmbackend.base.entity.QDateEntity(this);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath originFileName = createString("originFileName");

    public final StringPath storagePathName = createString("storagePathName");

    public final QStory story;

    public QStoryImage(String variable) {
        this(CommunityImage.class, forVariable(variable), INITS);
    }

    public QStoryImage(Path<? extends CommunityImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoryImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoryImage(PathMetadata metadata, PathInits inits) {
        this(CommunityImage.class, metadata, inits);
    }

    public QStoryImage(Class<? extends CommunityImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.story = inits.isInitialized("story") ? new QStory(forProperty("story"), inits.get("story")) : null;
    }

}

