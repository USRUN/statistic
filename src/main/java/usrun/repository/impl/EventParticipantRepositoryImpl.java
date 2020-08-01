package usrun.repository.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import usrun.model.EventParticipant;
import usrun.repository.EventParticipantRepository;

/**
 * @author phuctt4
 */

@Repository
public class EventParticipantRepositoryImpl implements EventParticipantRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public EventParticipantRepositoryImpl(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public List<EventParticipant> findAll() {
    MapSqlParameterSource params = new MapSqlParameterSource();
    String sql = "SELECT * FROM eventParticipant";
    return getEventParticipants(sql, params);
  }

  private List<EventParticipant> getEventParticipants(String sql, MapSqlParameterSource params) {
    return namedParameterJdbcTemplate.query(sql, params, (rs, i) -> new EventParticipant(
        rs.getLong("eventId"),
        rs.getLong("userId"),
        rs.getLong("teamId"),
        rs.getLong("distance")
    ));
  }
}
