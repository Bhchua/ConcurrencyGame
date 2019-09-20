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
 * The NetworkSelectScreen allows the user to choose between becoming a host for
 * an online session or connect to another host as the client.
 * 
 * @author Brandon Hua
 */

public class NetworkSelectScreen extends Screen {

  private ButtonList buttons;
  private ButtonListRenderer buttonRend;

  /**
   * The NetworkSelectScreen initialises with the buttons and background of the
   * menu.
   * 
   * @param gsm The GameScreenManager used to control the screen.
   */

  protected NetworkSelectScreen(GameScreenManager gsm) {
    super(gsm);
    background = new Texture("bg_blank.png");
    thandler = new TransitionHandler("fade_in");
    thandler.start();

    initButtons();
  }

  /**
   * A method to initialise the buttons of the menu.
   */

  public void initButtons() {
    buttons = new ButtonList();

    buttons.addToList(new Button("back", Core.width / 10, 11 * Core.height / 12, Core.width / 6,
        Core.width / 12, new ToMenu(), new TransitionHandler("wipe"), "button_back.png",
        "button_back_selected.png"));

    buttons.addToList(new Button("HOST", Core.width / 4, Core.height / 2, Core.width / 5,
        Core.height / 10, new ToNetworkHost(), new TransitionHandler("wipe")));

    buttons.addToList(new Button("JOIN", 3 * Core.width / 4, Core.height / 2, Core.width / 5,
        Core.height / 10, new ToNetworkClient(), new TransitionHandler("wipe")));

    buttonRend = new ButtonListRenderer(buttons);
    mthread = new MenuThread(buttons);
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

    String message = "NETWORKED PLAY";
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
   * A method to change the top most screen on the GameScreenManager to a
   * MainMenuScreen.
   * 
   * @author Brandon Hua
   */

  public class ToMenu implements ButtonAction {
    public void execute(Object data) {
      gsm.set(new MainMenuScreen(gsm));
    }
  }

  /**
   * A method to change the top most screen on the GameScreenManager to a
   * NetworkHostScreen.
   * 
   * @author Brandon Hua
   */

  public class ToNetworkHost implements ButtonAction {
    public void execute(Object data) {
      gsm.set(new NetworkHostScreen(gsm));
    }
  }

  /**
   * A method to change the top most screen on the GameScreenManager to a
   * NetworkClientScreen.
   * 
   * @author Brandon Hua
   */

  public class ToNetworkClient implements ButtonAction {
    public void execute(Object data) {
      gsm.set(new NetworkClientScreen(gsm));
    }
  }

}
