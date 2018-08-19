package ee.joonasvali.butterfly.simulation;

import ee.joonasvali.butterfly.code.Immutable;
import ee.joonasvali.butterfly.simulation.actor.Actor;

import java.util.Collections;
import java.util.List;

/**
 * @author Joonas Vali July 2016
 */
@Immutable
public class SimulationState {
  private final int width;
  private final int height;

  private final List<Actor> actors;
  private final List<Food> foods;
  private final int frameNumber;

  public SimulationState(int frameNumber, List<Actor> actors, List<Food> foods, int width, int height) {
    this.frameNumber = frameNumber;
    this.foods = Collections.unmodifiableList(foods);
    this.actors = Collections.unmodifiableList(actors);
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

  public List<Food> getFoods() {
    return foods;
  }

  public int getFrameNumber() {
    return frameNumber;
  }

  public Physical findPhysicalByUUID(PhysicalUID lookedUpPhysical) {
    for (Actor a : actors) {
      if (a.getUID() == lookedUpPhysical) {
        return a;
      }
    }
    for (Food food : foods) {
      if (food.getUID() == lookedUpPhysical) {
        return food;
      }
    }
    return null;
  }
}
