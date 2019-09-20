package game.entities;

import game.model.ShooterGame;

/**
 * The Entity class contains all the necessary fields for positional and size
 * information of any in-game object.
 * 
 * @author Brandon Hua
 */

public class Entity implements Comparable<Entity> {

  private int xpos;
  private int ypos;
  private int width;
  private int height;
  private String name;

  private int acceleration;
  private int decceleration;
  private float maxspeed;

  private float xvel = 0;
  private float yvel = 0;

  private float xdir = 0;
  private float ydir = 0;

  private String facing;
  private boolean moving;

  private boolean decelX;
  private boolean decelY;

  private int health;
  private boolean dead;
  private int damage;

  /**
   * The constructor of an entity.
   * 
   * @param xpos   The initial x-position of the entity.
   * @param ypos   The initial y-position of the entity.
   * @param width  The width of the object.
   * @param height The height of the object.
   */

  public Entity(int xpos, int ypos, int width, int height) {
    this.xpos = xpos;
    this.ypos = ypos;
    this.height = height;
    this.width = width;
    this.xvel = 0;
    this.yvel = 0;
    this.xdir = 0;
    this.ydir = 0;
    this.facing = "down";
    this.moving = false;
    this.decelX = false;
    this.decelY = false;
    this.dead = false;
    this.health = 1;
  }

  /**
   * The constructor of an entity, usually ones that move.
   * 
   * @param xpos   The initial x-position of the entity.
   * @param ypos   The initial y-position of the entity.
   * @param width  The width of the object.
   * @param height The height of the object.
   * @param health the initial health of the object.
   */

  public Entity(int xpos, int ypos, int width, int height, int health) {
    this.xpos = xpos;
    this.ypos = ypos;
    this.height = height;
    this.width = width;
    this.xvel = 0;
    this.yvel = 0;
    this.xdir = 0;
    this.ydir = 0;
    this.facing = "down";
    this.moving = false;
    this.decelX = false;
    this.decelY = false;
    this.dead = false;
    this.health = health;
  }

  public void subtractHealth(int remHealth) {
    setHealth(health - remHealth);
  }

  /**
   * A method that sets the health of the entity but does not allow it to be set
   * at a value lower than 0.
   * 
   * @param health The value to set the health at.
   */

  public void setHealth(int health) {
    this.health = health;
    if (health < 0) {
      this.health = 0;
    }
  }

  /**
   * A method that checks to see if an entity is dead by checking its health.
   * 
   * @return A boolean indicating death.
   */

  public boolean checkLiving() {
    if (health <= 0) {
      die();
    }
    return !dead;
  }

  public void die() {
    setDeath(true);
  }

  public void setDeath(boolean death) {
    dead = death;
  }

  public boolean isDead() {
    return dead;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

  public int getDamage() {
    return damage;
  }

  public int getHealth() {
    return health;
  }

  /**
   * A method that moves this entity in the opposite direction from another.
   * 
   * @param entity The entity to move away from.
   * @param force  The force of the knock-back.
   */

  public void knockback(Entity entity, int force) {

    if (getXpos() != entity.getXpos()) {
      xdir = (int) -(entity.getXpos() - getXpos()) / Math.abs(entity.getXpos() - getXpos());
    }

    if (getYpos() != entity.getYpos()) {
      ydir = (int) -(entity.getYpos() - getYpos()) / Math.abs(entity.getYpos() - getYpos());
    }

    this.setXVel(force);
    this.setYVel(force);
  }

  public void setXPos(int x) {
    this.xpos = x;
  }

  public int getXpos() {
    return xpos;
  }

  public void setYPos(int y) {
    this.ypos = y;
  }

  public int getYpos() {
    return ypos;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public String getName() {
    return name;
  }

  public int minX() {
    return getXpos() - width / 2;
  }

  public int maxX() {
    return getXpos() + width / 2;
  }

  public int minY() {
    return getYpos() - height / 2;
  }

  public int maxY() {
    return getYpos() + height / 2;
  }

  public void setAccel(int acc) {
    this.acceleration = acc;
  }

  public void setDecel(int decc) {
    this.decceleration = decc;
  }

  public void setMaxSpeed(float max) {
    this.maxspeed = max;
  }

  public int getAccel() {
    return this.acceleration;
  }

  public int getDeccel() {
    return this.decceleration;
  }

  public float getMaxSpeed() {
    return this.maxspeed;
  }

  public void setFacing(String facing) {
    this.facing = facing;
  }

  public float getXVel() {
    return this.xvel;
  }

  public float getYVel() {
    return this.yvel;
  }

  /**
   * Detect whether or not an entity is in collision with another entity.
   * 
   * @param entity The entity to check the collision of this object against.
   * @return A boolean value for whether it has collided or not.
   */

  public boolean collidedWith(Entity entity) {
    if (entity.minX() < maxX() && minX() < entity.maxX()) {
      if (entity.minY() < maxY() && minY() < entity.maxY()) {
        return true;
      }
    }
    return false;
  }

  public void setPos(int x, int y) {
    this.setXPos(x);
    this.setYPos(y);
  }

  /**
   * Set the velocity of the Y axis.
   * 
   * @param vel Velocity
   */

  public void setXVel(int vel) {
    xvel = vel * xdir;
  }

  /**
   * Set the velocity of the X axis.
   * 
   * @param vel Velocity
   */

  public void setYVel(int vel) {
    yvel = vel * ydir;
  }

  /**
   * Set the direction the entity is facing/moving in.
   * 
   * @param xdir Direction (1 or -1)
   */

  public void setXDir(float xdir) {
    this.xdir = xdir;
  }

  /**
   * Set the direction the entity is facing/moving in.
   * 
   * @param ydir Direction (1 or -1)
   */

  public void setYDir(float ydir) {
    this.ydir = ydir;
  }

  /**
   * The move class that takes current velocity and applies it to the entity's
   * current position.
   * 
   * @param gameSpeed The speed of the game.
   */

  public void move(float gameSpeed) {
    this.setXPos((int) (this.getXpos() + (xvel * gameSpeed)));
    this.setYPos((int) (this.getYpos() + (yvel * gameSpeed)));
  }

  /**
   * Change the values of the moving boolean and facing string to reflect the
   * current state of the entity.
   */

  public void changeFacing() {
    if (xvel != 0 || yvel != 0) {
      moving = true;
      if (decelX && decelY) {
        moving = false;
      }
    } else {
      moving = false;
    }

    if (ydir < 0) {
      facing = "down";
    }
    if (xdir > 0) {
      facing = "right";
    }

    if (xdir < 0) {
      facing = "left";
    }
    if (ydir > 0) {
      facing = "up";
      if (xdir > 0) {
        facing = "up_right";
      }
      if (xdir < 0) {
        facing = "up_left";
      }
    }
  }

  public String getFacing() {
    return facing;
  }

  public boolean isMoving() {
    return moving;
  }

  /**
   * Calculate the X velocity based on the acceleration and difference between the
   * time of the last frame.
   * 
   * @param deltaTime The difference in time from the last frame
   */

  public void accelerateX(long deltaTime) {
    xvel += ((float) deltaTime / 1000 * this.getAccel() * xdir);
    if (xvel > this.getMaxSpeed()) {
      xvel = this.getMaxSpeed();
    }
    if (xvel < -this.getMaxSpeed()) {
      xvel = -this.getMaxSpeed();
    }
    decelX = false;
  }

  /**
   * Calculate the Y velocity based on the acceleration and difference between the
   * time of the last frame.
   * 
   * @param deltaTime The difference in time from the last frame.
   */

  public void accelerateY(long deltaTime) {
    yvel += ((float) deltaTime / 1000 * this.getAccel() * ydir);
    if (yvel > this.getMaxSpeed()) {
      yvel = this.getMaxSpeed();
    }
    if (yvel < -this.getMaxSpeed()) {
      yvel = -this.getMaxSpeed();
    }
    decelY = false;
  }

  /**
   * Calculate the X velocity based on the deceleration and difference between the
   * time of the last frame.
   * 
   * @param deltaTime The difference in time from the last frame.
   */

  public void decelerateX(long deltaTime) {
    if (xvel > 0) {
      xvel -= ((float) deltaTime / 1000 * this.getDeccel());
      if (xvel < 0) {
        xvel = 0;
      }
    }
    if (xvel < 0) {
      xvel += ((float) deltaTime / 1000 * this.getDeccel());
      if (xvel > 0) {
        xvel = 0;
      }
    }
    decelX = true;
  }

  /**
   * Calculate the Y velocity based on the deceleration and difference between the
   * time of the last frame.
   * 
   * @param deltaTime The difference in time from the last frame.
   */

  public void decelerateY(long deltaTime) {
    if (yvel > 0) {
      yvel -= ((float) deltaTime / 1000 * this.getDeccel());
      if (yvel < 0) {
        yvel = 0;
      }
    }
    if (yvel < 0) {
      yvel += ((float) deltaTime / 1000 * this.getDeccel());
      if (yvel > 0) {
        yvel = 0;
      }
    }
    decelY = false;
  }

  /**
   * A method that accelerates the entity if it's moving (based on the x/ydir
   * variables) and decelerates the entity has stopped.
   * 
   * @param deltaTime The difference in time between the current frame and the
   *                  last frame.
   */

  public void accelDecel(long deltaTime) {
    if (xdir != 0) {
      accelerateX(deltaTime);
    } else {
      decelerateX(deltaTime);
    }

    if (ydir != 0) {
      accelerateY(deltaTime);
    } else {
      decelerateY(deltaTime);
    }
  }

  /**
   * Code for preventing a entity character from passing through wall objects.
   * 
   * @param entity The object the entity is being tested against.
   */

  public void solidCollision(Entity entity) {
    if (entity.minX() < maxX() && minX() < entity.maxX()) {

      if ((minY() + yvel) < entity.maxY() && maxY() > entity.maxY() - (entity.getHeight() * 0.8)) {
        // setYPos(getYPos() + 1);
        yvel = 0;
      }

      if ((maxY() + yvel) > entity.minY() && minY() < entity.minY() + (entity.getHeight() * 0.2)) {
        // setYPos(getYPos() - 1);
        yvel = 0;
      }
    }

    if (entity.minY() < maxY() && minY() < entity.maxY()) {

      if ((minX() + xvel) < entity.maxX() && maxX() > entity.maxX() - (entity.getWidth() * 0.8)) {
        // setXPos(getXPos() + 1);
        xvel = 0;
      }

      if ((maxX() + xvel) > entity.minX() && minX() < entity.minX() + (entity.getWidth() * 0.2)) {
        // setXPos(getXPos() - 1);
        xvel = 0;
      }
    }

    if (isStuck(entity)) {
      int xoffset = 0;
      int yoffset = 0;
      if (this.getXpos() != entity.getXpos()) {
        xoffset = (this.getXpos() - entity.getXpos())
            / Math.abs((this.getXpos() - entity.getXpos()));
      }
      if (this.getYpos() != entity.getYpos()) {
        yoffset = (this.getYpos() - entity.getYpos())
            / Math.abs((this.getYpos() - entity.getYpos()));
      }
      if (this.getXpos() == entity.getXpos() && this.getYpos() == entity.getYpos()) {
        int rand = (int) Math.floor(((Math.random() * Math.floor(2)) * 2) - 1);
        xoffset = rand;
        yoffset = rand;
      }

      int newxpos = getXpos() + xoffset;
      int newypos = getYpos() + yoffset;

      setXPos(newxpos);
      setYPos(newypos);
    }
  }

  /**
   * A method to check if an entity is stuck inside another.
   * 
   * @param entity The entity to compare to.
   * 
   * @return A boolean value for whether or not an object is inside it's
   *         boundaries.
   */

  public boolean isStuck(Entity entity) {
    if (entity.minX() < getXpos() + getWidth() / 2 && getXpos() - getWidth() / 2 < entity.maxX()) {
      if (entity.minY() < getYpos() + getHeight() / 2
          && getYpos() - getHeight() / 2 < entity.maxY()) {
        return true;
      }
    }
    return false;
  }

  /**
   * A method to adjust the total speed of a character moving diagonally.
   * 
   * <p>Both velocities are changed to 0.8 * maxspeed and not exatcly
   * sqrt((maxspeed^2)/2) for the simple reason of it feeling more responsive.
   */

  public void diagonalMax() {
    float speed = (float) Math.sqrt((xvel * xvel) + (yvel * yvel));

    if (xdir != 0 && ydir != 0) {
      if (speed > this.getMaxSpeed()) {
        xvel = (float) (this.getMaxSpeed() * 0.8 * xdir);
        // Times 0.8 and not exactly sqrt(this.getMaxSpeed()/2)
        // otherwise diagonal movement is too slow.
        yvel = (float) (this.getMaxSpeed() * 0.8 * ydir);
      }
    }
  }

  /**
   * A method that prevents the entity from moving out of bounds by making the x
   * or y velocities 0.
   * 
   * @param game The game that the entity should be kept in the bounds of.
   */

  public void keepInBound(ShooterGame game) {
    int[] bounds = game.getLevel().getBounds();
    if (minX() + xvel < bounds[0]) {
      xvel = 0;
      if (minX() < bounds[0]) {
        setXPos(bounds[0] + getWidth() / 2);
      }
    }

    if (maxX() + xvel > bounds[1]) {
      xvel = 0;
      if (maxX() > bounds[1]) {
        setXPos(bounds[1] - getWidth() / 2);
      }
    }

    if (minY() + yvel < bounds[2]) {
      yvel = 0;
      if (minY() < bounds[2]) {
        setYPos(bounds[2] + getHeight() / 2);
      }
    }

    if (maxY() + yvel > bounds[3]) {
      yvel = 0;
      if (maxY() > bounds[3]) {
        setYPos(bounds[3] - getHeight() / 2);
      }
    }
  }

  public float getXDir() {
    return xdir;
  }

  public float getYDir() {
    return ydir;
  }

  private int centerx = 0;
  private int centery = 0;

  @Override
  public int compareTo(Entity entity) {
    if (this.distToCenter() == entity.distToCenter()) {
      return 0;
    }
    return this.distToCenter() < entity.distToCenter() ? 1 : -1;
  }

  /**
   * A method that calculates the distance to a 'center' point.
   * 
   * @return The distance from a center point.
   */

  public double distToCenter() {
    double distx = (xpos - centerx) * (xpos - centerx);
    double disty = (ypos - centery) * (ypos - centery);
    return Math.sqrt(distx + disty);
  }

  public void setCenter(int x, int y) {
    centerx = x;
    centery = y;
  }

  /**
   * A method to stop the entity by setting the xvel and yvel to 0, as well as the
   * moving boolean.
   */

  public void stop() {
    xvel = 0;
    yvel = 0;
    moving = false;
  }

}
