package game.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.CameraVariables;
import game.Core;
import game.entities.PlayerCharacter;

/**
 * A renderer class to represent how much remaining health a player has.
 * 
 * @author Brandon Hua
 *
 */

public class HealthRenderer implements Renderer {

  private Texture healthSheet;
  private TextureRegion[][] healthRegions;
  private TextureRegion curFrame;
  private PlayerCharacter player;

  private final int SHEETCOLS = 3;
  private final int SHEETROWS = 6;

  private final int ANIMRATE = 250;

  /**
   * The HealthRenderer gets the player from the game model and loads the
   * appropriate health sprites.
   * 
   * @param player The player to be represented.
   */

  public HealthRenderer(PlayerCharacter player) {
    this.player = player;
    healthSheet = new Texture("health.png");

    TextureRegion[][] tmp = TextureRegion.split(healthSheet, healthSheet.getWidth() / SHEETCOLS,
        healthSheet.getHeight() / SHEETROWS);

    healthRegions = new TextureRegion[6][4];

    curFrame = new TextureRegion();

    for (int i = 0; i < 6; i++) {
      healthRegions[i][0] = tmp[i][0];
      healthRegions[i][1] = tmp[i][1];
      healthRegions[i][2] = tmp[i][2];
      healthRegions[i][3] = tmp[i][1];
    }

  }

  private int counter = 0;
  private int runtime = 0;

  /**
   * The changeCurFrame method modifies the current frame based on the remaining
   * health of the player.
   */

  public void changeCurFrame() {
    runtime += Core.deltaTime;
    if (runtime > ANIMRATE) {
      counter = (counter + 1) % 4;
      runtime = runtime % ANIMRATE;
    }

    int health = 6 - player.getHealth();
    health = health % 6;
    curFrame = healthRegions[health][counter];
  }

  @Override
  public void render(SpriteBatch sb, int focusx, int focusy) {

    int view = player.getPlayerNum() - 1;

    double xratioInverse = 1 / CameraVariables.xratio;
    double yratioInverse = 1 / CameraVariables.yratio;

    int width = (int) (Core.height / 8 * xratioInverse);
    int height = (int) (Core.height / 8 * yratioInverse);
    int xpos = (int) ((((2 * (view % 2)) - 1) * (6 * Core.width / 16)) + ((-2 * (view % 2)) + 1)
        + (Core.width / 2 - width / 2));
    ;
    int ypos = Core.height / 10 - height / 2;

    changeCurFrame();
    sb.draw(curFrame, xpos, ypos, width, height);
  }

  @Override
  public void dispose() {
    healthSheet.dispose();
  }

}
