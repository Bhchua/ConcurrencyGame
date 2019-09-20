package game.entities;

/**
 * The enemy type action class holds the behaviours for enemies as well as
 * providing the interfaces required to make them.
 * 
 * @author Brandon Hua
 *
 */

public class EnemyTypeAction {

  /**
   * The EnemyAction interface is the action an enemy takes when running.
   * 
   * @author Brandon Hua
   */

  public interface EnemyAction {
    /**
     * The action method performs the action of the enemy.
     * 
     * @param enemy     The enemy to apply the action on to.
     * @param target    The target to move towards.
     * @param deltaTime The difference in time from the last rendered frame.
     */
    public void action(Enemy enemy, Entity target, long deltaTime);
  }

  /**
   * The EnemyType interface holds different getters for enemy variables such as
   * the type name, the starting health, the damage and the movement speeds
   * amongst others.
   * 
   * @author Brandon Hua
   */

  public interface EnemyType {
    public String getType();

    public int getHealth();

    public int getDamage();

    public int getPoints();

    public float getSpeed();

    public boolean canShoot();

    public EnemyAction typeAction();
  }

  /**
   * The BasicEnemy type simply follows the player around until it gets within a
   * certain range and shoots at the player.
   * 
   * @author Brandon Hua
   *
   */

  public static class ShooterEnemy implements EnemyType {

    @Override
    public String getType() {
      return "shoot";
    }

    @Override
    public int getHealth() {
      return 3;
    }

    @Override
    public int getDamage() {
      return 1;
    }

    @Override
    public float getSpeed() {
      return 1.2f;
    }

    @Override
    public boolean canShoot() {
      return true;
    }

    @Override
    public EnemyAction typeAction() {
      return new ShootBehaviour();
    }

    @Override
    public int getPoints() {
      return 100;
    }

  }

  /**
   * The ChaserEnemy type follows the player but does not stop regardless of how
   * close they are to them.
   * 
   * @author Brandon Hua
   */

  public static class ChaserEnemy implements EnemyType {

    @Override
    public String getType() {
      return "chase";
    }

    @Override
    public int getHealth() {
      return 2;
    }

    @Override
    public int getDamage() {
      return 1;
    }

    @Override
    public float getSpeed() {
      return 2.0f;
    }

    @Override
    public boolean canShoot() {
      return false;
    }

    @Override
    public EnemyAction typeAction() {
      return new ChaseBehaviour();
    }

    @Override
    public int getPoints() {
      return 75;
    }

  }

  /**
   * A simple AI method that moves towards a target and decelerates if it is in
   * range of that target.
   * 
   * @author Brandon Hua
   */

  public static class ShootBehaviour implements EnemyAction {
    @Override
    public void action(Enemy enemy, Entity target, long deltaTime) {
      enemy.faceTowards(target);
      if (enemy.inRange(target)) {
        enemy.setXDir(0);
        enemy.setYDir(0);
      }
      enemy.accelDecel(deltaTime);
    }
  }

  /**
   * The chaser behaviour follows the target.
   * 
   * @author Brandon Hua
   */

  public static class ChaseBehaviour implements EnemyAction {
    @Override
    public void action(Enemy enemy, Entity target, long deltaTime) {
      enemy.faceTowards(target);
      enemy.accelDecel(deltaTime);
    }
  }

  private static EnemyType[] typeList = new EnemyType[] { new ShooterEnemy(), new ChaserEnemy() };

  public static EnemyType[] getTypeList() {
    return typeList;
  }

}
