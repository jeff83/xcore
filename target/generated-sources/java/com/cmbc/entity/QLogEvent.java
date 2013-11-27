package com.cmbc.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QLogEvent is a Querydsl query type for LogEvent
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QLogEvent extends EntityPathBase<LogEvent> {

    private static final long serialVersionUID = 312049675;

    public static final QLogEvent logEvent = new QLogEvent("logEvent");

    public final ch.rasc.edsutil.entity.QAbstractPersistable _super = new ch.rasc.edsutil.entity.QAbstractPersistable(this);

    public final DateTimePath<org.joda.time.DateTime> eventDate = createDateTime("eventDate", org.joda.time.DateTime.class);

    public final StringPath exception = createString("exception");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath ip = createString("ip");

    public final StringPath level = createString("level");

    public final StringPath logger = createString("logger");

    public final StringPath marker = createString("marker");

    public final StringPath message = createString("message");

    public final StringPath source = createString("source");

    public final StringPath thread = createString("thread");

    public final StringPath userAgent = createString("userAgent");

    public final StringPath userName = createString("userName");

    public QLogEvent(String variable) {
        super(LogEvent.class, forVariable(variable));
    }

    public QLogEvent(Path<? extends LogEvent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLogEvent(PathMetadata<?> metadata) {
        super(LogEvent.class, metadata);
    }

}

