package game.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.CameraVariables;
import game.Core;
import game.entities.structures.Structure;
import game.entities.structures.buildings.StructLoader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The Renderer class for structures.
 * 
 * @author Brandon Hua
 */

public class StructureRenderer implements Renderer {

  private TextureRegion[] curRegion;

  private ArrayList<Structure> structures;

  private int resolution = CameraVariables.sres;

  private int curLayers;
  private final float OFFSET = 0.04f;

  /**
   * The StructureRenderer initialises the list of structures to be rendered and
   * the building interfaces used.
   * 
   * @param structures The Structures to be rendered.
   */

  public StructureRenderer(ArrayList<Structure> structures) {
    this.structures = structures;
  }

  /**
   * A method to change the current region being rendered.
   * 
   * @param strct The current structure.
   */

  public void changeCurRegion(Structure strct) {
    for (int i = 0; i < StructLoader.getStructs().size(); i++) {
      if (strct.getType().equals(StructLoader.getStructs().get(i).getName())) {
        curRegion = StructLoader.getStructs().get(i).getRegions();
        curLayers = StructLoader.getStructs().get(i).getLayers();
      }
    }
  }

  @Override
  public void render(SpriteBatch sb, int playerx, int playery) {

    for (int i = 0; i < structures.size(); i++) {
      structures.get(i).setCenter(playerx, playery);
    }
    Collections.sort(structures);

    double xratioInverse = 1 / CameraVariables.xratio;
    double yratioInverse = 1 / CameraVariables.yratio;
    int zoom = CameraVariables.zoom;

    for (int i = 0; i < structures.size(); i++) {
      Structure curStruct = structures.get(i);
      changeCurRegion(curStruct);

      for (int j = 0; j < curLayers * resolution; j++) {

        float rgb = 0.2f + (float) j / (curLayers * resolution);
        if (rgb > 1) {
          rgb = 1;
        }
        float adjustx = (curStruct.getXpos() - playerx) * ((((float) j * OFFSET) / resolution));
        float adjusty = (curStruct.getYpos() - playery) * ((((float) j * OFFSET) / resolution));

        int drawx = (int) (Core.width / 2 - (playerx * zoom * xratioInverse)
            + ((curStruct.getXpos() + adjustx) * zoom * xratioInverse)
            - ((curStruct.getWidth()) / 2) * zoom * xratioInverse);
        int drawy = (int) (Core.height / 2 - (playery * zoom * yratioInverse)
            + ((curStruct.getYpos() + adjusty) * zoom * yratioInverse)
            - ((curStruct.getHeight()) / 2) * zoom * yratioInverse);
        int width = (int) (curStruct.getWidth() * zoom * xratioInverse);
        int height = (int) (curStruct.getHeight() * zoom * yratioInverse);

        sb.setColor(rgb, rgb, rgb, 1);
        sb.draw(curRegion[j / resolution], drawx, drawy, width, height);
        sb.setColor(1, 1, 1, 1);

      }

    }

  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub

  }

}
