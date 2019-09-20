package game.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import error.GlobalErrors;
import game.CameraVariables;
import game.Core;
import game.input.GlobalInput;
import game.renderer.ButtonListRenderer;
import game.renderer.TextRenderer;
import game.screens.GameScreenManager;
import game.screens.Screen;
import game.screens.menus.buttons.Button;
import game.screens.menus.buttons.ButtonList;
import game.screens.threads.MenuThread;
import game.screens.menus.buttons.Button.ButtonAction;

/**
 * The MainMenuScreen class holds the UI and methods required to move to
 * different screens.
 * 
 * @author Brandon Hua
 */

public class MainMenuScreen extends Screen {

  private Texture blank;
  private ButtonList buttons;
  private ButtonListRenderer buttonRend;

  private String error;
  private boolean hasError;

  /**
   * The constructor for the MainMenuScreen class.
   * 
   * @param gsm The GameScreenManager used to control the game.
   */

  public MainMenuScreen(GameScreenManager gsm) {
    super(gsm);

    CameraVariables.xratio = 1;
    CameraVariables.yratio = 1;
    CameraVariables.zoom = 1;

    background = new Texture("title_screen.png");
    thandler = new TransitionHandler("fade_in");
    thandler.start();

    initButtons();

  }

  /**
   * The constructor for the MainMenuScreen class with the addition of an error
   * message which is shown on top of the menu screen.
   * 
   * @param gsm   The GameScreenManager used to control the game.
   * @param error The error message to be displayed.
   */

  public MainMenuScreen(GameScreenManager gsm, String error) {
    super(gsm);
    CameraVariables.xratio = 1;
    CameraVariables.yratio = 1;
    CameraVariables.zoom = 1;

    background = new Texture("title_screen.png");
    blank = new Texture("black_boarder.png");
    thandler = new TransitionHandler("fade_in");
    thandler.start();

    this.error = error;
    hasError = true;

    buttons = new ButtonList();
    buttons.addToList(new Button("CLOSE", Core.width / 2, Core.height / 16, Core.width / 4,
        Core.height / 8, new CloseError()));
    buttonRend = new ButtonListRenderer(buttons);
    mthread = new MenuThread(buttons);
  }

  /**
   * A method to initialise the menu buttons.
   */

  public void initButtons() {

    buttons = new ButtonList();

    buttons.addToList(new Button("STOCK", Core.width / 2, 3 * Core.height / 6, Core.width / 5,
        Core.height / 10, new LoadPlayerSelect("stock"), new TransitionHandler("wipe")));

    buttons.addToList(new Button("TIMED", Core.width / 2, 2 * Core.height / 6, Core.width / 5,
        Core.height / 10, new LoadPlayerSelect("timed"), new TransitionHandler("wipe")));

    buttons.addToList(new Button("ONLINE VS", Core.width / 2, Core.height / 6, Core.width / 5,
        Core.height / 10, new LoadNetworkScreen(), new TransitionHandler("wipe")));

    buttons.addToList(new Button("info", 8 * Core.width / 9, Core.height / 5, 3 * Core.height / 10,
        Core.height / 10, new LoadHelpScreen(), new TransitionHandler("wipe"), "button_help.png",
        "button_help_selected.png"));

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

    if (hasError) {
      renderError(sb);
    }

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
   * A method that splits up an error message and wraps the text to as many lines
   * as needed on screen.
   * 
   * @param sb The SpriteBatch used to draw the error.
   */

  public void renderError(SpriteBatch sb) {
    sb.begin();
    int width = 3 * Core.width / 4;
    int height = 3 * Core.height / 4;
    int xpos = Core.width / 2 - width / 2;
    int ypos = Core.height / 2 - height / 2;
    sb.draw(blank, xpos, ypos, width, height);
    sb.end();

    String err = "ERROR";
    int errHeight = Core.height / 10;
    int errWidth = errHeight * err.length();
    TextRenderer.print(sb, err, "white_back", Core.width / 2, 15 * Core.height / 16, errWidth,
        errHeight);

    int lineLength = 32;
    int lines = error.length() / lineLength;
    int remaining = error.length() % lineLength;

    int lineHeight = width / lineLength;

    String[] errorSubstrings = new String[lines + (remaining > 0 ? 1 : 0)];
    for (int i = 0; i < errorSubstrings.length; i++) {
      int start = lineLength * i;
      int end = start;
      if (error.length() - start > lineLength) {
        end = lineLength * (i + 1);
      } else {
        end = (error.length() % lineLength) + start;
      }
      errorSubstrings[i] = error.substring(start, end);
    }

    for (int i = 0; i < errorSubstrings.length; i++) {
      int lineypos = (int) ((Core.height / 2 - i * lineHeight * 1.5)
          + (errorSubstrings.length * lineHeight * 1.5) / 2);
      int lineWidth = (width / lineLength) * errorSubstrings[i].length();
      TextRenderer.print(sb, errorSubstrings[i], "white", Core.width / 2, lineypos, lineWidth,
          lineHeight);
    }
  }

  /**
   * A ButtonAction that switches the current Screen in the GameScreenManager to
   * the PlayerSelect Screen.
   * 
   * @author Brandon Hua
   *
   */

  public class LoadPlayerSelect implements ButtonAction {

    String gameMode;

    /**
     * The constructor for the LoadPlayerSelect ButtonAction that passes the game
     * mode information to the PlayerSelect screen.
     * 
     * @param gameMode The name of the game mode.
     */

    public LoadPlayerSelect(String gameMode) {
      this.gameMode = gameMode;
    }

    @Override
    public void execute(Object data) {
      gsm.set(new PlayerSelectScreen(gsm, gameMode));
    }

  }

  /**
   * The CloseError ButtonAction sets the hasError boolean to false so the message
   * is no longer rendered and intialises the buttons for the menu.
   * 
   * @author Brandon Hua
   *
   */

  public class CloseError implements ButtonAction {
    @Override
    public void execute(Object data) {
      initButtons();
      hasError = false;
    }
  }

  /**
   * The LoadNetworkScreen class changes the topmost screen in the
   * GameScreenManager to a NetworkSelectScreen.
   * 
   * @author Brandon Hua
   *
   */

  public class LoadNetworkScreen implements ButtonAction {
    @Override
    public void execute(Object data) {
      gsm.set(new NetworkSelectScreen(gsm));
    }
  }

  /**
   * The LoadHelpScreen class changes the topmost screen in the GameScreenManager
   * to a HelpScreen.
   * 
   * @author Brandon Hua
   *
   */

  public class LoadHelpScreen implements ButtonAction {
    @Override
    public void execute(Object data) {
      gsm.set(new HelpScreen(gsm));
    }
  }

}
