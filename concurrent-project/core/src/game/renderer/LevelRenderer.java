package game.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.CameraVariables;
import game.Core;
import game.model.levels.Level;

/**
 * The renderer for a Level.
 * 
 * @author Brandon Hua
 *
 */

public class LevelRenderer implements Renderer {

  private Level level;
  private Texture background;

  /**
   * A constructor that sets the background image as whatever is specified in a
   * level object.
   * 
   * @param level The level to be rendered.
   */

  public LevelRenderer(Level level) {
    this.level = level;
    background = new Texture(level.getBgFileName());
  }

  @Override
  public void render(SpriteBatch sb, int playerx, int playery) {
    double xratioInverse = 1 / CameraVariables.xratio;
    double yratioInverse = 1 / CameraVariables.yratio;
    int zoom = CameraVariables.zoom;

    int xpos = (int) (Core.width / 2 - (playerx * zoom * xratioInverse) + level.getXPos()
        - ((level.getWidth() / 2) * zoom * xratioInverse));
    int ypos = (int) (Core.height / 2 - (playery * zoom * yratioInverse) + level.getYPos()
        - ((level.getHeight() / 2) * zoom * yratioInverse));
    int width = (int) (level.getWidth() * zoom * xratioInverse);
    int height = (int) (level.getHeight() * zoom * yratioInverse);

    sb.draw(background, xpos, ypos, width, height);
  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub
    
  }
}
