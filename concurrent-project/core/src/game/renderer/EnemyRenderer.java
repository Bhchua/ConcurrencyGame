package game.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.CameraVariables;
import game.Core;
import game.entities.Enemy;

import java.util.ArrayList;

/**
 * A renderer for the enemy entity.
 * 
 * @author Brandon Hua
 *
 */

public class EnemyRenderer implements Renderer {

  private static final int SHEETCOLS = 16;
  private static final int SHEETROWS = 16;
  private static final int FRAMES = 8;

  private static final int ANIMRATE = 200;

  private ArrayList<Enemy> enemies;

  private Texture enemySheet;

  private TextureRegion curFrame;

  private TextureRegion[] shooterAnimation;
  private TextureRegion[] chaserAnimation;

  /**
   * A constructor for the renderer class that initialises the sprites for an
   * enemy.
   * 
   * @param enemies An array list of enemies found in the game.
   */

  public EnemyRenderer(ArrayList<Enemy> enemies) {
    this.enemies = enemies;

    enemySheet = new Texture("enemy_sheet.png");

    TextureRegion[][] tmp = TextureRegion.split(enemySheet, enemySheet.getWidth() / SHEETCOLS,
        enemySheet.getHeight() / SHEETROWS);

    shooterAnimation = new TextureRegion[FRAMES];
    chaserAnimation = new TextureRegion[FRAMES];
    for (int i = 0; i < FRAMES; i++) {
      shooterAnimation[i] = tmp[0][i];
      chaserAnimation[i] = tmp[1][i];
    }
  }

  private int runtime = 0;
  private int counter = 0;

  /**
   * A method that changes the current frame based on the time elapsed.
   */

  public void changeCurFrame() {
    runtime += Core.deltaTime;
    if (runtime > ANIMRATE) {
      counter = (counter + 1) % FRAMES;
      runtime = runtime % ANIMRATE;
    }
  }

  /**
   * The render method for an enemy.
   */

  public void render(SpriteBatch sb, int focusx, int focusy) {
    double xratioInverse = 1 / CameraVariables.xratio;
    double yratioInverse = 1 / CameraVariables.yratio;
    int zoom = CameraVariables.zoom;

    changeCurFrame();

    for (int i = 0; i < enemies.size(); i++) {

      Enemy curEnemy = enemies.get(i);

      for (EnemyTypeSheet t : typeSheets) {
        if (t.getType().equals(curEnemy.getType())) {
          curFrame = t.getFrames()[counter];
        }
      }

      int drawx = (int) (Core.width / 2 - (focusx * zoom * xratioInverse) 
          + (curEnemy.getXpos() * zoom * xratioInverse)
          - ((curEnemy.getWidth()) / 2) * zoom * xratioInverse);
      int drawy = (int) (Core.height / 2 - (focusy * zoom * yratioInverse) 
          + (curEnemy.getYpos() * zoom * yratioInverse)
          - ((curEnemy.getHeight()) / 2) * zoom * yratioInverse);
      int width = (int) (curEnemy.getWidth() * zoom * xratioInverse);
      int height = (int) (curEnemy.getHeight() * zoom * yratioInverse);

      sb.draw(curFrame, drawx, drawy, width, height);
    }
  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub

  }

  private EnemyTypeSheet[] typeSheets = new EnemyTypeSheet[] { 
      new ShooterEnemySheet(), new ChaserEnemySheet() };

  /**
   * An interface to determine which sprite sheet to use depending on the type of
   * enemy.
   * 
   * @author Brandon Hua
   */

  public interface EnemyTypeSheet {
    /**
     * A method to get the name of the enemy type.
     * 
     * @return The name of the enemy type.
     */
    public String getType();

    /**
     * A method to get an array of TextureRegions an enemy uses.
     * 
     * @return The frames of the enemy.
     */
    public TextureRegion[] getFrames();
  }

  /**
   * The EnemyTypeSheet for shooter enemies.
   * 
   * @author Brandon Hua
   */

  public class ShooterEnemySheet implements EnemyTypeSheet {

    @Override
    public String getType() {
      return "shoot";
    }

    @Override
    public TextureRegion[] getFrames() {
      return shooterAnimation;
    }

  }

  /**
   * The EnemyTypeSheet for chaser enemies.
   * 
   * @author Brandon Hua
   */

  public class ChaserEnemySheet implements EnemyTypeSheet {

    @Override
    public String getType() {
      return "chase";
    }

    @Override
    public TextureRegion[] getFrames() {
      return chaserAnimation;
    }

  }

}
