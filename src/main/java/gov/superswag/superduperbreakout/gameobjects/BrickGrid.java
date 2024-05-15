package gov.superswag.superduperbreakout.gameobjects;

import static gov.superswag.superduperbreakout.SuperDuperBreakout.GAMEPLAY_WINDOW_WIDTH;

import gov.superswag.superduperbreakout.gameobjects.Brick.BrickLevel;
import gov.superswag.superduperbreakout.util.CollisionInformation;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Nullable;

public class BrickGrid {

  public static final int ROWS = 12;
  public static final int COLUMNS = 8;

  public static final int H_GAP = 3;
  public static final int V_GAP = 3;
  public static final int TOP_PADDING = 40;
  public static final int BRICK_HEIGHT = 5;
  public static final int BRICK_WIDTH = (int) (GAMEPLAY_WINDOW_WIDTH / (COLUMNS + 1));

  /** From top to bottom, the colors the bricks are (and how many points they are worth). Try to
   *  keep rows divisible by this list's index (it doesn't have to be, just looks better) */
  public static final List<BrickLevel> BRICK_LEVELS = List.of(
      new BrickLevel(Color.RED, 10),
      new BrickLevel(Color.ORANGE, 5),
      new BrickLevel(Color.YELLOW, 4),
      new BrickLevel(Color.GREEN, 3),
      new BrickLevel(Color.BLUE, 2),
      new BrickLevel(Color.PURPLE,1));

  private final Brick[][] bricks;

  private final GridPane pane;

  public BrickGrid() {

    pane = new GridPane(H_GAP, V_GAP);
    this.bricks = new Brick[COLUMNS][ROWS];

    for (int col = 0; col < COLUMNS; col++) {
      for (int row = 0; row < ROWS; row++) {

        Brick brick = new Brick(getLevel(row));
        bricks[col][row] = brick;

        pane.add(brick.rect, col, row);
      }
    }


    double bricksSpaceTotal = (COLUMNS * BRICK_WIDTH) + (H_GAP * (COLUMNS - 1));
    double remaining = (int) (GAMEPLAY_WINDOW_WIDTH - bricksSpaceTotal);

    pane.setPadding(new Insets(TOP_PADDING, remaining / 2, 0,remaining / 2));
  }

  private BrickLevel getLevel(int row) {

    int index;

    if (BRICK_LEVELS.size() >= ROWS) {
      //If there are more or equal colors than rows, every row can be different
      index = row;
    } else {
      //Otherwise, divide 'em up!
      double bricksPerColor = (double) ROWS / BRICK_LEVELS.size();
      index = (int) (Math.ceil((row + 1) / bricksPerColor)) - 1;
    }

    return BRICK_LEVELS.get(index);
  }

  public GridPane getPane() {
    return pane;
  }

  /** Returns the brick this bounding box collides with, or null if no collision is occurring. */
  public @Nullable CollisionInformation getCollision(Bounds other) {

    for (int i = 0; i < COLUMNS; i++) {
      for (int j = 0; j < ROWS; j++) {

        Brick brick = bricks[i][j];
        Bounds hitbox = brick.rect.getBoundsInParent();

        if (brick.isVisible() && hitbox.intersects(other)) {
          brick.onCollision();
          return new CollisionInformation(hitbox, other);
        }

      }
    }

    return null;
  }

}
