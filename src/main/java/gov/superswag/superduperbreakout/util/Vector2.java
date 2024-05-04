package gov.superswag.superduperbreakout.util;

/**
 * Immutable 2D Vector implementation. Represents a 2-dimensional vector using two double
 * (floating-point) numbers.
 *
 * @author Eli Michaud
 * @since 2024/04/26
 */
public record Vector2(double x, double y) {

  public static final Vector2 ZERO = new Vector2(0, 0);
  public static final Vector2 UP = new Vector2(0, -1);
  public static final Vector2 DOWN = new Vector2(0, 1);
  public static final Vector2 RIGHT = new Vector2(1, 0);
  public static final Vector2 LEFT = new Vector2(-1, 0);

  /**
   * Adds each element (x, y) from a given vector to this vector. For example, if the vector (5, 10)
   * is added to (10, -3), the result will be (15, 7)
   */
  public Vector2 plus(Vector2 other) {
    double x = this.x + other.x;
    double y = this.y + other.y;

    return new Vector2(x, y);
  }

  /**
   * Returns this vector multiplied by a constant scalar
   */
  public Vector2 multiply(double scalar) {
    double x = this.x * scalar;
    double y = this.y * scalar;

    return new Vector2(x, y);
  }

  /**
   * Returns a normalized version of this vector
   */
  public Vector2 normalize() {

    double length = length();

    if (length == 0) {
      return this;
    }

    double x = this.x / length;
    double y = this.y / length;
    return new Vector2(x, y);
  }

  /**
   * The length of this vector
   */
  public double length() {

    //If x or y are 0, we can skip the inefficient square root calculation
    if (x == 0) {
      return Math.abs(y);
    }
    if (y == 0) {
      return Math.abs(x);
    } else {
      //Square root of the sum of the squares of the x & y components.
      return Math.sqrt((x * x) + (y * y));
    }

  }

  public static double distance(Vector2 a, Vector2 b) {

    double v0 = b.x - a.x;
    double v1 = b.y - a.y;

    return Math.sqrt((v0 * v0) + (v1 * v1));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Vector2 vector2 = (Vector2) o;
    return Double.compare(x, vector2.x) == 0 && Double.compare(y, vector2.y) == 0;
  }

  @Override
  public String toString() {
    return "Vector2{" +
        "x=" + x +
        ", y=" + y +
        '}';
  }

}

