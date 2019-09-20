package game.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import game.CameraVariables;

/**
 * The TextRenderer class is a renderer that allows for custom font to be
 * displayed on screen.
 * 
 * @author Brandon Hua
 *
 */

public class TextRenderer {

  private final static int SHEETCOLS = 16;
  private final static int SHEETROWS = 16;
  private final static int CAPITALOFFSET = 65;
  private final static int LOWEROFFSET = 97;
  private final static int NUMBEROFFSET = 48;
  private final static int COMMA = 44;
  private final static int FULLSTOP = 46;
  private final static int SEMICOLON = 59;
  private final static int COLON = 58;

  private static Texture whiteFont;
  private static Texture blackFont;
  private static Texture blackBack;

  private static TextureRegion[] blackCharacters;
  private static TextureRegion[] whiteCharacters;

  /**
   * The constructor for a text renderer that initialises all the custom fonts in
   * png files.
   */

  public static void load() {
    whiteFont = new Texture("font_white.png");
    blackFont = new Texture("font_black.png");
    blackBack = new Texture("blank.png");

    TextureRegion[][] wtmp = TextureRegion.split(whiteFont, whiteFont.getWidth() / SHEETCOLS,
        whiteFont.getHeight() / SHEETROWS);

    TextureRegion[][] btmp = TextureRegion.split(blackFont, blackFont.getWidth() / SHEETCOLS,
        blackFont.getHeight() / SHEETROWS);

    whiteCharacters = new TextureRegion[SHEETCOLS * SHEETROWS];
    blackCharacters = new TextureRegion[SHEETCOLS * SHEETROWS];

    int counter = 0;
    for (int i = 0; i < SHEETCOLS; i++) {
      for (int j = 0; j < SHEETROWS; j++) {
        whiteCharacters[counter] = wtmp[i][j];
        blackCharacters[counter] = btmp[i][j];
        counter++;
      }
    }
  }

  /**
   * A method that converts a string into an int array of ascii positions.
   * 
   * @param text The text to be translated.
   * @return The array of ascii numbers from the string.
   */

  public static int[] stringToAscii(String text) {
    char[] chars = text.toCharArray();
    int[] charCodes = new int[chars.length];
    for (int i = 0; i < chars.length; i++) {
      charCodes[i] = (int) (chars[i]);
    }
    return charCodes;
  }

  /**
   * A method that takes the ascii character codes and offsets them to point to
   * characters listed in the custom font.
   * 
   * @param charCodes The ascii codes of a string.
   * @return The offset codes for use in the renderer.
   */

  public static int[] offsetToRegion(int[] charCodes) {
    int[] offset = new int[charCodes.length];
    Offset[] offsetList = new Offset[] { new CapitalOffset(), new LowerOffset(), new NumOffset(),
        new SymOffset() };
    for (int i = 0; i < charCodes.length; i++) {
      offset[i] = 127;

      for (Offset o : offsetList) {
        if (o.inRange(charCodes[i])) {
          offset[i] = o.getOffset(charCodes[i]);
        }
      }

    }
    return offset;
  }

  /**
   * An interface that compares an Ascii code to the range of the applicable
   * characters before returning the offset.
   * 
   * @author Brandon Hua
   */

  public interface Offset {

    /**
     * A method that checks to see if the given character code is in range of the
     * offset list.
     * 
     * @param charCode The ascii code of the character to be checked.
     * @return A boolean value for whether or not it is in range.
     */
    public boolean inRange(int charCode);

    /**
     * A simple getter to return the offset value.
     * 
     * @param charCode The value of the character to offset.
     * @return The int offset of the character.
     */

    public int getOffset(int charCode);
  }

  /**
   * The offset class for capital characters.
   * 
   * @author Brandon Hua
   */

  public static class CapitalOffset implements Offset {

    @Override
    public boolean inRange(int charCode) {
      return (CAPITALOFFSET <= charCode && charCode < CAPITALOFFSET + 26);
    }

    @Override
    public int getOffset(int charCode) {
      return charCode - CAPITALOFFSET;
    }

  }

  /**
   * The offset class for lower case characters.
   * 
   * @author Brandon Hua
   */

  public static class LowerOffset implements Offset {

    @Override
    public boolean inRange(int charCode) {
      return (LOWEROFFSET <= charCode && charCode < LOWEROFFSET + 26);
    }

    @Override
    public int getOffset(int charCode) {
      return charCode - LOWEROFFSET + 26;
    }
  }

  /**
   * The offset class for numerical characters.
   * 
   * @author Brandon Hua
   */

  public static class NumOffset implements Offset {

    @Override
    public boolean inRange(int charCode) {
      return (NUMBEROFFSET <= charCode && charCode < NUMBEROFFSET + 10);
    }

    @Override
    public int getOffset(int charCode) {
      return charCode - NUMBEROFFSET + 52;
    }
  }

  /**
   * The offset class for symbols.
   * 
   * @author Brandon Hua
   */

  public static class SymOffset implements Offset {

    private int[][] symList = new int[][] { { FULLSTOP, 62 }, { COMMA, 63 }, { COLON, 64 },
        { SEMICOLON, 65 } };
    private int index;

    @Override
    public boolean inRange(int charCode) {
      for (int i = 0; i < symList.length; i++) {
        if (symList[i][0] == charCode) {
          index = i;
          return true;
        }
      }
      return false;
    }

    @Override
    public int getOffset(int charCode) {
      return symList[index][1];
    }
  }

  /**
   * A method that converts a string to it's offset codes.
   * 
   * @param text The string to be translated
   * @return The offset character codes of the string.
   */

  public static int[] stringToCode(String text) {
    int[] ascii = stringToAscii(text);
    int[] offset = offsetToRegion(ascii);
    return offset;
  }

  /**
   * A method that sequentially prints out a string on the viewport.
   * 
   * @param sb     The sprite batch used for rendering.
   * @param text   The text to be printed.
   * @param font   The style of font to be used.
   * @param xpos   The x position of the middle of the string on screen.
   * @param ypos   The y position of the middle of the string on screen.
   * @param width  The width of the string as a whole.
   * @param height The height of the string as a whole.
   */

  public static void print(SpriteBatch sb, String text, String font, int xpos, int ypos, int width,
      int height) {
    if (text != null && text.length() > 0) {
      int[] textCodes = stringToCode(text);
      double spacer = 0.9;
      double charWidth = width / textCodes.length;
      double curX = xpos - ((width*spacer)/2 - charWidth/2);
      sb.begin();
      for (int i = 0; i < textCodes.length; i++) {
        render(sb, textCodes[i], font, (int) curX, ypos - height / 2, (int) charWidth, height);
        curX = (curX + (charWidth * spacer));
      }
      sb.end();
    }
  }

  /**
   * A class to convert and print a Long to time.
   * 
   * @param sb     The SpriteBatch used to draw.
   * @param time   The time intended to be converted (in milliseconds).
   * @param font   The font the time should be displayed in.
   * @param xpos   The position of the font on the x-axis.
   * @param ypos   The position of the font on the y-axis.
   * @param width  The width of the message.
   * @param height The height of the message.
   */

  public static void printTime(SpriteBatch sb, long time, String font, int xpos, int ypos,
      int width, int height) {
    long mins = time / 60000;
    double secs = (double) (time - (mins * 60000)) / 1000;
    String secString = String.valueOf(secs);
    String[] secSplit = secString.split("\\.");
    if (secSplit[0].length() < 2) {
      secString = "0" + secString;
    }
    if (secString.length() > 4) {
      secString = secString.substring(0, 5);
    }
    if (secString.length() < 4) {
      for (int i = 0; i < (4 - secString.length()); i++) {
        secString = secString + "0";
      }

    }
    String allTime = mins + "m " + secString + "s";
    print(sb, allTime, font, xpos, ypos, width, height);
  }

  /**
   * A method for rendering individual characters using their character codes.
   * 
   * @param sb       The sprite batch used for rendering.
   * @param charCode The offset codes for the character to be rendered.
   * @param font     The style of font to use.
   * @param xpos     The x position of the center of a character
   * @param ypos     The y position of the center of a character
   * @param width    The width of a character.
   * @param height   The height of a character.
   */

  public static void render(SpriteBatch sb, int charCode, String font, int xpos, int ypos,
      int width, int height) {
    if (charCode < 256) {
      TextureRegion[] curChars = whiteCharacters;
      Boolean backing = false;
      if (font.equals("black")) {
        curChars = blackCharacters;
      }
      if (font.equals("white_back")) {
        backing = true;
      }
      if (backing) {
        sb.draw(blackBack, xpos - width / 2, ypos, width, height);
      }
      sb.draw(curChars[charCode], xpos - width / 2, ypos, width, height);
    }
  }

  public void dispose() {
    whiteFont.dispose();
    blackFont.dispose();
  }

  /**
   * A method that returns the width of the characters of a string to allow for
   * proper scaling in a game view.
   * 
   * @param string The string being used.
   * @param height The height of the string.
   * 
   * @return The width of the characters.
   */

  public static int standardGameWidth(String string, int height) {
    int width = (int) (string.length() * height * CameraVariables.zoom * 1
        / CameraVariables.xratio);
    return width;
  }

  /**
   * A method that returns the height of a string that has been properly scaled.
   * 
   * @param height The height of the string.
   * 
   * @return The height of the string.
   */

  public static int standardGameHeight(int height) {
    int standHeight = (int) (height * CameraVariables.zoom * 1 / CameraVariables.yratio);
    return standHeight;
  }

}
