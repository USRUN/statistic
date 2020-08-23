package usrun.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author phuctt4
 */

@Component
public class CacheKeyGenerator {

  private final String PREFIX;

  public CacheKeyGenerator(@Value("${spring.profiles.active}") String env) {
    this.PREFIX = env + ":";
  }

  public String keyTeamStat() {
    return PREFIX + "team:stat:";
  }
}
