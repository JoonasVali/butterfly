package ee.joonasvali.butterfly.uiswing.listener;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DispatchToParentMouseListener implements MouseListener, MouseMotionListener {
  @Override
  public void mouseClicked(MouseEvent e) {
    Component source = (Component) e.getSource();
    source.getParent().dispatchEvent(e);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    Component source = (Component) e.getSource();
    source.getParent().dispatchEvent(e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    Component source = (Component) e.getSource();
    source.getParent().dispatchEvent(e);
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    Component source = (Component) e.getSource();
    source.getParent().dispatchEvent(e);
  }

  @Override
  public void mouseExited(MouseEvent e) {
    Component source = (Component) e.getSource();
    source.getParent().dispatchEvent(e);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    Component source = (Component) e.getSource();
    source.getParent().dispatchEvent(e);
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    Component source = (Component) e.getSource();
    source.getParent().dispatchEvent(e);
  }
}
