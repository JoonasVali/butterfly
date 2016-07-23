package ee.joonasvali.butterfly.simulation.actor.vision;

import ee.joonasvali.butterfly.code.Immutable;
import ee.joonasvali.butterfly.simulation.PhysicalImpl;
import ee.joonasvali.butterfly.simulation.actor.Actor;

/**
 * @author Joonas Vali July 2016
 */
@Immutable
public class VisibleObject {
  private final double relativeRotationToObject;
  private final double distance;
  private final double diameter;

  public VisibleObject(Actor observer, PhysicalImpl other) {
    this(calculateRotation(observer, other), calculateDistance(observer, other), other.getDiameter());
  }

  private static double calculateDistance(Actor observer, PhysicalImpl other) {
    double omidX = other.getX() + other.getDiameter() / 2;
    double omidY = other.getY() + other.getDiameter() / 2;
    double x = observer.getX() + observer.getDiameter() / 2;
    double y = observer.getY() + observer.getDiameter() / 2;
    return Math.sqrt(Math.pow(omidY - y, 2) + Math.pow(omidX - x, 2)) - (observer.getDiameter() / 2 + other.getDiameter() / 2);
  }

  private static double calculateRotation(Actor observer, PhysicalImpl other) {
    double rotationOffset = observer.getRotation();
    double omidX = other.getX() + other.getDiameter() / 2;
    double omidY = other.getY() + other.getDiameter() / 2;
    double x = observer.getX() + observer.getDiameter() / 2;
    double y = observer.getY() + observer.getDiameter() / 2;
    double deltaY = omidY - y;
    double deltaX = omidX - x;
    return Math.toDegrees(Math.atan2(deltaY, deltaX)) - rotationOffset;
  }

  public VisibleObject(double relativeRotationToObject, double distance, double diameter) {
    this.diameter = diameter;
    this.distance = distance;
    double result = relativeRotationToObject % 360;
    while (result < -180) result += 360;
    while (result > 180) result -= 360;
    this.relativeRotationToObject = result;
  }

  public double getDistance() {
    return distance;
  }

  public double getRelativeRotationToObject() {
    return relativeRotationToObject;
  }

  public double getDiameter() {
    return diameter;
  }
}
