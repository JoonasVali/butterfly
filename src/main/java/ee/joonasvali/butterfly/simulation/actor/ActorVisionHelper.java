package ee.joonasvali.butterfly.simulation.actor;

/**
 * @author Joonas Vali July 2016
 */
public class ActorVisionHelper {

  private static final int VISION_WIDENESS_DEGREES = 30;
  private static final int VISION_DISTANCE = 500;
  public static int getActorVisionAX(Actor actor) {
    return (int)(actor.getX() + actor.getDiameter() / 2 + Math.cos(Math.toRadians(actor.getRotation() - 30)) * VISION_DISTANCE);
  }

  public static int getActorVisionBX(Actor actor) {
    return (int)(actor.getX() + actor.getDiameter() / 2 + Math.cos(Math.toRadians(actor.getRotation() + 30)) * VISION_DISTANCE);
  }

  public static int getActorVisionCX(Actor actor) {
    return (int) (actor.getX() + actor.getDiameter() / 2);
  }

  public static int getActorVisionAY(Actor actor) {
    return (int)(actor.getY() + actor.getDiameter() / 2 + Math.sin(Math.toRadians(actor.getRotation() - 30)) * VISION_DISTANCE);
  }

  public static int getActorVisionBY(Actor actor) {
    return (int)(actor.getY() + actor.getDiameter() / 2 + Math.sin(Math.toRadians(actor.getRotation() + 30)) * VISION_DISTANCE);
  }

  public static int getActorVisionCY(Actor actor) {
    return (int) (actor.getY() + actor.getDiameter() / 2);
  }
}
