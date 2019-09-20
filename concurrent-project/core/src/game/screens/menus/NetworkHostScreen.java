package game.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import error.GlobalErrors;
import game.Core;
import game.input.GlobalInput;
import game.input.KeyboardInput;
import game.input.TextField;
import game.input.TextFieldList;
import game.renderer.ButtonListRenderer;
import game.renderer.TextFieldListRenderer;
import game.renderer.TextRenderer;
import game.screens.GameScreenManager;
import game.screens.Screen;
import game.screens.menus.buttons.Button;
import game.screens.menus.buttons.Button.ButtonAction;
import game.screens.menus.buttons.ButtonList;
import game.screens.shooter.GameScreen;
import game.screens.threads.MenuThread;
import java.io.IOException;
import network.threads.NetworkHostThread;

/**
 * The NetworkHostScreen is a Screen class that creates a network host threads
 * and allows users to set their ports to host a game session.
 * 
 * @author Brandon Hua
 */

public class NetworkHostScreen extends Screen {

  private ButtonList buttons;
  private ButtonListRenderer buttonRend;

  private NetworkHostThread host;

  private TextFieldList tfl;
  private TextField portField;

  private KeyboardInput kinput;

  private String menuState = "not connected";

  private int time = 30000;
  private String map = "test_level.lvl";

  /**
   * The NetworkHostScreen creates buttons and text fields on creation.
   * 
   * @param gsm The GameScreenManager used to control the screen.
   */

  protected NetworkHostScreen(GameScreenManager gsm) {
    super(gsm);
    background = new Texture("bg_blank.png");
    thandler = new TransitionHandler("fade_in");
    thandler.start();
    initButtons();

    tfl = new TextFieldList();
    portField = new TextField(Core.width / 2, 2 * Core.height / 3, Core.width / 3, Core.height / 12,
        "7777", 5, true);
    tfl.addToList(portField);

    kinput = new KeyboardInput();
    Gdx.input.setInputProcessor(kinput);
  }

  /**
   * A method to initialise the button list.
   */

  public void initButtons() {
    buttons = new ButtonList();

    buttons.addToList(new Button("back", Core.width / 10, 11 * Core.height / 12, Core.width / 6,
        Core.width / 12, new ToNetworkMenu(), new TransitionHandler("wipe"), "button_back.png",
        "button_back_selected.png"));

    buttons.addToList(new Button("START HOST", Core.width / 2, Core.height / 2, Core.width / 4,
        Core.width / 16, new StartServer()));

    buttonRend = new ButtonListRenderer(buttons);
    mthread = new MenuThread(buttons);
  }

  @Override
  public void handleInput() {
    GlobalInput.mouseX = Gdx.input.getX();
    GlobalInput.mouseY = Core.height - Gdx.input.getY();
    GlobalInput.confirm = Gdx.input.justTouched();
    tfl.detectKeyInput();
    tfl.detectMouseInput();
    buttons.performAction();
  }

  @Override
  public void update() {
    handleInput();
    updateButtons();
    correctPort();
  }

  /**
   * A method that changes the button list when a connection has been made.
   */

  public void updateButtons() {

    if (host != null && host.isConnected() && menuState.equals("not connected")) {
      buttons.getList().remove(1);
      buttons.addToList(new Button("1 MIN", Core.width / 2, 3 * Core.height / 4, Core.width / 4,
          Core.height / 8, new SetTime(60000), new TransitionHandler("none")));

      buttons.addToList(new Button("3 MINS", Core.width / 2, Core.height / 2, Core.width / 4,
          Core.height / 8, new SetTime(180000), new TransitionHandler("none")));

      buttons.addToList(new Button("5 MINS", Core.width / 2, Core.height / 4, Core.width / 4,
          Core.height / 8, new SetTime(300000), new TransitionHandler("none")));
      menuState = "time select";
    }

    if (menuState.equals("selected time")) {
      buttons.getList().remove(1);
      buttons.getList().remove(1);
      buttons.getList().remove(1);
      buttons.addToList(new Button("town", Core.width / 2, Core.height / 2, Core.width / 6,
          Core.width / 3, new SetMap("town.lvl"), new TransitionHandler("wipe"),
          "button_town.png", "button_town_selected.png"));
      menuState = "map select";
    }

    if (menuState.equals("selected map")) {
      buttons.getList().remove(1);
      buttons.addToList(new Button("START GAME", Core.width / 2, Core.height / 2, Core.width / 4,
          Core.width / 16, new StartGame(), new TransitionHandler("wipe")));
      menuState = "start game";
    }
  }

  /**
   * A method to round the port back down if it exceeds the maximum value of
   * 65535.
   */

  public void correctPort() {
    if (portField.getText().length() > 0) {
      int portNum = Integer.valueOf(portField.getText());
      if (portNum > 65535) {
        portField.setText("65535");
      }
    }
  }

  @Override
  public void render(SpriteBatch sb) {
    sb.begin();
    sb.draw(background, 0, 0, Core.width, Core.height);
    sb.end();

    if (menuState.equals("not connected")) {
      drawConnection(sb);
      TextRenderer.print(sb, "SERVER PORT:", "white", Core.width / 2, 3 * Core.height / 4,
          Core.width / 4, Core.height / 16);
      TextFieldListRenderer.render(sb, tfl);
    }

    buttonRend.render(sb);
    if (buttons.isConfirmed()) {
      buttons.getTransitionList().render(sb);
    }

    thandler.render(sb);
  }

  /**
   * A method that displays the connection status.
   * 
   * @param sb The SpriteBatch used to draw.
   */

  private void drawConnection(SpriteBatch sb) {
    String connectStatus = "NOT CONNECTED";
    if (host != null) {
      connectStatus = "SERVER STARTED";
      if (host.isConnected()) {
        connectStatus = "CONNECTED";
      }
    }
    int csHeight = Core.height / 16;
    int csWidth = csHeight * connectStatus.length();
    TextRenderer.print(sb, connectStatus, "white", Core.width / 2, Core.height / 4, csWidth,
        csHeight);
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
   * A ButtonAction that returns the user back to the Network selection menu and
   * stops any network threads that may be running.
   * 
   * @author Brandon Hua
   *
   */

  public class ToNetworkMenu implements ButtonAction {
    @Override
    public void execute(Object data) {
      if (host != null) {
        host.finish();
      }
      gsm.set(new NetworkSelectScreen(gsm));
    }
  }

  /**
   * A ButtonAction that starts a NetworkHostThread.
   * 
   * @author Brandon Hua
   */

  public class StartServer implements ButtonAction {
    @Override
    public void execute(Object data) {
      try {
        if (host == null) {
          int portNum = 1;
          if (portField.getText().length() > 0) {
            portNum = Integer.valueOf(portField.getText());
          }
          host = new NetworkHostThread(portNum);
          host.start();
        }
      } catch (IOException e) {
        e.printStackTrace();
        if (host != null) {
          host.finish();
        }
        String error = e.getClass().getSimpleName() + ": " + e.getMessage();
        GlobalErrors.setError(error);
      }
    }
  }

  /**
   * A ButtonAction that starts the networked game.
   * 
   * @author Brandon Hua
   */

  public class StartGame implements ButtonAction {
    @Override
    public void execute(Object data) {
      try {
        String outputString = "start " + time + " " + map;
        host.setOutputString(outputString);
        gsm.set(new GameScreen(gsm, time, "easy", map, host));
      } catch (Exception e) {
        e.printStackTrace();
        GlobalErrors.setError("Failed to load networked game");
      }
    }
  }

  /**
   * A ButtonAction that sets the time of the game.
   * 
   * @author Brandon Hua
   */

  public class SetTime implements ButtonAction {

    private int gameTime;

    public SetTime(int time) {
      gameTime = time;
    }

    public void execute(Object data) {
      time = gameTime;
      menuState = "selected time";
    }
  }

  /**
   * A ButtonAction that sets the map to be played.
   * 
   * @author Brandon Hua
   */

  public class SetMap implements ButtonAction {

    public String gameMap;

    public SetMap(String map) {
      gameMap = map;
    }

    public void execute(Object data) {
      map = gameMap;
      menuState = "selected map";
    }
  }

}
