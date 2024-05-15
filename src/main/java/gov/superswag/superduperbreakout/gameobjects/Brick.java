package gov.superswag.superduperbreakout.gameobjects;

import gov.superswag.superduperbreakout.SuperDuperBreakout;
import java.util.Objects;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

/**
 * A single brick
 */
public class Brick {

  public final BrickLevel level;
  private boolean visible = true;
  public Rectangle rect;

  public Brick(BrickLevel level) {

    this.rect = new Rectangle(0, 0, BrickGrid.BRICK_WIDTH, BrickGrid.BRICK_HEIGHT);
    this.level = level;

    rect.setFill(level.color);

    GridPane.setFillWidth(rect, true);
  }

  public boolean isVisible() {
    return visible;
  }

  public void onCollision() {
    this.visible = false;
    rect.setFill(Color.TRANSPARENT);
    SuperDuperBreakout.instance.scorePoint(level.numPoints);
  }

  /** The "level" of a brick is its color and how many points it is worth. This only exists so that
   * the colors & points are intrinsically linked; and so we don't need two separate lists (which
   * could be different lengths, which is confusing)*/
  public record BrickLevel(Paint color, int numPoints) {

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      BrickLevel that = (BrickLevel) o;
      return numPoints == that.numPoints && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
      return Objects.hash(color, numPoints);
    }

  }

}