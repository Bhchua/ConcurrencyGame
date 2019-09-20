package game.screens.threads;

import game.screens.menus.Transition;

public class TransitionThread implements Runnable {
  
  private Thread t;
  private Transition trandler;
  private boolean active;
  
  public TransitionThread(Transition trandler) {
    this.trandler = trandler; 
    t = new Thread(this);
  }
  
  public void start() {
    active = true;
    t.start();
  }

  @Override
  public void run() {
    while (active) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        System.out.println("Interrupted.");
      }
      if(trandler.perform()) {
        stopThread();
      }
    }
  }

  /**
   * A method to interrupt the run loop and thus terminate the thread.
   */

  public void stopThread() {
    active = false;
  }
  
  public boolean isActive() {
    return active;
  }
  
}
