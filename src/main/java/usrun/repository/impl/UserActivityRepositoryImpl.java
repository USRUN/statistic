package usrun.repository.impl;

import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import usrun.model.UserActivity;
import usrun.dto.UserActivityStatDTO;
import usrun.repository.UserActivityRepository;
import usrun.utility.ObjectUtils;

@Repository
public class UserActivityRepositoryImpl implements UserActivityRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public UserActivityRepositoryImpl(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  private MapSqlParameterSource getMapUserActivity(UserActivity userActivity) {
    MapSqlParameterSource map = new MapSqlParameterSource();
    map.addValue("userActivityId", userActivity.getUserActivityId());
    map.addValue("userId", userActivity.getUserId());
    map.addValue("createTime", userActivity.getCreateTime());
    map.addValue("totalDistance", userActivity.getTotalDistance());
    map.addValue("totalTime", userActivity.getTotalTime());
    map.addValue("totalStep", userActivity.getTotalStep());
    map.addValue("avgPace", userActivity.getAvgPace());
    map.addValue("avgHeart", userActivity.getAvgHeart());
    map.addValue("maxHeart", userActivity.getMaxHeart());
    map.addValue("calories", userActivity.getCalories());
    map.addValue("elevGain", userActivity.getElevGain());
    map.addValue("elevMax", userActivity.getElevMax());
    map.addValue("photo", ObjectUtils.toJsonString(userActivity.getPhotos()));
    map.addValue("title", userActivity.getTitle());
    map.addValue("description", userActivity.getDescription());
    map.addValue("totalLove", userActivity.getTotalLove());
    map.addValue("totalComment", userActivity.getTotalComment());
    map.addValue("totalShare", userActivity.getTotalShare());
    map.addValue("processed", userActivity.isProcessed());
    map.addValue("deleted", userActivity.getDeleted());
    map.addValue("privacy", userActivity.getPrivacy());
    return map;
  }

  @Override
  public List<UserActivityStatDTO> getStat() {
    String sql = "SELECT userId, SUM(totalDistance) as totalDistance, "
        + "SUM(totalTime) as totalTime, MAX(totalDistance) as maxDistance, "
        + "MAX(totalTime) as maxTime, COUNT(userActivityId) as userActivityCount "
        + "FROM userActivity GROUP BY userId";
    return namedParameterJdbcTemplate.query(sql, (rs, i) -> new UserActivityStatDTO(
        rs.getLong("userId"),
        rs.getLong("totalDistance"),
        rs.getLong("totalTime"),
        rs.getLong("maxTime"),
        rs.getLong("maxDistance"),
        rs.getLong("userActivityCount")
    ));
  }

}
