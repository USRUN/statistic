package usrun.repository.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import usrun.model.junction.TeamMember;
import usrun.model.type.TeamMemberType;
import usrun.dto.TeamStatDTO;
import usrun.repository.TeamMemberRepository;

@Repository
public class TeamMemberRepositoryImpl implements TeamMemberRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public TeamMemberRepositoryImpl(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  private TeamMember getTeamMember(String sql, MapSqlParameterSource params) {
    Optional<TeamMember> toReturn = namedParameterJdbcTemplate.query(
        sql,
        params,
        (rs, i) -> new TeamMember(
            rs.getLong("teamId"),
            rs.getLong("userId"),
            rs.getInt("teamMemberType"),
            rs.getDate("addTime"))).stream().findFirst();

    if (toReturn.isPresent()) {
      return toReturn.get();
    }
    return null;
  }

  private MapSqlParameterSource mapTeamMember(TeamMember toMap) {

    MapSqlParameterSource toReturn = new MapSqlParameterSource();

    toReturn.addValue("teamId", toMap.getTeamId());
    toReturn.addValue("userId", toMap.getUserId());
    toReturn.addValue("teamMemberType", toMap.getTeamMemberType().toValue());
    toReturn.addValue("addTime", toMap.getAddTime());

    return toReturn;
  }

  @Override
  public List<TeamStatDTO> getTeamStat() {
    String sql = "select tm.teamId,t.teamName, t.thumbnail, sum(ua.totalDistance) as totalDistance, max(ua.totalTime) as maxTime, max(ua.totalDistance) as maxDistance, count(userActivityId) as numActivity, count(distinct(tm.userId)) as numberUser from teamMember tm, userActivity ua, team t where ua.userId = tm.userId and t.teamId = tm.teamId group by tm.teamId order by totalDistance DESC;";
    List<TeamStatDTO> toReturn = namedParameterJdbcTemplate.query(
        sql,
        (rs, i) -> new TeamStatDTO(rs.getLong("teamId"),
            rs.getString("teamName"),
            rs.getString("thumbnail"),
            rs.getLong("totalDistance"),
            rs.getLong("maxTime"),
            rs.getLong("maxDistance"),
            0,
            rs.getInt("numberUser"),
            rs.getLong("numActivity")));
    return toReturn;
  }

  @Override
  public List<TeamMember> getAll() {
    String sql = "SELECT * FROM teamMember";
    return getTeamMembers(sql, new MapSqlParameterSource());
  }

  @Override
  public List<TeamMember> getAllByLessEqualTeamMemberType(TeamMemberType teamMemberType) {
    String sql = "SELECT * FROM teamMember WHERE teamMemberType <= :teamMemberType";
    return getTeamMembers(sql,
        new MapSqlParameterSource("teamMemberType", teamMemberType.toValue()));
  }


  private List<TeamMember> getTeamMembers(String sql, MapSqlParameterSource params) {
    return namedParameterJdbcTemplate.query(
        sql,
        params,
        (rs, i) -> new TeamMember(
            rs.getLong("teamId"),
            rs.getLong("userId"),
            rs.getInt("teamMemberType"),
            rs.getDate("addTime")));
  }
}
