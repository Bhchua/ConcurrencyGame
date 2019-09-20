package game.screens.threads;

import game.screens.menus.buttons.ButtonList;

/**
 * The MenuThread class is a Runnable that handles the methods required
 * to make the on screen menus function. Primarily using the ButtonList
 * methods to observe the mouse position and apply them to the model.
 * 
 * @author Brandon Hua
 */

public class MenuThread implements Runnable {
  
  private Thread t;
  private ButtonList buttonList;
  private boolean active;
  
  public MenuThread(ButtonList list) {
    this.buttonList = list;
    active = true;
    t = new Thread(this);
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
      if(buttonList.isActive()) {
        buttonList.mouseSelect(); 
        buttonList.confirmAction();
        if(buttonList.isConfirmed()) {
          if(!buttonList.getSelected().getTrandler().isStarted()) {
            buttonList.getSelected().getTrandler().start();
          }
          if(!buttonList.getSelected().getTrandler().isActive()) {
            buttonList.setConfirmed();
          }
        }
      }
    }
  }
  
  public void finish() {
    active = false;
  }

}
