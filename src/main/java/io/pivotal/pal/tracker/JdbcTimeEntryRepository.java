package io.pivotal.pal.tracker;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

  private JdbcTemplate jdbcTemplate;

  public JdbcTimeEntryRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public TimeEntry create(TimeEntry any) {

    KeyHolder holder = new GeneratedKeyHolder();
    String sqlQuery = "INSERT INTO time_entries (project_id, user_id, date, hours) VALUES(?,?,?,?)";
    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
          ps.setLong(1, any.getProjectId());
          ps.setLong(2, any.getUserId());
          ps.setDate(3, Date.valueOf(any.getDate()));
          ps.setLong(4, any.getHours());
          return ps;
        }, holder);

    int newUserId = holder.getKey().intValue();
    any.setId(newUserId);
    return any;

  }

  @Override
  public TimeEntry find(long timeEntryId) {

    return jdbcTemplate.query(
        "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
        new Object[]{timeEntryId},
        extractor);

  }

  @Override
  public List<TimeEntry> list() {

    try {
      return jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries",
          mapper);
    }
    catch(Exception e)
    {
      e.printStackTrace();

    }
    return null;
  }

  @Override
  public TimeEntry update(long eq, TimeEntry any) {

    String sqlQuery = "UPDATE time_entries SET project_id = ?, user_id = ?, date = ?, hours = ? WHERE id = ?";

    jdbcTemplate.update(sqlQuery, new Object[]{any.getProjectId(), any.getUserId(),  Date.valueOf(any.getDate()), any.getHours(), eq });
    return find(eq);
  }

  @Override
  public void delete(long timeEntryId) {

    String sqlQuery = "DELETE from time_entries WHERE id = ?";

    jdbcTemplate.update(sqlQuery, new Object[]{timeEntryId});

  }

  private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
      rs.getLong("id"),
      rs.getLong("project_id"),
      rs.getLong("user_id"),
      rs.getDate("date").toLocalDate(),
      rs.getInt("hours")
  );

  private final ResultSetExtractor<TimeEntry> extractor =
      (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
