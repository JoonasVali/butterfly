package ee.joonasvali.butterfly.simulation.generator;

import ee.joonasvali.butterfly.simulation.SimulationState;

/**
 * @author Joonas Vali October 2016
 */
public interface InitialStateGenerator {
  SimulationState createInitialState(int simWidth, int simHeight);
}
