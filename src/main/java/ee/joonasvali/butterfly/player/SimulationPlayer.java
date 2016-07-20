package ee.joonasvali.butterfly.player;

import ee.joonasvali.butterfly.simulation.SimulationContainer;
import ee.joonasvali.butterfly.simulation.SimulationState;

/**
 * @author Joonas Vali July 2016
 */
public class SimulationPlayer {

  private final SimulationContainer mainContainer;
  private final int totalFrames;

  public SimulationPlayer(SimulationContainer mainContainer, int totalFrames) {
    this.totalFrames = totalFrames;
    this.mainContainer = mainContainer;
  }

  public void calculateSimulation() {
    mainContainer.reset();
    for (int i = 0; i < totalFrames - 1; i++) {
       mainContainer.nextState();
    }
  }

  public SimulationState getState(int frame) {
    return mainContainer.getState(frame);
  }


}
