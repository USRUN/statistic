package usrun.model.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SportType {
  OTHER,
  CYCLING,
  RUNNING,
  WALKING,
  TRIATHLON,
  SWIMMING;

  @JsonValue
  public int toValue() {
    return ordinal();
  }
}
