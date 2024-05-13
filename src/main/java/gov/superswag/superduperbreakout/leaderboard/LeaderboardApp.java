package gov.superswag.superduperbreakout.leaderboard;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LeaderboardApp extends Application {
    private Leaderboard leaderboard;

    @Override
    public void start(Stage primaryStage) {
        leaderboard = new Leaderboard();
        // Pre-populate the leaderboard
        leaderboard.addScore("Alice", 150);
        leaderboard.addScore("Bob", 300);
        leaderboard.addScore("Charlie", 250);

        ListView<Player> listView = new ListView<>(leaderboard.getTopPlayers());
        listView.setCellFactory(lv -> new ListCell<Player>() {
            @Override
            protected void updateItem(Player player, boolean empty) {
                super.updateItem(player, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(player.getName() + ": " + player.getScore());
                }
            }
        });

        // Input form elements
        TextField nameInput = new TextField();
        nameInput.setPromptText("Name");
        TextField scoreInput = new TextField();
        scoreInput.setPromptText("Score");
        Button addButton = new Button("Add Score");
        addButton.setOnAction(e -> {
            try {
                String name = nameInput.getText();
                int score = Integer.parseInt(scoreInput.getText());
                leaderboard.addScore(name, score);
                listView.setItems(leaderboard.getTopPlayers());  // Refresh the list view
                nameInput.clear();
                scoreInput.clear();
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid score.");
            }
        });

        GridPane inputForm = new GridPane();
        inputForm.setHgap(10);
        inputForm.setVgap(10);
        inputForm.setPadding(new Insets(10, 10, 10, 10));
        inputForm.add(new Label("Name:"), 0, 0);
        inputForm.add(nameInput, 1, 0);
        inputForm.add(new Label("Score:"), 0, 1);
        inputForm.add(scoreInput, 1, 1);
        inputForm.add(addButton, 1, 2);

        VBox root = new VBox(10, listView, inputForm);
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("Leaderboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

