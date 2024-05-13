package gov.superswag.superduperbreakout;

import gov.superswag.superduperbreakout.controller.InputHandler;
import gov.superswag.superduperbreakout.gameobjects.Ball;
import gov.superswag.superduperbreakout.gameobjects.Bricks;
import gov.superswag.superduperbreakout.gameobjects.Paddle;
import gov.superswag.superduperbreakout.leaderboard.Leaderboard;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SuperDuperBreakout extends Application {

  public static final double GAMEPLAY_WINDOW_WIDTH = 300;
  public static final double GAMEPLAY_WINDOW_HEIGHT = 500;

  public static SuperDuperBreakout instance;

  Pane mainPane;
  Stage mainStage;
  Scene mainGameplayScene, startScene;

  public Paddle paddle;
  public Ball ball;
  public Bricks bricks;
  public Text scoreText;

  private int score;
  private Leaderboard leaderboard;

  @Override
  public void start(Stage primaryStage) throws IOException {
    instance = this;
    mainStage = primaryStage;
    leaderboard = new Leaderboard();

    startScene = buildStartScene();

    primaryStage.setTitle("Super Super Breakout");
    primaryStage.setScene(startScene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  private Scene buildStartScene() {

    VBox root = new VBox(10);
    root.setAlignment(Pos.CENTER);
    root.setStyle("-fx-padding: 20; -fx-background-color: black;");

    TextField nameInput = new TextField();
    nameInput.setPromptText("Enter your name");
    Button playButton = new Button("Play");

    playButton.setOnAction(e -> {
      String playerName = nameInput.getText().trim();
      if (!playerName.isEmpty()) {
        try {
          mainGameplayScene = buildGameplayScene();
          mainStage.setScene(mainGameplayScene);
          mainGameplayScene.setOnKeyPressed(InputHandler::onKeyPressed);
          mainGameplayScene.setOnKeyReleased(InputHandler::onKeyReleased);
        } catch (Exception ex) {
          System.out.println("Failed to initialize gameplay scene: " + ex.getMessage());
        }
      } else {
        nameInput.setPromptText("Please enter a name!");
      }
    });

    root.getChildren().addAll(nameInput, playButton);
    return new Scene(root, GAMEPLAY_WINDOW_WIDTH, GAMEPLAY_WINDOW_HEIGHT);
  }

  private Scene buildGameplayScene() {

    mainPane = new Pane();
    mainPane.setBackground(Background.fill(Color.BLACK));

    scoreText = new Text(10, 30, "Score: 0");
    scoreText.setFont(Font.font("Consolas", FontWeight.NORMAL, 24));
    scoreText.setFill(Color.WHITE);
    mainPane.getChildren().add(scoreText);

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

  /** Every time the player scores a point */
  public void scorePoint() {
    score++;
    scoreText.setText("Score: " + score);

    //leaderboard.addScore("PlayerName", score); // Adjust to use actual player name from input
  }

  public static void main(String[] args) {
    launch(args);
  }

  public void resetGame() {
    System.out.println("Resetting game...");
    ball.stop();
    paddle.stop();
    mainPane.getChildren().removeAll(ball.getCircle(), paddle.getRect());
    mainStage.close();

    Platform.runLater(() -> {
      try {
        new SuperDuperBreakout().start(new Stage());
      } catch (IOException e) {
        System.out.println("Couldn't reset scene: " + e.getMessage());
      }
    });

  }
}
