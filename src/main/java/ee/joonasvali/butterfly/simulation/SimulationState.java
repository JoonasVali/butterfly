package ee.joonasvali.butterfly.simulation;

import ee.joonasvali.butterfly.code.Immutable;
import ee.joonasvali.butterfly.simulation.actor.Actor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joonas Vali July 2016
 */
@Immutable
public class SimulationState {
  private final int width;
  private final int height;

  private final List<Actor> actors;
  private final List<Food> food;
  private final int frameNumber;

  public SimulationState(int frameNumber, List<Actor> actors, List<Food> food, int width, int height) {
    this.frameNumber = frameNumber;
    this.food = food;
    this.actors = actors;
    this.width = width;
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public List<Actor> getActors() {
    return actors;
  }

  public List<Food> getFood() {
    return food;
  }

  public int getFrameNumber() {
    return frameNumber;
  }
}
