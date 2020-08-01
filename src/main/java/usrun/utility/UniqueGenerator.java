package usrun.utility;

import java.util.Date;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class UniqueGenerator {

  public String randomString(int length) {
    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'
    Random random = new Random();

    return random.ints(leftLimit, rightLimit + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

  public Long generateTrackId(Long userId) {
    return Long.parseLong(Long.toString(new Date().getTime() / 1000) + userId);
  }
}
