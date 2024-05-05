package gov.superswag.superduperbreakout;

import gov.superswag.superduperbreakout.controller.InputHandler;
import gov.superswag.superduperbreakout.gameobjects.Ball;
import gov.superswag.superduperbreakout.gameobjects.Paddle;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SuperDuperBreakout extends Application {

  public static final double GAMEPLAY_WINDOW_WIDTH = 300;
  public static final double GAMEPLAY_WINDOW_HEIGHT = 500;

  Scene mainGameplayScene;
  Paddle paddle;
  Ball ball;

  @Override
  public void start(Stage primaryStage) throws IOException {

    mainGameplayScene = buildGameplayScene();

    //Stuff
    primaryStage.setTitle("Super Super Breakout");
    primaryStage.setScene(mainGameplayScene);
    primaryStage.setResizable(false);
    primaryStage.show();

    mainGameplayScene.setOnKeyPressed(InputHandler::keyPressed);
    mainGameplayScene.setOnKeyReleased(InputHandler::keyReleased);
  }

  private Scene buildGameplayScene() {

    Pane pane = new Pane();
    pane.setBackground(Background.fill(Color.BLACK));

    //Paddle
    double paddleX = (GAMEPLAY_WINDOW_WIDTH / 2f) - (Paddle.PADDLE_WIDTH / 2f);
    double paddleY = GAMEPLAY_WINDOW_HEIGHT - Paddle.PADDLE_Y;

    paddle = new Paddle(paddleX, paddleY);
    pane.getChildren().add(paddle.getRect());

    //Ball
    double ballX = GAMEPLAY_WINDOW_WIDTH / 2f;
    double ballY = GAMEPLAY_WINDOW_HEIGHT / 2f;

    ball = new Ball(ballX, ballY, paddle);
    pane.getChildren().add(ball.getCircle());

    return new Scene(pane, GAMEPLAY_WINDOW_WIDTH, GAMEPLAY_WINDOW_HEIGHT);
  }

  public static void main(String[] args) {
    launch();
  }

}