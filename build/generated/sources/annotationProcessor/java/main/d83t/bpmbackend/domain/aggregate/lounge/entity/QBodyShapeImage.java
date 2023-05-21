package d83t.bpmbackend.domain.aggregate.lounge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBodyShapeImage is a Querydsl query type for BodyShapeImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBodyShapeImage extends EntityPathBase<BodyShapeImage> {

    private static final long serialVersionUID = -730849524L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBodyShapeImage bodyShapeImage = new QBodyShapeImage("bodyShapeImage");

    public final d83t.bpmbackend.base.entity.QDateEntity _super = new d83t.bpmbackend.base.entity.QDateEntity(this);

    public final QBodyShape bodyShape;

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.ZonedDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath originFileName = createString("originFileName");

    public final StringPath storagePathName = createString("storagePathName");

    public QBodyShapeImage(String variable) {
        this(BodyShapeImage.class, forVariable(variable), INITS);
    }

    public QBodyShapeImage(Path<? extends BodyShapeImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBodyShapeImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBodyShapeImage(PathMetadata metadata, PathInits inits) {
        this(BodyShapeImage.class, metadata, inits);
    }

    public QBodyShapeImage(Class<? extends BodyShapeImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bodyShape = inits.isInitialized("bodyShape") ? new QBodyShape(forProperty("bodyShape"), inits.get("bodyShape")) : null;
    }

}

