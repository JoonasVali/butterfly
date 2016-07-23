package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.player.SimulationPlayer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * @author Joonas Vali July 2016
 */
public class SimulationPlayerPainterImpl implements SimulationPlayerPainter {

  private final Image image;
  private final Graphics g;

  public SimulationPlayerPainterImpl(int windowWidth, int windowHeight) throws SlickException {
    this.image = new Image(windowWidth - 100, 50);
    this.g = image.getGraphics();
  }

  @Override
  public Image getPlayerImage(SimulationPlayer player) {
    /* TODO */
    g.clear();
    g.setColor(Color.white);
    g.drawLine(0, 0, 10, 10);
    g.flush();
    return image;
  }
}
