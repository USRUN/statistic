package usrun.repository;

import java.util.List;
import usrun.dto.UserDTO;

/**
 * @author phuctt4
 */
public interface UserRepository {
  List<UserDTO> getAllUserDTO();
}
