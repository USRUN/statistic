package usrun.repository;

import java.util.List;
import usrun.model.junction.TeamMember;
import usrun.model.type.TeamMemberType;
import usrun.dto.TeamStatDTO;

public interface TeamMemberRepository {

  List<TeamStatDTO> getTeamStat();

  List<TeamMember> getAll();

  List<TeamMember> getAllByLessEqualTeamMemberType(TeamMemberType teamMemberType);

}
