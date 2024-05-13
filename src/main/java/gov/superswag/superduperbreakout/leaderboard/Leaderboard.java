package gov.superswag.superduperbreakout.leaderboard;

import java.util.PriorityQueue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Leaderboard {
    private PriorityQueue<Player> topPlayers;
    private final int maxSize = 5;

    public Leaderboard() {
        // Initialize the priority queue with a custom comparator that treats higher scores as higher priority
        topPlayers = new PriorityQueue<>(maxSize, (a, b) -> Integer.compare(a.getScore(), b.getScore()));
    }

    public void addScore(String name, int score) {
        Player newPlayer = new Player(name, score);
        if (topPlayers.size() < maxSize) {
            topPlayers.add(newPlayer);
        } else if (score > topPlayers.peek().getScore()) {
            topPlayers.poll();  // Remove the player with the lowest score
            topPlayers.add(newPlayer);
        }
    }

    public ObservableList<Player> getTopPlayers() {
        ObservableList<Player> players = FXCollections.observableArrayList(topPlayers);
        FXCollections.sort(players, (a, b) -> Integer.compare(b.getScore(), a.getScore()));  // Sort descending for display
        return players;
    }
}
