package game.screens.threads;

import java.util.ArrayList;

import game.model.ShooterGame;
import game.model.rules.ShooterRules;

/**
 * The clock scheduler class is is used to restrict access to a resource from a
 * thread by having a list of thread names that is incremented, then checked by
 * the thread to decide whether or not it can use a resource.
 * 
 * @author Brandon Hua
 *
 */

public class ClockScheduler {

  private ArrayList<String> threadNames = new ArrayList<String>();
  private int currentThread = 0;
  private final static int MAXTIME = 8; // milliseconds
  private long runtime = 0;
  private ShooterRules rules;
  
  public ClockScheduler(ShooterRules rules) {
    this.rules = rules;
  }
  
  public ShooterRules getRules() {
    return rules;
  }
  
  /**
   * A method to add the name of a thread into the list of thread names.
   * 
   * @param threadName the name of the thread.
   */

  public void addThread(String threadName) {
    threadNames.add(threadName);
  }

  /**
   * A method to get the name of the thread that is allowed to use the resource.
   * 
   * @return The name of the current thread.
   */

  public String getCurrentThread() {
    return threadNames.get(currentThread);
  }

  /**
   * Cycle the currently checked thread and move it to the end of the array.
   */

  public void cycle() {
    currentThread = (currentThread + 1) % threadNames.size();
    runtime = 0;
  }

  /**
   * A method to remove all the thread names in the array.
   */

  public void clearScheduler() {
    threadNames = new ArrayList<String>();
  }

  /**
   * A method to cycle the array if the thread has taken too long and exceeded the
   * maximum time set.
   * 
   * @param deltaTime The difference in time from the last frame.
   */

  public void checkCycle(long deltaTime) {
    runtime = runtime + deltaTime;
    if (runtime > MAXTIME) {
      cycle();
    }
  }

  /**
   * Remove a specific thread if it no longer requires the resource.
   * 
   * @param threadName The name of the thread to be removed.
   */

  public void removeThread(String threadName) {
    int counter = 0;
    while (counter < threadNames.size()) {
      if (threadName.equals(threadNames.get(counter))) {
        threadNames.remove(counter);
        counter = threadNames.size() + 1;
      }
      counter++;
    }
  }

}
