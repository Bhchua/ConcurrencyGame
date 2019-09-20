package game.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.screens.menus.buttons.Button;
import game.screens.menus.buttons.ButtonList;

import java.util.ArrayList;

/**
 * The Renderer class for the ButtonList object.
 * 
 * @author Brandon Hua
 *
 */

public class ButtonListRenderer {

  private Texture[] buttons;
  private ButtonList buttonList;

  /**
   * The ButtonListRenderer constructor initialises the menu button images.
   * 
   * @param buttonList The ButtonList the renderer is in charge of.
   */

  public ButtonListRenderer(ButtonList buttonList) {
    buttons = new Texture[2];
    buttons[0] = new Texture("menu_button.png");
    buttons[1] = new Texture("menu_button_selected.png");

    this.buttonList = buttonList;
  }

  /**
   * The Render method draws the ButtonList to screen.
   * 
   * @param sb The SpriteBatch used to draw.
   */

  public void render(SpriteBatch sb) {
    ArrayList<Button> list = buttonList.getList();
    if (buttonList.isActive()) {
      for (int i = 0; i < list.size(); i++) {
        Button button = list.get(i);
        int selected = (button.isSelected()) ? 0 : 1;
        int width = button.getWidth();
        int height = button.getHeight();
        int x = button.getXpos();
        int y = button.getYpos();
        if (button.isCustom()) {
          Texture[] textures = new Texture[] { button.getSelectedTexture(),
              button.getDefaultTexture() };
          sb.begin();
          sb.draw(textures[selected], x - width / 2, y - height / 2, width, height);
          sb.end();
        } else {
          String font;
          if (button.isSelected()) {
            font = "black";
          } else {
            font = "white";
          }
          sb.begin();
          sb.draw(buttons[selected], x - width / 2, y - height / 2, width, height);
          sb.end();

          String text = button.getText();
          int textw = (int) (0.9 * width);
          int texth = textw / text.length();
          TextRenderer.print(sb, text, font, x, y, textw, texth);
        }
      }
    }
  }

  /**
   * A method to clear elements from memory.
   */

  public void dispose() {
    for (int i = 0; i < buttons.length; i++) {
      buttons[i].dispose();
    }
  }

}
