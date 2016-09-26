package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.simulation.SimulationState;

/**
 * @author Joonas Vali July 2016
 */
public interface SimulationPainter {
  void draw(int simulationX, int simulationY, SimulationState state);
}
