package usrun.model.junction;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import usrun.model.type.TeamMemberType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamMember {

  private Long teamId;
  private Long userId;
  private TeamMemberType teamMemberType;
  private Date addTime;

  public TeamMember(Long teamId, Long userId, int teamMemberType, Date addTime) {
    this.teamId = teamId;
    this.userId = userId;
    this.teamMemberType = TeamMemberType.fromInt(teamMemberType);
    this.addTime = addTime;
  }
}
