package game.entities;

import game.Core;
import game.model.ShooterGame;

/**
 * A The spawner object places enemies into a game model in pre-determined
 * intervals.
 * 
 * @author Brandon hua
 *
 */

public class Spawner {

  private int spawnInterval;
  private long lastSpawn = Core.time;
  private int xpos;
  private int ypos;

  /**
   * The constructor for a spawner.
   * 
   * @param xpos          The x-position of the spawner.
   * @param ypos          The x-position of the spawner.
   * @param spawnInterval The time between each enemy spawn.
   */

  public Spawner(int xpos, int ypos, int spawnInterval) {
    this.xpos = xpos;
    this.ypos = ypos;
    this.spawnInterval = spawnInterval;
  }

  /**
   * The update method increases an interval based on the the difference between
   * the last spawned time and the current time. When the interval is reached it
   * uses the spawn() method to place an enemy in the game.
   * 
   * @param gameSpeed A modifier that speeds up or slows down the rate at which
   *                  enemies are spawned.
   * @param game      The game to spawn the enemies in.
   */

  public void update(float gameSpeed, ShooterGame game) {
    if (gameSpeed > 0) {
      long curTime = Core.time;
      if (lastSpawn + (long) spawnInterval < curTime) {
        long spawnNumber = (curTime - lastSpawn) / spawnInterval;
        for (int i = 0; i < spawnNumber; i++) {
          spawn(game);
        }
        lastSpawn = Core.time;
      }
    }
  }

  /**
   * A method that places an enemy into a game.
   * 
   * @param game The game for the enemy to be placed in.
   */

  public void spawn(ShooterGame game) {
    int random = (int) Math.floor(Math.random() * Math.floor(2));
    game.addEnemy(xpos, ypos, 64, 64, typeNumber[random]);
  }

  private String[] typeNumber = new String[] { "shoot", "chase" };

}
