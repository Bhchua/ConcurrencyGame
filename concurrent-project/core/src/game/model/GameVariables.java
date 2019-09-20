package game.model;

/**
 * The game variables class holds variables for a single game rule, separately
 * to the GameRulesVariables that stores the variables for the entire session.
 * 
 * @author Brandon Hua
 */

public class GameVariables {
  
  private int score = 0;
  private float gameSpeed = 1f;
  private int defeated = 0;
  
  private long curTime = 0;
  private long timeLimit = 0;
  
  private int curEnemies = 0;
  private int maxEnemies = 8;

  private int lives = 1;
  private int netScore = 0;
  
  public int getCurrentEnemies() {
    return curEnemies;
  }
  
  public void setCurEnemies(int enemies) {
    this.curEnemies = enemies;
  }
  
  public int getMaxEnemies() {
    return maxEnemies;
  }
  
  public void setMaxEnemies(int maxEnemies) {
    this.maxEnemies = maxEnemies;
  }
  
  public void setTimeLimit(Long time) {
    timeLimit = time;
  }
  
  public long getTimeLimit() {
    return timeLimit;
  }
  
  public boolean timeUp() {
    return (curTime >= timeLimit);
  }
  
  public void setLives(int lives) {
    this.lives = lives;
  }
  
  public void decrementLives() {
    lives = lives - 1;
  }
  
  public int getLives() {
    return lives;
  }
  
  public void addScore(int scoreAdd) {
    this.score = this.score + scoreAdd;
  }
  
  public int getScore() {
    return this.score;
  }
  
  public float getGameSpeed() {
    return gameSpeed;
  }
  
  public void setGameSpeed(float speed) {
    this.gameSpeed = speed;
  }
  
  public void incrDefeatCount() {
    defeated++;
  }
  
  public int getDefeated() {
    return defeated;
  }
  
  public void addToTime(long deltaTime) {
    curTime += deltaTime;
  }
  
  public long getCurTime() {
    return curTime;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public void setNetScore(int score) {
    this.netScore = score;
  }
  
  public int getNetScore() {
    return netScore;
  }
  
}
