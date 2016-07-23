package ee.joonasvali.butterfly.simulation;

import ee.joonasvali.butterfly.code.Immutable;

@Immutable
public class Food extends Physical {
  private final double points;
  public Food(double x, double y, int diameter, double rotation, double xImpulse, double yImpulse, double rotationImpulse, double points) {
    super(x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse);
    this.points = points;
  }

  public double getPoints() {
    return points;
  }
}
