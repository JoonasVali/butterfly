package ee.joonasvali.butterfly.simulation.actor;

import ee.joonasvali.butterfly.code.Immutable;

/**
 * @author Joonas Vali July 2016
 */
@Immutable
public class Action {
  private final double thrust;
  private final double rotation;

  public Action(double thrust, double rotation) {
    this.thrust = thrust;
    this.rotation = rotation;
  }

  public double getThrust() {
    return thrust;
  }

  public double getRotation() {
    return rotation;
  }
}
