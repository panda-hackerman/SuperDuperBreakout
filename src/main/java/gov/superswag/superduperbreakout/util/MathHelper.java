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
   * Convert nanoseconds to seconds
   * @param nano The amount of nanoseconds
   */
  public static double nanoToSeconds(double nano) {
    return nano / (1e9); //(divide by 1 billion)
  }

  /**
   * Calculate the reflection of a Vector2 against a surface.
   *
   * @param vector The incoming direction
   * @param normal The normal of the surface of collision. Will point between d and r (the returned
   *               value). If this value is {@link Vector2#UP}, the result is equivalent to
   *               inverting the Y value. Behavior is not defined if this value is equal to (0, 0).
   * @return The calculated reflection
   */
  public static Vector2 calculateReflectionVector(Vector2 vector, Vector2 normal) {

    normal = normal.normalize(); //Ensure it's normalized

    //We're using the formula r = d - 2 (d * n) n.
    //d is the vector (direction), n is the normal, and d * n is the dot product.
    double dx = vector.x();
    double dy = vector.y();
    double nx = normal.x();
    double ny = normal.y();
    double dn = Vector2.dotProduct(normal, vector);

    double x = dx - 2 * dn * nx;
    double y = dy - 2 * dn * ny;

    return new Vector2(x, y);
  }

}
