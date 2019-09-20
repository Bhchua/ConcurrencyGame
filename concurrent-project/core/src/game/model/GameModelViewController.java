package game.model;

import game.Core;
import game.model.levels.Level;
import game.model.rules.ShooterRules;
import game.renderer.GameRenderList;
import game.screens.GameScreenManager;
import game.screens.menus.GameEndOverlay;
import game.screens.threads.ClockScheduler;
import game.screens.threads.ShooterThread;
import game.screens.threads.ShooterThreadFactory;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The GameModelController creates all the necessary objects and threads for a
 * game to function. It encompasses the entire model of a game and applies the
 * rule sets to each instance.
 * 
 * @author Brandon Hua
 *
 */

public class GameModelViewController {

  private Level level;
  private ShooterGame[] games;
  private GameRenderList[] renderLists;
  private ShooterThread[] gameThreads;
  private ShooterThreadFactory factory;
  private ShooterRules rules;
  private ClockScheduler clock;

  private boolean gameFinished;

  private GameEndOverlay endOverlay;

  public GameModelViewController(GameScreenManager gsm, int players, String gameMode, Object data,
      String levelFileName) throws FileNotFoundException, IOException {
    gameFinished = false;
    initGame(players, gameMode, data, levelFileName);
  }

  /**
   * A method used to initialise the game, including loading levels and assigning
   * rule sets to games.
   * 
   * @param players       The number of players in the session.
   * @param gameMode      The game mode to be played.
   * @param data          The data to be used as the parameters of the ruleset.
   * @param levelFileName The filename of the level to be loaded.
   * @throws FileNotFoundException If the level file cannot be found.
   * @throws IOException           If the level file cannot be read.
   */

  public void initGame(int players, String gameMode, Object data, String levelFileName)
      throws FileNotFoundException, IOException {
    level = new Level(levelFileName);
    games = new ShooterGame[players];
    renderLists = new GameRenderList[players];
    gameThreads = new ShooterThread[players];

    for (int i = 0; i < players; i++) {
      games[i] = new ShooterGame(level, i + 1, (gameMode.equals("network")));
      renderLists[i] = new GameRenderList(games[i]);
    }

    rules = new ShooterRules(games[0], gameMode, data);
    clock = new ClockScheduler(rules);
    factory = new ShooterThreadFactory(clock);

    for (int i = 0; i < players; i++) {
      gameThreads[i] = factory.getThread(games[i], String.valueOf(i));
    }
  }

  /**
   * A method that updates the game depending on the state.
   */

  public void update() {
    for (int i = 0; i < stateList.length; i++) {
      if (stateList[i].gameState()) {
        stateList[i].stateUpdate();
      }
    }
  }

  /**
   * A method to synchronise the pause state of all the views.
   * 
   * <p>
   * Different threads read inputs at different times so they may miss the pause
   * input if the down press is particularly brief.
   */

  public void synchronisePause() {
    for (int i = 1; i < games.length; i++) {
      games[i].setPause(games[0].isPaused());
    }
  }

  /**
   * A method to check if all the games are paused.
   * 
   * @return A boolean for whether or not it is paused.
   */

  public boolean allPaused() {
    for (int i = 0; i < games.length; i++) {
      if (!games[i].isPaused()) {
        return false;
      }
    }
    return true;
  }

  public ShooterGame[] getGames() {
    return games;
  }

  public ShooterRules getRules() {
    return rules;
  }

  //
  // Classes below this point are GameStateUpdates.
  //

  private GameStateUpdate[] stateList = new GameStateUpdate[] { new GameWonUpdate(),
      new GameLostUpdate(), new DefaultUpdate() };

  /**
   * The game state interface provides a way to change the update function based
   * on the current state of the rule set.
   * 
   * @author Brandon Hua
   */

  public interface GameStateUpdate {

    /**
     * This method checks the rules object to see the current state of the game.
     * 
     * @return A boolean value for whether or not it is currently in the desired
     *         state.
     */

    public boolean gameState();

    /**
     * A method that updates the game.
     */

    public void stateUpdate();
  }

  /**
   * The GameWonUpdate performs an action when a game has been won.
   * 
   * @author Brandon Hua
   */

  public class GameWonUpdate implements GameStateUpdate {

    @Override
    public boolean gameState() {
      return rules.isGameWon();
    }

    @Override
    public void stateUpdate() {
      int[] gameScores = new int[games.length];
      for (int i = 0; i < games.length; i++) {
        gameScores[i] = games[i].getVars().getScore();
      }
      if (rules.getMode().equals("network")) {
        try {
          Thread.sleep(50);
          gameScores = new int[] { games[0].getVars().getScore(),
              games[0].getVars().getNetScore() };
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      endOverlay = new GameEndOverlay(rules.getMode(), gameScores, rules.getVars().getTimePassed());
      gameFinished = true;
    }

  }

  /**
   * The GameLostUpdate performs an action when a game has been lost.
   * 
   * @author Brandon Hua
   */

  public class GameLostUpdate implements GameStateUpdate {

    @Override
    public boolean gameState() {
      return rules.isGameOver();
    }

    @Override
    public void stateUpdate() {
      int[] gameScores = new int[games.length];
      for (int i = 0; i < games.length; i++) {
        gameScores[i] = games[i].getVars().getScore();
      }
      endOverlay = new GameEndOverlay("gameover", gameScores, rules.getVars().getTimePassed());
      gameFinished = true;
    }

  }

  /**
   * The DefaultUpdate is the default action when none of the game states have
   * been changed.
   * 
   * @author Brandon Hua
   */

  public class DefaultUpdate implements GameStateUpdate {

    @Override
    public boolean gameState() {
      return (!rules.isGameOver() && !rules.isGameWon());
    }

    @Override
    public void stateUpdate() {
      clock.checkCycle(Core.deltaTime);
      synchronisePause();
    }

  }

  public GameEndOverlay getEndOverlay() {
    return endOverlay;
  }

  public boolean isGameFinished() {
    return gameFinished;
  }

  public GameRenderList[] getRenderList() {
    return renderLists;
  }

}
