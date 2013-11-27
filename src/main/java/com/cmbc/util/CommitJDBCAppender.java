package com.cmbc.util;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(name = "CommitJDBC", category = "Core", elementType = "appender", printObject = true)
public final class CommitJDBCAppender extends AbstractDatabaseAppender<CommitJDBCDatabaseManager> {
	private final String description;

	private CommitJDBCAppender(final String name, final Filter filter, final boolean ignoreExceptions,
			final CommitJDBCDatabaseManager manager) {
		super(name, filter, ignoreExceptions, manager);
		this.description = this.getName() + "{ manager=" + this.getManager() + " }";
	}

	@Override
	public String toString() {
		return this.description;
	}

	@PluginFactory
	public static CommitJDBCAppender createAppender(@PluginAttribute("name") final String name,
			@PluginAttribute("ignoreExceptions") final String ignore, @PluginElement("Filter") final Filter filter,
			@PluginElement("ConnectionSource") final ConnectionSource connectionSource,
			@PluginAttribute("bufferSize") final String bufferSize,
			@PluginAttribute("tableName") final String tableName,
			@PluginElement("ColumnConfigs") final ColumnConfig[] columnConfigs) {

		final int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
		final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);

		final StringBuilder managerName = new StringBuilder("jdbcManager{ description=").append(name)
				.append(", bufferSize=").append(bufferSizeInt).append(", connectionSource=")
				.append(connectionSource.toString()).append(", tableName=").append(tableName).append(", columns=[ ");

		int i = 0;
		for (final ColumnConfig column : columnConfigs) {
			if (i++ > 0) {
				managerName.append(", ");
			}
			managerName.append(column.toString());
		}

		managerName.append(" ] }");

		final CommitJDBCDatabaseManager manager = CommitJDBCDatabaseManager.getJDBCDatabaseManager(
				managerName.toString(), bufferSizeInt, connectionSource, tableName, columnConfigs);
		if (manager == null) {
			return null;
		}

		return new CommitJDBCAppender(name, filter, ignoreExceptions, manager);
	}
}
