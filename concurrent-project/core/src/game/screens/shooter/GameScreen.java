package game.screens.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.CameraVariables;
import game.Core;
import game.input.GlobalInput;
import game.input.KeyboardInput;
import game.model.GameModelViewController;
import game.model.ShooterGame;
import game.model.rules.ShooterRules;
import game.renderer.ButtonListRenderer;
import game.renderer.GameRenderList;
import game.renderer.GameRenderer;
import game.renderer.TextRenderer;
import game.screens.GameScreenManager;
import game.screens.Screen;
import game.screens.menus.GameEndOverlay;
import game.screens.menus.MainMenuScreen;
import game.screens.menus.TransitionHandler;
import game.screens.menus.buttons.Button;
import game.screens.menus.buttons.Button.ButtonAction;
import game.screens.menus.buttons.ButtonList;
import game.screens.threads.MenuThread;
import java.io.FileNotFoundException;
import java.io.IOException;
import network.NetworkMsgController;
import network.threads.NetworkThread;

/**
 * The GameScreen class holds the components for the game to function (the
 * model, game threads, rules etc) as well as the objects used to render the
 * contained games.
 * 
 * @author Brandon Hua
 *
 */

public class GameScreen extends Screen {

  // Game related variables;
  private int views;
  private GameModelViewController gmvc;

  // UI related variables
  private ButtonList buttons;
  private ButtonListRenderer buttonRend;
  private GameModeui gmui;
  private boolean startedEndOverlay = false;
  private GameEndOverlay endOverlay;

  private KeyboardInput kinput;

  private NetworkMsgController nmc;

  private int[][] screenOrder = { { 1 }, { 1, 2 }, { 3, 2, 1 }, { 3, 4, 1, 2 } };
  private double[][] ratioOrder = { { 1, 1 }, { 0.5, 1 }, { 0.5, 0.5 }, { 0.5, 0.5 } };

  /**
   * The constructor for the GameScreen class.
   * 
   * @param gsm           The GameScreenManager used to update the current screen.
   * @param players       The number of players in this screen.
   * @param gameMode      The game mode to be played.
   * @param data          The data to be used as the parameters of the ruleset.
   * @param difficulty    The difficulty of the game.
   * @param levelFileName The name of the level file to be loaded.
   * @throws FileNotFoundException if a level cannot be loaded.
   * @throws IOException           If a level is invalid.
   */

  public GameScreen(GameScreenManager gsm, int players, String gameMode, Object data,
      String difficulty, String levelFileName) throws FileNotFoundException, IOException {
    super(gsm);
    CameraVariables.xratio = ratioOrder[players - 1][0];
    CameraVariables.yratio = ratioOrder[players - 1][1];
    CameraVariables.zoom = CameraVariables.zoomConfig[players - 1];

    background = new Texture("blank.png");
    views = players;
    this.gmvc = new GameModelViewController(gsm, players, gameMode, data, levelFileName);
    initMenu(gameMode);
    kinput = new KeyboardInput();
    Gdx.input.setInputProcessor(kinput);
  }

  /**
   * The constructor for the GameScreen class in a networked mode.
   * 
   * @param gsm           The GameScreenManager used to update the current screen.
   * @param data          The perameters of the game.
   * @param difficulty    The difficulty of the game.
   * @param levelFileName The level to be loaded.
   * @param gameThread    The NetworkThread used for the connection.
   * @throws FileNotFoundException If a level cannot be loaded.
   * @throws IOException           If a level file is invalid.
   */

  public GameScreen(GameScreenManager gsm, Object data, String difficulty, String levelFileName,
      NetworkThread gameThread) throws FileNotFoundException, IOException {
    super(gsm);
    CameraVariables.xratio = ratioOrder[0][0];
    CameraVariables.yratio = ratioOrder[0][1];
    CameraVariables.zoom = CameraVariables.zoomConfig[0];

    background = new Texture("blank.png");
    views = 1;
    this.gmvc = new GameModelViewController(gsm, 1, "network", data, levelFileName);
    initMenu("network");
    nmc = new NetworkMsgController(gmvc, gameThread);
    nmc.start();

    kinput = new KeyboardInput();
    Gdx.input.setInputProcessor(kinput);
  }

  /**
   * A method for initialising the menu used in a game screen. Each view is
   * automatically assigned their own menu elements.
   * 
   * @param gameMode The name of the game mode.
   */

  public void initMenu(String gameMode) {
    thandler = new TransitionHandler("fade_in");
    thandler.start();

    buttons = new ButtonList();

    buttons.addToList(new Button("BACK TO MENU", Core.width / 2, Core.height / 2, Core.width / 4,
        Core.height / 8, new ToMenu(), new TransitionHandler("fade_out")));

    for (int i = 0; i < views; i++) {
      int curView = screenOrder[views - 1][i];
      int buttonx = Core.width / 2;
      int buttony = Core.height / 4;
      if (views > 1) {
        buttonx = (((((i) % 2) * 2) + 1) * Core.width / 4);
        buttony = (((((i) / 2) * 2) + 1) * Core.height / 4);
      }
      buttons.addToList(new Button("P" + (curView) + " UNSTUCK", buttonx, buttony, Core.width / 4,
          Core.height / 8, new Unstuck(curView)));
    }

    buttons.setActive(false);
    buttonRend = new ButtonListRenderer(buttons);
    mthread = new MenuThread(buttons);

    for (GameModeui g : uiList) {
      if (gameMode.equals(g.modeName())) {
        gmui = g;
      }
    }

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
    gmvc.update();
    buttons.setActive(gmvc.allPaused());
    finish();
  }

  /**
   * The finish() method initialises a new button list and starts a new
   * TransitionHandler to fade the screen and make the ending statistics easier to
   * view.
   * 
   * <p>
   * The ending overlay is also initialised based on the game variables at the
   * time of end.
   */

  public void finish() {
    if (gmvc.isGameFinished()) {
      if (!startedEndOverlay) {
        endOverlay = gmvc.getEndOverlay();
        ShooterGame[] games = gmvc.getGames();
        games[0].pause();
        gmvc.synchronisePause();
        buttons = new ButtonList();

        buttons.addToList(new Button("BACK TO MENU", Core.width / 2, Core.height / 6,
            Core.width / 4, Core.height / 8, new ToMenu(), new TransitionHandler("fade_out")));

        buttons.setActive(true);
        buttonRend = new ButtonListRenderer(buttons);
        mthread = new MenuThread(buttons);
        thandler = new TransitionHandler("darken");
        thandler.start();
        startedEndOverlay = true;
      }
    }
  }

  @Override
  public void render(SpriteBatch sb) {
    sb.begin();
    sb.draw(background, 0, 0, Core.width, Core.height);
    sb.end();

    renderGames(sb);

    Gdx.gl.glViewport(0, 0, Core.width, Core.height);

    if (!gmvc.allPaused()) {
      gmui.render(sb);
    } else {
      if (!startedEndOverlay) {
        renderPaused(sb);
      }
    }

    thandler.render(sb);

    if (startedEndOverlay) {
      endOverlay.render(sb);
    }

    buttonRend.render(sb);
    if (buttons.isConfirmed()) {
      buttons.getTransitionList().render(sb);
    }
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
   * A method to take the list of games and renderer them all to screen with the
   * correct aspect ratios using the ratioOrder Double array.
   * 
   * @param sb The SpriteBatch used to draw to screen.
   */

  public void renderGames(SpriteBatch sb) {
    ShooterGame[] games = gmvc.getGames();
    GameRenderList[] renderList = gmvc.getRenderList();
    for (int i = 0; i < views; i++) {
      int curView = screenOrder[views - 1][i];
      int viewWidth = (int) ((ratioOrder[views - 1][0] * Core.width));
      int viewHeight = (int) ((ratioOrder[views - 1][1] * Core.height));
      int viewx = ((i) % 2) * viewWidth;
      int viewy = ((i) / 2) * viewHeight;
      Gdx.gl.glViewport(viewx, viewy, viewWidth, viewHeight);
      GameRenderer.render(sb, games[curView - 1], renderList[curView - 1].getRenderArray(),
          gmvc.getRules().getMode(), i);
    }
  }

  /**
   * A method to render a pause message on screen.
   * 
   * @param sb The SpriteBatch used to draw to screen.
   */

  public static void renderPaused(SpriteBatch sb) {
    String pause = "PAUSED";
    int width = Core.width / 4;
    int height = Core.height / 10;
    TextRenderer.print(sb, pause, "white_back", Core.width / 2, 4 * Core.height / 6, width, height);
  }

  /**
   * A button action that sets the GameScreenManager to the main menu.
   * 
   * @author Brandon Hua
   *
   */

  public class ToMenu implements ButtonAction {
    @Override
    public void execute(Object data) {
      if (nmc != null) {
        nmc.finish();
      }
      gsm.set(new MainMenuScreen(gsm));
    }
  }

  /**
   * A button action that forces the player to respawn regardless of their health.
   * 
   * @author Brandon Hua
   *
   */

  public class Unstuck implements ButtonAction {

    private final int viewNumber;

    public Unstuck(int view) {
      viewNumber = view;
    }

    @Override
    public void execute(Object data) {
      ShooterGame[] games = gmvc.getGames();
      for (int i = 0; i < views; i++) {
        games[i].setPause(false);
      }
      games[viewNumber - 1].getPlayer().die();
      buttons.reset();
    }
  }

  //
  // Classes below this point are GameModeUIs.
  //

  public GameModeui[] uiList = new GameModeui[] { new Timeui(), new Stockui(), new Netui() };

  /**
   * The GameModeUI interface provides the ability to display different UIs based
   * on the current game mode the rules object has been bound to.
   * 
   * @author Brandon Hua
   *
   */

  public interface GameModeui {

    /**
     * A method that simply returns the name of the game mode.
     * 
     * @return The name of the game mode.
     */
    public String modeName();

    /**
     * The render method draws the UI of the game mode to screen.
     * 
     * @param sb The SpriteBatch used to draw to screen.
     */
    public void render(SpriteBatch sb);
  }

  /**
   * The GameModeUI that is used when the rule set is declared as "timed"
   * 
   * <p>
   * The render method simply draws the current time passed on screen.
   * 
   * @author Brandon Hua
   */

  public class Timeui implements GameModeui {

    @Override
    public String modeName() {
      return "timed";
    }

    @Override
    public void render(SpriteBatch sb) {
      ShooterRules rules = gmvc.getRules();

      long time = Math.abs(rules.getVars().getTimeLimit() - rules.getVars().getTimePassed());
      if (time < 5000) {
        int countdown = (int) ((time / 1000) + 1);
        TextRenderer.print(sb, String.valueOf(countdown), "white_back", Core.width / 2,
            Core.height / 2, Core.height / 6, Core.height / 6);
      }
      TextRenderer.printTime(sb, time, "white_back", Core.width / 2, 10 * Core.height / 12,
          Core.width / 6, Core.height / 16);

      String timeRem = "TIME REMAINING";
      int timeRemHeight = Core.height / 24;
      int timeRemWidth = timeRemHeight * timeRem.length();
      TextRenderer.print(sb, timeRem, "white_back", Core.width / 2, 11 * Core.height / 12,
          timeRemWidth, timeRemHeight);
    }

  }

  /**
   * The GameModeUI that is used when the rule set is declared as "network"
   * 
   * <p>
   * The render method also draws the current time passed on screen.
   * 
   * @author Brandon Hua
   */

  public class Netui implements GameModeui {

    @Override
    public String modeName() {
      return "network";
    }

    @Override
    public void render(SpriteBatch sb) {
      ShooterRules rules = gmvc.getRules();

      long time = Math.abs(rules.getVars().getTimeLimit() - rules.getVars().getTimePassed());
      if (time < 5000) {
        int countdown = (int) ((time / 1000) + 1);
        TextRenderer.print(sb, String.valueOf(countdown), "white_back", Core.width / 2,
            Core.height / 2, Core.height / 6, Core.height / 6);
      }
      TextRenderer.printTime(sb, time, "white_back", Core.width / 2, 10 * Core.height / 12,
          Core.width / 6, Core.height / 16);

      String timeRem = "TIME REMAINING";
      int timeRemHeight = Core.height / 24;
      int timeRemWidth = timeRemHeight * timeRem.length();
      TextRenderer.print(sb, timeRem, "white_back", Core.width / 2, 11 * Core.height / 12,
          timeRemWidth, timeRemHeight);
    }

  }

  /**
   * The GameModeUI that is used when the rule set is declared as "stock"
   * 
   * <p>
   * The render method draws the remaining lives and enemies in a level.
   * 
   * @author Brandon Hua
   */

  public class Stockui implements GameModeui {

    @Override
    public String modeName() {
      return "stock";
    }

    @Override
    public void render(SpriteBatch sb) {
      ShooterRules rules = gmvc.getRules();
      int remaining = rules.getVars().getDefeatQuota() - rules.getVars().getDefeated();

      String remString = String.valueOf(remaining);
      int height = Core.height / 16;
      int width = height * remString.length();
      TextRenderer.print(sb, remString, "white_back", Core.width / 2, 10 * Core.height / 12, width,
          height);

      String lives = String.valueOf(rules.getVars().getLives());
      width = height * lives.length();
      TextRenderer.print(sb, "LIVES", "white_back", Core.width / 2, 3 * Core.height / 12,
          Core.width / 8, Core.height / 18);
      TextRenderer.print(sb, lives, "white_back", Core.width / 2, 2 * Core.height / 12, width,
          height);

      String left = "ENEMIES LEFT";
      int leftHeight = Core.height / 24;
      int leftWidth = leftHeight * left.length();
      TextRenderer.print(sb, left, "white_back", Core.width / 2, 11 * Core.height / 12, leftWidth,
          leftHeight);
    }

  }

}
