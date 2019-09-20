package game.screens.threads;

import game.Core;
import game.input.ShooterGameInputHandler;
import game.model.ShooterGame;
import game.model.rules.ShooterRules;

/**
 * The ShooterThread class is specific to the shooter game and manages the rule
 * set that is applied to the game model as well as housing the model itself.
 * 
 * @author Brandon Hua
 *
 */
public class ShooterThread implements Runnable {

  private Thread t;
  private ShooterGame game;
  private String threadName;
  private ClockScheduler clock;
  private boolean active;
  private ShooterGameInputHandler inputHandler;

  /**
   * Constructor for the ShooterThreads.
   * 
   * @param game       The model to be held in the thread.
   * @param clock      The Scheduler used to control access to the rules object.
   * @param threadName The name of the thread.
   */

  public ShooterThread(ShooterGame game, String threadName, ClockScheduler clock) {
    this.threadName = threadName;
    this.game = game;
    inputHandler = new ShooterGameInputHandler(game);
    this.clock = clock;
    active = true;
    t = new Thread(this);
    t.start();
  }

  /**
   * The method holding the actions of a thread.
   */

  @Override
  public void run() {
    while (active) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        System.out.println("Interrupted.");
      }
      
      if (clock.getCurrentThread().equals(threadName)) {
        inputHandler.handleInputs();
        clock.getRules().setAndUpdate(game);
        clock.cycle();
      } 
      game.update();
    }
  }

  public ShooterGame getGame() {
    return game;
  }

  public String getName() {
    return threadName;
  }

  /**
   * A method to interrupt the run loop and thus terminate the thread.
   */

  public void stopThread() {
    active = false;
  }

}
