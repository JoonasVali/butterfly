package ee.joonasvali.butterfly.simulation;

/**
 * @author Joonas Vali July 2016
 */
public class Physical {
  private final double x;
  private final double y;
  private final double rotation;
  private final double rotationImpulse;
  private final double xImpulse;
  private final double yImpulse;
  private final int diameter;

  public Physical(double x, double y, int diameter, double rotation, double xImpulse, double yImpulse, double rotationImpulse) {
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

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public int getRoundedX() {
    return (int) Math.round(x);
  }

  public int getRoundedY() {
    return (int) Math.round(y);
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
