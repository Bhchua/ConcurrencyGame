package game.input;

import java.util.ArrayList;

/**
 * The TextFieldList class holds an array list of TextFields and methods for UI
 * interaction.
 * 
 * @author Brandon Hua
 */

public class TextFieldList {

  private ArrayList<TextField> fieldList;
  private int selected = 0;
  private boolean justEntered = false;

  public TextFieldList() {
    fieldList = new ArrayList<TextField>();
  }

  /**
   * The add to list method adds a TextField to the array list.
   * 
   * @param field The TextField to be added.
   */

  public void addToList(TextField field) {
    fieldList.add(field);
  }

  /**
   * A method that checks the GlobalInputs class and adds the characters that have
   * their input set to true.
   */

  public void detectKeyInput() {
    if (justEntered) {
      justEntered = GlobalInput.allKeysUp();
    } else {
      for (int i = 0; i < 26; i++) {
        if (GlobalInput.letters[i] && !fieldList.get(selected).isNumOnly()) {
          fieldList.get(selected).addToText(Character.toString((char) (i + 65)));
          justEntered = true;
        }
      }
      for (int i = 0; i < 10; i++) {
        if (GlobalInput.numkeys[i]) {
          fieldList.get(selected).addToText(String.valueOf(i));
          justEntered = true;
        }
      }

      if (GlobalInput.backspace) {
        fieldList.get(selected).backspace();
        justEntered = true;
      }

      if (GlobalInput.period && !fieldList.get(selected).isNumOnly()) {
        fieldList.get(selected).addToText(".");
        justEntered = true;
      }
    }
  }

  public void setNotJustEntered() {
    justEntered = false;
  }

  /**
   * A method that looks at the regions of a TextField and sets them as selected
   * if the mouse if over them.
   */

  public void detectMouseInput() {
    int mousex = GlobalInput.mouseX;
    int mousey = GlobalInput.mouseY;
    if (GlobalInput.confirm) {
      for (int i = 0; i < fieldList.size(); i++) {
        int minx = fieldList.get(i).getMinx();
        int maxx = fieldList.get(i).getMaxx();
        int miny = fieldList.get(i).getMiny();
        int maxy = fieldList.get(i).getMaxy();
        if (minx < mousex && mousex < maxx) {
          if (miny < mousey && mousey < maxy) {
            selected = i;
          }
        }
      }
    }
  }

  /**
   * A method that gets the text of the selected field.
   * 
   * @return The String value of the text field.
   */

  public String getSelectedText() {
    return fieldList.get(selected).getText();
  }

  public ArrayList<TextField> getList() {
    return fieldList;
  }

}
