package usrun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import usrun.model.Team;

/**
 * @author phuctt4
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {

  private Long teamId;

  private String teamName;

  private String thumbnail;
}
