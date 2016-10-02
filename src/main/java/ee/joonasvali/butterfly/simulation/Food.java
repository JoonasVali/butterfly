package ee.joonasvali.butterfly.simulation;

import ee.joonasvali.butterfly.code.Immutable;

@Immutable
public class Food extends PhysicalImpl {
  private final double points;
  public Food(double x, double y, int diameter, double rotation, double xImpulse, double yImpulse, double rotationImpulse, double points) {
    super(x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse);
    this.points = points;
  }

  public double getPoints() {
    return points;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Food)) return false;
    if (!super.equals(o)) return false;

    Food food = (Food) o;

    return Double.compare(food.points, points) == 0;

  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    long temp;
    temp = Double.doubleToLongBits(points);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
