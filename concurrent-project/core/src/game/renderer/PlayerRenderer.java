package game.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.CameraVariables;
import game.Core;
import game.entities.PlayerCharacter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A renderer for the player character.
 * 
 * @author Brandon Hua
 *
 */

public class PlayerRenderer implements Renderer {

  private final int SHEETCOLS = 16;
  private final int SHEETROWS = 16;

  private final int ANIMRATE = 250;
  private final int FLASHRATE = 50;

  private PlayerCharacter character;

  private Texture charSheet;

  private TextureRegion curFrame;
  private TextureRegion invincible;

  private TextureRegion[] charUpFrames;
  private TextureRegion[] charDownFrames;
  private TextureRegion[] charRightFrames;
  private TextureRegion[] charUpRightFrames;
  private TextureRegion[] charDeathFrames;

  private boolean rightFlipped = false;
  private boolean upRightFlipped = false;

  /**
   * A constructor for the player renderer.
   * 
   * @param character The character to be rendered
   */

  public PlayerRenderer(PlayerCharacter character) {
    this.character = character;
    charSheet = new Texture("character_sheet.png");
    int playerNum = (character.getPlayerNum() - 1) * 4;

    TextureRegion[][] tmp = TextureRegion.split(charSheet, charSheet.getWidth() / SHEETCOLS,
        charSheet.getHeight() / SHEETROWS);

    invincible = tmp[SHEETCOLS - 1][SHEETROWS - 1];
    charUpFrames = new TextureRegion[4];
    charDownFrames = new TextureRegion[4];
    charRightFrames = new TextureRegion[4];
    charUpRightFrames = new TextureRegion[4];
    charDeathFrames = new TextureRegion[4];
    for (int i = 0; i < 4; i++) {
      charDownFrames[i] = tmp[0][i + playerNum];
      charUpFrames[i] = tmp[1][i + playerNum];
      charRightFrames[i] = tmp[2][i + playerNum];
      charUpRightFrames[i] = tmp[3][i + playerNum];
      charDeathFrames[i] = tmp[4][i + playerNum];
    }
  }

  private int runtime = 0;
  private int deathRuntime = 0;
  private int counter = 0;
  private int flashCounter = 0;
  private int deathCounter = 0;

  /**
   * A method that can change the currently rendered frame based on character
   * states and the time elsaped since the last frame was played.
   */

  public void changeCurFrame() {
    runtime += Core.deltaTime;
    if (runtime > ANIMRATE) {
      counter = (counter + 1) % 4;
      runtime = runtime % ANIMRATE;
    }

    if (!character.isMoving()) {
      counter = 0;
    }

    TextureRegion cframe = new TextureRegion();

    for (FrameCondition f : frameConditions) {
      if (f.condition()) {
        cframe = f.changeFrame();
      }
    }

    if (character.isInvincible()) {
      if (runtime > FLASHRATE * flashCounter) {
        flashCounter = (flashCounter + 1) % 4;
        cframe = invincible;
      }
    }

    curFrame = cframe;
  }

  /**
   * The FrameChangeCondition allows the renderer to change the current frame based on
   * a determined condition.
   * 
   * @author Brandon Hua
   */

  public interface FrameCondition {
    /**
     * A method to check if the condition has been met.
     * 
     * @return A boolean value for whether or not the condition has been met.
     */
    public boolean condition();

    /**
     * A method to get the frames of the condition.
     * 
     * @return The TextureRegion of the current frame.
     */
    public TextureRegion changeFrame();
  }

  /**
   * The FrameCondition for a character moving upwards.
   */
  
  public class UpFrames implements FrameCondition {

    @Override
    public boolean condition() {
      return character.getFacing().equals("up");
    }

    @Override
    public TextureRegion changeFrame() {
      return charUpFrames[counter];
    }

  }
  
  /**
   * The FrameCondition for a character moving downwards.
   * 
   * @author Brandon Hua
   */

  public class DownFrames implements FrameCondition {

    @Override
    public boolean condition() {
      return character.getFacing().equals("down");
    }

    @Override
    public TextureRegion changeFrame() {
      return charDownFrames[counter];
    }

  }

  /**
   * The FrameCondition for a character moving left.
   * 
   * @author Brandon Hua
   */
  
  public class LeftFrames implements FrameCondition {

    @Override
    public boolean condition() {
      return character.getFacing().equals("left");
    }

    @Override
    public TextureRegion changeFrame() {
      if (!rightFlipped) {
        flip(charRightFrames);
        rightFlipped = true;
      }
      return charRightFrames[counter];
    }

  }
  
  /**
   * The FrameCondition for a character moving right.
   * 
   * @author Brandon Hua
   */

  public class RightFrames implements FrameCondition {

    @Override
    public boolean condition() {
      return character.getFacing().equals("right");
    }

    @Override
    public TextureRegion changeFrame() {
      if (rightFlipped) {
        flip(charRightFrames);
        rightFlipped = false;
      }
      return charRightFrames[counter];
    }

  }
  
  /**
   * The FrameCondition for a character moving up and left.
   * 
   * @author Brandon Hua
   */

  public class UpLeftFrames implements FrameCondition {

    @Override
    public boolean condition() {
      return character.getFacing().equals("up_left");
    }

    @Override
    public TextureRegion changeFrame() {
      if (!upRightFlipped) {
        flip(charUpRightFrames);
        upRightFlipped = true;
      }
      return charUpRightFrames[counter];
    }

  }
  
  /**
   * The FrameCondition for a character moving up and down.
   * 
   * @author Brandon Hua
   */

  public class UpRightFrames implements FrameCondition {

    @Override
    public boolean condition() {
      return character.getFacing().equals("up_right");
    }

    @Override
    public TextureRegion changeFrame() {
      if (upRightFlipped) {
        flip(charUpRightFrames);
        upRightFlipped = false;
      }
      return charUpRightFrames[counter];
    }

  }
  
  /**
   * The FrameCondition for a dying character.
   * 
   * @author Brandon Hua
   */

  public class DeathFrames implements FrameCondition {

    @Override
    public boolean condition() {
      if (!character.isDying()) {
        deathCounter = 0;
        deathRuntime = 0;
      }
      return character.isDying();
    }

    @Override
    public TextureRegion changeFrame() {
      deathRuntime += Core.deltaTime;
      if (deathRuntime > ANIMRATE) {
        deathCounter = (deathCounter + 1);
        if (deathCounter > 3) {
          deathCounter = 3;
        }
        deathRuntime = deathRuntime % ANIMRATE;
      }
      return charDeathFrames[deathCounter];
    }
  }

  private ArrayList<FrameCondition> frameConditions = new ArrayList<FrameCondition>(
      Arrays.asList(new UpFrames(), new DownFrames(), new LeftFrames(), 
          new RightFrames(), new UpLeftFrames(), new UpRightFrames(), new DeathFrames()));

  /**
   * A method to flip a texture.
   * 
   * @param region The TextureRegion to be flipped.
   */
  
  public void flip(TextureRegion[] region) {
    for (int i = 0; i < 4; i++) {
      region[i].flip(true, false);
    }
  }

  @Override
  public void render(SpriteBatch sb, int focusx, int focusy) {
    double xratioInverse = 1 / CameraVariables.xratio;
    double yratioInverse = 1 / CameraVariables.yratio;
    int zoom = CameraVariables.zoom;

    int drawx = (int) (Core.width / 2 - (focusx * zoom * xratioInverse) 
        + (character.getXpos() * zoom * xratioInverse)
        - ((character.getWidth()) / 1) * zoom * xratioInverse);
    int drawy = (int) (Core.height / 2 - (focusy * zoom * yratioInverse) 
        + (character.getYpos() * zoom * yratioInverse)
        - ((character.getHeight()) / 2) * zoom * yratioInverse);
    int width = (int) (character.getWidth() * zoom * xratioInverse) * 2;
    int height = (int) (character.getHeight() * zoom * yratioInverse);

    curFrame = charDownFrames[0];
    changeCurFrame();
    try {
      sb.draw(curFrame, drawx, drawy, width, height);
    } catch (NullPointerException e) {
      Core.displayError("Tried to draw frame: " + counter + ". But it apparently does not exist.");
    }
  }

  public void dispose() {
    charSheet.dispose();
  }

}
