package game.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

/**
 * Primarily auto generated code from the InputProcessor from LibGDX.
 */

public class KeyboardInput implements InputProcessor {

  @Override
  public boolean keyDown(int keycode) {

    if (Keys.W == keycode) {
      GlobalInput.playerUp[0] = 1;
    }
    if (Keys.A == keycode) {
      GlobalInput.playerLeft[0] = 1;
    }
    if (Keys.S == keycode) {
      GlobalInput.playerDown[0] = 1;
    }
    if (Keys.D == keycode) {
      GlobalInput.playerRight[0] = 1;
    }

    if (Keys.SPACE == keycode) {
      GlobalInput.playerShoot[0] = true;
    }

    if (Keys.UP == keycode) {
      GlobalInput.playerUp[1] = 1;
    }
    if (Keys.LEFT == keycode) {
      GlobalInput.playerLeft[1] = 1;
    }
    if (Keys.DOWN == keycode) {
      GlobalInput.playerDown[1] = 1;
    }
    if (Keys.RIGHT == keycode) {
      GlobalInput.playerRight[1] = 1;
    }

    if (Keys.SHIFT_RIGHT == keycode) {
      GlobalInput.playerShoot[1] = true;
    }

    if (Keys.ESCAPE == keycode) {
      GlobalInput.paused = true;
    }

    if (Keys.PERIOD == keycode) {
      GlobalInput.period = true;
    }

    if (Keys.ENTER == keycode) {
      GlobalInput.enter = true;
    }

    if (Keys.BACKSPACE == keycode) {
      GlobalInput.backspace = true;
    }

    for (int i = 0; i < letterList.length; i++) {
      if (letterList[i] == keycode) {
        GlobalInput.letters[i] = true;
      }
    }

    for (int i = 0; i < numList.length; i++) {
      if (numList[i] == keycode) {
        GlobalInput.numkeys[i] = true;
      }
    }

    return false;
  }

  @Override
  public boolean keyUp(int keycode) {

    if (Keys.W == keycode) {
      GlobalInput.playerUp[0] = 0;
    }
    if (Keys.A == keycode) {
      GlobalInput.playerLeft[0] = 0;
    }
    if (Keys.S == keycode) {
      GlobalInput.playerDown[0] = 0;
    }
    if (Keys.D == keycode) {
      GlobalInput.playerRight[0] = 0;
    }

    if (Keys.SPACE == keycode) {
      GlobalInput.playerShoot[0] = false;
    }

    if (Keys.UP == keycode) {
      GlobalInput.playerUp[1] = 0;
    }
    if (Keys.LEFT == keycode) {
      GlobalInput.playerLeft[1] = 0;
    }
    if (Keys.DOWN == keycode) {
      GlobalInput.playerDown[1] = 0;
    }
    if (Keys.RIGHT == keycode) {
      GlobalInput.playerRight[1] = 0;
    }

    if (Keys.SHIFT_RIGHT == keycode) {
      GlobalInput.playerShoot[1] = false;
    }

    if (Keys.ESCAPE == keycode) {
      GlobalInput.paused = false;
    }

    if (Keys.PERIOD == keycode) {
      GlobalInput.period = false;
    }

    if (Keys.ENTER == keycode) {
      GlobalInput.enter = false;
    }

    if (Keys.BACKSPACE == keycode) {
      GlobalInput.backspace = false;
    }

    for (int i = 0; i < letterList.length; i++) {
      if (letterList[i] == keycode) {
        GlobalInput.letters[i] = false;
      }
    }

    for (int i = 0; i < numList.length; i++) {
      if (numList[i] == keycode) {
        GlobalInput.numkeys[i] = false;
      }
    }

    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean scrolled(int amount) {
    // TODO Auto-generated method stub
    return false;
  }

  private int[] letterList = new int[] { Keys.A, Keys.B, Keys.C, Keys.D, 
      Keys.E, Keys.F, Keys.G, Keys.H, Keys.I, Keys.J, Keys.K, Keys.L, 
      Keys.M, Keys.N, Keys.O, Keys.P, Keys.Q, Keys.R, Keys.S, Keys.T, 
      Keys.U, Keys.V, Keys.W, Keys.X, Keys.Y, Keys.Z };

  private int[] numList = new int[] { Keys.NUM_0, Keys.NUM_1, Keys.NUM_2, 
      Keys.NUM_3, Keys.NUM_4, Keys.NUM_5, Keys.NUM_6, Keys.NUM_7, 
      Keys.NUM_8, Keys.NUM_9 };

}
