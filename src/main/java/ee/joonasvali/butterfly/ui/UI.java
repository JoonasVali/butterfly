package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.simulation.SimulationState;
import org.newdawn.slick.Graphics;

public interface UI {
  void drawUI(Graphics g);
  void drawUITop(Graphics g, int totalFrames);
  void drawSimulation(SimulationPainter painter, SimulationState simulationState);
}
