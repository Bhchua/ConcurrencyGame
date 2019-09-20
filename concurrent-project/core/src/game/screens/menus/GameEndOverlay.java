package game.screens.menus;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Core;
import game.renderer.TextRenderer;

/**
 * The GameEndOverlay is intended to be rendered over a game view and displays
 * the scores of that session.
 * 
 * @author Brandon Hua
 */

public class GameEndOverlay {

  private long time;
  private int[] scores;

  private EndMode endMode;

  /**
   * The GameEndOverlay class initialises local variables for the time passed and
   * the scores of each view.
   * 
   * <p>
   * The constructor also initialises the mode for rendering.
   * 
   * @param mode   Game mode to be drawn.
   * @param scores The scores of the games.
   * @param time   The time elapsed.
   */

  public GameEndOverlay(String mode, int[] scores, long time) {

    this.time = time;
    this.scores = scores;

    for (EndMode e : endList) {
      if (e.isEndMode(mode)) {
        endMode = e;
      }
    }
  }

  public void render(SpriteBatch sb) {
    endMode.renderMsg(sb);
  }

  private ArrayList<EndMode> endList = new ArrayList<EndMode>(
      Arrays.asList(new GameOver(), new TimedMode(), new StockMode(), new NetworkMode()));

  /**
   * The EndMode Interface allows different end screens to be shown depending on
   * the game mode.
   * 
   * @author Brandon Hua
   */

  public interface EndMode {
    /**
     * The isEndMode method simply checks to see if the given mode name is the name
     * of this particular mode.
     * 
     * @param name The name of the mode being played.
     * @return A boolean value for whether or not it is the mode.
     */
    public boolean isEndMode(String name);

    /**
     * A method that renders the end screen message.
     * 
     * @param sb The SpriteBatch used to draw the message.
     */
    public void renderMsg(SpriteBatch sb);
  }

  public class GameOver implements EndMode {

    @Override
    public boolean isEndMode(String name) {
      return name.equals("gameover");
    }

    @Override
    public void renderMsg(SpriteBatch sb) {
      String gameOver = "GAME OVER";
      int goHeight = Core.height / 10;
      int goWidth = goHeight * gameOver.length();
      TextRenderer.print(sb, "GAME OVER", "white", Core.width / 2, 7 * Core.height / 8, goWidth,
          goHeight);
      printTime(sb);
    }

  }

  /**
   * A method to print the time elapsed.
   * 
   * @param sb The SpriteBatch used to draw.
   */

  public void printTime(SpriteBatch sb) {
    String timeMsg = "TIME SURVIVED";
    int mgsHeight = Core.height / 14;
    int mgsWidth = mgsHeight * timeMsg.length();
    TextRenderer.print(sb, timeMsg, "white", Core.width / 2, 6 * Core.height / 8,
        mgsWidth, mgsHeight);
    TextRenderer.printTime(sb, time, "white", Core.width / 2, 5 * Core.height / 8, Core.width / 6,
        Core.height / 16);
    printScores(sb);
  }

  /**
   * The TimedMode displays the end message for the "timed" game mode.
   * 
   * @author Brandon Hua
   */

  public class TimedMode implements EndMode {

    @Override
    public boolean isEndMode(String name) {
      return name.equals("timed");
    }

    @Override
    public void renderMsg(SpriteBatch sb) {
      int winner = 0;
      int highest = 0;
      for (int i = 0; i < scores.length; i++) {
        if (scores[i] >= highest) {
          winner = i + 1;
          highest = scores[i];
        }
      }
      if (scores.length > 1) {
        if (draw()) {
          TextRenderer.print(sb, "DRAW", "white", Core.width / 2, 3 * Core.height / 4,
              Core.width / 8, Core.height / 12);
        } else {
          TextRenderer.print(sb, "PLAYER " + winner + " WINS", "white", Core.width / 2,
              3 * Core.height / 4, Core.width / 3, Core.height / 14);
        }
      } else {
        TextRenderer.print(sb, "FINAL SCORE", "white", Core.width / 2, 3 * Core.height / 4,
            Core.width / 3, Core.height / 14);
      }
      printScores(sb);
    }

  }

  /**
   * The TimedMode displays the end message for the "stock" game mode.
   * 
   * <p>
   * The "stock" game mode displays a collated score in addition to each user
   * score.
   * 
   * @author Brandon Hua
   */

  public class StockMode implements EndMode {

    @Override
    public boolean isEndMode(String name) {
      return name.equals("stock");
    }

    @Override
    public void renderMsg(SpriteBatch sb) {
      TextRenderer.print(sb, "Final Score", "white", Core.width / 2, 3 * Core.height / 4,
          Core.width / 3, Core.height / 14);
      int total = addScores();
      int height = Core.height / 12;
      int width = height * String.valueOf(total).length();
      TextRenderer.print(sb, String.valueOf(total), "white", Core.width / 2, 5 * Core.height / 8,
          width, height);
      printScores(sb);
    }

  }

  /**
   * The TimedMode displays the end message for the "Networked" game mode.
   * 
   * <p>
   * A "You win" or "You lose" message is shown depending on if your score is
   * higher than the connected player.
   * 
   * @author Brandon Hua
   */

  public class NetworkMode implements EndMode {

    @Override
    public boolean isEndMode(String name) {
      return name.equals("network");
    }

    @Override
    public void renderMsg(SpriteBatch sb) {
      int localScore = scores[0];
      int connectedScore = scores[1];
      String winLose = "Draw";
      if (localScore < connectedScore) {
        winLose = "YOU LOSE";
      }
      if (localScore > connectedScore) {
        winLose = "YOU WIN";
      }
      int winLoseheight = Core.width / 16;
      int winLoseWidth = winLoseheight * winLose.length();
      TextRenderer.print(sb, winLose, "white", Core.width / 2, 3 * Core.height / 4, winLoseWidth,
          winLoseheight);
      printScores(sb);
    }

  }

  /**
   * A method to add the scores and get the total.
   * 
   * @return The total value of all the scores.
   */

  public int addScores() {
    int scoreTotal = 0;
    for (int i = 0; i < scores.length; i++) {
      scoreTotal = scoreTotal + scores[i];
    }
    return scoreTotal;
  }

  /**
   * A method to print the scores of each view.
   * 
   * @param sb The SpriteBatch used to draw.
   */

  public void printScores(SpriteBatch sb) {
    for (int i = 0; i < scores.length; i++) {
      int xspacing = Core.width / (scores.length * 2);
      xspacing = xspacing * ((i * 2) + 1);
      TextRenderer.print(sb, "P" + (i + 1), "white", xspacing, Core.height / 2, Core.width / 24,
          Core.height / 12);
      int height = Core.height / 20;
      int width = height * String.valueOf(scores[i]).length();
      TextRenderer.print(sb, String.valueOf(scores[i]), "white", xspacing, Core.height / 3, width,
          height);
    }
  }

  /**
   * A method to check if the scores are same or not.
   * 
   * @return A boolean value for whether or not the players have the same scores.
   */

  public boolean draw() {
    int compareScore = scores[0];
    if (scores.length > 1) {
      for (int i = 0; i < scores.length; i++) {
        if (scores[i] != compareScore) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

}
