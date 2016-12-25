package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.player.SimulationPlayer;
import ee.joonasvali.butterfly.simulation.SimulationState;
import ee.joonasvali.butterfly.ui.message.Message;
import ee.joonasvali.butterfly.ui.message.WarningMessage;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.util.Optional;

public class UIImpl implements UI {
  private static final Color HELP_BACKGROUND = new Color(80, 80, 170);
  public static final int HELP_X = 100;
  public static final int HELP_Y = 100;
  public static final int HELP_WIDTH = 900;
  public static final int HELP_HEIGHT = 500;

  private final int playerX;
  private final int playerY;
  private final int playerWidth;
  private final int playerHeight;

  private final ButterFlyConfig config;

  private final int simulationX;
  private final int simulationY;
  /**
   * Width of simulation space on screen
   */
  private final int simulationScreenWidth;
  /**
   * Height of simulation space on screen
   */
  private final int simulationScreenHeight;
  private volatile Message message;
  private volatile boolean help;

  public static final String[] HELP_CONTENT = new String[]{
      "Press SPACE to pause/resume",
      "Press '1' '2' '3' to choose speed. '0' for reverse. ",
      "Press 'T' to switch track. The lower track is the one you can edit.",
      "While on lower track, click on screen to select actor or food.",
      "Press 'delete' to alter timeline and remove the actor or food from existence.",
      "Press 'B' to toggle Butterfly Effect view on / off. (Works if you've altered the timeline.)",
  };

  public UIImpl(ButterFlyConfig config) {
    this.config = config;
    this.simulationX = 10 + 1;
    this.simulationY = 10 + 1;
    this.simulationScreenWidth = config.getWindowResolutionWidth() - (simulationX + 10) ;
    this.simulationScreenHeight = config.getWindowResolutionHeight() - (simulationY + 100);
    this.playerX = 10;
    this.playerY = config.getWindowResolutionHeight() - 90;
    this.playerWidth = config.getWindowResolutionWidth() - 100; // TODO unify. The image returned should match it.
    this.playerHeight = 100; // TODO unify. The image returned should match it.
  }

  @Override
  public void drawUI(Graphics g, SimulationPlayer player, SimulationPlayerPainter painter) {
    g.setColor(Color.orange);
    g.drawRect(10, 10, config.getWindowResolutionWidth() - 20, config.getWindowResolutionHeight() - 20);
    g.drawLine(10, config.getWindowResolutionHeight() - 100, config.getWindowResolutionWidth() - 10, config.getWindowResolutionHeight() - 100);
    Image image = painter.getPlayerImage(player);
    image.draw(10, config.getWindowResolutionHeight() - 90);
  }

  @Override
  public void drawUITop(Graphics g, int totalFrames, int trackPlayed) {
    drawFrameCounter(g, totalFrames);
    drawTrack(g, trackPlayed);
    drawWarningIfPresent(g, message);
    if (help) {
      drawHelp(g);
    }
  }

  private void drawHelp(Graphics g) {
    g.setColor(HELP_BACKGROUND);
    g.fillRect(HELP_X, HELP_Y, HELP_WIDTH, HELP_HEIGHT);
    g.setColor(Color.white);
    g.drawRect(HELP_X, HELP_Y, HELP_WIDTH, HELP_HEIGHT);
    String[] helpContent = HELP_CONTENT;
    for (int i = 0; i < helpContent.length; i++) {
      g.drawString(helpContent[i], HELP_X + 20, HELP_Y + 20 + i * 20);
    }

  }

  private void drawWarningIfPresent(Graphics g, Message message) {
    if (message == null || message.isExpired()) {
      return;
    }
    g.setColor(Color.yellow);
    g.drawString(message.getMessage(), 250, 20);
  }

  private void drawTrack(Graphics g, int trackPlayed) {
    g.setColor(Color.white);
    g.drawString("Track: " + trackPlayed, 250, 10);
  }

  public void drawSimulation(SimulationPainter painter, SimulationState simulationState, Optional<SimulationState> originalState) {
    painter.draw(simulationX, simulationY, simulationState, originalState);
  }

  @Override
  public MouseDispatcher getMouseDispatcher() {
    return new MouseDispatcher() {
      private MouseListener player;
      private MouseListener simulation;
      private MouseListener settings;
      @Override
      public void registerMouseListener(MouseListener listener, Area area) {
        if (!(area instanceof AreaImpl)) {
          throw new IllegalArgumentException("Wrong Area type: " + area);
        }
        switch ((AreaImpl)area) {
          case PLAYER: player = listener; break;
          case SIMULATION: simulation = listener; break;
          case SETTINGS: settings = listener; break;
          default: throw new IllegalArgumentException("wrong Area type: " + area);
        }
      }

      @Override
      public void mouseClicked(int button, int x, int y, int clickCount) {
        if (x > simulationX && x < simulationScreenWidth + simulationX
            && y > simulationY && y < simulationScreenHeight + simulationY) {
          if (simulation != null) {
            simulation.mouseClicked(button, x - simulationX, y - simulationY, clickCount);
          }
          return;
        }

        if (x > playerX && x < playerWidth + playerX
            && y > playerY && y < playerHeight + playerY) {
          if (player != null) {
            player.mouseClicked(button, x - playerX, y - playerY, clickCount);
          }
        }

        if (settings != null) {
          settings.mouseClicked(button, x, y, clickCount);
        }
      }
    };
  }

  @Override
  public void displayWarning(String message) {
    this.message = new WarningMessage(message);
  }

  @Override
  public void toggleHelp() {
    this.help = !this.help;
  }

  @Override
  public boolean isHelpVisible() {
    return this.help;
  }

  public int getSimulationScreenWidth() {
    return simulationScreenWidth;
  }

  public int getSimulationScreenHeight() {
    return simulationScreenHeight;
  }

  private void drawFrameCounter(Graphics g, int totalFrames) {
    g.setColor(Color.white);
    g.drawString("Frame: " + totalFrames, 100, 10);
  }


}
