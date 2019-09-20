package game.input;

/**
 * The TextField class is used to capture and store user input in a text field.
 * 
 * @author Brandon Hua
 */

public class TextField {

  private final int XPOS;
  private final int YPOS;
  private final int WIDTH;
  private final int HEIGHT;
  private final int MAX;
  private final boolean NUMONLY;
  private String text;

  /**
   * A TextField object must be initialised with it's position in the GUI window.
   * 
   * @param x       The x position of the TextField.
   * @param y       The y position of the TextField.
   * @param width   The width position of the TextField.
   * @param height  The height position of the TextField.
   * @param text    The default text of a text field.
   * @param max     The max number of characters the string can be.
   * @param numOnly Whether or not letters can be used in the field.
   */

  public TextField(int x, int y, int width, int height, String text, int max, boolean numOnly) {
    this.XPOS = x;
    this.YPOS = y;
    this.WIDTH = width;
    this.HEIGHT = height;
    this.text = text;
    this.MAX = max;
    this.NUMONLY = numOnly;
  }

  /**
   * A TextField object must be initialised with it's position in the GUI window.
   * 
   * @param x       The x position of the TextField.
   * @param y       The y position of the TextField.
   * @param width   The width position of the TextField.
   * @param height  The height position of the TextField.
   * @param max     The max number of characters the string can be.
   * @param numOnly Whether or not letters can be used in the field.
   */

  public TextField(int x, int y, int width, int height, int max, boolean numOnly) {
    this.XPOS = x;
    this.YPOS = y;
    this.WIDTH = width;
    this.HEIGHT = height;
    this.text = " ";
    this.MAX = max;
    this.NUMONLY = numOnly;
  }

  /**
   * A method that removes the last character of the string.
   */

  public void backspace() {
    if (text != null && text.length() > 0) {
      text = text.substring(0, text.length() - 1);
    }
  }

  /**
   * A method to append a string to the text of the field.
   * 
   * @param s The String to be added.
   */

  public void addToText(String s) {
    if (text.length() < MAX && (text.length() + s.length()) <= MAX) {
      text = text + s;
    }

  }

  public String getText() {
    return text;
  }

  public int getx() {
    return XPOS;
  }

  public int gety() {
    return YPOS;
  }

  public int getMinx() {
    return XPOS - (WIDTH / 2);
  }

  public int getMaxx() {
    return XPOS + (WIDTH / 2);
  }

  public int getMiny() {
    return YPOS - (HEIGHT / 2);
  }

  public int getMaxy() {
    return YPOS + (HEIGHT / 2);
  }

  public int getWidth() {
    return WIDTH;
  }

  public int getHeight() {
    return HEIGHT;
  }

  public void setText(String text) {
    this.text = text;
  }
  
  public boolean isNumOnly() {
    return NUMONLY;
  }
}
