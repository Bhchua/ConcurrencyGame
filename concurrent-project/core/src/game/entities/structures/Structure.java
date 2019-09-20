package game.entities.structures;

import game.entities.Wall;

/**
 * A Structure is intended to be a wall object with a type variable, allowing
 * walls to be represented differently in ui. The reason for not just placing
 * the type variable in the walls class is to separate a wall (which can be
 * placed without ui representation) and a structure (which cannot).
 * 
 * @author Brandon Hua
 */

public class Structure extends Wall {

  private final String TYPE;

  /**
   * The Structure object initialises with it's position and size, along with the
   * type and whether or not it can be collided with.
   * 
   * @param x          The x position of the structure.
   * @param y          The y position of the structure.
   * @param wallWidth  The width of the structure.
   * @param wallHeight The height of the structure.
   * @param type       The type of structure.
   * @param enemyCol   A boolean fow whether or not an enemy collides with the
   *                   wall.
   */
  public Structure(int x, int y, int wallWidth, int wallHeight, String type, boolean enemyCol) {
    super(x, y, wallWidth, wallHeight);
    TYPE = type;
    setEnemyCol(enemyCol);
  }

  public String getType() {
    return TYPE;
  }

}
