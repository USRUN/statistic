package usrun.repository;

import java.util.List;
import usrun.dto.TeamDTO;
import usrun.model.Team;

public interface TeamRepository {

  List<Team> findAllTeam();

  List<TeamDTO> findAllTeamDTO();
}
