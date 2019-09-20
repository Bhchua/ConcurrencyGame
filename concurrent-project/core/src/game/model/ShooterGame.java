package game.model;

import game.Core;
import game.entities.Bullet;
import game.entities.Enemy;
import game.entities.PlayerCharacter;
import game.model.levels.Level;
import game.renderer.Renderer;
import game.screens.shooter.GameCamera;

import java.util.ArrayList;

/**
 * The shooter game class houses the model for a shooter game, including all the
 * entities and level data.
 * 
 * @author Brandon Hua
 *
 */

public class ShooterGame {

  private final int GAMENUM;

  private PlayerCharacter player;
  private PlayerCharacter netPlayer;

  private GameCamera cam;

  private Level level;

  private ArrayList<Enemy> enemies = new ArrayList<Enemy>();

  private ArrayList<Renderer> renderArray = new ArrayList<Renderer>();

  private ArrayList<Bullet> bullets = new ArrayList<Bullet>();

  private GameVariables vars;

  private boolean finished = false;

  private boolean networked = false;

  private int pauseInterval = 250;
  private long pausedTime = 0;
  private boolean paused = false;

  /**
   * A constructor for the ShooterGame class made for a single screen.
   * 
   * @param level     The level that will be contained in this instance of the
   *                  game.
   * @param playerNum The player number of the game.
   * @param netGame   A boolean for whether or not it is a networked multiplayer
   *                  game.
   */

  public ShooterGame(Level level, int playerNum, boolean netGame) {
    this.level = level;
    vars = new GameVariables();
    cam = new GameCamera(0, 0, this);

    GAMENUM = playerNum;
    player = new PlayerCharacter(0, 0, 32, 64, GAMENUM);

    if (netGame) {
      networked = true;
      netPlayer = new PlayerCharacter(0, 0, 32, 64, 2);
    }
  }

  /**
   * A method to get the array holding the renderers for every entity of the game.
   * 
   * @return The array of renderers
   */

  public ArrayList<Renderer> getRenderArray() {
    return renderArray;
  }

  public ArrayList<Enemy> getEnemies() {
    return enemies;
  }

  public int getGameNum() {
    return GAMENUM;
  }

  /**
   * A method to place an enemy in a specified location and a set size.
   * 
   * @param xpos   The x position the enemy starts at.
   * @param ypos   The y position the enemy starts at.
   * @param width  The width of the enemy.
   * @param height The height of the enemy.
   * @param type   The name of the type of enemy.
   */

  public void addEnemy(int xpos, int ypos, int width, int height, String type) {
    if (getVars().getCurrentEnemies() < getVars().getMaxEnemies()) {
      enemies.add(new Enemy(xpos, ypos, width, height, type));
    }
  }

  public Level getLevel() {
    return level;
  }

  public PlayerCharacter getPlayer() {
    return player;
  }

  public PlayerCharacter getNetPlayer() {
    return netPlayer;
  }

  /**
   * A method that adds a bullet that shoots from a specified location.
   * 
   * @param xpos        The x position the bullet starts at.
   * @param ypos        The y position the bullet starts at.
   * @param width       The width of the bullet.
   * @param height      The height of the bullet.
   * @param xdir        The direction in the x axis the bullet will be moving in.
   * @param ydir        The direction in the y axis the bullet will be moving in.
   * @param friendly    A boolean for whether or not a bullet is friendly.
   * @param damage      The damage value of the bullet.
   * @param bulletSpeed The maximum speed of the bullet.
   */

  public void addBullet(int xpos, int ypos, int width, int height, float xdir, float ydir,
      boolean friendly, int damage, float bulletSpeed) {
    bullets.add(new Bullet(xpos, ypos, width, height, xdir, ydir, friendly, damage, bulletSpeed));
  }

  /**
   * A method to apply the changes (calculated by delta time) to all the entities
   * in a game.
   */

  public void update() {
    float gameSpeed = vars.getGameSpeed();
    player.update(gameSpeed);
    for (int i = 0; i < enemies.size(); i++) {
      enemies.get(i).update(gameSpeed);
    }
    for (int i = 0; i < bullets.size(); i++) {
      bullets.get(i).update(gameSpeed);
    }
    level.spawnerUpdate(gameSpeed, this);

    int deltaTime = (int) (Core.deltaTime * gameSpeed);
    updateVariables(deltaTime);
  }

  /**
   * The updateVariables method changes the variables held by the containing game
   * model.
   * 
   * @param deltaTime The amount of time between the last frame.
   */

  public void updateVariables(int deltaTime) {
    vars.setCurEnemies(enemies.size());
    vars.addToTime(deltaTime);
  }

  public ArrayList<Bullet> getBullets() {
    return bullets;
  }

  public GameVariables getVars() {
    return vars;
  }

  /**
   * A method to spawn a friendly projectiles at the player's location and
   * depending on where they are looking.
   */

  public void spawnPlayerProjectile() {
    if (getPlayer().canFire()) {

      float bulletXDir = 0;
      float bulletYDir = 0;

      if (getPlayer().getFacing().equals("up") || getPlayer().getFacing().equals("up_right")
          || getPlayer().getFacing().equals("up_left")) {
        bulletYDir = 1;
      }
      if (getPlayer().getFacing().equals("down")) {
        bulletYDir = -1;
        ;
      }
      if (getPlayer().getFacing().equals("left") || getPlayer().getFacing().equals("up_left")) {
        bulletXDir = -1;
        ;
      }
      if (getPlayer().getFacing().equals("right") || getPlayer().getFacing().equals("up_right")) {
        bulletXDir = 1;
        ;
      }

      int xpos = (int) (getPlayer().getXpos() + bulletXDir * (getPlayer().getWidth() / 2));
      int ypos = (int) (getPlayer().getYpos() + bulletYDir * (getPlayer().getHeight() / 2));

      bulletXDir = bulletXDir + getPlayer().getXVel();
      bulletYDir = bulletYDir + getPlayer().getYVel();

      addBullet(xpos, ypos, 16, 16, bulletXDir, bulletYDir, true, getPlayer().getDamage(),
          getPlayer().getBulletSpeed());
      getPlayer().setLastFired(0);
    }
  }

  /**
   * A method that places an "unfriendly" bullet at the position of a given enemy.
   * 
   * @param enemy The enemy that the bullet is to be placed at.
   */

  public void spawnEnemyProjectile(Enemy enemy) {
    if (enemy.canFire()) {
      float bulletXDir = 0;
      float bulletYDir = 0;

      int xdiff = getPlayer().getXpos() - enemy.getXpos();
      int ydiff = getPlayer().getYpos() - enemy.getYpos();

      if ((Math.abs(xdiff) + Math.abs(ydiff)) > 0) {
        float ratio = (float) 1 / (float) (Math.abs(xdiff) + Math.abs(ydiff));
        bulletXDir = ratio * xdiff;
        bulletYDir = ratio * ydiff;
      }

      int xpos = (int) (enemy.getXpos() + bulletXDir * (enemy.getWidth() / 2));
      int ypos = (int) (enemy.getYpos() + bulletYDir * (enemy.getHeight() / 2));

      addBullet(xpos, ypos, 16, 16, bulletXDir, bulletYDir, false, enemy.getDamage(),
          enemy.getBulletSpeed());
      enemy.setLastFired(0);
    }
  }

  /**
   * A method that sets the boolean value paused to the opposite of it's current
   * value.
   */

  public void pause() {
    if (Core.time - pausedTime >= pauseInterval) {
      if (!paused) {
        setPause(true);
      } else {
        setPause(false);
      }
    }
  }

  /**
   * A method that sets the boolean pause variable to true or false, in addition
   * to updating the game speed to be 0 or 1.
   * 
   * @param paused The boolean value for pausing.
   */

  public void setPause(boolean paused) {
    if (paused) {
      vars.setGameSpeed(0);
      pausedTime = Core.time;
      this.paused = true;
    } else {
      vars.setGameSpeed(1);
      pausedTime = Core.time;
      this.paused = false;
    }
  }

  public boolean isPaused() {
    return paused;
  }

  public GameCamera getCam() {
    return cam;
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }

  public boolean isFinished() {
    return finished;
  }

  public boolean isNetworked() {
    return networked;
  }

  public void setPauseInterval(int interval) {
    pauseInterval = interval;
  }

}
