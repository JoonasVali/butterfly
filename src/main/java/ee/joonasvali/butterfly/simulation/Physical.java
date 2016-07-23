package ee.joonasvali.butterfly.simulation;

/**
 * @author Joonas Vali July 2016
 */
public interface Physical {
  double getX();
  double getY();
  double getRotation();
  double getRotationImpulse();
  double getXImpulse();
  double getYImpulse();
  int getDiameter();
}
