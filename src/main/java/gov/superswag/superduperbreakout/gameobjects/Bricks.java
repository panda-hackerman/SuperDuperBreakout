package gov.superswag.superduperbreakout.gameobjects;

import static gov.superswag.superduperbreakout.SuperDuperBreakout.GAMEPLAY_WINDOW_WIDTH;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.Nullable;

public class Bricks {

  public static final int ROWS = 8;
  public static final int COLUMNS = 8;
  public static final Paint[] paint = new Paint[ROWS];

  public static final int HGAP = 3;
  public static final int VGAP = 3;
  public static final int TOP_PADDING = 40;

  public static final int BRICK_HEIGHT = 5;
  public static final int BRICK_WIDTH = (int) (GAMEPLAY_WINDOW_WIDTH / (COLUMNS + 1));

  private final Brick[][] bricks;

  private final GridPane pane;

  public Bricks() {

    //Initialize
    pane = new GridPane(HGAP, VGAP);

    this.bricks = new Brick[COLUMNS][ROWS];

    for (int i = 0; i < COLUMNS; i++) {
      for (int j = 0; j < ROWS; j++) {

        Brick brick = new Brick(i, j);
        bricks[i][j] = brick;

        pane.add(brick.rect, i, j);
      }
    }

    double bricksSpaceTotal = (COLUMNS * BRICK_WIDTH) + (HGAP * (COLUMNS - 1));
    double remaining = (int) (GAMEPLAY_WINDOW_WIDTH - bricksSpaceTotal);

    pane.setPadding(new Insets(TOP_PADDING, remaining / 2, 0,remaining / 2));
  }

  /** Returns the brick this bounding box collides with, or null if no collision is occurring. */
  public @Nullable Brick isColliding(Bounds other) {

    for (int i = 0; i < COLUMNS; i++) {
      for (int j = 0; j < ROWS; j++) {

        Brick brick = bricks[i][j];

        if (brick.visible && brick.rect.getBoundsInParent().intersects(other)) {
          return brick;
        }

      }
    }

    return null;
  }

  public GridPane getPane() {
    return pane;
  }

  /** A single brick */
  public static class Brick {

    public final int col;
    public final int row;
    private boolean visible = true;
    public Rectangle rect;

    public Brick(int col, int row) {
      this.col = col;
      this.row = row;

      rect = new Rectangle(0, 0, BRICK_WIDTH, BRICK_HEIGHT);
      rect.setFill(Color.WHITE);
      GridPane.setFillWidth(rect, true);
    }

    public void setVisible() {
      this.visible = true;
      rect.setFill(Color.WHITE);
    }

    public void setInvisible() {
      this.visible = false;
      rect.setFill(Color.TRANSPARENT);
    }

    public void onCollision() {
      setInvisible();
    }
  }

}
