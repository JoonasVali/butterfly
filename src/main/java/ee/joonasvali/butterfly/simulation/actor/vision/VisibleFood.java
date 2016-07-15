package ee.joonasvali.butterfly.simulation.actor.vision;

import ee.joonasvali.butterfly.simulation.Food;
import ee.joonasvali.butterfly.simulation.actor.Actor;

/**
 * @author Joonas Vali July 2016
 */
public class VisibleFood extends VisibleObject {
  public VisibleFood(double relativeRotationToActor, double distance, double diameter) {
    super(relativeRotationToActor, distance, diameter);
  }

  public VisibleFood(Actor observer, Food other) {
    super(observer, other);
  }
}
