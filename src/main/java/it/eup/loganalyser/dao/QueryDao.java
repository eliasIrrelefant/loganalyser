package it.eup.loganalyser.dao;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.internal.SessionImpl;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import it.eup.loganalyser.logging.Logger;

@ApplicationScoped
public class QueryDao {

	List<String> indexColumns = Arrays.asList("PATH", "STATUSCODE", "RESPONSETIME", "REQUESTDATE", "QUERYSTRING");

	@Inject
	EntityManager entityManager;

	public void execute(String queryString) {
		JdbcTemplate template = getJdbcTemplate();

		long time = System.currentTimeMillis();
		template.execute(queryString);
		long duration = System.currentTimeMillis() - time;

		Logger.log("SQL: {0} ({1}ms)", queryString, duration);
	}

	public List<Map<String, Object>> query(String queryString) {
		JdbcTemplate template = getJdbcTemplate();
		template.setMaxRows(1000);

		List<Map<String, Object>> result = template.query(queryString, new ColumnMapRowMapper());

		return result;
	}

	private JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(getDataSource());
	}

	private DataSource getDataSource() {
		SessionImpl sessionImpl = entityManager.unwrap(SessionImpl.class);
		Connection connection = sessionImpl.connection();
		SingleConnectionDataSource dataSource = new SingleConnectionDataSource(connection, true);
		return dataSource;
	}

	public void createIndex() {
		for (String s : indexColumns) {
			String format = String.format("CREATE INDEX IF NOT EXISTS IDX_%s ON LOGDATA(%s)", s, s);
			execute(format);
		}
	}

	public void dropIndex() {
		for (String s : indexColumns) {
			String format = String.format("DROP INDEX IF EXISTS IDX_%s", s);
			execute(format);
		}
	}

	public void wipeDatabase() {
		execute("truncate table logdata");
		execute("delete from origin");
	}
}
