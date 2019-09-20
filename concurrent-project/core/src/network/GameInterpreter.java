package network;

import error.GlobalErrors;
import game.model.ShooterGame;

/**
 * The GameInterpreters class contains methods that read incoming strings from a
 * networked connection and performs actions based on them.
 * 
 * @author Brandon Hua
 */

public class GameInterpreter {

  private boolean gameStarted = false;

  public boolean hasGameStarted() {
    return gameStarted;
  }

  /**
   * The interpreter interface provides methods to check if the incoming string
   * has the same header as the rule name and then perform an action accordingly.
   * 
   * @author Brandon Hua
   *
   */

  public interface Interpreter {
    /**
     * This method checks to see if the first value of a string[] is the rule name
     * and then performs an action.
     * 
     * @param data The parameters of the action.
     * @param game The game to be modified.
     */
    public void checkStringAction(String[] data, ShooterGame game);
  }

  /**
   * An interpreter for changing the position of the other players' character and
   * direction.
   * 
   * @author Brandon Hua
   */

  public class InterpretChangePos implements Interpreter {
    @Override
    public void checkStringAction(String[] data, ShooterGame game) {
      if (data[0].equals("pos")) {
        try {
          String facing = data[1];
          int xpos = Integer.valueOf(data[2]);
          int ypos = Integer.valueOf(data[3]);
          game.getNetPlayer().setFacing(facing);
          game.getNetPlayer().setPos(xpos, ypos);
        } catch (Exception e) {
          System.out.println(data[1] + " " + data[2] + " " + data[3]);
          System.out.println("The positional data cannot be interpreted.");
          GlobalErrors.setError("The positional data cannot be interpreted.");
        }
      }
    }
  }

  /**
   * An interpreter for changing the position of the other players' character and
   * direction.
   * 
   * @author Brandon Hua
   */

  public class InterpretFinish implements Interpreter {
    @Override
    public void checkStringAction(String[] data, ShooterGame game) {
      if (data[0].equals("finished")) {
        int score = Integer.valueOf(data[1]);
        game.getVars().setNetScore(score);
      }
    }
  }

  /**
   * A interpreter that sets the static boolean value gameStarted to true.
   * 
   * @author Brandon Hua
   *
   */

  public class InterpretStart implements Interpreter {
    @Override
    public void checkStringAction(String[] data, ShooterGame game) {
      if (data[0].equals("start")) {
        gameStarted = true;
      }
    }
  }

  /**
   * A method that initialises and returns an array of interpreters.
   * 
   * @return The different Interpreters to read incoming strings.
   */

  public Interpreter[] getInterpreters() {
    Interpreter[] inter = new Interpreter[] { new InterpretChangePos(), new InterpretFinish(),
        new InterpretStart() };
    return inter;
  }

}
