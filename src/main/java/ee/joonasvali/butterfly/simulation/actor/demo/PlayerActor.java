package ee.joonasvali.butterfly.simulation.actor.demo;

import ee.joonasvali.butterfly.simulation.actor.Action;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.actor.vision.VisibleActor;
import ee.joonasvali.butterfly.simulation.actor.vision.VisibleFood;

import java.util.List;

/**
 * For debugging reasons.
 */
public class PlayerActor extends Actor {
  public PlayerActor(double x, double y, int diameter, double rotation, double xImpulse, double yImpulse, double rotationImpulse, int health, double speed) {
    super(x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse, health, speed);
  }

  private volatile double thrust;
  private volatile double rotate;

  public void setMove(double thrust, double rotate) {
    this.thrust = thrust;
    this.rotate = rotate;
  }

  @Override
  public Action move(List<VisibleActor> actorList, List<VisibleFood> foodList) {
    return new Action(thrust, rotate);
  }
}
