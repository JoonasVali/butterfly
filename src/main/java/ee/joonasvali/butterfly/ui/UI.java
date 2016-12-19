package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.player.SimulationPlayer;
import ee.joonasvali.butterfly.simulation.SimulationState;
import org.newdawn.slick.Graphics;

import java.util.Optional;

public interface UI {
  void drawUI(Graphics g, SimulationPlayer player, SimulationPlayerPainter painter);
  void drawUITop(Graphics g, int totalFrames, int trackPlayed);
  void drawSimulation(SimulationPainter painter, SimulationState simulationState, Optional<SimulationState> originalState);
  MouseDispatcher getMouseDispatcher();
  void displayWarning(String message);
  void toggleHelp();
  boolean isHelpVisible();
}
