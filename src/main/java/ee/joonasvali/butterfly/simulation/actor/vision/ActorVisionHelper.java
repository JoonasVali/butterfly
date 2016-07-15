package ee.joonasvali.butterfly.simulation.actor.vision;

import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.simulation.actor.Actor;

/**
 * @author Joonas Vali July 2016
 */
public class ActorVisionHelper {

  private final int visionWidenessDegrees;
  private final int visionDistance;

  public ActorVisionHelper(ButterFlyConfig config) {
    visionWidenessDegrees = config.getActorVisionWidnessDegrees();
    visionDistance = config.getActorVisionDistance();
  }

  public int getActorVisionAX(Actor actor) {
    return (int)(actor.getX() + actor.getDiameter() / 2 + Math.cos(Math.toRadians(actor.getRotation() - visionWidenessDegrees)) * visionDistance);
  }

  public int getActorVisionBX(Actor actor) {
    return (int)(actor.getX() + actor.getDiameter() / 2 + Math.cos(Math.toRadians(actor.getRotation() + visionWidenessDegrees)) * visionDistance);
  }

  public int getActorVisionCX(Actor actor) {
    return (int) (actor.getX() + actor.getDiameter() / 2);
  }

  public int getActorVisionAY(Actor actor) {
    return (int)(actor.getY() + actor.getDiameter() / 2 + Math.sin(Math.toRadians(actor.getRotation() - visionWidenessDegrees)) * visionDistance);
  }

  public int getActorVisionBY(Actor actor) {
    return (int)(actor.getY() + actor.getDiameter() / 2 + Math.sin(Math.toRadians(actor.getRotation() + visionWidenessDegrees)) * visionDistance);
  }

  public int getActorVisionCY(Actor actor) {
    return (int) (actor.getY() + actor.getDiameter() / 2);
  }
}
