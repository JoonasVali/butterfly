/*
 * Copyright (c) 2015, Jartin. All rights reserved. This application is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; This application is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. Do not remove this header.
 */

package ee.joonasvali.butterfly.uiswing.listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ScrollPaneHandScrollListener extends MouseAdapter {
  private final Cursor defCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
  private final Cursor hndCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
  private final Point pp = new Point();
  private final Rectangle rect = new Rectangle();
  private final JPanel underlyingPanel;
  private final JViewport viewport;

  public ScrollPaneHandScrollListener(JViewport viewport, JPanel underlyingPanel) {
    this.underlyingPanel = underlyingPanel;
    this.viewport = viewport;
  }

  public void mouseDragged(final MouseEvent e) {
    Point cp = e.getPoint();
    Point vp = viewport.getViewPosition();
    vp.translate(pp.x - cp.x, pp.y - cp.y);
    rect.setSize(viewport.getSize());
    rect.setLocation(vp);
    underlyingPanel.scrollRectToVisible(rect);
  }

  public void mousePressed(MouseEvent e) {
    underlyingPanel.setCursor(hndCursor);
    pp.setLocation(e.getPoint());
  }

  public void mouseReleased(MouseEvent e) {
    underlyingPanel.setCursor(defCursor);
    underlyingPanel.repaint();
  }
}