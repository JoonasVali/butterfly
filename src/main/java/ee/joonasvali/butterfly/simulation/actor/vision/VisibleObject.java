package ee.joonasvali.butterfly.simulation.actor.vision;

import ee.joonasvali.butterfly.simulation.Physical;
import ee.joonasvali.butterfly.simulation.actor.Actor;

/**
 * @author Joonas Vali July 2016
 */
public class VisibleObject {
  private final double relativeRotationToActor;
  private final double distance;
  private final double diameter;

  public VisibleObject(Actor observer, Physical other) {
    this(calculateRotation(observer, other), calculateDistance(observer, other), other.getDiameter());
  }

  private static double calculateDistance(Actor observer, Physical other) {
    return 0;
  }

  private static double calculateRotation(Actor observer, Physical other) {
    return 0;
  }

  public VisibleObject(double relativeRotationToActor, double distance, double diameter) {
    this.diameter = diameter;
    this.relativeRotationToActor = relativeRotationToActor;
    this.distance = distance;
  }

  public double getDistance() {
    return distance;
  }

  public double getRelativeRotationToActor() {
    return relativeRotationToActor;
  }

  public double getDiameter() {
    return diameter;
  }
}
