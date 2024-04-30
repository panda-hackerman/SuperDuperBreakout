package gov.superswag.superduperbreakout;

import gov.superswag.superduperbreakout.game_objects.Paddle;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class SuperDuperBreakout extends Application {

  public static final double GAMEPLAY_WINDOW_WIDTH = 300;
  public static final double GAMEPLAY_WINDOW_HEIGHT = 500;

  Scene mainGameplayScene;
  Paddle paddle;

  @Override
  public void start(Stage primaryStage) throws IOException {

    mainGameplayScene = buildGameplayScene();

    //Stuff
    primaryStage.setTitle("Super Super Breakout");
    primaryStage.setScene(mainGameplayScene);
    primaryStage.setResizable(false);
    primaryStage.show();

    paddle.getRect().requestFocus();
  }

  private Scene buildGameplayScene() {

    Pane pane = new Pane();
    pane.setBackground(Background.fill(Color.BLACK));

    double paddleX = (GAMEPLAY_WINDOW_WIDTH / 2f) - (Paddle.PADDLE_WIDTH / 2f);
    double paddleY = GAMEPLAY_WINDOW_HEIGHT - 75;

    paddle = new Paddle(paddleX, paddleY);
    Shape paddleRect = paddle.getRect();

    pane.getChildren().add(paddleRect);

    return new Scene(pane, GAMEPLAY_WINDOW_WIDTH, GAMEPLAY_WINDOW_HEIGHT);
  }

  public static void main(String[] args) {
    launch();
  }

}