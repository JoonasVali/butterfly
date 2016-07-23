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
    int frames = player.getTotalFrames();

    g.clear();
    g.setColor(Color.gray);
    g.drawRect(10, 10, image.getWidth() - 20, image.getHeight() - 20);
    g.setColor(Color.red);
    int breakPoint = player.getCurrentFrame();
    g.drawLine(15, 15, 15 + breakPoint, 15);
    g.setColor(Color.white);
    g.drawLine(15 + breakPoint, 15, 15 + frames - 1, 15);
    g.flush();
    return image;
  }
}
