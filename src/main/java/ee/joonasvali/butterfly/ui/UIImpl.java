package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.player.SimulationPlayer;
import ee.joonasvali.butterfly.simulation.SimulationState;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class UIImpl implements UI {
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

  public UIImpl(ButterFlyConfig config) {
    this.config = config;
    this.simulationX = 10 + 1;
    this.simulationY = 10 + 1;
    this.simulationScreenWidth = config.getWindowResolutionWidth() - (simulationX + 10) ;
    this.simulationScreenHeight = config.getWindowResolutionHeight() - (simulationY + 100);
    this.playerX = 10;
    this.playerY = config.getWindowResolutionHeight() - 90;
    this.playerWidth = config.getWindowResolutionWidth() - 100; // TODO unify. The image returned should match it.
    this.playerHeight = 50; // TODO unify. The image returned should match it.
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
  }

  private void drawTrack(Graphics g, int trackPlayed) {
    g.setColor(Color.white);
    g.drawString("Track: " + trackPlayed, 250, 10);
  }

  public void drawSimulation(SimulationPainter painter, SimulationState simulationState) {
    painter.draw(simulationX, simulationY, simulationState);
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
