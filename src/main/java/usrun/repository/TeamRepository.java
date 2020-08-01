package usrun.repository;

import java.util.List;
import usrun.model.Team;

public interface TeamRepository {

  List<Team> findAllTeam();
}
