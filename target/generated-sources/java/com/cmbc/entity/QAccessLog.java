package com.cmbc.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QAccessLog is a Querydsl query type for AccessLog
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAccessLog extends EntityPathBase<AccessLog> {

    private static final long serialVersionUID = 1710579595;

    public static final QAccessLog accessLog = new QAccessLog("accessLog");

    public final ch.rasc.edsutil.entity.QAbstractPersistable _super = new ch.rasc.edsutil.entity.QAbstractPersistable(this);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final DateTimePath<org.joda.time.DateTime> logIn = createDateTime("logIn", org.joda.time.DateTime.class);

    public final DateTimePath<org.joda.time.DateTime> logOut = createDateTime("logOut", org.joda.time.DateTime.class);

    public final StringPath sessionId = createString("sessionId");

    public final StringPath userAgent = createString("userAgent");

    public final StringPath userName = createString("userName");

    public QAccessLog(String variable) {
        super(AccessLog.class, forVariable(variable));
    }

    public QAccessLog(Path<? extends AccessLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccessLog(PathMetadata<?> metadata) {
        super(AccessLog.class, metadata);
    }

}

