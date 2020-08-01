package usrun.model.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;

public enum RoleType {
  ROLE_USER(1),
  ROLE_ADMIN(2)
  ;

  private int value;
  private static final HashMap<Integer, RoleType> returnMap = new HashMap<>();

  static {
    for (RoleType role : RoleType.values()) {
      returnMap.put(role.value, role);
    }
  }

  RoleType(int value) {
    this.value = value;
  }

  @JsonCreator
  public static RoleType fromInt(int iValue) {
    return returnMap.get(iValue);
  }

  @JsonValue
  public int toValue() {
    return this.value;
  }
}
