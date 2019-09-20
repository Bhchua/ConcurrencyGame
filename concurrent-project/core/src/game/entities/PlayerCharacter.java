package game.entities;

import game.Core;

/**
 * The player class that contains all the methods for movement and collisions.
 * 
 * @author Brandon Hua
 *
 */

public class PlayerCharacter extends Entity {

  private final int PLAYERNUM;
  private final int KBFORCE = 6;
  private final int DEATHTIME = 5000;
  private final int INVULNPERIOD = 1000;
  private static int initHealth = 6;
  private int fireRate = 500; // milliseconds
  private int lastFired = fireRate + 1;
  private int lastHit = INVULNPERIOD + 1;
  private int deathCounter = 0;
  private boolean invincible;
  private boolean dying = false;
  private int startx;
  private int starty;
  private int accel = 60;
  private int decel = 20;
  private float maxSpeed = 3.5f;
  private int damage = 1;
  private float bulletSpeed = 6;

  /**
   * A basic constructor for the object, initialises the position.
   * 
   * @param x            The initial x position.
   * @param y            The initial y position.
   * @param playerWidth  The width of the character.
   * @param playerHeight The height of the character.
   * @param playerNum    The player number.
   */

  public PlayerCharacter(int x, int y, int playerWidth, int playerHeight, int playerNum) {
    super(x, y, playerWidth, playerHeight, initHealth);
    startx = x;
    starty = y;
    PLAYERNUM = playerNum;
    setAccel(accel);
    setDecel(decel);
    setMaxSpeed(maxSpeed);
    setDamage(damage);
  }

  public int getPlayerNum() {
    return PLAYERNUM;
  }

  public float getBulletSpeed() {
    return bulletSpeed;
  }

  /**
   * The update method of the player character that moves the character and
   * changes the states of the character accordingly.
   * 
   * @param gameSpeed The speed of the current game model.
   */

  public void update(float gameSpeed) {
    if (!isDying()) {
      move(gameSpeed);
      updateLastHit(gameSpeed);
      updateLastFired(gameSpeed);
      changeFacing();
    } else {
      incrDeathCounter(gameSpeed);
    }
  }
  
  /**
   * A method to update the time between the last fired bullet.
   * 
   * @param gameSpeed The speed of the game.
   */

  public void updateLastFired(float gameSpeed) {
    long time = (long) (Core.deltaTime * gameSpeed);
    long lastFired = getLastFired() + time;
    setLastFired(lastFired);
  }

  /**
   * A method that resets certain values and places the player back
   * at the initial position.
   */
  
  public void respawn() {
    setPos(startx, starty);
    setHealth(initHealth);
    setDeath(false);
    deathCounter = 0;
    dying = false;
    deathCounter = 0;
    invincible = false;
    lastHit = 0;
    invincible = true;
  }

  public void incrDeathCounter(float gameSpeed) {
    deathCounter += Core.deltaTime * gameSpeed;
  }
  
  /**
   * A method that checks to see if the elapsed time has passed the maximum time 
   * a player character is dead for.
   * 
   * @return A boolean value for whether or not the time has passed.
   */

  public boolean deathTimeUp() {
    if (deathCounter < DEATHTIME) {
      return false;
    }
    return true;
  }

  public void setFireRate(int fireRate) {
    this.fireRate = fireRate;
  }

  public int getFireRate() {
    return fireRate;
  }

  public void setLastFired(long deltaTime) {
    lastFired = (int) deltaTime;
  }

  public int getLastFired() {
    return lastFired;
  }
  
  /**
   * A method that checks to see if the player can fire.
   * 
   * @return A boolean for whether or not a player could fire.
   */

  public boolean canFire() {
    if (lastFired > fireRate) {
      return true;
    }
    return false;
  }
  
  /**
   * A method that damages the player based on the damage of an entity.
   * 
   * @param entity The entity to get the damage value from.
   * @return A boolean value for whether the enemy has been damaged.
   */

  public boolean damage(Entity entity) {
    if (!invincible) {
      if (collidedWith(entity)) {
        this.knockback(entity, KBFORCE);
        int damage = entity.getDamage();
        this.subtractHealth(damage);
        lastHit = 0;
        invincible = true;
        return true;
      }
    }
    return false;
  }
  
  /**
   * A method that increases the last hit value and disables invincibility if the 
   * invulnerable period has passed.
   * 
   * @param gameSpeed The speed of the game.
   */

  public void updateLastHit(float gameSpeed) {
    if (gameSpeed > 0) {
      if (invincible) {
        lastHit = lastHit + (int) (Core.deltaTime * gameSpeed);
        if (lastHit > INVULNPERIOD) {
          invincible = false;
        }
      }
    }
  }

  public boolean isInvincible() {
    return invincible;
  }
  
  /**
   * A method that checks to see if the player is alive and then stops them from moving.
   * 
   * @return A boolean for whether or not a player is dying.
   */

  public boolean isDying() {
    dying = !checkLiving();
    if (dying) {
      setFacing("down");
      stop();
      invincible = false;
    }
    return dying;
  }

}
