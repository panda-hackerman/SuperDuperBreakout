package gov.superswag.superduperbreakout.leaderboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Stores the top scores.
 *
 * @author Khalid Hankeer
 */
public class Leaderboard implements Serializable {

  private static final int MAX_ENTRIES = 5;
  private final PriorityQueue<ScoreEntry> topScores;

  public Leaderboard() {
    this.topScores = new PriorityQueue<>(MAX_ENTRIES);
  }

  public void addScore(String playerName, int score) {

    if (topScores.size() < MAX_ENTRIES) { //There are slots left, so just add it
      topScores.add(new ScoreEntry(playerName, score));
    } else if (score > topScores.peek().score()) { //No slots left, so must be a high score
      topScores.poll();
      topScores.add(new ScoreEntry(playerName, score));
    }

  }

  public String getFormattedLeaderboard() {

    List<ScoreEntry> scores = new ArrayList<>(topScores);
    Collections.sort(scores);
    Collections.reverse(scores); //Highest to lowest

    StringBuilder strBuilder = new StringBuilder("Top Scores:\n");

    for (ScoreEntry entry : scores) {
      String str = entry.playerName() + ": " + entry.score() + "\n";
      strBuilder.append(str);
    }

    return strBuilder.toString();
  }

  public record ScoreEntry(String playerName, int score) implements Comparable<ScoreEntry> {
    @Override
    public int compareTo(ScoreEntry other) {
      return Integer.compare(this.score, other.score);
    }
  }
}
