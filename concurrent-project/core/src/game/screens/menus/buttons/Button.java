package game.screens.menus.buttons;

import com.badlogic.gdx.graphics.Texture;

import game.screens.menus.TransitionHandler;

/**
 * A button is used for menu interaction within a game.
 * 
 * @author Brandon Hua
 */

public class Button {

  private String text;
  private int xpos;
  private int ypos;
  private int width;
  private int height;
  private boolean selected;
  private ButtonAction command;
  private boolean confirmed;
  private TransitionHandler trandler;

  private boolean custom = false;
  private Texture defaultTexture;
  private Texture selectedTexture;

  /**
   * The interface for the action made when clicking a button.
   * 
   * @author Brandon Hua
   */

  public interface ButtonAction {
    /**
     * The execute method performs the action of the button.
     * 
     * @param data Data to be used in the action.
     */
    public void execute(Object data);
  }

  /**
   * A method that executes a button's action.
   * 
   * @param data Potential parameters to be used as part of the execution.
   */

  public void performAction(Object data) {
    command.execute(data);
    selected = false;
    confirmed = false;
  }

  /**
   * A constructor for the Button class without a transition.
   * 
   * @param text    The text to be shown over the button.
   * @param x       The X position of the button.
   * @param y       The Y position of the button.
   * @param width   The width of the button.
   * @param height  The height of the button.
   * @param command The ButtonAction to be enacted when pressed.
   */

  public Button(String text, int x, int y, int width, int height, ButtonAction command) {
    this.text = text;
    this.xpos = x;
    this.ypos = y;
    this.width = width;
    this.height = height;
    this.command = command;
    confirmed = false;
    trandler = new TransitionHandler("none");
  }

  /**
   * A constructor for the Button class with a transition.
   * 
   * @param text     The text to be shown over the button.
   * @param x        The X position of the button.
   * @param y        The Y position of the button.
   * @param width    The width of the button.
   * @param height   The height of the button.
   * @param command  The ButtonAction to be enacted when pressed.
   * @param trandler A transition handler that runs after a button is pressed.
   */

  public Button(String text, int x, int y, int width, int height, ButtonAction command,
      TransitionHandler trandler) {
    this.text = text;
    this.xpos = x;
    this.ypos = y;
    this.width = width;
    this.height = height;
    this.command = command;
    confirmed = false;
    this.trandler = trandler;
  }

  /**
   * A constructor for the Button class with a transition.
   * 
   * @param text                The text to be shown over the button.
   * @param x                   The X position of the button.
   * @param y                   The Y position of the button.
   * @param width               The width of the button.
   * @param height              The height of the button.
   * @param command             The ButtonAction to be enacted when pressed.
   * @param trandler            A transition handler that runs after a button is
   *                            pressed.
   * @param defaultTextureName  The name of the unselected button texture.
   * @param selectedTextureName The name of the selected button texture.
   */

  public Button(String text, int x, int y, int width, int height, ButtonAction command,
      TransitionHandler trandler, String defaultTextureName, String selectedTextureName) {
    this.text = text;
    this.xpos = x;
    this.ypos = y;
    this.width = width;
    this.height = height;
    this.command = command;
    confirmed = false;
    this.trandler = trandler;
    defaultTexture = new Texture(defaultTextureName);
    selectedTexture = new Texture(selectedTextureName);
    custom = true;
  }

  /**
   * A method that calculates the left most side of the button.
   * 
   * @return An int value of the minimum x boundary.
   */

  public int minX() {
    int minX = xpos - width / 2;
    return minX;
  }

  /**
   * A method that calculates the right most side of the button.
   * 
   * @return An int value of the maximum x boundary.
   */

  public int maxX() {
    int maxX = xpos + width / 2;
    return maxX;
  }

  /**
   * A method that calculates the lowest side of the button.
   * 
   * @return An int value of the minimum y boundary.
   */

  public int minY() {
    int minY = ypos - height / 2;
    return minY;
  }

  /**
   * A method that calculates the highest side of the button.
   * 
   * @return An int value of the maximum y boundary.
   */

  public int maxY() {
    int maxY = ypos + height / 2;
    return maxY;
  }

  /**
   * A method that checks to see if the given position of the cursor is
   * overlapping the parts of the button.
   * 
   * @param mouseX The x position of the mouse.
   * @param mouseY The y position of the mouse.
   * 
   * @return A boolean for whether or not the mouse is over the button.
   */

  public boolean mousedOver(int mouseX, int mouseY) {
    setSelected(false);
    if (minX() < mouseX && mouseX < maxX()) {
      if (minY() < mouseY && mouseY < maxY()) {
        setSelected(true);
        return true;
      }
    }
    return false;
  }

  /**
   * An equals method that checks the values of the other objects to see whether
   * or not they are the same.
   * 
   * <p>
   * A button is returned as equal if the: Text, position and sizes are the same.
   */

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Button other = (Button) obj;

    return this.text.equals(other.getText()) && this.xpos == other.getXpos()
        && this.ypos == other.getYpos() && this.width == other.getWidth()
        && this.height == other.getHeight();
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public void setConfirmed(boolean conf) {
    this.confirmed = conf;
  }

  public TransitionHandler getTrandler() {
    return trandler;
  }

  public String getText() {
    return text;
  }

  public int getXpos() {
    return xpos;
  }

  public int getYpos() {
    return ypos;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public boolean isSelected() {
    return selected;
  }

  public boolean isCustom() {
    return custom;
  }

  public Texture getDefaultTexture() {
    return defaultTexture;
  }

  public Texture getSelectedTexture() {
    return selectedTexture;
  }
  
  public void dispose() {
    if(isCustom()) {
      defaultTexture.dispose();
      selectedTexture.dispose();
    }

  }

}
