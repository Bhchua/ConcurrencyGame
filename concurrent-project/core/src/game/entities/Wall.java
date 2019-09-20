package game.entities;

/**
 * An extremely basic entity object with no functionality, only acts as an
 * obstacle to block other entities.
 * 
 * @author Brandon Hua
 *
 */

public class Wall extends Entity {

  private boolean enemyCollision = true;

  /**
   * A constructor for the wall class.
   * 
   * @param x          The x position of a wall.
   * @param y          The y position of a wall.
   * @param wallWidth  The width of a wall.
   * @param wallHeight The height of a wall.
   */

  public Wall(int x, int y, int wallWidth, int wallHeight) {
    super(x, y, wallWidth, wallHeight);
  }

  public boolean getEnemyCol() {
    return enemyCollision;
  }

  public void setEnemyCol(boolean enemyCol) {
    enemyCollision = enemyCol;
  }

}
