package ee.joonasvali.butterfly.simulation;

/**
 * @author Joonas Vali July 2016
 */
public class Physical {
  private final int x;
  private final int y;
  private final double rotation;
  private final double rotationImpulse;
  private final double xImpulse;
  private final double yImpulse;
  private final int diameter;

  public Physical(int x, int y, int diameter, double rotation, double xImpulse, double yImpulse, double rotationImpulse) {
    this.x = x;
    this.y = y;
    this.diameter = diameter;
    this.rotation = rotation;
    this.rotationImpulse = rotationImpulse;
    this.xImpulse = xImpulse;
    this.yImpulse = yImpulse;
  }

  public int getDiameter() {
    return diameter;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public double getRotation() {
    return rotation;
  }

  public double getRotationImpulse() {
    return rotationImpulse;
  }

  public double getXImpulse() {
    return xImpulse;
  }

  public double getYImpulse() {
    return yImpulse;
  }
}
