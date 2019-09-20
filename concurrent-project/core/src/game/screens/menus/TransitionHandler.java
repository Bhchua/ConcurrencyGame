package game.screens.menus;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.screens.threads.TransitionThread;

/**
 * The TranstitionHandler binds a transition to a thread and controls the
 * starting and stopping of said transition.
 * 
 * @author Brandon Hua
 *
 */

public class TransitionHandler {

  private TransitionThread thread;
  private Transition transition;
  private boolean started = false;

  /**
   * The TransitionHandler initialises the transition with a given mode and
   * creates a new TransitionThread using the newly created transition.
   * 
   * @param mode The mode of operation for the transition.
   */

  public TransitionHandler(String mode) {
    transition = new Transition(mode);
    thread = new TransitionThread(transition);
  }

  /**
   * A method to start the thread and set the started boolean to true.
   */

  public void start() {
    thread.start();
    started = true;
  }

  /**
   * A method to render the Transition.
   * 
   * @param sb The SpriteBatch used to draw the Transition.
   */

  public void render(SpriteBatch sb) {
    transition.render(sb);
  }

  public boolean isStarted() {
    return started;
  }

  /**
   * A method to check if the thread is still alive.
   *
   * @return A boolean value for whether or not the thread is alive.
   */

  public boolean isActive() {
    return thread.isActive();
  }
  
  /**
   * A method of clearing the memory of the stored transition and thread.
   */

  public void dispose() {
    transition.dispose();
    thread.stopThread();
  }

}
