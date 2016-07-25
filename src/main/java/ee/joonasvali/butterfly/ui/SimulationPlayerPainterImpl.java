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

  public static final int LINE_X = 15;
  public static final int LINE_Y = 15;
  private static final Color LINE_WHITE = new Color(200, 200, 200);
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
    g.drawLine(LINE_X, LINE_Y, 15 + breakPoint, LINE_Y);
    g.drawLine(LINE_X, LINE_Y + 1, 15 + breakPoint, LINE_Y + 1);
    g.setColor(LINE_WHITE);
    g.drawLine(15 + breakPoint, LINE_Y, 15 + frames - 1, LINE_Y);
    g.drawLine(15 + breakPoint, LINE_Y + 1, 15 + frames - 1, LINE_Y + 1);

    g.setColor(Color.white);
    for (int i = 0; i < 5; i++) {
      g.drawLine(15 + breakPoint + i, 15, 15 + breakPoint + i, 20);
    }



    g.flush();
    return image;
  }


  public MouseListener createMouseListener(SimulationPlayer player) {
    return new PlayerListener(player);
  }

  private class PlayerListener implements MouseListener {
    private final SimulationPlayer player;

    public PlayerListener(SimulationPlayer player) {
      this.player = player;
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
      x = x - LINE_X;
      y = y - LINE_Y;
      if (x < player.getTotalFrames() && x >= 0) {
        player.setFrameIndex(x);
      }
      System.out.println(button + " " + x + " " + y);
    }
  }

}
