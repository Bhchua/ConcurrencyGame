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

import network.threads.NetworkClientThread;

/**
 * The Screen class for the Network Client. This class allows the user to
 * connect to other game hosts.
 * 
 * @author Brandon Hua
 */

public class NetworkClientScreen extends Screen {

  private ButtonList buttons;
  private ButtonListRenderer buttonRend;

  private NetworkClientThread client;

  private boolean blistChanged = false;

  private TextFieldList tfl;
  private TextField domainField;
  private TextField portField;

  private KeyboardInput kinput;

  /**
   * The NetworkClientScreen creates buttons and text fields on creation.
   * 
   * @param gsm The GameScreenManager used to control the screen.
   */

  protected NetworkClientScreen(GameScreenManager gsm) {
    super(gsm);
    background = new Texture("bg_blank.png");
    thandler = new TransitionHandler("fade_in");
    thandler.start();
    initButtons();

    tfl = new TextFieldList();
    portField = new TextField(Core.width / 2,  2 * Core.height / 3, Core.width / 3, Core.height / 12,
        "7777", 5, true);
    domainField = new TextField(Core.width / 2,  Core.height / 2, Core.width / 3,
        Core.height / 12, "LOCALHOST", 64, false);
    tfl.addToList(portField);
    tfl.addToList(domainField);

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

    buttons.addToList(new Button("CONNECT", Core.width / 2, Core.height / 4, Core.width / 4,
        Core.width / 16, new Connect()));

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
    startGame();
    correctPort();
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

  /**
   * A method that changes the button list when a connection has been made.
   */

  public void updateButtons() {
    if (client != null && client.isConnected() && !blistChanged) {
      buttons.getList().remove(1);
      blistChanged = true;
    }
  }

  /**
   * A method to change the topmost screen in the gsm to a new networked game.
   */

  public void startGame() {
    if (client != null && client.isConnected()) {
      String[] input = client.getInputString().split(" ");
      if (input[0].equals("start")) {
        int time = Integer.valueOf(input[1]);
        String level = input[2];
        try {
          gsm.set(new GameScreen(gsm, time, "easy", level, client));
        } catch (IOException e) {
          e.printStackTrace();
          GlobalErrors.setError("Could not load networked game");
        }
      }
    }
  }

  @Override
  public void render(SpriteBatch sb) {
    sb.begin();
    sb.draw(background, 0, 0, Core.width, Core.height);
    sb.end();

    drawConnection(sb);

    TextRenderer.print(sb, "DOMAIN AND PORT:", "white", Core.width / 2, 11 * Core.height / 12,
        Core.width / 3, Core.height / 16);
    TextFieldListRenderer.render(sb, tfl);

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
    if (client != null) {
      connectStatus = "CONNECTING...";
      if (client.isConnected()) {
        connectStatus = "CONNECTED";
        String choice = "The host is configuring the game...";
        int chWidth = Core.width / 2;
        int chHeight = chWidth / choice.length();
        TextRenderer.print(sb, choice, "white", Core.width / 2, Core.height / 9, chWidth, chHeight);
      }
    }
    int csWidth = Core.width / 3;
    int csHeight = csWidth / connectStatus.length();
    TextRenderer.print(sb, connectStatus, "white", Core.width / 2, Core.height / 6, csWidth,
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

  public class ToNetworkMenu implements ButtonAction {
    @Override
    public void execute(Object data) {
      if (client != null) {
        client.finish();
      }
      gsm.set(new NetworkSelectScreen(gsm));
    }
  }

  /**
   * A ButtonAction that starts a NetworkClientThread.
   * 
   * @author Brandon Hua
   */

  public class Connect implements ButtonAction {
    @Override
    public void execute(Object data) {
      if (client == null) {
        String domain = domainField.getText();
        if (domain == "") {
          domain = "localhost";
        }
        int port = 1;
        if (portField.getText().length() > 0) {
          port = Integer.valueOf(portField.getText());
        }
        client = new NetworkClientThread(domain, port);
        client.start();
      }

    }
  }

}
