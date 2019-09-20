package game.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.CameraVariables;
import game.Core;
import game.model.ShooterGame;
import java.util.ArrayList;

/**
 * A class that simply gets all the renderers for entities in a game and runs
 * through the list sequentially rendering each one.
 * 
 * @author Brandon Hua
 *
 */

public class GameRenderer {

  /**
   * The render method draws the game to screen.
   * 
   * @param sb       The SpriteBatch used to draw the game.
   * @param gameMode The game mode to draw different UI.
   * @param view     The view of the game to change UI locations.
   * @param renderArray  The renderArray to represent the game.
   * @param game The game to be rendered.
   */

  public static void render(SpriteBatch sb, ShooterGame game, ArrayList<Renderer> renderArray, 
      String gameMode, int view) {
    game.getCam().basicFollow(game.getPlayer(), Core.deltaTime);
    int camx = game.getCam().getXpos();
    int camy = game.getCam().getYpos();
    sb.begin();
    for (int i = 0; i < renderArray.size(); i++) {
      renderArray.get(i).render(sb, camx, camy);
    }
    sb.end();

    if (!game.isPaused()) {
      if (!game.getPlayer().isDying()) {
        renderScore(sb, game, gameMode, view);
      } else {
        renderDeathMsg(sb);
      }
    }

  }

  /**
   * A method that looks at the current score and renders it to screen, offsetting
   * the position so it constantly stays in the center.
   * 
   * @param sb       The SpriteBatch used to draw the image.
   * @param game     The game to render the score of.
   * @param gameMode The gamemode of the rule set to see if the score should be
   *                 rendered or not.
   * @param view     The view number to determine where to draw the score.
   */

  public static void renderScore(SpriteBatch sb, ShooterGame game, String gameMode, int view) {
    if (!gameMode.equals("timed")) {

      String message = "SCORE";

      int messageh = (int) ((Core.height / 24) * (1 / CameraVariables.yratio));
      int messagew = (int) ((Core.height / 24) * message.length() * (1 / CameraVariables.xratio));
      int messagex = (int) ((((2 * (view % 2)) - 1) * (7 * Core.width / 16))
          + ((-2 * (view % 2)) + 1) * (messageh * message.length()) + Core.width / 2);
      int messagey = 15 * Core.height / 16;

      TextRenderer.print(sb, message, "white_back", messagex, messagey, messagew, messageh);

      String score = Integer.toString(game.getVars().getScore());

      int scoreHeight = (int) ((Core.height / 24) * (1 / CameraVariables.yratio));
      int scoreWidth = (int) ((Core.height / 24) * score.length() * (1 / CameraVariables.xratio));
      int scorex = messagex;
      int scorey = (int) (messagey - (1.5 * scoreHeight));

      TextRenderer.print(sb, score, "white_back", scorex, scorey, scoreWidth, scoreHeight);
    }
  }

  /**
   * A method to render the death message.
   * 
   * @param sb The SpriteBatch used to render the text.
   */

  public static void renderDeathMsg(SpriteBatch sb) {
    String died = "YOU DIED";
    int height = (int) ((Core.height / 14) / CameraVariables.yratio);
    int width = (int) ((height * died.length()) / CameraVariables.xratio);
    TextRenderer.print(sb, died, "white_back", Core.width / 2, 4 * Core.height / 6, width, height);
  }

}
