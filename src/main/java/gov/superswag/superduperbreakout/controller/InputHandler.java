package gov.superswag.superduperbreakout.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Manages everything about keyboard input and movement.
 */
public class InputHandler {

  private static final ArrayList<KeyCode> pressedInput = new ArrayList<>();

  public static final Collection<KeyCode> movementKeysL = List.of(KeyCode.LEFT, KeyCode.A);
  public static final Collection<KeyCode> movementKeysR = List.of(KeyCode.RIGHT, KeyCode.D);

  public static void keyPressed(KeyEvent e) {
    KeyCode code = e.getCode();
    if (!pressedInput.contains(code)) {
      pressedInput.add(code);
    }
  }

  public static void keyReleased(KeyEvent e) {
    KeyCode code = e.getCode();
    pressedInput.remove(code);
  }

  public static boolean isPressed(KeyCode code) {
    return pressedInput.contains(code);
  }

  public static boolean leftPressed() {
    return pressedInput.stream().anyMatch(movementKeysL::contains);
  }

  public static boolean rightPressed() {
    return pressedInput.stream().anyMatch(movementKeysR::contains);
  }

  public static InputDirection getInputDirection() {

    boolean left = leftPressed();
    boolean right = rightPressed();

    if (left && right) {
      return InputDirection.BOTH;
    } else if (left) {
      return InputDirection.LEFT;
    } else if (right) {
      return InputDirection.RIGHT;
    } else {
      return InputDirection.NONE;
    }

  }

  /** Represents which of the movement keys are being pressed. */
  public enum InputDirection {
    NONE,
    LEFT,
    RIGHT,
    /** In the case where opposite directions are being pressed at the same time */
    BOTH
  }
}
