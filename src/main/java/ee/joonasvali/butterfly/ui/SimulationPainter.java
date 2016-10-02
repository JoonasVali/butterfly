package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.simulation.SimulationState;

import java.util.Optional;

/**
 * @author Joonas Vali July 2016
 */
public interface SimulationPainter {
  void draw(int simulationX, int simulationY, SimulationState state, Optional<SimulationState> originalState);
  void addSelectionListener(SelectionListener listener);
}
