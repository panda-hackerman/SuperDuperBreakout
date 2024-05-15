package gov.superswag.superduperbreakout;

import gov.superswag.superduperbreakout.controller.InputHandler;
import gov.superswag.superduperbreakout.gameobjects.Ball;
import gov.superswag.superduperbreakout.gameobjects.BrickGrid;
import gov.superswag.superduperbreakout.gameobjects.Paddle;
import gov.superswag.superduperbreakout.leaderboard.Leaderboard;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/** Main gameplay class */
public class SuperDuperBreakout extends Application {

  public static final double GAMEPLAY_WINDOW_WIDTH = 300;
  public static final double GAMEPLAY_WINDOW_HEIGHT = 500;

  public static SuperDuperBreakout instance;
  private static Leaderboard leaderboard; //Singleton

  Pane mainPane;
  Stage mainStage;
  Scene mainGameplayScene, startScene, endGameScene;

  public Paddle paddle;
  public Ball ball;
  public BrickGrid bricks;
  public Text scoreText;

  private int score;
  private String playerName;

  @Override
  public void start(Stage primaryStage) throws IOException {

    instance = this;
    mainStage = primaryStage;
    startScene = buildStartScene();

    if (leaderboard == null) { //Keep it, if it already exists.
      leaderboard = new Leaderboard();
    }

    primaryStage.setTitle("Super Super Breakout");
    primaryStage.setScene(startScene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

  /** Transition to gameplay */
  public void startGame() {

    score = 0;

    mainGameplayScene = buildGameplayScene();
    mainStage.setScene(mainGameplayScene);

    InputHandler.clear();
    mainGameplayScene.setOnKeyPressed(InputHandler.keyPressedHandler);
    mainGameplayScene.setOnKeyReleased(InputHandler.keyReleasedHandler);
  }

  /** Transition from gameplay to end screen */
  public void endGame() {

    ball.stop();
    paddle.stop();

    mainGameplayScene.removeEventHandler(KeyEvent.KEY_PRESSED, InputHandler.keyPressedHandler);
    mainGameplayScene.removeEventHandler(KeyEvent.KEY_RELEASED, InputHandler.keyReleasedHandler);

    leaderboard.addScore(playerName, score);

    endGameScene = buildEndScreen();
    mainStage.setScene(endGameScene);
  }

  /** Resets the game (to the start screen) and all variables. This method creates a new
   * {@link SuperDuperBreakout} class, and the current one should be discarded */
  public void resetGame() {

    System.out.println("Resetting game...");

    ball.stop();
    paddle.stop();
    mainPane.getChildren().removeAll(ball.getCircle(), paddle.getRect());
    mainStage.close();

    //Quick and dirty... Might cause a memory leak (oops)
    Platform.runLater(() -> {
      try {
        new SuperDuperBreakout().start(new Stage());
      } catch (IOException e) {
        System.out.println("Couldn't reset scene: " + e.getMessage());
      }
    });

  }

  private Scene buildStartScene() {

    VBox root = new VBox(10);

    root.setAlignment(Pos.CENTER);
    root.setStyle("-fx-padding: 20; -fx-background-color: black;");

    TextField nameInput = new TextField();
    Button playButton = new Button("Play");

    nameInput.setPromptText("Enter your name");
    playButton.setOnAction(e -> {
      playerName = nameInput.getText().trim();

      if (!playerName.isEmpty()) {
        startGame();
      }
    });

    //Press the button when enter is pressed
    nameInput.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        playButton.fire();
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
    bricks = new BrickGrid();

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
    double ballY = GAMEPLAY_WINDOW_HEIGHT / 2.5f;

    ball = new Ball(ballX, ballY);
    mainPane.getChildren().add(ball.getCircle());

    return new Scene(mainPane, GAMEPLAY_WINDOW_WIDTH, GAMEPLAY_WINDOW_HEIGHT);
  }

  public Scene buildEndScreen() {

    // Display leaderboard
    String leaderboardText = leaderboard.getFormattedLeaderboard();
    Label leaderboardLabel = new Label(leaderboardText);
    leaderboardLabel.setTextFill(Color.web("#89CFF0"));
    leaderboardLabel.setFont(new Font("Arial", 18));

    // Play Again button
    Button playAgainButton = new Button("Play Again");
    playAgainButton.setFont(new Font("Arial", 16));
    playAgainButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: #FDFEFE;");

    playAgainButton.setOnAction(e -> startGame());

    // Main Menu button
    Button mainMenuButton = new Button("Main Menu");
    mainMenuButton.setFont(new Font("Arial", 16));
    mainMenuButton.setStyle("-fx-background-color: #5499C7; -fx-text-fill: #FDFEFE;");

    mainMenuButton.setOnAction(e -> resetGame());

    // Layout configuration
    VBox layout = new VBox(20);
    layout.setAlignment(Pos.CENTER);
    layout.getChildren().addAll(leaderboardLabel, playAgainButton, mainMenuButton);

    return new Scene(layout, GAMEPLAY_WINDOW_WIDTH, GAMEPLAY_WINDOW_HEIGHT);
  }

  /** Every time the player scores a point */
  public void scorePoint(int amount) {
    score += amount;
    scoreText.setText("Score: " + score);
  }
}
