package game.screens.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Core;
import game.input.GlobalInput;
import game.renderer.ButtonListRenderer;
import game.screens.GameScreenManager;
import game.screens.Screen;
import game.screens.menus.buttons.Button;
import game.screens.menus.buttons.Button.ButtonAction;
import game.screens.menus.buttons.ButtonList;
import game.screens.threads.MenuThread;

/**
 * A help screen that shows a variety of information about the game.
 * 
 * @author Brandon Hua
 */

public class HelpScreen extends Screen {

  private ButtonList buttons;
  private ButtonListRenderer buttonRend;
  private Texture[] helpList;
  private int curHelp = 0;

  /**
   * The HelpScreen intialises a list of Textures that hold the help information
   * and the buttons to navigate between them.
   * 
   * @param gsm The GameScreenManager used to control the screen.
   */

  protected HelpScreen(GameScreenManager gsm) {
    super(gsm);
    helpList = new Texture[] { new Texture("control_info.png"), new Texture("game_info.png"),
        new Texture("enemy_info.png") };

    buttons = new ButtonList();

    buttons.addToList(new Button("back", Core.width / 10, 11 * Core.height / 12, Core.width / 6,
        Core.width / 12, new ToMainMenu(), new TransitionHandler("wipe"), "button_back.png",
        "button_back_selected.png"));

    buttons.addToList(new Button("NEXT", 5 * Core.width / 6, Core.height / 5, Core.width / 8,
        Core.height / 16, new NextHelp()));

    buttons.addToList(new Button("PREV", Core.width / 6, Core.height / 5, Core.width / 8,
        Core.height / 16, new PrevHelp()));

    buttonRend = new ButtonListRenderer(buttons);
    mthread = new MenuThread(buttons);

    thandler = new TransitionHandler("fade_in");
    thandler.start();
  }

  @Override
  public void handleInput() {
    GlobalInput.mouseX = Gdx.input.getX();
    GlobalInput.mouseY = Core.height - Gdx.input.getY();
    GlobalInput.confirm = Gdx.input.justTouched();
    buttons.performAction();// TODO Auto-generated method stub
  }

  @Override
  public void update() {
    handleInput();

  }

  @Override
  public void render(SpriteBatch sb) {
    sb.begin();
    sb.draw(helpList[curHelp], 0, 0, Core.width, Core.height);
    sb.end();

    buttonRend.render(sb);

    if (buttons.isConfirmed()) {
      buttons.getTransitionList().render(sb);
    }

    thandler.render(sb);
  }

  @Override
  public void dispose() {
    for (int i = 0; i < helpList.length; i++) {
      helpList[i].dispose();
    }
    thandler.dispose();
    mthread.finish();
    buttonRend.dispose();
    buttons.dispose();
  }

  /**
   * A ButtonAction that changes the screen back to a MainMenuScreen.
   * 
   * @author Brandon Hua
   */

  public class ToMainMenu implements ButtonAction {
    @Override
    public void execute(Object data) {
      gsm.set(new MainMenuScreen(gsm));
    }
  }

  /**
   * A ButtonAction that increments the curHelp variable.
   * 
   * @author Brandon Hua
   */

  public class NextHelp implements ButtonAction {
    @Override
    public void execute(Object data) {
      curHelp = (curHelp + 1) % helpList.length;
    }
  }

  /**
   * A ButtonAction that decrements the curHelp variable.
   * 
   * @author Brandon Hua
   */

  public class PrevHelp implements ButtonAction {
    @Override
    public void execute(Object data) {
      curHelp = (curHelp - 1);
      if (curHelp < 0) {
        curHelp = helpList.length - 1;
      }
    }
  }

}
