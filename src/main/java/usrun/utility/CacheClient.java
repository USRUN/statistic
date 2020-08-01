package usrun.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import usrun.dto.TeamStatDTO;

/**
 * @author phuctt4
 */
@Component
public class CacheClient {

  private final RedissonClient redissonClient;

  private final CacheKeyGenerator cacheKeyGenerator;

  public CacheClient(RedissonClient redissonClient, CacheKeyGenerator cacheKeyGenerator) {
    this.redissonClient = redissonClient;
    this.cacheKeyGenerator = cacheKeyGenerator;
  }

  public void setTeamStat(List<TeamStatDTO> teamStat) {
    RBucket<String> rBucket = redissonClient
        .getBucket(cacheKeyGenerator.keyTeamStat());
    rBucket.set(ObjectUtils.toJsonString(teamStat), 7, TimeUnit.DAYS);
  }

  public List<TeamStatDTO> getTeamStat() {
    RBucket<String> rBucket = redissonClient
        .getBucket(cacheKeyGenerator.keyTeamStat());
    return ObjectUtils.fromJsonString(rBucket.get(),
        new TypeReference<List<TeamStatDTO>>() {
        });
  }
}
