package game.entities;

/**
 * The bullet class is a type of entity with only rudamentary functions such as
 * move().
 * 
 * @author Brandon Hua
 *
 */

public class Bullet extends Entity {

  private final int LIFETIME = 1500;
  
  private int totalLife = 0;
  private Boolean friendly;

  /**
   * A constructor for the bullet class that takes several values to determine it's
   * position and direction.
   * 
   * @param xpos     The initial x position of the bullet.
   * @param ypos     The initial y position of the bullet.
   * @param width    The width of the bullet.
   * @param height   The height of the bullet.
   * @param xdir     The direction the bullet is facing on the x axis.
   * @param ydir     The direction the bullet is facing on the Y axis.
   * @param friendly A boolean for whether or not it is friendly.
   * @param damage   The damage done by the bullet.
   * @param maxSpeed The max travel speed of the bullet.
   */

  public Bullet(int xpos, int ypos, int width, int height, 
      float xdir, float ydir, boolean friendly, int damage, float maxSpeed) {
    super(xpos, ypos, width, height);
    this.setXDir(xdir);
    this.setYDir(ydir);
    this.setAccel(100);
    this.setDecel(0);
    this.setMaxSpeed(maxSpeed);
    this.friendly = friendly;
    this.setDamage(damage);
  }

  /**
   * A method that accelerates the bullet if the total lived time
   * is less that the maximum life-span of a bullet.
   * Otherwise the bullet is marked as 'dead'.
   * 
   * @param deltaTime The difference in time from the last frame (in milliseconds).
   */
  
  public void moveBullet(long deltaTime) {
    accelDecel(deltaTime);
    totalLife = (int) (totalLife + deltaTime);
    if (totalLife > LIFETIME) {
      this.die();
    }
  }

  public boolean isFriendly() {
    return friendly;
  }
  
  /**
   * The update method moves the bullet and scales it depending
   * on the game speed.
   * @param gameSpeed The speed at which the game is running.
   */

  public void update(float gameSpeed) {
    this.move(gameSpeed);
  }

}
