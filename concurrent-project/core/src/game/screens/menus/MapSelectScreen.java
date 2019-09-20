package game.screens.menus;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.CameraVariables;
import game.Core;
import game.input.GlobalInput;
import game.renderer.ButtonListRenderer;
import game.renderer.TextRenderer;
import game.screens.GameScreenManager;
import game.screens.Screen;
import game.screens.menus.PlayerSelectScreen.ToGameOptions;
import game.screens.menus.PlayerSelectScreen.ToMenu;
import game.screens.menus.buttons.Button;
import game.screens.menus.buttons.ButtonList;
import game.screens.shooter.GameScreen;
import game.screens.menus.buttons.Button.ButtonAction;
import game.screens.threads.MenuThread;

/**
 * The MapSelectScreen allows users to choose the map to play on.
 * 
 * @author Brandon Hua
 */

public class MapSelectScreen extends Screen {

  private String gameMode;
  private ButtonList buttons;
  private ButtonListRenderer buttonRend;
  private int players;
  private Object gameData;

  /**
   * The MapSelectScreen initialises with the player count, game mode and game
   * data used in the game session (to be passed to the game screen).
   * 
   * @param gsm      The GameScreenManager used to control the screen.
   * @param players  The number of players in a game.
   * @param gameMode The game mode to be played.
   * @param gameData The game data to use (time, enemies, etc)
   */

  protected MapSelectScreen(GameScreenManager gsm, int players, String gameMode, Object gameData) {
    super(gsm);

    background = new Texture("bg_blank.png");

    buttons = new ButtonList();

    buttons.addToList(new Button("back", Core.width / 10, 11 * Core.height / 12, Core.width / 6, 
        Core.width / 12, new ToGameOptions(), new TransitionHandler("wipe"), "button_back.png", 
        "button_back_selected.png"));

    buttons.addToList(new Button("town", Core.width / 2, Core.height / 2, Core.width / 6, 
        Core.width / 3, new ToGame("town.lvl"), new TransitionHandler("wipe"), 
        "button_town.png", "button_town_selected.png"));

    buttonRend = new ButtonListRenderer(buttons);
    mthread = new MenuThread(buttons);

    this.gameMode = gameMode;
    this.players = players;
    this.gameData = gameData;

    thandler = new TransitionHandler("fade_in");
    thandler.start();

  }

  @Override
  public void handleInput() {
    GlobalInput.mouseX = Gdx.input.getX();
    GlobalInput.mouseY = Core.height - Gdx.input.getY();
    GlobalInput.confirm = Gdx.input.justTouched();
    buttons.performAction();
  }

  @Override
  public void update() {
    handleInput();
  }

  @Override
  public void render(SpriteBatch sb) {
    sb.begin();
    sb.draw(background, 0, 0, Core.width, Core.height);
    sb.end();

    String message = "SELECT MAP";
    int height = Core.height / 20;
    int width = height * message.length();

    TextRenderer.print(sb, message, "white_back", Core.width / 2, 9 * Core.height / 10, width, height);
    
    buttonRend.render(sb);
    if (buttons.isConfirmed()) {
      buttons.getTransitionList().render(sb);
    }

    thandler.render(sb);
  }

  @Override
  public void dispose() {
    background.dispose();
    thandler.dispose();
    mthread.finish();
    buttonRend.dispose();
    buttons.dispose();
  }

  /**
   * A ButtonAction that changes the current screen to a MapSelectScreen.
   * 
   * @author Brandon Hua
   */

  public class ToGameOptions implements ButtonAction {
    @Override
    public void execute(Object data) {
      gsm.set(new GameOptionScreen(gsm, players, gameMode));
    }

  }

  /**
   * A ButtonAction that changes the current screen to a GameScreen.
   * 
   * @author Brandon Hua
   */

  public class ToGame implements ButtonAction {

    String level;

    public ToGame(String level) {
      this.level = level;
    }

    @Override
    public void execute(Object data) {
      try {
        gsm.set(new GameScreen(gsm, players, gameMode, gameData, "easy", level));
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

}
