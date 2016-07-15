package ee.joonasvali.butterfly.simulation;

import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.actor.demo.PlayerActor;
import ee.joonasvali.butterfly.ui.SimulationPainter;

import java.util.ArrayList;

/**
 * @author Joonas Vali July 2016
 */
public class SimulationContainer {
  private final SimulationPainter painter;
  private final int width;
  private final int height;
  private final PhysicsRunner runner;
  private final ArrayList<SimulationState> states;
  // Temp stuff
  private volatile PlayerActor actor;

  public SimulationContainer(PhysicsRunner runner, SimulationState genesis, SimulationPainter painter, int width, int height) {
    this.painter = painter;
    this.width = width;
    this.height = height;
    this.runner = runner;
    states = new ArrayList<>();
    states.add(genesis);
  }

  public SimulationPainter getPainter() {
    return painter;
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
    for(Actor act : next.getActors()) {
      if (act instanceof PlayerActor) {
        this.actor = (PlayerActor) act;
      }
    }
    states.add(next);
  }

  public int getTotalFrames() {
    return states.size();
  }

  public PlayerActor getActor() {
    return actor;
  }

  public SimulationState getState() {
    return states.get(states.size() - 1);
  }

  public void reset() {
    if (states.size() > 0) {
      SimulationState s = states.get(0);
      states.clear();
      states.add(s);
    }

  }
}
