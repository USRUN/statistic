package usrun.repository.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import usrun.dto.UserDTO;
import usrun.repository.UserRepository;

/**
 * @author phuctt4
 */

@Repository
public class UserRepositoryImpl implements UserRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public UserRepositoryImpl(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public List<UserDTO> getAllUserDTO() {
    MapSqlParameterSource params = new MapSqlParameterSource();
    String sql = "SELECT userId, displayName, avatar FROM user";
    return getUserDTOs(sql, params);
  }

  private List<UserDTO> getUserDTOs(String sql, MapSqlParameterSource params) {
    return namedParameterJdbcTemplate.query(sql, params, (rs, i) ->
        new UserDTO(
            rs.getLong("userId"),
            rs.getString("displayName"),
            rs.getString("avatar")));
  }
}
