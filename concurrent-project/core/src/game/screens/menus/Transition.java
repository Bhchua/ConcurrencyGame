package game.screens.menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Core;

/**
 * The Transition class is made to render transitions to mask operations (such
 * as moving to another screen).
 * 
 * @author Brandon Hua
 */

public class Transition {

  private Texture currentTexture;

  private final float WIPESPEED = 8;
  private final float FADESPEED = .001f;

  private int xpos;
  private int ypos;

  private int width;
  private int height;

  private float alpha = 1;

  private String mode;

  /**
   * The constructor for the Transition object where the mode is set and the
   * appropriate textures are loaded.
   * 
   * @param transition The mode the transition will be;
   */

  public Transition(String transition) {
    mode = transition;
    changeMode(mode);
  }

  /**
   * A constructor for the Transition object which defaults to "none".
   */
  
  public Transition() {
    xpos = Core.width / 2;
    ypos = Core.height / 2;

    width = Core.width;
    height = Core.height;
    
    mode = "none";
    changeMode(mode);
  }
  
  /**
   * A method to iterate through the transitions and change the mode of
   * operation.
   * 
   * @param transition The transition to change to.
   */

  public void changeMode(String transition) {
    mode = transition;
    transitionList[4].changeMode();
    for (TransitionAction t : transitionList) {
      if (t.transitionName().equals(mode)) {
        t.changeMode();
      }
    }
  }

  public void reset() {
    changeMode(mode);
  }
  
  /**
   * A method which performs the transition.
   * 
   * @return A boolean for whether or not the transition has finished.
   */

  public boolean perform() {
    for (TransitionAction t : transitionList) {
      if (t.transitionName().equals(mode)) {
        return t.perform();
      }
    }
    return false;
  }

  public TransitionAction[] transitionList = new TransitionAction[] { new FadeIn(), new FadeOut(),
      new Wipe(), new Darken(), new None() };

  /**
   * The TransitionAction interface allows the Transition to determine which action to perform
   * depending on the mode.
   * 
   * @author Brandon Hua
   */
  
  public interface TransitionAction {
    /**
     * A getter for the mode name.
     * 
     * @return The name of the mode.
     */
    public String transitionName();

    /**
     * A method which switches assets and sets default values for different modes.
     */
    public void changeMode();

    /**
     * A method which performs the transition.
     * 
     * @return A boolean value for whether or not it is finished.
     */
    public boolean perform();
  }
  
  /**
   * A TransitionAction that fades the screen into view.
   * 
   * @author Brandon Hua
   */

  public class FadeIn implements TransitionAction {

    @Override
    public String transitionName() {
      return "fade_in";
    }

    @Override
    public boolean perform() {
      alpha = alpha - FADESPEED * Core.deltaTime;
      if (alpha <= 0) {
        alpha = 0;
        return true;
      }
      return false;
    }

    @Override
    public void changeMode() {
      currentTexture = new Texture("transition_fade.png");

      xpos = Core.width / 2;
      ypos = Core.height / 2;

      width = Core.width;
      height = Core.height;
    }

  }
  
  /**
   * A TransitionAction that fades the screen out of view.
   * 
   * @author Brandon Hua
   */

  public class FadeOut implements TransitionAction {

    @Override
    public String transitionName() {
      return "fade_out";
    }

    @Override
    public boolean perform() {
      alpha = alpha + FADESPEED * Core.deltaTime;
      if (alpha >= 1) {
        alpha = 1;
        return true;
      }
      return false;
    }

    @Override
    public void changeMode() {
      currentTexture = new Texture("transition_fade.png");

      xpos = Core.width / 2;
      ypos = Core.height / 2;

      width = Core.width;
      height = Core.height;

      alpha = 0;
    }

  }
  
  /**
   * A TransitionAction that sends a wipe accross the screen.
   * 
   * @author Brandon Hua
   */

  public class Wipe implements TransitionAction {

    @Override
    public String transitionName() {
      return "wipe";
    }

    @Override
    public boolean perform() {
      xpos = (int) (xpos - (WIPESPEED * Core.deltaTime));
      if (xpos < Core.width / 4) {
        return true;
      }
      return false;
    }

    @Override
    public void changeMode() {
      currentTexture = new Texture("transition_wipe.png");

      xpos = Core.width * 2;
      ypos = Core.height / 2;

      width = Core.width * 2;
      height = Core.height;
    }

  }
  
  /**
   * A TransitionAction that simply darkens the view.
   * 
   * @author Brandon Hua
   */

  public class Darken implements TransitionAction {

    @Override
    public String transitionName() {
      return "darken";
    }

    @Override
    public boolean perform() {
      alpha = alpha + (FADESPEED / 2) * Core.deltaTime;
      if (alpha >= 0.8) {
        alpha = 0.8f;
        return true;
      }
      return false;
    }

    @Override
    public void changeMode() {
      currentTexture = new Texture("blank.png");

      xpos = Core.width / 2;
      ypos = Core.height / 2;

      width = Core.width;
      height = Core.height;

      alpha = 0;
    }

  }
  
  /**
   * A transition that doesn't perform an action.
   * 
   * @author Brandon Hua
   */

  public class None implements TransitionAction {

    @Override
    public String transitionName() {
      return "none";
    }

    @Override
    public boolean perform() {
      return true;
    }

    @Override
    public void changeMode() {

    }

  }
  
  /**
   * The render method draws the transitions.
   * 
   * @param sb The SpriteBatch used to draw.
   */

  public void render(SpriteBatch sb) {
    if (!mode.equals("none")) {
      sb.begin();
      Color c = sb.getColor();
      sb.setColor(c.r, c.g, c.b, alpha);
      sb.draw(currentTexture, xpos - width / 2, ypos - height / 2, width, height);
      sb.setColor(c.r, c.g, c.b, 1f);
      sb.end();
    }
  }

  public void dispose() {
    currentTexture.dispose();
  }

  public String getMode() {
    return mode;
  }
  
  /**
   * A method which changes the transition image out of the window view.
   */

  public void moveOutOfView() {
    xpos = 5 * Core.width;
    ypos = 5 * Core.height;
  }

}
