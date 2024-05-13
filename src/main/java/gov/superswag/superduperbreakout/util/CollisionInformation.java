package gov.superswag.superduperbreakout.util;

import javafx.geometry.Bounds;

public class CollisionInformation {

  /** The box that was hit */
  Bounds hitBox;
  /** The box that is hitting*/
  Bounds hittingBox;

  /** What side of the hitbox was collided with */
  CollisionSide collisionSide;

  public void calculateCollisionSide() {

    //TODO: The answer is in the dot product, Luke!

  }

  public enum CollisionSide {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM
  }
}
