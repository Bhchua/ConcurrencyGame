package game.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Core;
import game.input.GlobalInput;
import game.renderer.ButtonListRenderer;
import game.renderer.TextRenderer;
import game.screens.GameScreenManager;
import game.screens.Screen;
import game.screens.menus.buttons.Button;
import game.screens.menus.buttons.Button.ButtonAction;
import game.screens.menus.buttons.ButtonList;
import game.screens.threads.MenuThread;

/**
 * The PlayerSelectScreen allows the user to select how many people will be
 * playing in a game session.
 * 
 * @author Brandon Hua
 */

public class PlayerSelectScreen extends Screen {

  private String gameMode;
  private ButtonList buttons;
  private ButtonListRenderer buttonRend;

  /**
   * The PlayerSelectScreen initialises by creating the button list and loading
   * the background image.
   * 
   * @param gsm The GameScreenManager used to control the screen.
   * @param gameMode The game mode to be played.
   */

  protected PlayerSelectScreen(GameScreenManager gsm, String gameMode) {
    super(gsm);

    thandler = new TransitionHandler("fade_in");
    thandler.start();

    background = new Texture("bg_blank.png");

    buttons = new ButtonList();

    buttons.addToList(new Button("1", Core.width / 4, Core.height / 2, Core.width / 8,
        Core.width / 8, new ToGameOptions(1), new TransitionHandler("wipe"),
        "button_one_player.png", "button_one_player_selected.png"));

    buttons.addToList(new Button("2", 3 * Core.width / 4, Core.height / 2, Core.width / 8,
        Core.width / 8, new ToGameOptions(2), new TransitionHandler("wipe"),
        "button_two_player.png", "button_two_player_selected.png"));

    buttons.addToList(new Button("back", Core.width / 10, 11 * Core.height / 12, Core.width / 6,
        Core.width / 12, new ToMenu(), new TransitionHandler("wipe"), "button_back.png",
        "button_back_selected.png"));

    buttonRend = new ButtonListRenderer(buttons);
    mthread = new MenuThread(buttons);

    this.gameMode = gameMode;
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

    String message = "NUMBER OF PLAYERS";
    int height = Core.height / 20;
    int width = height * message.length();

    TextRenderer.print(sb, message, "white_back", Core.width / 2, 9 * Core.height / 10, width, height);

    TextRenderer.print(sb, "1", "white", Core.width / 4, Core.height / 3, Core.height / 12,
        Core.height / 12);
    TextRenderer.print(sb, "2", "white", 3 * Core.width / 4, Core.height / 3, Core.height / 12,
        Core.height / 12);

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
   * A ButtonAction that changes the current screen to a GameOptionScreen.
   * 
   * @author Brandon Hua
   *
   */

  public class ToGameOptions implements ButtonAction {

    int players;

    public ToGameOptions(int players) {
      this.players = players;
    }

    @Override
    public void execute(Object data) {
      gsm.set(new GameOptionScreen(gsm, players, gameMode));
    }
  }

  /**
   * A button action that sets the GameScreenManager to the main menu.
   * 
   * @author Brandon Hua
   */

  public class ToMenu implements ButtonAction {
    public void execute(Object data) {
      gsm.set(new MainMenuScreen(gsm));
    }
  }

}
