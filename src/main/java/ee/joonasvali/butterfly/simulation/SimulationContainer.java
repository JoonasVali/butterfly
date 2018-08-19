package ee.joonasvali.butterfly.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Joonas Vali July 2016
 */
public class SimulationContainer {
  private final int width;
  private final int height;
  private final PhysicsRunner runner;
  private final ArrayList<SimulationState> states;
  private final ArrayList<Integer> alteredStates;
  private final SimulationState inception;

  public SimulationContainer(PhysicsRunner runner, SimulationState inception, int width, int height) {
    this(runner, inception, width, height, new ArrayList<>());
  }

  private SimulationContainer(PhysicsRunner runner, SimulationState inception, int width, int height, ArrayList<Integer> alteredStates) {
    this.width = width;
    this.height = height;
    this.runner = runner;
    states = new ArrayList<>();
    this.alteredStates = new ArrayList<>(alteredStates);
    this.inception = inception;
    states.add(inception);
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public void nextState() {
    SimulationState state = states.get(states.size() - 1);
    SimulationState next = runner.run(state);
    states.add(next);
  }

  public int getTotalFrames() {
    return states.size();
  }

  public void reset() {
    if (states.size() > 0) {
      SimulationState s = states.get(0);
      states.clear();
      states.add(s);
    }
  }

  public SimulationState getState(int frame) {
    return states.get(frame);
  }

  public SimulationContainer copy() {
    return new SimulationContainer(runner, states.get(0), getWidth(), getHeight(), alteredStates);
  }

  /**
   * Reverts altered states, runs from beginning
   */
  public void clearState() {
    int size = states.size();
    states.clear();
    states.add(inception);
    for (int i = states.size(); i < size; i++) {
      nextState();
    }
    alteredStates.clear();
  }

  /**
   * Alter history. Reruns simulation after the altered frame.
   */
  public void alterState(SimulationState currentState) {
    int size = states.size();
    states.removeIf(state -> state.getFrameNumber() >= currentState.getFrameNumber());
    alteredStates.removeIf(num -> num >= currentState.getFrameNumber());
    alteredStates.add(currentState.getFrameNumber());
    states.add(currentState.getFrameNumber(), currentState);
    for (int i = states.size(); i < size; i++) {
      nextState();
    }
  }

  public List<Integer> getAlteredStates() {
    return Collections.unmodifiableList(alteredStates);
  }
}
