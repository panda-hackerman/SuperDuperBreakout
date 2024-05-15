package gov.superswag.superduperbreakout.gameobjects;

import gov.superswag.superduperbreakout.SuperDuperBreakout;
import gov.superswag.superduperbreakout.controller.InputHandler;
import gov.superswag.superduperbreakout.controller.InputHandler.InputDirection;
import gov.superswag.superduperbreakout.util.MathHelper;
import gov.superswag.superduperbreakout.util.Vector2;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/** The main paddle (player) */
public class Paddle {

  /** (In pixels per second) */
  public static final double SPEED = 300;

  public static final double PADDLE_WIDTH = 70;
  public static final double PADDLE_HEIGHT = 7.5f;
  /** The paddle's distance from the bottom */
  public static final int PADDLE_Y = 75;

  final double minX = 0;
  final double maxX = SuperDuperBreakout.GAMEPLAY_WINDOW_WIDTH - PADDLE_WIDTH;

  private final AnimationTimer animationTimer;
  private final Rectangle rect;

  Vector2 position;
  private long lastUpdateTime = 0;

  public Paddle(double x, double y) {

    rect = new Rectangle(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
    rect.setFill(Color.LIGHTBLUE);

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

  public Shape getRect() {
    return rect;
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
    double movementAmount = elapsedSeconds * getVelocity(); //deltaX
    moveHorizontal(movementAmount);

    lastUpdateTime = now;
  }

  /**
   * Get velocity based on the input direction.
   *
   * @see InputHandler
   */
  private double getVelocity() {
    InputDirection input = InputHandler.getInputDirection();
    return switch (input) {
      case LEFT -> -SPEED;
      case RIGHT -> SPEED;
      case NONE, BOTH -> 0.0;
    };
  }

  /**
   * Move a certain amount in the horizontal direction
   *
   * @param moveX The amount of pixels to move
   */
  private void moveHorizontal(double moveX) {

    //Add movement to current position, and clamp to bounds.
    double newX = MathHelper.clamp(position.x() + moveX, minX, maxX);

    position = new Vector2(newX, position.y());

    rect.setX(position.x());
    rect.setY(position.y());
  }

}