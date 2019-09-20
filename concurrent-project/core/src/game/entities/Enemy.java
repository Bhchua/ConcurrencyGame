package game.entities;

import game.Core;
import game.entities.EnemyTypeAction.EnemyAction;
import game.entities.EnemyTypeAction.EnemyType;

/**
 * The enemy class is an entity with simple UI that allows them to face an
 * object.
 * 
 * @author Brandon Hua
 *
 */

public class Enemy extends Entity {

  private final int RANGE = 128;
  private final int KBFORCE = 16;
  private final int FIRERATE = 10000; // milliseconds
  private int lastFired = FIRERATE + 1;
  private float bulletSpeed = 3;
  private String type;
  private EnemyAction action;
  private boolean shoots;
  private int points;

  /**
   * The constructor for an enemy object.
   * 
   * @param xpos   The initial x position of the enemy.
   * @param ypos   The initial y position of the enemy.
   * @param width  The width of the enemy.
   * @param height The height of the enemy.
   * @param type   The type of enemy.
   */

  public Enemy(int xpos, int ypos, int width, int height, String type) {
    super(xpos, ypos, width, height);
    this.setAccel(30);
    this.setDecel(20);
    this.type = type;
    for (EnemyType t : EnemyTypeAction.getTypeList()) {
      if (t.getType().equals(type)) {
        this.action = t.typeAction();
        this.shoots = t.canShoot();
        this.setHealth(t.getHealth());
        this.setDamage(t.getDamage());
        this.setMaxSpeed(t.getSpeed());
        this.points = t.getPoints();
      }
    }
  }

  /**
   * A method to check whether or not the enemy is in range of a target.
   * 
   * @param target The target to check against.
   * @return True if it is in range, false otherwise.
   */

  public boolean inRange(Entity target) {
    if (getXpos() - RANGE < target.getXpos() && target.getXpos() < getXpos() + RANGE) {
      if (getYpos() - RANGE < target.getYpos() && target.getYpos() < getYpos() + RANGE) {
        return true;
      }
    }
    return false;
  }

  /**
   * A method to change the direction of an enemy towards a target.
   * 
   * @param target The target the enemy is to face towards.
   */

  public void faceTowards(Entity target) {
    float xdir = 0;
    float ydir = 0;

    int xdiff = target.getXpos() - this.getXpos();
    int ydiff = target.getYpos() - this.getYpos();

    if ((Math.abs(xdiff) + Math.abs(ydiff)) > 0) {
      float ratio = (float) 1 / (float) (Math.abs(xdiff) + Math.abs(ydiff));
      xdir = ratio * xdiff;
      ydir = ratio * ydiff;
    }

    this.setXDir(xdir);
    this.setYDir(ydir);
  }

  /**
   * The method to apply the movement.
   * 
   * @param gameSpeed The speed of the game model.
   */

  public void update(float gameSpeed) {
    move(gameSpeed);
    updateLastFired(gameSpeed);
  }

  public int getPoints() {
    return this.points;
  }

  /**
   * A method to apply damage if a bullet collides with this enemy.
   * 
   * @param bullet The bullet to check collision against.
   * @return A boolean for whether or not it took damage.
   */

  public boolean damage(Bullet bullet) {
    if (bullet.isFriendly()) {
      if (this.collidedWith(bullet)) {
        this.knockback(bullet, KBFORCE);
        int damage = bullet.getDamage();
        this.subtractHealth(damage);
        return true;
      }
    }
    return false;
  }

  public int getFireRate() {
    return FIRERATE;
  }

  public void setLastFired(long deltaTime) {
    lastFired = (int) deltaTime;
  }

  public int getLastFired() {
    return lastFired;
  }

  /**
   * A method that updates the time the enemy last fired at.
   * 
   * @param gameSpeed A modifier to speed up or slow down the rate of fire.
   */

  public void updateLastFired(float gameSpeed) {
    long time = (long) (Core.deltaTime * gameSpeed);
    long lastFired = getLastFired() + time;
    setLastFired(lastFired);
  }

  /**
   * A method to get a boolean value for whether or not an enemy can fire based on
   * the last time fired.
   * 
   * @return A boolean value representing if an enemy can fire.
   */

  public boolean canFire() {
    if (shoots) {
      if (lastFired > FIRERATE) {
        return true;
      }
    }
    return false;
  }

  public float getBulletSpeed() {
    return bulletSpeed;
  }

  /**
   * A method that performs the action assigned to the enemy.
   * 
   * @param target    The target to follow/shoot at.
   * @param deltaTime The time since the last frame.
   */

  public void performAction(Entity target, long deltaTime) {
    action.action(this, target, deltaTime);
  }

  public String getType() {
    return type;
  }

}
