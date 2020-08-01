package usrun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author phuctt4
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipant {
  private long eventId;
  private long userId;
  private long teamId;
  private long distance;
}
