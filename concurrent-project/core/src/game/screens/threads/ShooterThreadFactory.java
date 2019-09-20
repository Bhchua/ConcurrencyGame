package game.screens.threads;

import game.model.ShooterGame;
import game.model.rules.ShooterRules;

/**
 * A simple factory design pattern that assigns the clock scheduler a 
 * ShooterThread on creation, before returning the newly created thread.
 * 
 * @author Brandon Hua
 *
 */

public class ShooterThreadFactory {
  
  private ClockScheduler clock;
  
  public ShooterThreadFactory(ClockScheduler clock) {
    this.clock = clock;
  }

  /**
   * The method to create and return a ShooterThread.
   * 
   * @param gameModel The game the thread will be in charge of.
   * @param threadName The name of the thread.
   * @return The newly created thread.
   */
  
  public ShooterThread getThread(ShooterGame gameModel, String threadName) {
    clock.addThread(threadName);
    ShooterThread thread = new ShooterThread(gameModel, threadName, clock);
    return thread;
  }
  
}
