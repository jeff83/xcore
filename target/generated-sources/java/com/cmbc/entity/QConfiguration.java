package com.cmbc.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QConfiguration is a Querydsl query type for Configuration
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QConfiguration extends EntityPathBase<Configuration> {

    private static final long serialVersionUID = 233892801;

    public static final QConfiguration configuration = new QConfiguration("configuration");

    public final ch.rasc.edsutil.entity.QAbstractPersistable _super = new ch.rasc.edsutil.entity.QAbstractPersistable(this);

    public final EnumPath<ConfigurationKey> confKey = createEnum("confKey", ConfigurationKey.class);

    public final StringPath confValue = createString("confValue");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public QConfiguration(String variable) {
        super(Configuration.class, forVariable(variable));
    }

    public QConfiguration(Path<? extends Configuration> path) {
        super(path.getType(), path.getMetadata());
    }

    public QConfiguration(PathMetadata<?> metadata) {
        super(Configuration.class, metadata);
    }

}

