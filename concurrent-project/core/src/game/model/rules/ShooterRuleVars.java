package game.model.rules;

/**
 * The ShooterRulesVariables class holds all the variables required for the game
 * rule set.
 * 
 * @author Brandon Hua
 */

public class ShooterRuleVars {
  
  private long timeLimit;
  private long timePassed = 0;
  private String mode;
  private int globalLives = 10;
  private boolean gameOver = false;
  private long totalScore;
  private int defeated;
  private int defeatQuota;
  
  /**
   * The ShooterRulesVariables initialises the game mode.
   * 
   * @param mode The game mode being played.
   */
  public ShooterRuleVars(String mode) {
    this.mode = mode;
  }
  
  public String getMode() {
    return mode;
  }
  
  public void setGameOver() {
    gameOver = true;
  }
  
  public boolean isGameOver() {
    return gameOver;
  }
  
  public void addTime(long deltaTime) {
    timePassed = timePassed + deltaTime;
  }
  
  public boolean timeUp() {
    return (timePassed > timeLimit);
  }
  
  public void setLives(int lives) {
    globalLives = lives;
  }
  
  public void subtractLives() {
    globalLives = globalLives - 1;
  }

  public boolean hasLives() {
    return (globalLives <= 0);
  }

  public void setTimeLimit(Long limit) {
    timeLimit = limit;
  }

  public long getTimePassed() {
    return timePassed;
  }
  
  public void addToTotal(int addedScore) {
    totalScore = totalScore + addedScore;
  }

  public int getLives() {
    return globalLives;
  }
  
  public void incrDefeated() {
    defeated++;
  }
  
  public void setDefeatQuota(int quota) {
    defeatQuota = quota;
  }

  public int getDefeated() {
    return defeated;
  }
  
  public int getDefeatQuota() {
    return defeatQuota;
  }

  public long getTimeLimit() {
    return timeLimit;
  }
  
}
