package ee.joonasvali.butterfly.simulation.actor.vision;

import ee.joonasvali.butterfly.simulation.actor.Actor;

/**
 * @author Joonas Vali July 2016
 */
public class VisibleActor extends VisibleObject {

  public VisibleActor(double relativeRotationToActor, double distance, double diameter) {
    super(relativeRotationToActor, distance, diameter);
  }

  public VisibleActor(Actor observer, Actor other) {
    super(observer, other);
  }
}
