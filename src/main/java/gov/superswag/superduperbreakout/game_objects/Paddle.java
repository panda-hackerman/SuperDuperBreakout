package gov.superswag.superduperbreakout.game_objects;

import gov.superswag.superduperbreakout.SuperDuperBreakout;
import gov.superswag.superduperbreakout.util.Vector2;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Paddle {

  public static final double SPEED = 10;
  public static final int PADDLE_WIDTH = 75;
  public static final int PADDLE_HEIGHT = 20;
  private final Rectangle rect;

  private Vector2 position;

  public Paddle(double x, double y) {
    position = new Vector2(x, y);
    rect = new Rectangle(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
    rect.setFill(Color.WHITE);

    rect.setOnKeyPressed(this::handleKeyboardInput);
  }

  private void handleKeyboardInput(KeyEvent event) {

    System.out.println("KEY PRESSED: " + event.getCode());

    Vector2 direction;

    switch (event.getCode()) {
      case LEFT, A, J -> direction = Vector2.LEFT.multiply(SPEED);
      case RIGHT, D, L -> direction = Vector2.RIGHT.multiply(SPEED);
      default -> {
        return;
      }
    }

    move(direction);
  }

  public Shape getRect() {
    return rect;
  }

  public void move(Vector2 direction) {

    Vector2 newPos = position.plus(direction);

    double boundsL = 0;
    double boundsR = SuperDuperBreakout.GAMEPLAY_WINDOW_WIDTH - PADDLE_WIDTH;

    if (newPos.x() < boundsL || newPos.x() > boundsR) {
      return; //Outside of bounds
    }

    //Set position
    position = newPos;
    rect.setX(newPos.x());
    rect.setY(newPos.y());
  }

}