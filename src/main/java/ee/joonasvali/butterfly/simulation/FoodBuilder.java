package ee.joonasvali.butterfly.simulation;

/**
 * @author Joonas Vali July 2016
 */
public class FoodBuilder implements Physical {

  private double x;
  private double y;
  private double rotation;
  private double rotationImpulse;
  private double xImpulse;
  private double yImpulse;
  private int diameter;
  private double points;

  private final Food original;

  public FoodBuilder(Food food) {
    this.x = food.getX();
    this.y = food.getY();
    this.rotation = food.getRotation();
    this.rotationImpulse = food.getRotationImpulse();
    this.xImpulse = food.getXImpulse();
    this.yImpulse = food.getYImpulse();
    this.diameter = food.getDiameter();
    this.points = food.getPoints();
    this.original = food;
  }

  public Food getOriginal() {
    return original;
  }

  public Food build() {
    return new Food(x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse, points);
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getRotation() {
    return rotation;
  }

  public void setRotation(double rotation) {
    this.rotation = rotation;
  }

  public double getRotationImpulse() {
    return rotationImpulse;
  }

  public void setRotationImpulse(double rotationImpulse) {
    this.rotationImpulse = rotationImpulse;
  }

  public double getXImpulse() {
    return xImpulse;
  }

  public void setXImpulse(double xImpulse) {
    this.xImpulse = xImpulse;
  }

  public double getYImpulse() {
    return yImpulse;
  }

  public void setYImpulse(double yImpulse) {
    this.yImpulse = yImpulse;
  }

  public int getDiameter() {
    return diameter;
  }

  public void setDiameter(int diameter) {
    this.diameter = diameter;
  }

  public double getPoints() {
    return points;
  }

  public void setPoints(double points) {
    this.points = points;
  }
}
