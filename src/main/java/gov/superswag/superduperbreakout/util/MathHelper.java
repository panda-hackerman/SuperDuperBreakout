package gov.superswag.superduperbreakout.util;

public class MathHelper {

  /** Clamps a value between a given minimum and maximum. */
  public static <T extends Comparable<T>> T clamp(T value, T min, T max) {

    if (max.compareTo(min) < 0) {
      throw new IllegalArgumentException("Max must be greater than (or equal to) min");
    }

    if (value.compareTo(min) < 0) {
      return min;
    } else if (value.compareTo(max) > 0) {
      return max;
    } else {
      return value;
    }
  }

  /**
   * Convert nanoseconds to seconds (divide by 1 billion)
   * @param nano The amount of nanoseconds
   */
  public static double nanoToSeconds(double nano) {
    return nano / 1_000_000_000.0;
  }

}
