package ee.joonasvali.butterfly.simulation;

public class Food extends Physical {
  private final double points;
  public Food(int x, int y, int diameter, double rotation, double xImpulse, double yImpulse, double rotationImpulse, double points) {
    super(x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse);
    this.points = points;
  }

  public double getPoints() {
    return points;
  }
}
