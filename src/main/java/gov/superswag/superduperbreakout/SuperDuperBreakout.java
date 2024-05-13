package gov.superswag.superduperbreakout;

import gov.superswag.superduperbreakout.controller.InputHandler;
import gov.superswag.superduperbreakout.gameobjects.Ball;
import gov.superswag.superduperbreakout.gameobjects.Paddle;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SuperDuperBreakout extends Application {

  public static final double GAMEPLAY_WINDOW_WIDTH = 300;
  public static final double GAMEPLAY_WINDOW_HEIGHT = 500;

  public static SuperDuperBreakout instance;

  Pane mainPane;
  Stage mainStage;
  Scene mainGameplayScene;

  Paddle paddle;
  Ball ball;

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

    //Paddle
    double paddleX = (GAMEPLAY_WINDOW_WIDTH / 2f) - (Paddle.PADDLE_WIDTH / 2f);
    double paddleY = GAMEPLAY_WINDOW_HEIGHT - Paddle.PADDLE_Y;

    paddle = new Paddle(paddleX, paddleY);
    mainPane.getChildren().add(paddle.getRect());

    //Ball
    double ballX = GAMEPLAY_WINDOW_WIDTH / 2f;
    double ballY = GAMEPLAY_WINDOW_HEIGHT / 2f;

    ball = new Ball(ballX, ballY, paddle);
    mainPane.getChildren().add(ball.getCircle());

    return new Scene(mainPane, GAMEPLAY_WINDOW_WIDTH, GAMEPLAY_WINDOW_HEIGHT);
  }

  public static void main(String[] args) {
    launch();
  }

}