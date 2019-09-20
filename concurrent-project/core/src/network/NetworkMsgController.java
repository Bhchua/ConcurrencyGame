package network;

import error.GlobalErrors;
import game.model.GameModelViewController;
import game.model.ShooterGame;
import network.GameInterpreter.Interpreter;
import network.threads.NetworkThread;

/**
 * The NetworkMsgController manages all the networks to be sent to and from the
 * Network Thread.
 * 
 * @author Brandon Hua
 *
 */

public class NetworkMsgController extends Thread {

  private GameModelViewController gmvc;
  NetworkThread nthread;
  private GameInterpreter gameInterp;
  Interpreter[] interpreters;
  private boolean finished = false;

  /**
   * The NetworkMsgController initialises a local GameModelViewController to get
   * the game being played.
   * 
   * <p>A network thread is also used to retrieve the network input.
   * 
   * @param gmvc    The GameModelViewController containing the game intended to be
   *                played in network mode.
   * @param nthread The NetworkThread used to control the connection.
   */

  public NetworkMsgController(GameModelViewController gmvc, NetworkThread nthread) {
    this.gmvc = gmvc;
    gameInterp = new GameInterpreter();
    interpreters = gameInterp.getInterpreters();
    this.nthread = nthread;
  }

  /**
   * The update method gets the current state of the game and sets the output
   * string accordingly.
   * 
   * <p>This method also interprets the incoming data and applies them to the game.
   */

  public void update() {
    ShooterGame game = gmvc.getGames()[0];
    if (!game.isFinished()) {
      String pos = GameNetworkData.getClientPosition(game);
      nthread.setOutputString(pos);
    } else {
      String finish = GameNetworkData.getFinalScore(game);
      nthread.setOutputString(finish);
    }
    String[] data = nthread.getInputString().split(" ");
    interpret(data, game);
  }

  /**
   * A method to take an array list of Strings and apply actions to the game if
   * they match the condition.
   * 
   * @param data The data to be used in the interpretation.
   * @param game The game the changes are to be applied to.
   */

  public void interpret(String[] data, ShooterGame game) {
    for (Interpreter i : interpreters) {
      i.checkStringAction(data, game);
    }
  }

  /**
   * The run method starts a loop that repeatedly updates the output string of the
   * network thread.
   */
  public void run() {
    while (!finished) {
      try {
        Thread.sleep(10);
        update();
      } catch (InterruptedException e) {
        String error = e.getClass().getSimpleName() + ": " + e.getMessage();
        GlobalErrors.setError(error);
      }
    }
  }

  /**
   * The finish method stops the current thread and the networked thread.
   */

  public void finish() {
    finished = true;
    nthread.finish();
  }

}
