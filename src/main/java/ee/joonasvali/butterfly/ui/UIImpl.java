package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.player.SimulationPlayer;
import ee.joonasvali.butterfly.simulation.SimulationState;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class UIImpl implements UI {
  private final ButterFlyConfig config;

  private final int simulationX;
  private final int simulationY;
  /**
   * Width of simulation space on screen
   */
  private final int simulationWidth;
  /**
   * Height of simulation space on screen
   */
  private final int simulationHeight;

  public UIImpl(ButterFlyConfig config) {
    this.config = config;
    this.simulationX = 10 + 1;
    this.simulationY = 10 + 1;
    this.simulationWidth = config.getWindowResolutionWidth() - (simulationX + 10) ;
    this.simulationHeight = config.getWindowResolutionHeight() - (simulationY + 100);
  }

  @Override
  public void drawUI(Graphics g, SimulationPlayer player) {
    g.setColor(Color.orange);
    g.drawRect(10, 10, config.getWindowResolutionWidth() - 20, config.getWindowResolutionHeight() - 20);
    g.drawLine(10, config.getWindowResolutionHeight() - 100, config.getWindowResolutionWidth() - 10, config.getWindowResolutionHeight() - 100);
    drawPlayer(player);
  }

  private void drawPlayer(SimulationPlayer player) {
    int frames = player.getTotalFrames();
    
  }

  @Override
  public void drawUITop(Graphics g, int totalFrames) {
    drawFrameCounter(g, totalFrames);
  }

  public void drawSimulation(SimulationPainter painter, SimulationState simulationState) {
    Image i = painter.draw(simulationState);
    float simulationScale;
    if (simulationWidth > simulationHeight) {
      simulationScale = (float) simulationHeight / (float) i.getHeight();
    } else {
      // Not sure if this should ever happen, but still..
      simulationScale = (float) simulationWidth / (float) i.getWidth();
    }

    i.draw(simulationX, simulationY, simulationScale);
  }

  private void drawFrameCounter(Graphics g, int totalFrames) {
    g.setColor(Color.white);
    g.drawString("Frame: " + totalFrames, 100, 10);
  }


}
