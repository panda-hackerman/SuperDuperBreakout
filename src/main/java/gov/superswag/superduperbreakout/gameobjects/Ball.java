package gov.superswag.superduperbreakout.gameobjects;

import gov.superswag.superduperbreakout.SuperDuperBreakout;
import gov.superswag.superduperbreakout.util.CollisionInformation;
import gov.superswag.superduperbreakout.util.MathHelper;
import gov.superswag.superduperbreakout.util.Vector2;
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {

  /** (In pixels per second) */
  public static final double SPEED = 300;
  public static final double RADIUS = 4.0;

  public static final double minX = 0 + RADIUS;
  public static final double minY = 0 + RADIUS;
  public static final double maxX = SuperDuperBreakout.GAMEPLAY_WINDOW_WIDTH - RADIUS;
  public static final double maxY = SuperDuperBreakout.GAMEPLAY_WINDOW_HEIGHT - RADIUS;

  private final AnimationTimer animationTimer;
  private final Circle circle;

  private Vector2 lastFramePosition;
  private Vector2 position;
  private Vector2 direction = Vector2.fromAngle(Math.toRadians(45), SPEED);

  private long lastUpdateTime = 0;

  Paddle paddle = SuperDuperBreakout.instance.paddle;
  BrickGrid bricks = SuperDuperBreakout.instance.bricks;

  public Ball(double x, double y) {

    circle = new Circle(x, y, RADIUS, Color.WHITE);

    lastFramePosition = Vector2.ZERO;
    position = new Vector2(x, y);

    //Start the update loop
    animationTimer = new AnimationTimer() {
      @Override
      public void handle(long currentTime) {
        onAnimUpdate(currentTime);
      }
    };
    animationTimer.start();
  }

  public Circle getCircle() {
    return circle;
  }

  /** Delegate method to stop the animation timer. Prevents the update loop from running*/
  public void stopUpdateLoop() {
    animationTimer.stop();
  }

  private void onAnimUpdate(long now) {

    if (lastUpdateTime == 0) {
      lastUpdateTime = now;
      return; //Ignore first frame
    }

    double elapsedSeconds = MathHelper.nanoToSeconds(now - lastUpdateTime); //deltaTime
    Vector2 movementAmount = direction.scale(elapsedSeconds); //delta
    move(movementAmount);
    checkCollision();

    lastUpdateTime = now;
  }

  private void move(Vector2 direction) {

    double x = position.x() + direction.x();
    double y = position.y() + direction.y();

    lastFramePosition = position;
    position = new Vector2(
        MathHelper.clamp(x, minX, maxX),
        MathHelper.clamp(y, minY, maxY));

    circle.setCenterX(position.x());
    circle.setCenterY(position.y());

  }

  private void checkCollision() {

    //SIDE WALL
    if (position.x() == minX || position.x() == maxX) { //L and R
      direction = direction.flipX();
    }

    //TOP WALL
    if (position.y() == minY) {
      direction = direction.flipY();
    }

    //BOTTOM WALL (you lose!)
    if (position.y() == maxY) {
      System.out.println("Game over!");
      SuperDuperBreakout.instance.endGame();
      return;
    }

    //A BRICK
    Bounds ballHitbox = circle.getBoundsInParent();
    CollisionInformation brickCollision = bricks.getCollision(ballHitbox);

    if (brickCollision != null) {

      //Switch based on where it hit
      direction = switch (brickCollision.collisionSide()) {
        case LEFT, RIGHT -> direction.flipX();
        case TOP, BOTTOM -> direction.flipY();
        case UNKNOWN -> {
          System.out.println("UNKNOWN DIRECTION... Flip y by default"); //TODO: what?
          yield direction.flipY();
        }
      };

      return; //Can't hit a brick & the paddle on the same frame (unless something is very wrong!)
    }

    //PADDLE
    double paddleX = paddle.position.x();
    double paddleY = paddle.position.y();

    double paddleMinX = paddleX - RADIUS;
    double paddleMinY = paddleY - RADIUS;
    double paddleMaxX = paddleX + Paddle.PADDLE_WIDTH + RADIUS;
    double paddleMaxY = paddleY + Paddle.PADDLE_HEIGHT + RADIUS;

    //If ball is "inside" the paddle.
    if (position.y() > paddleMinY && position.y() < paddleMaxY &&
        position.x() > paddleMinX && position.x() < paddleMaxX) {

      //Set ball outside of paddle, so we don't count collision twice
      position = position.setY(paddleMinY);

      //Calculate the normal of the paddle.
      Vector2 normal = calculateCollisionNormal(paddleX);

      //Calculate the direction
      direction = MathHelper.calculateReflectionVector(direction, normal);
    }

  }

  /** Calculate the normal for the collision between */
  private Vector2 calculateCollisionNormal(double paddleX) {

    double paddleMiddle = paddleX + (Paddle.PADDLE_WIDTH / 2.0); //Middle pos, instead of top left
    double distance = position.x() - paddleMiddle; //Distance from the middle to the collision point
    double x = distance / (2 * Paddle.PADDLE_WIDTH); //Normalizes

    //If the distance is 0 (in the middle) then the vector will be (0, -1) (the same as Vector2.UP)
    //which makes MathHelper.calculateReflectionVector return a value equivalent to flipping the y
    //value (in code, that would be `direction.setY(-y)`).
    //In the case where it's normalized to be between -1 and 1:
    //If it's at the farthest left, the vector will be (-1, -1), and normalized to ~(-0.7, -0.7),
    //which has an angle of -135 (inverse of 45)
    //Normalizing between different values (e.g -0.5 to 0.5 instead) makes the angle less steep.
    return new Vector2(x, -1).normalize();
  }
}
