package game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import error.GlobalErrors;
import game.entities.structures.buildings.StructLoader;
import game.renderer.TextFieldListRenderer;
import game.renderer.TextRenderer;
import game.screens.GameScreenManager;
import game.screens.menus.MainMenuScreen;

/**
 * The core class is where most of the application variables are determined and
 * points to the first screen to be loaded when the application is started.
 * 
 * @author Mostly automatically generated code from the LibGDX library.
 *
 */

public class Core extends ApplicationAdapter {

  public static int width = 1920;
  public static int height = 1080;

  public static long time = System.currentTimeMillis();
  public static long lastRenderTime = System.currentTimeMillis();
  public static long deltaTime;

  public static final String TITLE = "Shooter";

  private static GameScreenManager gsm;
  private SpriteBatch sb;

  public static void setWidth(int w) {
    width = w;
  }

  public static void setHeight(int h) {
    height = h;
  }

  @Override
  public void create() {
    sb = new SpriteBatch();
    gsm = new GameScreenManager();
    TextRenderer.load();
    TextFieldListRenderer.load();
    gsm.push(new MainMenuScreen(gsm));
    Pixmap pm = new Pixmap(Gdx.files.internal("mouse.png"));
    Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
    StructLoader.loadStructs();
    pm.dispose();
  }

  @Override
  public void render() {
    try {
      Gdx.gl.glClearColor(1, 0, 0, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      time = System.currentTimeMillis();
      deltaTime = time - lastRenderTime;
      lastRenderTime = time;
      gsm.update();
      gsm.render(sb);
      observeErrors();
    } catch (Exception e) {
      e.printStackTrace();
      String error = e.getClass().getSimpleName() + ": " + e.getMessage();
      GlobalErrors.setError(error);
    }
  }

  @Override
  public void dispose() {
    sb.dispose();
  }

  public static void displayError(String error) {
    gsm.set(new MainMenuScreen(gsm, error));
  }

  /**
   * A method that simply uses the hasError method to check if an error has been
   * flagged and then displays it accordingly.
   */

  public void observeErrors() {
    if (GlobalErrors.hasError()) {
      displayError(GlobalErrors.getErrorMessage());
      GlobalErrors.resetError();
    }
  }

}
