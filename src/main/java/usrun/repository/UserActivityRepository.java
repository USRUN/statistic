package usrun.repository;

import java.util.List;
import usrun.dto.UserActivityStatDTO;

public interface UserActivityRepository {

  List<UserActivityStatDTO> getStat();
}
