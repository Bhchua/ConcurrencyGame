package game;

/**
 * A class that holds some simple variables that determines how a view of the
 * game will be warped in order to display correctly.
 * 
 * <p>
 * The camera variables are different to what is held in a GameCamera as these
 * are used globally across every view in a split screen environment but a
 * GameCamera holds data specific to that instance of a game.
 * 
 * @author Brandon Hua
 *
 */

public class CameraVariables {

  public static double xratio = 1;
  public static double yratio = 1;
  public static int zoom = 1;
  public static int sres = 1;

  public static int[] zoomConfig = new int[] { 2, 1, 1, 1 };

}
