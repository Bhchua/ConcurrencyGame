package game.model.rules;

import game.Core;
import game.entities.Bullet;
import game.entities.Enemy;
import game.entities.PlayerCharacter;
import game.entities.structures.Structure;
import game.model.ShooterGame;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class that contains the entire rule set for the shooter game.
 * 
 * @author Brandon Hua
 *
 */

public class ShooterRules {

  private ShooterGame game;
  private ShooterRuleVars vars;
  private GameMode mode;
  private boolean gameWon;
  private boolean gameOver;
  private int[] defeatCounter;

  /**
   * A constructor that sets the initial game held in the rules object.
   * 
   * @param initGame The first game to be checked against.
   * @param modeName The name of the game mode.
   * @param data     The data to be used as the mode parameters
   */

  public ShooterRules(ShooterGame initGame, String modeName, Object data) {
    setGame(initGame);
    for (GameMode m : modeList) {
      if (m.modeName().equals(modeName)) {
        mode = m;
        m.initMode(m.convertData(data));
      }
    }
    defeatCounter = new int[] { 0, 0, 0, 0 };
  }

  public ShooterRules() {
    // A constructor just for testing
  }

  /**
   * A method that can be called by threads to set the current game being checked
   * and then update it.
   * 
   * @param game The game the ruleset is intended to be applied on.
   */

  public void setAndUpdate(ShooterGame game) {
    setGame(game);
    updateGame();
  }

  public void setGame(ShooterGame game) {
    this.game = game;
  }

  /**
   * A method that simply calls all the methods to be applied to the game model.
   */

  public void updateGame() {
    long curTime = (long) (Core.deltaTime * game.getVars().getGameSpeed());
    updateAi(curTime);
    updateProjectiles(curTime);
    enforceCollisions();
    checkDeath();
    defeatCounter[game.getGameNum() - 1] = game.getVars().getDefeated();
    mode.updateMode(defeatCounter);
    gameWon = mode.winCondition();
    gameOver = mode.loseCondition();
  }

  public ShooterRuleVars getVars() {
    return vars;
  }

  /**
   * A method that checks every specified entity within a game, ensuring they
   * respond appropriately to colliding with different objects.
   */

  private void enforceCollisions() {
    ArrayList<Structure> strcts = game.getLevel().getStructures();
    PlayerCharacter player = game.getPlayer();
    ArrayList<Enemy> enemies = game.getEnemies();
    ArrayList<Bullet> bullets = game.getBullets();
    enemyCollisions(player, enemies);
    bulletCollisions(bullets, enemies, player);
    wallCollisions(strcts, player, enemies, bullets);
  }

  /**
   * A method that checks the wall collisions between every solid object.
   * 
   * @param walls   The walls to be checked with.
   * @param player  The player controlled character.
   * @param enemies The enemies of a level.
   */

  private void wallCollisions(ArrayList<Structure> walls, PlayerCharacter player, 
      ArrayList<Enemy> enemies, ArrayList<Bullet> bullets) {
    for (int i = 0; i < walls.size(); i++) {
      player.solidCollision(walls.get(i));

      if (walls.get(i).getEnemyCol()) {
        for (int j = 0; j < enemies.size(); j++) {
          enemies.get(j).solidCollision(walls.get(i));
        }
      }

      for (int k = 0; k < bullets.size(); k++) {
        if (bullets.get(k).collidedWith(walls.get(i))) {
          bullets.get(k).die();
        }
      }
    }

    for (Enemy e : enemies) {
      for (Enemy e2 : enemies) {
        if (e.compareTo(e2) != 0) {
          e.solidCollision(e2);
          e.keepInBound(game);
        }
      }
    }

    player.keepInBound(game);
  }

  /**
   * A method that checks collisions between entities and bullets.
   * 
   * @param bullets The list of bullets to be checked.
   * @param enemies The enemies of a level.
   */

  private void bulletCollisions(ArrayList<Bullet> bullets, ArrayList<Enemy> enemies, 
      PlayerCharacter player) {
    for (int i = 0; i < bullets.size(); i++) {
      for (int j = 0; j < enemies.size(); j++) {
        if (bullets.get(i).isFriendly()) {
          if (enemies.get(j).damage(bullets.get(i))) {
            bullets.get(i).die();
          }
        }
      }
      if (!bullets.get(i).isFriendly()) {
        if (player.damage(bullets.get(i))) {
          bullets.get(i).die();
        }
      }
    }
  }

  private void enemyCollisions(PlayerCharacter player, ArrayList<Enemy> enemies) {
    for (int i = 0; i < enemies.size(); i++) {
      player.damage(enemies.get(i));
    }
  }

  /**
   * updateAI() increments through a list of enemies found in the game and applies
   * a basic AI method.
   * 
   * @param time The time difference between the last updated frame
   */

  private void updateAi(long time) {
    ArrayList<Enemy> enemies = game.getEnemies();
    for (int i = 0; i < enemies.size(); i++) {
      enemies.get(i).performAction(game.getPlayer(), time);
      game.spawnEnemyProjectile(enemies.get(i));
    }
  }

  /**
   * A method that checks a list of projectiles and observes whether or not they
   * have persisted beyond their maximum time limit and removing them if they have
   * been flagged as 'dead'.
   */

  private void checkDeath() {
    ArrayList<Bullet> bullets = game.getBullets();
    ArrayList<Enemy> enemies = game.getEnemies();

    int enemyCounter = 0;
    while (enemyCounter < enemies.size()) {
      if (!enemies.get(enemyCounter).checkLiving()) {
        game.getVars().addScore(enemies.get(enemyCounter).getPoints());
        game.getVars().incrDefeatCount();
        game.getEnemies().remove(enemyCounter);
        vars.incrDefeated();
        enemyCounter = enemyCounter - 1;
      }
      enemyCounter++;
    }

    int bulletCounter = 0;
    while (bulletCounter < bullets.size()) {
      if (!bullets.get(bulletCounter).checkLiving()) {
        game.getBullets().remove(bulletCounter);
        bulletCounter = bulletCounter - 1;
      }
      bulletCounter++;
    }

    if (game.getPlayer().deathTimeUp()) {
      game.getVars().decrementLives();
      int score = (game.getVars().getScore() / 2);
      game.getVars().setScore(score);
      vars.subtractLives();
      game.getPlayer().respawn();
      game.getEnemies().clear();
    }

  }

  /**
   * A method to update the position of projectiles over time.
   * 
   * @param time The time difference between the last updated frame
   */

  private void updateProjectiles(long time) {
    ArrayList<Bullet> bullets = game.getBullets();
    for (int i = 0; i < bullets.size(); i++) {
      try {
        bullets.get(i).moveBullet(time);
      } catch (IndexOutOfBoundsException e) {
        bullets = game.getBullets();
      }
    }
  }

  public boolean isGameWon() {
    return gameWon;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public String getMode() {
    return mode.modeName();
  }

  // =======================================================================================
  // Classes below this point are GameMode objects.
  // =======================================================================================

  private ArrayList<GameMode> modeList = new ArrayList<GameMode>(
      Arrays.asList(new TimedMode(), new StockMode(), new NetworkMode()));

  public interface GameMode {

    /**
     * A method that returns the name of the game mode.
     * 
     * @return The game mode name.
     */
    public String modeName();

    /**
     * A method to initialise the game mode.
     * 
     * @param data The parameter used for the victory condition.
     */
    public void initMode(Object data);

    /**
     * A method to convert the given data into the correct type to be used in the
     * victory condition.
     * 
     * @param data The data for the victory condition.
     * 
     * @return The data converted to the correct type.
     */
    public Object convertData(Object data);

    /**
     * A method that updates the associated game mode variables.
     * 
     * @param data Potential data to be used as part of the update process.
     */

    public void updateMode(Object data);

    /**
     * A method that checks to see if the victory condition has been reached in a
     * game model.
     * 
     * @return A boolean for whether or not the game has been won.
     */
    public boolean winCondition();

    /**
     * A method that checks to see if a lose condition has been met.
     * 
     * @return A boolean for whether or not the game has been lost.
     */
    public boolean loseCondition();
  }

  /**
   * A game mode that has its conditions completed if the time of survival is
   * reached.
   * 
   * @author Brandon Hua
   */

  public class TimedMode implements GameMode {

    private long timeLastUpdated = Core.time;

    @Override
    public String modeName() {
      return "timed";
    }

    @Override
    public void initMode(Object data) {
      vars = new ShooterRuleVars(this.modeName());
      if (data instanceof Long) {
        vars.setTimeLimit((long) data);
      }
    }

    @Override
    public void updateMode(Object data) {
      long dtime = Core.time - timeLastUpdated;
      vars.addTime((long) (dtime * game.getVars().getGameSpeed()));
      timeLastUpdated = Core.time;
    }

    @Override
    public boolean winCondition() {
      game.setFinished(vars.timeUp());
      return game.isFinished();
    }

    @Override
    public Object convertData(Object data) {
      if (data instanceof Long) {
        return data;
      }
      if (data instanceof Integer) {
        int tmp = (int) data;
        return (long) tmp;
      }
      return null;
    }

    @Override
    public boolean loseCondition() {
      // TODO Auto-generated method stub
      return false;
    }

  }

  /**
   * A the Networked game mode, the victory and loss conditions are the same as timed.
   * 
   * @author Brandon Hua
   */

  public class NetworkMode implements GameMode {

    private long timeLastUpdated = Core.time;

    @Override
    public String modeName() {
      return "network";
    }

    @Override
    public void initMode(Object data) {
      vars = new ShooterRuleVars(this.modeName());
      if (data instanceof Long) {
        vars.setTimeLimit((long) data);
      }
    }

    @Override
    public void updateMode(Object data) {
      long dtime = Core.time - timeLastUpdated;
      vars.addTime((long) (dtime * game.getVars().getGameSpeed()));
      timeLastUpdated = Core.time;
    }

    @Override
    public boolean winCondition() {
      game.setFinished(vars.timeUp());
      return game.isFinished();
    }

    @Override
    public Object convertData(Object data) {
      if (data instanceof Long) {
        return data;
      }
      if (data instanceof Integer) {
        int tmp = (int) data;
        return (long) tmp;
      }
      return null;
    }

    @Override
    public boolean loseCondition() {
      // TODO Auto-generated method stub
      return false;
    }

  }

  /**
   * A cooperative game mode where each player shares from the same pool of lives,
   * the win condition is met when a certain number of enemies have been defeated.
   * 
   * @author Brandon Hua
   */

  public class StockMode implements GameMode {

    private long timeLastUpdated = Core.time;

    @Override
    public String modeName() {
      return "stock";
    }

    @Override
    public void initMode(Object data) {
      vars = new ShooterRuleVars(this.modeName());
      if (data instanceof int[]) {
        int[] varData = (int[]) data;
        vars.setLives(varData[0]);
        vars.setDefeatQuota(varData[1]);
      }
    }

    @Override
    public Object convertData(Object data) {
      if (data instanceof int[]) {
        return (int[]) data;
      }
      return null;
    }

    @Override
    public void updateMode(Object data) {
      long dtime = Core.time - timeLastUpdated;
      vars.addTime(dtime);
      timeLastUpdated = Core.time;
    }

    @Override
    public boolean winCondition() {
      game.setFinished(vars.getDefeated() >= vars.getDefeatQuota());
      return game.isFinished();
    }

    @Override
    public boolean loseCondition() {
      return vars.hasLives();
    }

  }

}
