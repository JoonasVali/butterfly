package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.player.SimulationPlayer;
import ee.joonasvali.butterfly.simulation.SimulationState;
import org.newdawn.slick.Graphics;

public interface UI {
  void drawUI(Graphics g, SimulationPlayer player, SimulationPlayerPainter painter);
  void drawUITop(Graphics g, int totalFrames);
  void drawSimulation(SimulationPainter painter, SimulationState simulationState);
}
