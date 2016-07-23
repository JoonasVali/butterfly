package ee.joonasvali.butterfly.simulation.actor;

import ee.joonasvali.butterfly.code.Immutable;
import ee.joonasvali.butterfly.simulation.PhysicalImpl;
import ee.joonasvali.butterfly.simulation.actor.vision.VisibleActor;
import ee.joonasvali.butterfly.simulation.actor.vision.VisibleFood;

import java.util.List;

/**
 * @author Joonas Vali July 2016
 */
@Immutable
public class Actor extends PhysicalImpl {
  private final int health;
  private final double speed;
  private final String id;

  public Actor(String id, double x, double y, int diameter, double rotation, double xImpulse, double yImpulse, double rotationImpulse, int health, double speed) {
    super(x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse);
    this.id = id;
    this.health = health;
    this.speed = speed;
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

        return new Action(speed / 2, -3);
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
}
