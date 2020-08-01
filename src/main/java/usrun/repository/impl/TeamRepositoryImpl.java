package usrun.repository.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import usrun.model.Team;
import usrun.model.type.TeamMemberType;
import usrun.dto.TeamDTO;
import usrun.repository.TeamRepository;

@Slf4j
@Repository
public class TeamRepositoryImpl implements TeamRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public TeamRepositoryImpl(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public List<Team> findAllTeam() {
    MapSqlParameterSource params = new MapSqlParameterSource();
    String sql = "SELECT * FROM team";
    return getTeamsSQLParamMap(sql, params);
  }

  private MapSqlParameterSource mapTeamObject(Team toMap) {
    MapSqlParameterSource toReturn = new MapSqlParameterSource();

    toReturn.addValue("teamId", toMap.getId());
    toReturn.addValue("teamName", toMap.getTeamName());
    toReturn.addValue("privacy", toMap.getPrivacy());
    toReturn.addValue("thumbnail", toMap.getThumbnail());
    toReturn.addValue("banner", toMap.getBanner());
    toReturn.addValue("province", toMap.getProvince());
    toReturn.addValue("totalMember", toMap.getTotalMember());
    toReturn.addValue("createTime", toMap.getCreateTime());
    toReturn.addValue("deleted", toMap.isDeleted());
    toReturn.addValue("verified", toMap.isVerified());
    toReturn.addValue("description", toMap.getDescription());

    return toReturn;
  }

  private Set<Team> getMultipleTeamSQLParamMap(String sql, MapSqlParameterSource params) {
    return namedParameterJdbcTemplate.query(sql, params, rs -> {
          Set<Team> set = new HashSet<Team>();
          while (rs.next()) {
            Team team = new Team(
                rs.getLong("teamId"),
                rs.getInt("privacy"),
                rs.getInt("totalMember"),
                rs.getString("teamName"),
                rs.getString("thumbnail"),
                rs.getString("banner"),
                rs.getBoolean("verified"),
                rs.getBoolean("deleted"),
                rs.getDate("createTime"),
                rs.getInt("province"),
                rs.getString("description")
            );

            set.add(team);
          }
          return set;
        }
    );
  }

  private List<Team> getTeamsSQLParamMap(String sql, MapSqlParameterSource params) {
    return namedParameterJdbcTemplate.query(
        sql,
        params,
        (rs, i) -> new Team(
            rs.getLong("teamId"),
            rs.getInt("privacy"),
            rs.getInt("totalMember"),
            rs.getString("teamName"),
            rs.getString("thumbnail"),
            rs.getString("banner"),
            rs.getBoolean("verified"),
            rs.getBoolean("deleted"),
            rs.getDate("createTime"),
            rs.getInt("province"),
            rs.getString("description")
        ));
  }

  private List<TeamDTO> getTeamDTOsSQLParamMap(String sql, MapSqlParameterSource params) {
    return namedParameterJdbcTemplate.query(
        sql,
        params,
        (rs, i) -> new TeamDTO(
            rs.getLong("teamId"),
            rs.getInt("privacy"),
            rs.getInt("totalMember"),
            rs.getString("teamName"),
            rs.getString("thumbnail"),
            rs.getString("banner"),
            rs.getBoolean("verified"),
            rs.getBoolean("deleted"),
            rs.getDate("createTime"),
            rs.getInt("province"),
            rs.getString("description"),
            TeamMemberType.fromInt(rs.getInt("teamMemberType"))
        ));
  }

  private Team getTeamSQLParamMap(String sql, MapSqlParameterSource params) {
    Optional<Team> toReturn = getTeamsSQLParamMap(sql, params).stream().findFirst();

    if (toReturn.isPresent()) {
      if (toReturn.get().isDeleted()) {
        return null;
      }
      return toReturn.get();
    }
    log.error("Can't find team with {}", params);
    return null;
  }

}
