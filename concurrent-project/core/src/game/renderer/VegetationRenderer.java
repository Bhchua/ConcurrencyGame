package game.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.CameraVariables;
import game.Core;
import game.entities.tiles.VegetationTile;

import java.util.ArrayList;

/**
 * The renderer class for the VegetationTile object.
 * 
 * @author Brandon Hua
 */

public class VegetationRenderer implements Renderer {

  private final int SHEETCOLS = 8;
  private final int SHEETROWS = 8;
  private final int ANIMRATE = 1000;

  private ArrayList<VegetationTile> tiles;

  private Texture spSheet;

  private TextureRegion[] grass;
  private int grassFrames = 8;

  private TextureRegion[] flower;
  private int flowerFrames = 2;

  private TextureRegion curTex;

  public int counter;
  public long runtime = Core.time;

  /**
   * The VegetationRenderer initialises the textures for VegetationTiles.
   * 
   * @param tiles The VegetationTiles to render.
   */
  
  public VegetationRenderer(ArrayList<VegetationTile> tiles) {
    this.tiles = tiles;
    spSheet = new Texture("vegetation.png");
    grass = new TextureRegion[grassFrames];
    flower = new TextureRegion[flowerFrames];

    TextureRegion[][] tmp = TextureRegion.split(spSheet, spSheet.getWidth() / SHEETCOLS,
        spSheet.getHeight() / SHEETROWS);

    for (int i = 0; i < grassFrames; i++) {
      grass[i] = tmp[0][i];
    }

    for (int i = 0; i < flowerFrames; i++) {
      flower[i] = tmp[1][i];
    }
  }

  /**
   * The changeCurFrame method changes the current frame based on the run time
   * of the application and the animation rate.
   * 
   * @param tile The tile to change the frames to.
   */
  
  public void changeCurFrame(VegetationTile tile) {
    int frames = flowerFrames;
    TextureRegion[] curRegion = flower;

    if (tile.getType().equals("grass")) {
      frames = grassFrames;
      curRegion = grass;
    }

    if (Core.time - runtime > ANIMRATE) {
      counter = (counter + 1);
      runtime = Core.time;
    }

    curTex = curRegion[counter % frames];

    counter = counter % (grassFrames * flowerFrames);

  }

  @Override
  public void render(SpriteBatch sb, int playerx, int playery) {

    double xratioInverse = 1 / CameraVariables.xratio;
    double yratioInverse = 1 / CameraVariables.yratio;
    int zoom = CameraVariables.zoom;

    for (int i = 0; i < tiles.size(); i++) {
      VegetationTile curTile = tiles.get(i);
      changeCurFrame(curTile);

      int drawx = (int) (Core.width / 2 - (playerx * zoom * xratioInverse)
          + ((curTile.getXpos()) * zoom * xratioInverse) 
          - ((curTile.getWidth()) / 2) * zoom * xratioInverse);
      int drawy = (int) (Core.height / 2 - (playery * zoom * yratioInverse)
          + ((curTile.getYpos()) * zoom * yratioInverse) 
          - ((curTile.getHeight()) / 2) * zoom * yratioInverse);
      int width = (int) (curTile.getWidth() * zoom * xratioInverse);
      int height = (int) (curTile.getHeight() * zoom * yratioInverse);

      sb.draw(curTex, drawx, drawy, width, height);
    }
  }

  public void dispose() {
    spSheet.dispose();
  }

}
