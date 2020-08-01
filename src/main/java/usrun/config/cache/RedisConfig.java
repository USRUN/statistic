package usrun.config.cache;

import lombok.Getter;
import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

  private String url;
  private String password;

  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    config.useSingleServer()
        .setTimeout(10000000)
        .setAddress(url)
        .setPassword(password)
        .setConnectionPoolSize(10).setConnectionMinimumIdleSize(10);
    config.setCodec(StringCodec.INSTANCE);
//    KryoCodec kryoCodec = new KryoCodecWithDefaultSerializer();
//    config.setCodec(kryoCodec);
    return Redisson.create(config);
  }
}
