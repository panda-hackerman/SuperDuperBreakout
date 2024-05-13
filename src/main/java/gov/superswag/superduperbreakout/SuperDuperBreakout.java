package gov.superswag.superduperbreakout;

import gov.superswag.superduperbreakout.controller.InputHandler;
import gov.superswag.superduperbreakout.gameobjects.Ball;
import gov.superswag.superduperbreakout.gameobjects.Bricks;
import gov.superswag.superduperbreakout.gameobjects.Paddle;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SuperDuperBreakout extends Application {

  public static final double GAMEPLAY_WINDOW_WIDTH = 300;
  public static final double GAMEPLAY_WINDOW_HEIGHT = 500;

  public static SuperDuperBreakout instance;

  Pane mainPane;
  Stage mainStage;
  Scene mainGameplayScene;

  public Paddle paddle;
  public Ball ball;
  public Bricks bricks;

  private int score;

  @Override
  public void start(Stage primaryStage) throws IOException {

    instance = this;
    mainStage = primaryStage;
    mainGameplayScene = buildGameplayScene();

    //Stuff
    primaryStage.setTitle("Super Super Breakout");
    primaryStage.setScene(mainGameplayScene);
    primaryStage.setResizable(false);
    primaryStage.show();

    mainGameplayScene.setOnKeyPressed(InputHandler::onKeyPressed);
    mainGameplayScene.setOnKeyReleased(InputHandler::onKeyReleased);
  }

  /**
   * Reset the game to its initial state. This is quick and dirty...
   * It also might cause a memory leak? lol
   */
  public void resetGame() {

    System.out.println("Resetting game...");

    ball.stop();
    paddle.stop();
    mainPane.getChildren().removeAll(ball.getCircle(), paddle.getRect());

    mainStage.close();

    Platform.runLater(() -> {
      try {
        new SuperDuperBreakout()
            .start(new Stage());
      } catch (IOException e) {
        System.out.println("Couldn't reset scene... " + e);
      }
    });
  }

  //Build the game :)
  private Scene buildGameplayScene() {

    mainPane = new Pane();
    mainPane.setBackground(Background.fill(Color.BLACK));

    //Bricks
    bricks = new Bricks();

    StackPane stackPane = new StackPane();
    stackPane.setPrefSize(GAMEPLAY_WINDOW_WIDTH, GAMEPLAY_WINDOW_HEIGHT / 3);
    stackPane.getChildren().add(bricks.getPane());

    StackPane.setAlignment(bricks.getPane(), Pos.TOP_CENTER);

    mainPane.getChildren().add(stackPane);

    //Paddle
    double paddleX = (GAMEPLAY_WINDOW_WIDTH / 2f) - (Paddle.PADDLE_WIDTH / 2f);
    double paddleY = GAMEPLAY_WINDOW_HEIGHT - Paddle.PADDLE_Y;

    paddle = new Paddle(paddleX, paddleY);
    mainPane.getChildren().add(paddle.getRect());

    //Ball
    double ballX = GAMEPLAY_WINDOW_WIDTH / 2f;
    double ballY = GAMEPLAY_WINDOW_HEIGHT / 2f;

    ball = new Ball(ballX, ballY);
    mainPane.getChildren().add(ball.getCircle());

    return new Scene(mainPane, GAMEPLAY_WINDOW_WIDTH, GAMEPLAY_WINDOW_HEIGHT);
  }

  public void scorePoint() {
    score++;
    System.out.println("Score: " + score);
  }

  public static void main(String[] args) {
    launch();
  }

}