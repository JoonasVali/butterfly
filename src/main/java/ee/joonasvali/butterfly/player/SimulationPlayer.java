package ee.joonasvali.butterfly.player;

import ee.joonasvali.butterfly.simulation.SimulationContainer;
import ee.joonasvali.butterfly.simulation.SimulationState;

/**
 * @author Joonas Vali July 2016
 */
public class SimulationPlayer {

  private final SimulationContainer mainContainer;
  private final int totalFrames;
  private final Clock clock;

  public SimulationPlayer(SimulationContainer mainContainer, int totalFrames, Clock clock) {
    this.totalFrames = totalFrames;
    this.mainContainer = mainContainer;
    this.clock = clock;
  }

  public void calculateSimulation() {
    mainContainer.reset();
    for (int i = 0; i < totalFrames - 1; i++) {
       mainContainer.nextState();
    }
  }

  public SimulationState getState() {
    return mainContainer.getState(clock.getFrameIndex());
  }

  public SimulationState getState(int frame) {
    return mainContainer.getState(frame);
  }

  public int getCurrentFrame() {
    return clock.getFrameIndex();
  }

  public void passTime(int timeMs) {
    clock.passTime(timeMs);
  }

  public int getTotalFrames() {
    return totalFrames;
  }
}
