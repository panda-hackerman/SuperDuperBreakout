package gov.superswag.superduperbreakout.gameobjects;

import gov.superswag.superduperbreakout.SuperDuperBreakout;
import gov.superswag.superduperbreakout.util.MathHelper;
import gov.superswag.superduperbreakout.util.Vector2;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {

  /** (In pixels per second) */
  public static final double SPEED = 250.0;
  public static final double RADIUS = 5.0;

  public static final double minX = 0 + RADIUS;
  public static final double minY = 0 + RADIUS;
  public static final double maxX = SuperDuperBreakout.GAMEPLAY_WINDOW_WIDTH - RADIUS;
  public static final double maxY = SuperDuperBreakout.GAMEPLAY_WINDOW_HEIGHT - RADIUS;

  private final Circle circle;

  private Vector2 position;
  private Vector2 direction = Vector2.fromAngle(Math.toRadians(45), SPEED);

  private long lastUpdateTime = 0;

  Paddle paddle;

  public Ball(double x, double y, Paddle paddle) {

    this.paddle = paddle;

    circle = new Circle(x, y, RADIUS, Color.WHITE);

    position = new Vector2(x, y);

    //Start the update loop
    AnimationTimer animationTimer = new AnimationTimer() {
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

  public void onAnimUpdate(long now) {

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

  public void move(Vector2 direction) {

    double x = position.x() + direction.x();
    double y = position.y() + direction.y();

    position = new Vector2(
        MathHelper.clamp(x, minX, maxX),
        MathHelper.clamp(y, minY, maxY));

    circle.setCenterX(position.x());
    circle.setCenterY(position.y());

  }

  public void checkCollision() {

    //If it hit the wall
    if (position.x() == minX || position.x() == maxX) {
      direction = new Vector2(direction.x() * -1, direction.y()); //Flip x
      System.out.println("Direction: " + direction + ", angle:" + Math.toDegrees(direction.angle()));
    }

    if (position.y() == minY || position.y() == maxY) {
      direction = new Vector2(direction.x(), direction.y() * -1); //Flip y
      System.out.println("Direction: " + direction + ", angle:" + Math.toDegrees(direction.angle()));
    }

    //TODO: Lose if hit the bottom wall

    //If it hit the paddle
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

      direction = MathHelper.calculateReflectionVector(direction, normal);

      System.out.println("Direction: " + direction + ", angle:" + Math.toDegrees(direction.angle()));

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
