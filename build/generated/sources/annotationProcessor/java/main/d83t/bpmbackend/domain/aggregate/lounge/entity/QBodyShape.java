package d83t.bpmbackend.domain.aggregate.lounge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBodyShape is a Querydsl query type for BodyShape
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBodyShape extends EntityPathBase<BodyShape> {

    private static final long serialVersionUID = 813792143L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBodyShape bodyShape = new QBodyShape("bodyShape");

    public final d83t.bpmbackend.base.entity.QDateEntity _super = new d83t.bpmbackend.base.entity.QDateEntity(this);

    public final d83t.bpmbackend.domain.aggregate.profile.entity.QProfile author;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<BodyShapeImage, QBodyShapeImage> images = this.<BodyShapeImage, QBodyShapeImage>createList("images", BodyShapeImage.class, QBodyShapeImage.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> modifiedDate = _super.modifiedDate;

    public QBodyShape(String variable) {
        this(BodyShape.class, forVariable(variable), INITS);
    }

    public QBodyShape(Path<? extends BodyShape> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBodyShape(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBodyShape(PathMetadata metadata, PathInits inits) {
        this(BodyShape.class, metadata, inits);
    }

    public QBodyShape(Class<? extends BodyShape> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new d83t.bpmbackend.domain.aggregate.profile.entity.QProfile(forProperty("author"), inits.get("author")) : null;
    }

}

