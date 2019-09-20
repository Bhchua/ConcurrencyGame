package game.input;

import game.Core;
import game.model.GameVariables;
import game.model.ShooterGame;

/**
 * The ShooterGameInputHandler contains code to change aspects of the game as a
 * reaction to player input.
 * 
 * @author Brandon Hua
 *
 */

public class ShooterGameInputHandler {

  private ShooterGame game;

  /**
   * The constructor for the ShooterGameInputHandler.
   * 
   * @param game The ShooterGame that the input handler is responsible for.
   */

  public ShooterGameInputHandler(ShooterGame game) {
    this.game = game;
  }

  /**
   * A method that runs all input methods.
   */

  public void handleInputs() {
    playerInput();
    pauseCheck();
  }

  /**
   * The playerInput() method checks the movement and shoot inputs. Moving the
   * player or adding elements to the game as necessary.
   */

  public void playerInput() {
    GameVariables vars = game.getVars();
    if (vars.getGameSpeed() > 0) {
      int playerNum = game.getPlayer().getPlayerNum() - 1;
      float xdir = GlobalInput.playerRight[playerNum] - GlobalInput.playerLeft[playerNum];
      float ydir = GlobalInput.playerUp[playerNum] - GlobalInput.playerDown[playerNum];

      if (GlobalInput.playerShoot[playerNum]) {
        game.spawnPlayerProjectile();
      }

      long time = (long) (Core.deltaTime * vars.getGameSpeed());
      game.getPlayer().setXDir(xdir);
      game.getPlayer().setYDir(ydir);
      game.getPlayer().accelDecel(time);
      game.getPlayer().diagonalMax();
    }
  }

  /**
   * A method that checks if the pause button has been pressed and pausing the
   * game if it is detected as down.
   */

  public void pauseCheck() {
    if (GlobalInput.paused) {
      if (!game.isNetworked()) {
        game.pause();
      }
    }
  }

}
