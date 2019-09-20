package game.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The frame of a renderer class.
 * The reason for this being a class rather than an
 * interface is due to the use of one of the render methods
 * but not both.
 * 
 * @author Brandon Hua
 */

public interface Renderer {

  /**
   * The render method uses a sprite batch to draw the classes' sprites on screen.
   * 
   * @param sb The SpriteBatch used to draw.
   * @param focusx The relative x-position to draw against.
   * @param focusy The relative y-position to draw against.
   */
  
  public void render(SpriteBatch sb, int focusx, int focusy);
  
  /**
   * A method to clear the textures from memory.
   */
  
  public void dispose();

}
