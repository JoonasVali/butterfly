package ee.joonasvali.butterfly;

import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.player.Clock;
import ee.joonasvali.butterfly.player.SimulationPlayer;
import ee.joonasvali.butterfly.simulation.Food;
import ee.joonasvali.butterfly.simulation.Physical;
import ee.joonasvali.butterfly.simulation.PhysicsRunner;
import ee.joonasvali.butterfly.simulation.SimulationConfiguration;
import ee.joonasvali.butterfly.simulation.SimulationContainer;
import ee.joonasvali.butterfly.simulation.SimulationState;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.actor.vision.ActorVisionHelper;
import ee.joonasvali.butterfly.simulation.generator.InitialStateGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ButterFly {
  private final ButterFlyConfig config;
  private final PhysicsRunner runner;
  private final ActorVisionHelper visionHelper;
  private final Clock clock;
  private final InitialStateGenerator generator;
  private final SimulationConfiguration runtimeConfig;
  private int simulationWidth;
  private int simulationHeight;

  private volatile SimulationContainer[] containers = new SimulationContainer[2];
  private volatile SimulationPlayer player;

  private final static Logger log = LoggerFactory.getLogger(ButterFly.class);

  public ButterFly(ButterFlyConfig config, PhysicsRunner runner, InitialStateGenerator generator, ActorVisionHelper visionHelper) {
    this.generator = generator;
    this.runner = runner;
    this.visionHelper = visionHelper;
    this.config = config;
    this.runtimeConfig = new SimulationConfiguration();
    clock = new Clock(config.getFramesInSimulation());
  }

  public void init() {
    simulationWidth = config.getSimulationWidth();
    simulationHeight = config.getSimulationHeight();

    SimulationContainer container = new SimulationContainer(
        runner,
        generator.createInitialState(simulationWidth, simulationHeight),
        simulationWidth,
        simulationHeight
    );
    this.containers = new SimulationContainer[]{container, container.copy()};

    this.player = new SimulationPlayer(
        containers,
        config.getFramesInSimulation(), clock
    );

    this.player.calculateSimulation();
  }

  public SimulationContainer getActiveTrackContainer() {
    return containers[player.getTrackPlayed()];
  }

  public void tick(long timeMs) {
    player.passTime(timeMs);
  }

  // TODO, this seems hacky, maybe move to SimulationContainer?
  public void removePhysical(Physical selected) {
    if (selected instanceof Actor) {
      // remove actor
      SimulationState state = getActiveTrackContainer().getState(clock.getFrameIndex());
      List<Actor> actors = new ArrayList<>(state.getActors());
      if (actors.remove(selected)) {
        SimulationState newState = new SimulationState(state.getFrameNumber(), actors, state.getFoods(), state.getWidth(), state.getHeight());
        getActiveTrackContainer().alterState(newState);
      } else {
        log.warn("actor " + selected + " is not found from the active track " + player.getTrackPlayed());
      }
    } else if (selected instanceof Food) {
      // remove food
      SimulationState state = getActiveTrackContainer().getState(clock.getFrameIndex());
      List<Food> food = new ArrayList<>(state.getFoods());
      if (food.remove(selected)) {
        SimulationState newState = new SimulationState(state.getFrameNumber(), state.getActors(), food, state.getWidth(), state.getHeight());
        getActiveTrackContainer().alterState(newState);
      } else {
        log.warn("Food " + selected + " is not found from the active track " + player.getTrackPlayed());
      }
    }
  }

  public SimulationPlayer getPlayer() {
    return player;
  }

  public void shutdown() {
    System.exit(0);
  }

  public int getSimulationWidth() {
    return simulationWidth;
  }

  public int getSimulationHeight() {
    return simulationHeight;
  }

  public ActorVisionHelper getVisionHelper() {
    return visionHelper;
  }

  public SimulationConfiguration getSimulationConfiguration() {
    return runtimeConfig;
  }
}
