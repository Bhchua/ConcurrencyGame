package game.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.CameraVariables;
import game.Core;
import game.entities.Wall;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A renderer class for the Wall entity.
 * 
 * @author Brandon Hua
 *
 */

public class WallRenderer implements Renderer {

  private final int SHEETCOLS = 8;
  private final int SHEETROWS = 8;

  private ArrayList<Wall> walls;

  private Texture wallSheet;

  private TextureRegion[] wallTextures;

  /**
   * The constructor for the wall class that initialses the tile sets of the
   * walls.
   * 
   * @param walls The list of walls from the game model to be rendered.
   */

  public WallRenderer(ArrayList<Wall> walls) {
    this.walls = walls;
    wallTextures = new TextureRegion[SHEETCOLS * SHEETROWS];

    wallSheet = new Texture("temp_walls.png");
    TextureRegion[][] tmp = TextureRegion.split(wallSheet, wallSheet.getWidth() / SHEETCOLS,
        wallSheet.getHeight() / SHEETROWS);

    wallTextures[0] = tmp[0][0];
    wallTextures[1] = tmp[0][1];
  }

  /**
   * The render method for a wall that simply draws it's position relative to the
   * player's which is assumed to be in the center of the screen.
   */

  @Override
  public void render(SpriteBatch sb, int playerx, int playery) {

    for (int i = 0; i < walls.size(); i++) {
      walls.get(i).setCenter(playerx, playery);
    }
    Collections.sort(walls);

    double xratioInverse = 1 / CameraVariables.xratio;
    double yratioInverse = 1 / CameraVariables.yratio;
    int zoom = CameraVariables.zoom;

    sb.begin();
    for (int i = 0; i < walls.size(); i++) {

      Wall curWall = walls.get(i);

      for (int j = 0; j < 6; j++) {
        float rgb = 0.5f + (float) j / 12;
        float adjustx = (curWall.getXpos() - playerx) * ((float) j / 24);
        float adjusty = (curWall.getYpos() - playery) * ((float) j / 24);

        int drawx = (int) (Core.width / 2 - (playerx * zoom * xratioInverse)
            + ((curWall.getXpos() + adjustx) * zoom * xratioInverse)
            - ((curWall.getWidth()) / 2) * zoom * xratioInverse);
        int drawy = (int) (Core.height / 2 - (playery * zoom * yratioInverse)
            + ((curWall.getYpos() + adjusty) * zoom * yratioInverse)
            - ((curWall.getHeight()) / 2) * zoom * yratioInverse);
        int width = (int) (curWall.getWidth() * zoom * xratioInverse);
        int height = (int) (curWall.getHeight() * zoom * yratioInverse);

        sb.setColor(rgb, rgb, rgb, 1);
        sb.draw(wallTextures[0], drawx, drawy, width, height);
        sb.setColor(1, 1, 1, 1);
      }
    }
    sb.end();
  }

  public void dispose() {
    wallSheet.dispose();
  }

}
