package game.input;

import java.util.ArrayList;

/**
 * A simple class that holds variables for general inputs that arent bound to
 * specific keys.
 * 
 * @author Brandon Hua
 */

public class GlobalInput {

  public static final int PLAYERS = 4; // This specifies required size of the arrays.

  // Movement made into floats for potential analogue movement
  public static float[] playerUp = new float[PLAYERS];
  public static float[] playerDown = new float[PLAYERS];
  public static float[] playerLeft = new float[PLAYERS];
  public static float[] playerRight = new float[PLAYERS];

  public static boolean[] playerShoot = new boolean[PLAYERS];

  public static boolean paused;

  public static int mouseX = 0;
  public static int mouseY = 0;
  public static boolean confirm = false;

  public static boolean[] numkeys = new boolean[10];
  public static boolean enter = false;
  public static boolean backspace = false;
  public static boolean period = false;
  public static boolean[] letters = new boolean[26];

  /**
   * A method that checks each input and returns true if any of them are pressed.
   * 
   * @return Whether or not the inputs are pressed.
   */
  public static boolean allKeysUp() {
    ArrayList<Boolean> everyInput = new ArrayList<Boolean>();

    for (boolean b : numkeys) {
      everyInput.add(b);
    }
    for (boolean b : letters) {
      everyInput.add(b);
    }

    everyInput.add(enter);
    everyInput.add(backspace);
    everyInput.add(period);

    for (boolean b : everyInput) {
      if (b) {
        return true;
      }
    }
    return false;
  }

}
