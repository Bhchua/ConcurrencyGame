package game.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.CameraVariables;
import game.Core;
import game.entities.Bullet;

import java.util.ArrayList;

/**
 * A renderer for the bullet entity.
 * 
 * @author Brandon Hua
 *
 */

public class BulletRenderer implements Renderer {

  private final int SHEETCOLS = 8;
  private final int SHEETROWS = 8;
  private final int FRAMECOUNT = 2;
  private final int ANIMRATE = 100;

  private ArrayList<Bullet> bullets;

  private Texture bulletSheet;

  private TextureRegion curFrame;

  private TextureRegion[] friendlyBulletFrames;
  private TextureRegion[] enemyBulletFrames;

  /**
   * The initialiser that takes an list of bullets and initialises the sprite
   * sheet to represent the bullets.
   * 
   * @param bullets An array list of all the bullets in a game.
   */

  public BulletRenderer(ArrayList<Bullet> bullets) {
    this.bullets = bullets;

    bulletSheet = new Texture("bullets.png");

    TextureRegion[][] tmp = TextureRegion.split(bulletSheet, bulletSheet.getWidth() / SHEETCOLS,
        bulletSheet.getHeight() / SHEETROWS);

    friendlyBulletFrames = new TextureRegion[FRAMECOUNT];
    enemyBulletFrames = new TextureRegion[FRAMECOUNT];
    for (int i = 0; i < FRAMECOUNT; i++) {
      friendlyBulletFrames[i] = tmp[0][i];
      enemyBulletFrames[i] = tmp[1][i];
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
      counter = (counter + 1) % 2;
      runtime = runtime % ANIMRATE;
    }
  }

  /**
   * The render method for a bullet that changes the image based on whether a
   * bullet is friendly or not.
   */

  public void render(SpriteBatch sb, int playerx, int playery) {
    double xratioInverse = 1 / CameraVariables.xratio;
    double yratioInverse = 1 / CameraVariables.yratio;
    int zoom = CameraVariables.zoom;
    changeCurFrame();

    for (int i = 0; i < bullets.size(); i++) {
      Bullet curBullet = bullets.get(i);
      if (curBullet.isFriendly()) {
        curFrame = friendlyBulletFrames[counter];
      } else {
        curFrame = enemyBulletFrames[counter];
      }

      int drawx = (int) (Core.width / 2 - (playerx * zoom * xratioInverse)
          + (curBullet.getXpos() * zoom * xratioInverse) 
          - ((curBullet.getWidth()) / 2) * zoom * xratioInverse);
      int drawy = (int) (Core.height / 2 - (playery * zoom * yratioInverse)
          + (curBullet.getYpos() * zoom * yratioInverse) 
          - ((curBullet.getHeight()) / 2) * zoom * yratioInverse);
      int width = (int) (curBullet.getWidth() * zoom * xratioInverse);
      int height = (int) (curBullet.getHeight() * zoom * yratioInverse);

      sb.draw(curFrame, drawx, drawy, width, height);
    }
  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub
    
  }

}
