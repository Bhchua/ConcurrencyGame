package game.screens.shooter;

import game.CameraVariables;
import game.Core;
import game.entities.Entity;
import game.model.ShooterGame;
import game.model.levels.Level;

/**
 * The GameCamera is an Entity which is intended to be the point of focus for a
 * game, it is where the rendering position of every other object is calculated.
 * 
 * @author Brandon Hua
 */

public class GameCamera extends Entity {

  private int accel = 200;
  private int decel = 40;
  private float maxSpeed = 5f;
  private ShooterGame game;

  /**
   * The GameCamera initialises the travel speed of the Camera in the Entity super
   * class.
   * 
   * @param xpos The initial x position of the camera.
   * @param ypos The initial y position of the camera.
   * @param game The game that the camera is bound to.
   */
  
  public GameCamera(int xpos, int ypos, ShooterGame game) {
    super(xpos, ypos, 0, 0);
    setAccel(accel);
    setDecel(decel);
    setMaxSpeed(maxSpeed);
    this.game = game;
  }


  /**
   * A simple AI method that moves towards a target and decelerates if it is in
   * range of that target.
   * 
   * @param target    The target to move towards.
   * @param deltaTime The difference in time from the last rendered frame.
   */

  public void basicFollow(Entity target, long deltaTime) {
    int xpos = target.getXpos();
    int ypos = target.getYpos();
    xpos = stayInBoarderx(xpos);
    ypos = stayInBoardery(ypos);
    setXPos(xpos);
    setYPos(ypos);
  }

  /**
   * A method to keep the camera within the bounds of the stage on the x-axis.
   * 
   * @param xpos The initial x position of the camera.
   * @return An integer for the corrected x-position.
   */

  public int stayInBoarderx(int xpos) {
    int[] bounds = game.getLevel().getBounds();
    int camWidth = (int) ((Core.width / CameraVariables.zoom) * CameraVariables.xratio);
    int adjustedx = xpos;
    if (xpos - (camWidth / 2) < bounds[0]) {
      adjustedx = bounds[0] + camWidth / 2;
    }
    if (xpos + (camWidth / 2) > bounds[1]) {
      adjustedx = bounds[1] - camWidth / 2;
    }
    return adjustedx;
  }

  /**
   * A method to keep the camera within the bounds of the stage on the y-axis.
   * 
   * @param ypos The initial y position of the camera.
   * @return An integer for the corrected y-position.
   */

  public int stayInBoardery(int ypos) {
    int[] bounds = game.getLevel().getBounds();
    int camHeight = (int) ((Core.height / CameraVariables.zoom) * CameraVariables.yratio);
    int adjustedy = ypos;
    if (ypos - (camHeight / 2) < bounds[2]) {
      adjustedy = bounds[2] + camHeight / 2;
    }
    if (ypos + (camHeight / 2) > bounds[3]) {
      adjustedy = bounds[3] - camHeight / 2;
    }
    return adjustedy;
  }

}
