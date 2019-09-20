package game.screens.menus.buttons;

import game.input.GlobalInput;
import game.screens.menus.TransitionList;

import java.util.ArrayList;

/**
 * ButtonList holds an ArrayList of Buttons and controls the methods required
 * for interactivity with UI.
 * 
 * @author Brandon Hua
 *
 */

public class ButtonList {

  private ArrayList<Button> buttonList;
  private int currentSelected;
  private int confirmedButton;
  private boolean confirmed;
  private boolean active;
  private TransitionList tlist;

  /**
   * The constructor for the button list method, sets the currently selected
   * button to the 0th item in the list and is set to "active" by default.
   */

  public ButtonList() {
    buttonList = new ArrayList<Button>();
    currentSelected = 0;
    active = true;
    tlist = new TransitionList();
  }

  /**
   * A method to add a button to the list.
   * 
   * @param button The button to be added.
   */

  public void addToList(Button button) {
    if (buttonList.isEmpty()) {
      button.setSelected(true);
    }
    buttonList.add(button);
    tlist.add(button.getTrandler());
  }

  /**
   * A method that checks the GlobalInput values for the mouse positions to check
   * if any of the buttons in the list overlap the cursor.
   */

  public void mouseSelect() {
    for (int i = 0; i < buttonList.size(); i++) {
      if (buttonList.get(i).mousedOver(GlobalInput.mouseX, GlobalInput.mouseY)) {
        currentSelected = i;
      }
    }
  }

  /**
   * A method to change the selected button to the next one in the list.
   */

  public void selectNext() {
    buttonList.get(currentSelected).setSelected(false);
    currentSelected = (currentSelected + 1) % buttonList.size();
    buttonList.get(currentSelected).setSelected(true);
  }

  /**
   * A method to change the selected button to the previous one in the list.
   */

  public void selectPrev() {
    buttonList.get(currentSelected).setSelected(false);
    currentSelected = (currentSelected + (buttonList.size() - 1)) % buttonList.size();
    buttonList.get(currentSelected).setSelected(true);
  }

  /**
   * A method that iterates through the button list and sets the currently
   * confirmed button to the one selected.
   */

  public void confirmAction() {
    if (GlobalInput.confirm) {
      for (int i = 0; i < buttonList.size(); i++) {
        if (buttonList.get(i).isSelected()) {
          confirmedButton = i;
          confirmed = true;
        }
      }
    }
  }

  /**
   * A method that iterates through the button list and performs the action of the
   * currently confirmed button.
   */

  public void performAction() {
    for (int i = 0; i < buttonList.size(); i++) {
      if (buttonList.get(i).isConfirmed()) {
        buttonList.get(i).performAction(buttonList.get(i).getText());
        confirmed = false;
      }
    }
  }

  /**
   * A method that iterates through the button list and performs the action of the
   * currently confirmed button with extra parameters.
   * 
   * @param data Parameters to be used as part of the button action.
   */

  public void performAction(Object data) {
    for (int i = 0; i < buttonList.size(); i++) {
      if (buttonList.get(i).isConfirmed()) {
        buttonList.get(i).performAction(data);
      }
    }
  }

  /**
   * A method that de-confirms the button list and the currently selected button.
   */

  public void reset() {
    confirmed = false;
    for (int i = 0; i < buttonList.size(); i++) {
      if (buttonList.get(i).isConfirmed()) {
        buttonList.get(i).setConfirmed(false);
      }
    }
  }

  public void setConfirmed() {
    buttonList.get(confirmedButton).setConfirmed(true);
  }

  public ArrayList<Button> getList() {
    return buttonList;
  }

  public Button getSelected() {
    return buttonList.get(currentSelected);
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public TransitionList getTransitionList() {
    return tlist;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isActive() {
    return active;
  }
  
  public void dispose() {
    for(Button b: buttonList) {
      b.dispose();
    }
  }

}
