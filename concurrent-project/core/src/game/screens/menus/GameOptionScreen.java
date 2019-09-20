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
 * The GameOptions screen allows users to select thing like time limits or how
 * many enemies to defeat in a game.
 * 
 * @author Brandon Hua
 */

public class GameOptionScreen extends Screen {

  private String gameMode;
  private ButtonList buttons;
  private ButtonListRenderer buttonRend;
  private int players;

  /**
   * The GameOptionScreen initialises with the game mode and number of players.
   * 
   * @param gsm      The GameScreenManager to control this screen.
   * @param players  The number of players in a game.
   * @param gameMode The game mode to determine what options to show.
   */

  protected GameOptionScreen(GameScreenManager gsm, int players, String gameMode) {
    super(gsm);
    this.gameMode = gameMode;
    this.players = players;

    background = new Texture("bg_blank.png");

    initButtons(gameMode);

    buttonRend = new ButtonListRenderer(buttons);
    mthread = new MenuThread(buttons);

    thandler = new TransitionHandler("fade_in");
    thandler.start();
  }

  /**
   * A method to initialise the buttons of the game options screen depending on
   * the game mode.
   * 
   * @param gameMode The name of the game mode.
   */

  public void initButtons(String gameMode) {
    buttons = new ButtonList();

    buttons.addToList(new Button("back", Core.width / 10, 11 * Core.height / 12, 
        Core.width / 6, Core.width / 12,
        new ToPlayerSelect(), new TransitionHandler("wipe"), "button_back.png", 
        "button_back_selected.png"));

    for (ButtonLayout b : layoutList) {
      if (b.layoutName().equals(gameMode)) {
        b.initButtonLayout();
      }
    }
  }

  private ButtonLayout[] layoutList = new ButtonLayout[] { new TimedButtons(), new StockButtons() };

  /**
   * An interface that changes the button layout depending on the name of the
   * layout.
   * 
   * @author Brandon Hua
   */

  public interface ButtonLayout {
    public String layoutName();

    public void initButtonLayout();
  }

  /**
   * The ButtonLayout for the timed game mode.
   * 
   * @author Brandon Hua
   */

  public class TimedButtons implements ButtonLayout {

    @Override
    public String layoutName() {
      return "timed";
    }

    @Override
    public void initButtonLayout() {
      buttons.addToList(new Button("1 MIN", Core.width / 2, 3 * Core.height / 4, 
          Core.width / 4, Core.height / 8, new ToMapSelect(60000), 
          new TransitionHandler("wipe")));

      buttons.addToList(new Button("3 MINS", Core.width / 2, Core.height / 2, 
          Core.width / 4, Core.height / 8, new ToMapSelect(180000), 
          new TransitionHandler("wipe")));

      buttons.addToList(new Button("5 MINS", Core.width / 2, Core.height / 4, 
          Core.width / 4, Core.height / 8, new ToMapSelect(300000), 
          new TransitionHandler("wipe")));
    }

  }

  /**
   * The ButtonLayout for the stock game mode.
   * 
   * @author Brandon Hua
   */

  public class StockButtons implements ButtonLayout {

    @Override
    public String layoutName() {
      return "stock";
    }

    @Override
    public void initButtonLayout() {
      buttons.addToList(new Button("25 ENEMIES", Core.width / 2, 3 * Core.height / 4, 
          Core.width / 4, Core.height / 8,
          new ToMapSelect(new int[] { 3, 25 }), new TransitionHandler("wipe")));

      buttons.addToList(new Button("50 ENEMIES", Core.width / 2, Core.height / 2, 
          Core.width / 4, Core.height / 8,
          new ToMapSelect(new int[] { 6, 50 }), new TransitionHandler("wipe")));

      buttons.addToList(new Button("100 ENEMIES", Core.width / 2, Core.height / 4, 
          Core.width / 4, Core.height / 8,
          new ToMapSelect(new int[] { 9, 100 }), new TransitionHandler("wipe")));
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
  }

  @Override
  public void render(SpriteBatch sb) {
    sb.begin();
    sb.draw(background, 0, 0, Core.width, Core.height);
    sb.end();

    buttonRend.render(sb);

    String message = "GAME OPTIONS";
    int height = Core.height / 20;
    int width = height * message.length();

    TextRenderer.print(sb, message, "white_back", Core.width / 2, 9 * Core.height / 10, width, height);
    
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
   *
   */

  public class ToMapSelect implements ButtonAction {

    Object gameData;

    public ToMapSelect(Object gameData) {
      this.gameData = gameData;
    }

    @Override
    public void execute(Object data) {
      gsm.set(new MapSelectScreen(gsm, players, gameMode, gameData));
    }
  }

  /**
   * A ButtonAction that changes the current screen to a MapSelectScreen.
   * 
   * @author Brandon Hua
   */

  public class ToPlayerSelect implements ButtonAction {
    @Override
    public void execute(Object data) {
      gsm.set(new PlayerSelectScreen(gsm, gameMode));
    }
  }

}
