package game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.screens.menus.TransitionHandler;
import game.screens.threads.MenuThread;

/**
 * A screen class holds all the necessary functions for a game screen.
 * 
 * @author Code from Brent Aureli's LibGDX Tutorials.
 *
 */
public abstract class Screen {

  protected GameScreenManager gsm;
  protected Texture background;
  protected TransitionHandler thandler;
  protected MenuThread mthread;
  
  /**
   * The constructor for a screen object.
   *  
   * @param gsm The GameScreenManager used to control the screen.
   */
  
  protected Screen(GameScreenManager gsm) {
    this.gsm = gsm;
  }
  
  /**
   * A method to control the inputs of a screen.
   */
  
  public abstract void handleInput();
  
  /**
   * The method used to update elements specific to the screen.
   */
  
  public abstract void update();
  
  /**
   * A method to render the screen within the application window.
   * 
   * @param sb The SpriteBatch used to render.
   */
  
  public abstract void render(SpriteBatch sb);
  
  /**
   * A method to clear unused sprites from memory.
   */
  
  public abstract void dispose();
  
}

