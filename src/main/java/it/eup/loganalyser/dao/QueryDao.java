package it.eup.loganalyser.dao;

import it.eup.loganalyser.logging.Logger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class QueryDao {

  List<String> indexColumns = Arrays.asList("PATH", "STATUS_CODE", "RESPONSE_TIME", "REQUEST_DATE", "QUERY_STRING");

  @Autowired
  private DataSource dataSource;

  @Autowired
  private Logger logger;

  public void execute(String queryString) {
    JdbcTemplate template = getJdbcTemplate();

    long time = System.currentTimeMillis();
    template.execute(queryString);
    long duration = System.currentTimeMillis() - time;

    logger.log("SQL: {0} ({1}ms)", queryString, duration);
  }

  public List<Map<String, Object>> query(String queryString) {
    JdbcTemplate template = getJdbcTemplate();
    template.setMaxRows(1000);

    List<Map<String, Object>> result = template.query(queryString, new ColumnMapRowMapper());

    return result;
  }

  private JdbcTemplate getJdbcTemplate() {
    return new JdbcTemplate(dataSource);
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
