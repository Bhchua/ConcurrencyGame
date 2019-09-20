package game.screens;

import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Core;

/**
 * The GameScreenManager places all the 'screens' into a stack and only
 * updates the one at the top. This can be used to suspend other screens
 * temporarily while switching to another.
 * 
 * @author Code from Brent Aureli's LibGDX Tutorials.
 * 
 */

public class GameScreenManager {
  private Stack<Screen> screens;
  
  public GameScreenManager() {
    screens = new Stack<Screen>();
  }
  
  public void push(Screen scr) {
    screens.push(scr);
  }
  
  public void pop() {
    screens.pop();
  }
  
  public void set(Screen scr) {
    screens.peek().dispose();
    screens.pop();
    screens.push(scr);
  }
  
  public void update() {
    screens.peek().update();
  }
  
  public void render(SpriteBatch sb) {
    screens.peek().render(sb);
  }
}
