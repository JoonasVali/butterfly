package ee.joonasvali.butterfly.simulation;

public class Food extends Physical {
  private final double points = 20;
  public Food(int x, int y, int diameter, double rotation, double xImpulse, double yImpulse, double rotationImpulse) {
    super(x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse);
  }

  public double getPoints() {
    return points;
  }
}
