package network;

import game.model.ShooterGame;

/**
 * The GameNetworkData class interprets the data of the game being played
 * locally and turns that information into strings which is then sent to the
 * other client.
 * 
 * @author Brandon Hua
 *
 */

public class GameNetworkData {
  
  /**
   * The position of the local character and the direction they're facing.
   * 
   * @param game The game model to pull the data from.
   * @return A string in the format "pos dir x y".
   */

  public static String getClientPosition(ShooterGame game) {
    String facing = game.getPlayer().getFacing();
    int[] gamePos = new int[] { game.getPlayer().getXpos(), game.getPlayer().getYpos() };
    String xy = String.valueOf(gamePos[0]) + " " + String.valueOf(gamePos[1]);
    String movement = "pos " + facing + " " + xy;
    return movement;
  }
  
  /**
   * A method to return the final score of the local game.
   * 
   * @param game The game model to pull the data from.
   * @return A string in the format "finished score"
   */

  public static String getFinalScore(ShooterGame game) {
    String score = String.valueOf("finished " + game.getVars().getScore());
    return score;
  }
  
  



}
