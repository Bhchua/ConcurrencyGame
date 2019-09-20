package game.screens.menus;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The TransitionList allows all the Transitions in a screen to be held in a
 * single object and rendered concurrently.
 * 
 * @author Brandon Hua
 *
 */

public class TransitionList {

  private ArrayList<TransitionHandler> list;

  public TransitionList() {
    list = new ArrayList<TransitionHandler>();
  }

  public void add(TransitionHandler thandler) {
    list.add(thandler);
  }

  /**
   * A method to render all the active Transitions.
   * 
   * @param sb The SpriteBatch used to draw the Transitions.
   */

  public void render(SpriteBatch sb) {
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).isStarted()) {
        list.get(i).render(sb);
      }
    }
  }

}
