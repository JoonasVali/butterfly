package ee.joonasvali.butterfly.simulation;

import ee.joonasvali.butterfly.code.Immutable;
import ee.joonasvali.butterfly.simulation.actor.Actor;

import java.util.ArrayList;

/**
 * @author Joonas Vali July 2016
 */
@Immutable
public class SimulationState {
  private final int width;
  private final int height;

  private final ArrayList<Actor> actors;
  private final ArrayList<Food> food;
  private final int frameNumber;

  public SimulationState(int frameNumber, ArrayList<Actor> actors, ArrayList<Food> food, int width, int height) {
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

  public ArrayList<Actor> getActors() {
    return actors;
  }

  public ArrayList<Food> getFood() {
    return food;
  }

  public int getFrameNumber() {
    return frameNumber;
  }
}
