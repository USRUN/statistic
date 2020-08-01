package usrun.repository;

import java.util.List;
import usrun.model.EventParticipant;

/**
 * @author phuctt4
 */

public interface EventParticipantRepository {

  List<EventParticipant> findAll();
}
