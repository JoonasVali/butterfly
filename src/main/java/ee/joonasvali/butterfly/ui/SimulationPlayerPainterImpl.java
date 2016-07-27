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
    g.clear();
    g.setColor(Color.gray);
    g.drawRect(10, 10, image.getWidth() - 10 * 2, image.getHeight() - 15);
    drawPlayerAt(10, 10, g, player);
    drawPlayerAt(10, 30, g, player);

    g.flush();
    return image;
  }

  public void drawPlayerAt(int x, int y, Graphics g, SimulationPlayer player) {
    int frames = player.getTotalFrames();

    g.setColor(Color.red);
    int breakPoint = player.getCurrentFrame();

    // Draw red line
    g.drawLine(x + 5, y + 5, x + 5 + breakPoint, y + 5);
    g.drawLine(x + 5, y + 5 + 1, x + 5 + breakPoint, y + 5 + 1);

    // Draw white line
    g.setColor(LINE_WHITE);
    g.drawLine(x + 5 + breakPoint, y + 5, x + 5 + frames - 1, y + 5);
    g.drawLine(x + 5 + breakPoint, y + 5 + 1, x + 5 + frames - 1, y + 5 + 1);

    g.setColor(Color.white);
    for (int i = 0; i < 5; i++) {
      g.drawLine(x + 5 + breakPoint + i, y + 5, x + 5 + breakPoint + i, y + 10);
    }
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
