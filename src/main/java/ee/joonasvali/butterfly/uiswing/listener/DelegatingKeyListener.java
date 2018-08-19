package ee.joonasvali.butterfly.uiswing.listener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class DelegatingKeyListener {
  private final ArrayList<KeyListener> listeners = new ArrayList<>();

  public DelegatingKeyListener() {
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
      if (e.getID() == KeyEvent.KEY_PRESSED) {
        keyPressed(e);
      }
      if (e.getID() == KeyEvent.KEY_RELEASED) {
        keyReleased(e);
      }
      if (e.getID() == KeyEvent.KEY_TYPED) {
        keyTyped(e);
      }

      return false;
    });
  }

  public void keyTyped(KeyEvent e) {
    listeners.forEach(keyListener -> keyListener.keyTyped(e));
  }

  public void keyPressed(KeyEvent e) {
    listeners.forEach(keyListener -> keyListener.keyPressed(e));
  }

  public void keyReleased(KeyEvent e) {
    listeners.forEach(keyListener -> keyListener.keyReleased(e));
  }

  public void addListener(KeyListener listener) {
    listeners.add(listener);
  }

  public boolean removeListener(KeyListener listener) {
    return listeners.remove(listener);
  }
}
