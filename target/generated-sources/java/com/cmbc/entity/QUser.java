package com.cmbc.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -837708640;

    public static final QUser user = new QUser("user");

    public final ch.rasc.edsutil.entity.QAbstractPersistable _super = new ch.rasc.edsutil.entity.QAbstractPersistable(this);

    public final StringPath email = createString("email");

    public final BooleanPath enabled = createBoolean("enabled");

    public final NumberPath<Integer> failedLogins = createNumber("failedLogins", Integer.class);

    public final StringPath firstName = createString("firstName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath locale = createString("locale");

    public final DateTimePath<org.joda.time.DateTime> lockedOut = createDateTime("lockedOut", org.joda.time.DateTime.class);

    public final StringPath name = createString("name");

    public final StringPath passwordHash = createString("passwordHash");

    public final StringPath role = createString("role");

    public final StringPath userName = createString("userName");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata<?> metadata) {
        super(User.class, metadata);
    }

}

