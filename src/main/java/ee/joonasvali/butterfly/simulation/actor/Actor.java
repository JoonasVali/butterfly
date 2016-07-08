package ee.joonasvali.butterfly.simulation.actor;

import ee.joonasvali.butterfly.simulation.Physical;

/**
 * @author Joonas Vali July 2016
 */
public class Actor extends Physical {
  private final int health;
  private final double speed;

  public Actor(int x, int y, int diameter, double rotation, double xImpulse, double yImpulse, double rotationImpulse, int health, double speed) {
    super(x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse);
    this.health = health;
    this.speed = speed;
  }

  public int getHealth() {
    return health;
  }

  public Actor copy() {
    return new Actor(getX(), getY(), getDiameter(), getRotation(), getXImpulse(), getYImpulse(), getRotationImpulse(), getHealth(), getSpeed());
  }

  public double getSpeed() {
    return speed;
  }

  // TODO
  public Action move(WorldView view) {
    if (System.currentTimeMillis() % 5000 < 1000) {
      return new Action(0, 0);
    }
    if (System.currentTimeMillis() % 2000 > 1000)
      return new Action(speed, 1);
    else {
      return new Action(speed, -1);
    }
  }
}
