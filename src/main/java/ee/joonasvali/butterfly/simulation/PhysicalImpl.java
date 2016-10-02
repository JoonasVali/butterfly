package ee.joonasvali.butterfly.simulation;

import ee.joonasvali.butterfly.code.Immutable;

/**
 * @author Joonas Vali July 2016
 */
@Immutable
public class PhysicalImpl implements Physical {
  private final double x;
  private final double y;
  private final double rotation;
  private final double rotationImpulse;
  private final double xImpulse;
  private final double yImpulse;
  private final int diameter;

  public PhysicalImpl(double x, double y, int diameter, double rotation, double xImpulse, double yImpulse, double rotationImpulse) {
    this.x = x;
    this.y = y;
    this.diameter = diameter;
    this.rotation = rotation % 360;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PhysicalImpl)) return false;

    PhysicalImpl physical = (PhysicalImpl) o;

    if (Double.compare(physical.x, x) != 0) return false;
    if (Double.compare(physical.y, y) != 0) return false;
    if (Double.compare(physical.rotation, rotation) != 0) return false;
    if (Double.compare(physical.rotationImpulse, rotationImpulse) != 0) return false;
    if (Double.compare(physical.xImpulse, xImpulse) != 0) return false;
    if (Double.compare(physical.yImpulse, yImpulse) != 0) return false;
    return diameter == physical.diameter;

  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(x);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(rotation);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(rotationImpulse);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(xImpulse);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(yImpulse);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + diameter;
    return result;
  }
}
