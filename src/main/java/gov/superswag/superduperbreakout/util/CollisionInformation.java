package gov.superswag.superduperbreakout.util;

import java.util.Objects;
import javafx.geometry.Bounds;

/**
 * Information about a collision between two objects
 *
 * @param hitBox        The box that was hit (e.g. a brick)
 * @param hittingBox    The box that is hitting (e.g. the ball)
 * @param collisionSide What side of the hitbox was collided with
 */
public record CollisionInformation(Bounds hitBox, Bounds hittingBox, CollisionSide collisionSide) {

  public enum CollisionSide { UNKNOWN, LEFT, RIGHT, TOP, BOTTOM }

  public CollisionInformation(Bounds hitBox, Bounds hittingBox) {
    this(hitBox, hittingBox, calculateCollisionSide(hitBox, hittingBox));
  }

  /**
   * Guess what side the collision happened on.
   *
   * @param hitBox The box that was hit (e.g. a brick)
   * @param hittingBox The box that is hitting (e.g. the ball)
   * @return The collision side
   */
  public static CollisionSide calculateCollisionSide(Bounds hitBox, Bounds hittingBox) {

    if (hittingBox.getCenterY() < hitBox.getMinY()) {
      return CollisionSide.TOP;
    } else if (hittingBox.getCenterY() > hitBox.getMaxY()) {
      return CollisionSide.BOTTOM;
    } else if (hittingBox.getCenterX() < hitBox.getMinX()) {
      return CollisionSide.LEFT;
    } else if (hittingBox.getCenterX() > hitBox.getMaxX()) {
      return CollisionSide.RIGHT;
    }

    return CollisionSide.UNKNOWN;
  }

  @Override
  public String toString() {
    return "CollisionInformation{" + "hitBox=" + hitBox + ", hittingBox=" + hittingBox + '}';
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CollisionInformation that = (CollisionInformation) o;

    return Objects.equals(hitBox, that.hitBox)
        && Objects.equals(hittingBox, that.hittingBox);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hitBox, hittingBox);
  }
}
