package game.entities.tiles;

import game.entities.Entity;

/**
 * A VegetationTile is an Entity purely for aesthetic purposes.
 * 
 * @author Brandon Hua
 */

public class VegetationTile extends Entity {

  private final String TYPE;

  /**
   * The VegetationTile initialises the position, size and type data.
   * 
   * @param xpos   The x position of the structure.
   * @param ypos   The y position of the structure.
   * @param width  The y position of the structure.
   * @param height The y position of the structure.
   * @param type   The type of VegetationTile.
   */
  public VegetationTile(int xpos, int ypos, int width, int height, String type) {
    super(xpos, ypos, width, height);
    TYPE = type;
  }

  public String getType() {
    return TYPE;
  }

}
