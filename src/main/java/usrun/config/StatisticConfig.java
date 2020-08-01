package usrun.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author phuctt4
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "statistic")
public class StatisticConfig {

  private long teamIntervalInSeconds;
}
