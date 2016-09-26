package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.player.Clock;
import ee.joonasvali.butterfly.player.SimulationPlayer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * @author Joonas Vali July 2016
 */
public class SimulationPlayerPainterImpl implements SimulationPlayerPainter {

  private static final Color ACTIVE_LINE_LIGHT = new Color(200, 200, 200);
  private static final Color ACTIVE_RED = Color.red;
  private static final Color ACTIVE_WHITE = Color.white;

  private static final Color INACTIVE_LINE_LIGHT = new Color(100, 100, 100);
  private static final Color INACTIVE_RED = new Color(100, 0, 0);
  private static final Color INACTIVE_WHITE = new Color(150, 150, 150);

  public static final int CONTROLS_X = 30;
  public static final int CONTROLS_Y = 45;
  public static final int BUTTON_STANDARD_DIMENSION = 20;

  private final Image image;
  private final Graphics g;
  private final int trackWidth;

  public SimulationPlayerPainterImpl(int windowWidth, int windowHeight) throws SlickException {
    this.image = new Image(windowWidth - 100, 90);
    this.g = image.getGraphics();
    trackWidth = image.getWidth() - 30;

  }

  @Override
  public Image getPlayerImage(SimulationPlayer player) {
    g.clear();
    g.setColor(Color.gray);
    g.drawRect(10, 1, image.getWidth() - 10 * 2, image.getHeight() - 15);
    drawTrackAt(10, 10, g, player, player.getTrackPlayed() == 0);
    drawTrackAt(10, 30, g, player, player.getTrackPlayed() == 1);
    drawControlsAt(player.getClock(), CONTROLS_X, CONTROLS_Y, g);
    g.flush();
    return image;
  }

  private void drawControlsAt(Clock clock, int x, int y, Graphics g) {
    g.setColor(Color.white);
    // Draw play
    if (clock.isPause()) {
      int length = 20;
      g.drawLine(x, y, x + length, y + 10);
      g.drawLine(x, y, x, y + 20);
      g.drawLine(x, y + 20, x + length, y + 10);
    } else {

      int width = 7;
      int height = BUTTON_STANDARD_DIMENSION;
      int space = 5;
      // horizontals
      g.drawLine(x, y, x + width, y);
      g.drawLine(x, y + height, x + width, y + height);
      g.drawLine(x + width + space, y, x + width * 2 + space, y);
      g.drawLine(x + width + space, y + height, x + width * 2 + space, y + height);
      // verticals
      g.drawLine(x, y, x, y + height);
      g.drawLine(x + width, y, x + width, y + height);
      g.drawLine(x + width + space, y, x + width + space, y + height);
      g.drawLine(x + width * 2 + space, y, x + width * 2 + space, y + height);
    }
  }

  public void drawTrackAt(int x, int y, Graphics g, SimulationPlayer player, boolean active) {

    g.setColor(active ? ACTIVE_RED : INACTIVE_RED);
    int breakPoint = (int)(trackWidth * (double)player.getCurrentFrame() / (double)player.getTotalFrames());


    // Draw red line
    g.drawLine(x + 5, y + 5, x + 5 + breakPoint, y + 5);
    g.drawLine(x + 5, y + 5 + 1, x + 5 + breakPoint, y + 5 + 1);

    // Draw white line
    g.setColor(active ? ACTIVE_LINE_LIGHT : INACTIVE_LINE_LIGHT);
    g.drawLine(x + 5 + breakPoint, y + 5, x + 5 + trackWidth - 1, y + 5);
    g.drawLine(x + 5 + breakPoint, y + 5 + 1, x + 5 + trackWidth - 1, y + 5 + 1);

    g.setColor(active ? ACTIVE_WHITE : INACTIVE_WHITE);
    for (int i = 0; i < 5; i++) {
      g.drawLine(x + 5 + breakPoint + i, y + 5, x + 5 + breakPoint + i, y + 10);
    }
  }


  public MouseListener createMouseListener(SimulationPlayer player, Clock clock) {
    return new PlayerListener(player, clock);
  }

  private class PlayerListener implements MouseListener {
    private final SimulationPlayer player;
    private final Clock clock;

    public PlayerListener(SimulationPlayer player, Clock clock) {
      this.player = player;
      this.clock = clock;
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
      if (x > CONTROLS_X && y > CONTROLS_Y && x < CONTROLS_X + BUTTON_STANDARD_DIMENSION && y < CONTROLS_Y + BUTTON_STANDARD_DIMENSION) {
        if (clock.isPause()) {
          clock.pause(false);
        } else {
          clock.pause(true);
        }
      }


      if (x < trackWidth + 10 && x >= 10) {
        x -= 10;

        int index = (int) (((double)player.getTotalFrames() / (double) trackWidth) * x);
        player.setFrameIndex(index);
      }
    }
  }

}
