package game.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.input.TextField;
import game.input.TextFieldList;

import java.util.ArrayList;

/**
 * The TextFieldListRenderer is the renderer class for the TextFieldList.
 * 
 * @author Brandon Hua
 */

public class TextFieldListRenderer {

  private static Texture box;

  public static void load() {
    box = new Texture("box_back.png");
  }

  /**
   * The render method to draw the TextFieldList.
   * 
   * @param sb  The SpriteBatch used to draw.
   * @param tfl The TextFieldList to be drawn.
   */

  public static void render(SpriteBatch sb, TextFieldList tfl) {
    ArrayList<TextField> fieldList = tfl.getList();
    for (TextField t : fieldList) {
      sb.begin();
      sb.draw(box, t.getx() - t.getWidth() / 2, t.gety() - t.getHeight() 
          / 2, t.getWidth(), t.getHeight());
      sb.end();
      String text = t.getText();
      int textHeight = t.getHeight();
      int textWidth = textHeight * text.length();
      if (textWidth > t.getWidth()) {
        textWidth = t.getWidth();
        textHeight = textWidth / text.length();
      }
      TextRenderer.print(sb, t.getText(), "white", t.getx(), t.gety(), textWidth, textHeight);
    }
  }

  public void dispose() {
    box.dispose();
  }

}
