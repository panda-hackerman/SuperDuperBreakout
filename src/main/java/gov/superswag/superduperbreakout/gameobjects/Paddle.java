package gov.superswag.superduperbreakout.gameobjects;

import static javafx.scene.input.KeyCode.*;

import gov.superswag.superduperbreakout.SuperDuperBreakout;
import gov.superswag.superduperbreakout.controller.InputHandler;
import gov.superswag.superduperbreakout.controller.InputHandler.InputDirection;
import gov.superswag.superduperbreakout.util.MathHelper;
import gov.superswag.superduperbreakout.util.Vector2;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Paddle {

  /** Speed in pixels per second */
  public static final double SPEED = 200;

  public static final int PADDLE_WIDTH = 75;
  public static final int PADDLE_HEIGHT = 20;
  /** The paddle's distance from the bottom */
  public static final int PADDLE_Y = 75;

  final double minX = 0;
  final double maxX = SuperDuperBreakout.GAMEPLAY_WINDOW_WIDTH - PADDLE_WIDTH;

  private final Rectangle rect;

  private Vector2 position;
  private double velocity = 0;
  private long lastUpdateTime = 0;

  public Paddle(double x, double y) {

    position = new Vector2(x, y);
    rect = new Rectangle(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
    rect.setFill(Color.WHITE);

    AnimationTimer animationTimer = new AnimationTimer() {
      @Override
      public void handle(long currentTime) {
        handleAnimation(currentTime);
      }
    };
    animationTimer.start();
  }

  public Shape getRect() {
    return rect;
  }

  public void handleAnimation(long now) {

    //What keys are being pressed?
    velocity = switch (InputHandler.getInputDirection()) {
      case LEFT -> -SPEED;
      case RIGHT -> SPEED;
      case NONE, BOTH -> 0.0;
    };

    //Calculate movement
    if (lastUpdateTime > 0) {
      double elapsedSeconds = MathHelper.nanoToSeconds(now - lastUpdateTime);
      double deltaX = elapsedSeconds * velocity; //How much we should move
      move(deltaX);
    }

    lastUpdateTime = now;
  }

  public void move(double moveX) {

    //Add movement to current position, and clamp to bounds.
    double newX = MathHelper.clamp(position.x() + moveX, minX, maxX);

    position = new Vector2(newX, position.y());

    rect.setX(position.x());
    rect.setY(position.y());
  }

}