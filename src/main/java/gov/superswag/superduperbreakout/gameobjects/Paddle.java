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
  public static final double SPEED = 200;

  public static final int PADDLE_WIDTH = 75;
  public static final int PADDLE_HEIGHT = 10;
  /** The paddle's distance from the bottom */
  public static final int PADDLE_Y = 75;

  final double minX = 0;
  final double maxX = SuperDuperBreakout.GAMEPLAY_WINDOW_WIDTH - PADDLE_WIDTH;

  Vector2 position;

  private final Rectangle rect;
  private long lastUpdateTime = 0;

  public Paddle(double x, double y) {

    rect = new Rectangle(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
    rect.setFill(Color.WHITE);

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

  public Shape getRect() {
    return rect;
  }

  public void onAnimUpdate(long now) {

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
   * @see InputHandler
   */
  public double getVelocity() {
    InputDirection input = InputHandler.getInputDirection();
    return switch (input) {
      case LEFT -> -SPEED;
      case RIGHT -> SPEED;
      case NONE, BOTH -> 0.0;
    };
  }

  public void moveHorizontal(double moveX) {

    //Add movement to current position, and clamp to bounds.
    double newX = MathHelper.clamp(position.x() + moveX, minX, maxX);

    position = new Vector2(newX, position.y());

    rect.setX(position.x());
    rect.setY(position.y());
  }

}