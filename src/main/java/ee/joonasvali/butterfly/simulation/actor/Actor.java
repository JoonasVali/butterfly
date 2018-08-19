package ee.joonasvali.butterfly.simulation.actor;

import ee.joonasvali.butterfly.code.Immutable;
import ee.joonasvali.butterfly.simulation.PhysicalImpl;
import ee.joonasvali.butterfly.simulation.PhysicalUID;
import ee.joonasvali.butterfly.simulation.actor.vision.VisibleActor;
import ee.joonasvali.butterfly.simulation.actor.vision.VisibleFood;

import java.util.List;
import java.util.Random;

/**
 * @author Joonas Vali July 2016
 */
@Immutable
public class Actor extends PhysicalImpl {
  private final int health;
  private final double speed;
  private final String id;
  private final int children;

  public Actor(PhysicalUID uid, String id, double x, double y, int diameter, double rotation, double xImpulse,
               double yImpulse, double rotationImpulse, int health, double speed, int children) {
    super(uid, x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse);
    this.id = id;
    this.health = health;
    this.speed = speed;
    this.children = children;
  }

  public int getHealth() {
    return health;
  }

  public double getSpeed() {
    return speed;
  }

  /**
   * Very simple stateless AI implementation.
   */
  public Action move(List<VisibleActor> actorList, List<VisibleFood> foodList) {
      double nearestDistance = Double.MAX_VALUE;
      VisibleFood nearest = null;
      for (VisibleFood f: foodList) {
        if (f.getDistance() < nearestDistance) {
          nearest = f;
          nearestDistance = f.getDistance();
        }
      }
      if (nearest == null) {
        double rotation = (getPredictableRandom().nextDouble() - 0.5d) * 10;
        return new Action(speed / 2, rotation);
      }
      double rot = nearest.getRelativeRotationToObject();
      if (Math.abs(rot) < 5) {
        return new Action(speed, 0);
      }

      return new Action(speed / 2, rot / 15);
  }

  public String getId() {
    return id;
  }

  /**
   * @return predictable seed random generator from final values
   */
  public Random getPredictableRandom() {
    return new Random((long)(getX() * getY() * getDiameter() / getHealth() * getId().hashCode() / getSpeed() * getXImpulse() * getYImpulse()));
  }

  public int getChildren() {
    return children;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Actor)) return false;
    if (!super.equals(o)) return false;

    Actor actor = (Actor) o;

    if (health != actor.health) return false;
    if (Double.compare(actor.speed, speed) != 0) return false;
    return id.equals(actor.id);

  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    long temp;
    result = 31 * result + health;
    temp = Double.doubleToLongBits(speed);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + id.hashCode();
    return result;
  }
}
