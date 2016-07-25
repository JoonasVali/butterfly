package ee.joonasvali.butterfly.simulation;

import ee.joonasvali.butterfly.code.Immutable;
import ee.joonasvali.butterfly.simulation.actor.Action;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.actor.vision.ActorVisionHelper;
import ee.joonasvali.butterfly.simulation.actor.vision.VisibleActor;
import ee.joonasvali.butterfly.simulation.actor.vision.VisibleFood;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Joonas Vali July 2016
 */
@Immutable
public class PhysicsRunnerImpl implements PhysicsRunner {
  public static final int MAX_ROTATION_IMPULSE = 5;
  public static final double IMPULSE_DECAY = 1.1;
  public static final int HEALTH_DECAY = 1;
  public static final int DIAMETER_DETECTION = 100;
  public static final int SIDEWAYS_IMPULSE_MODIFIER = 170;
  private final ActorVisionHelper visionHelper;

  public PhysicsRunnerImpl(ActorVisionHelper visionHelper) {
    this.visionHelper = visionHelper;
  }

  @Override
  public SimulationState run(SimulationState original) {
    List<Actor> actors = original.getActors();
    List<ActorBuilder> actorBuilders = modifyActors(actors, original.getFood(), original.getWidth(), original.getHeight());
    List<Food> newFood = modifyFood(original.getFood(), actorBuilders, original.getWidth(), original.getHeight());
    List<Actor> finalActors = new ArrayList<>();
    for (ActorBuilder ab : actorBuilders) {
      if (ab.getHealth() > 0) {
        finalActors.add(ab.build());
      }
    }
    return new SimulationState(original.getFrameNumber() + 1, finalActors, newFood, original.getWidth(), original.getHeight());
  }

  private List<Food> modifyFood(List<Food> food, List<ActorBuilder> actors, int width, int height) {
    List<FoodBuilder> result = food.stream().map(FoodBuilder::new).collect(Collectors.toList());

    for (ActorBuilder actor : actors) {
      double x = actor.getX();
      double y = actor.getY();
      int diameter = actor.getDiameter();
      Iterator<FoodBuilder> it = result.iterator();
      while (it.hasNext()) {
        FoodBuilder f = it.next();

        double midX = x + diameter / 2;
        double midY = y + diameter / 2;

        double fmidX = f.getX() + (double) f.getDiameter() / 2;
        double fmidY = f.getY() + (double) f.getDiameter() / 2;

        if (isInRadius(f, x, y, diameter)) {
          actor.setHealth((int) (actor.getHealth() + Math.round(f.getPoints())));
          it.remove();
        } else if (isInRadius(f, x - DIAMETER_DETECTION / 2, y - DIAMETER_DETECTION / 2, diameter + DIAMETER_DETECTION)) {
          // If food close to actor, but not in radius.
          f.setXImpulse(f.getXImpulse() + (fmidX - midX) / (SIDEWAYS_IMPULSE_MODIFIER - Math.abs(actor.getYImpulse() /* Y on purpose */)));
          f.setYImpulse(f.getYImpulse() + (fmidY - midY) / (SIDEWAYS_IMPULSE_MODIFIER - Math.abs(actor.getXImpulse() /* X on purpose */)));
        }
      }
    }

    // Make the actual movement
    for (FoodBuilder f : result) {
      f.setX(f.getX() + f.getXImpulse());
      f.setY(f.getY() + f.getYImpulse());

      /*
        Limit world boundaries
      */
      f.setX(Math.min(Math.max(0, f.getX()), width - f.getDiameter()));
      f.setY(Math.min(Math.max(0, f.getY()), height - f.getDiameter()));

      f.setXImpulse((f.getX() - f.getOriginal().getX()) / IMPULSE_DECAY);
      f.setYImpulse((f.getY() - f.getOriginal().getY()) / IMPULSE_DECAY);
    }

    return result.stream().map(FoodBuilder::build).collect(Collectors.toList());
  }

  private boolean isInRadius(Physical f, double x, double y, int diameter) {
    double radius = diameter / 2;
    double midX = x + radius;
    double midY = y + radius;

    int fRadius = f.getDiameter() / 2;
    double fXmid = f.getX() + (double) fRadius;
    double fYmid = f.getY() + (double) fRadius;
    double dist = Math.sqrt(Math.pow(midX - fXmid, 2) + Math.pow(midY - fYmid, 2));
    return dist < radius + fRadius;


  }

  private ArrayList<ActorBuilder> modifyActors(List<Actor> actors, List<Food> food, int width, int height) {
    ArrayList<ActorBuilder> newActors = new ArrayList<>(actors.size());
    for (Actor actor : actors) {
      newActors.add(act(actor, actors, food, width, height));
    }
    return newActors;
  }

  private ActorBuilder act(Actor actor, List<Actor> actors, List<Food> foods, int width, int height) {
    ActorBuilder builder = new ActorBuilder(actor);
    builder.setHealth(builder.getHealth() - HEALTH_DECAY);

    List<VisibleActor> visibleActors = getVisibleActors(actor, actors);
    List<VisibleFood> visibleFoods = getVisibleFoods(actor, foods);

    Action action = actor.move(visibleActors, visibleFoods);

    builder.setRotationImpulse(builder.getRotationImpulse() + action.getRotation());
    builder.setRotationImpulse(Math.min(builder.getRotationImpulse(), MAX_ROTATION_IMPULSE));
    builder.setRotationImpulse(Math.max(builder.getRotationImpulse(), -MAX_ROTATION_IMPULSE));

    double thrust = action.getThrust();

    builder.setRotation(builder.getRotation() + builder.getRotationImpulse());
    builder.setRotationImpulse((builder.getRotation() - actor.getRotation()) / IMPULSE_DECAY);

    double yMovement;
    double xMovement;

    xMovement = thrust * Math.cos(Math.toRadians(builder.getRotation()));
    yMovement = thrust * Math.sin(Math.toRadians(builder.getRotation()));

    builder.setX(builder.getX() + xMovement + builder.getXImpulse());
    builder.setY(builder.getY() + yMovement + builder.getYImpulse());

    /*
      Limit world boundaries
     */
    builder.setX(Math.min(Math.max(0, builder.getX()), width - actor.getDiameter()));
    builder.setY(Math.min(Math.max(0, builder.getY()), height - actor.getDiameter()));

    builder.setXImpulse((builder.getX() - actor.getX()) / IMPULSE_DECAY);
    builder.setYImpulse((builder.getY() - actor.getY()) / IMPULSE_DECAY);

    return builder;
  }

  private List<VisibleFood> getVisibleFoods(Actor actor, List<Food> foods) {
    int ax = visionHelper.getActorVisionAX(actor);
    int ay = visionHelper.getActorVisionAY(actor);
    int bx = visionHelper.getActorVisionBX(actor);
    int by = visionHelper.getActorVisionBY(actor);
    int cx = visionHelper.getActorVisionCX(actor);
    int cy = visionHelper.getActorVisionCY(actor);

    List<VisibleFood> result = Collections.emptyList();
    for (Food food : foods) {
      if (isPointInTriangle(food.getRoundedX() + food.getDiameter() / 2, food.getRoundedY() + food.getDiameter() / 2, ax, ay, bx, by, cx, cy)) {
        if (!(result instanceof ArrayList)) {
          result = new ArrayList<>();
        }
        result.add(new VisibleFood(actor, food));
      }
    }
    return result;
  }

  private List<VisibleActor> getVisibleActors(Actor actor, List<Actor> actors) {
    int ax = visionHelper.getActorVisionAX(actor);
    int ay = visionHelper.getActorVisionAY(actor);
    int bx = visionHelper.getActorVisionBX(actor);
    int by = visionHelper.getActorVisionBY(actor);
    int cx = visionHelper.getActorVisionCX(actor);
    int cy = visionHelper.getActorVisionCY(actor);

    List<VisibleActor> result = Collections.emptyList();
    for (Actor other : actors) {
      if (other == actor) {
        continue;
      }
      if (isPointInTriangle(other.getRoundedX() + other.getDiameter() / 2, other.getRoundedY() + other.getDiameter() / 2, ax, ay, bx, by, cx, cy)) {
        if (!(result instanceof ArrayList)) {
          result = new ArrayList<>(3);
        }
        result.add(new VisibleActor(actor, other));
      }
    }
    return result;
  }

  /**
   * function taken from http://stackoverflow.com/questions/2049582/how-to-determine-a-point-in-a-2d-triangle
   */
  private boolean isPointInTriangle(int x, int y, int ax, int ay, int bx, int by, int cx, int cy) {
    int as_x = x - ax;
    int as_y = y - ay;

    boolean s_ab = (bx - ax) * as_y - (by - ay) * as_x > 0;

    if ((cx - ax) * as_y - (cy - ay) * as_x > 0 == s_ab) return false;

    if ((cx - bx) * (y - by) - (cy - by) * (x - bx) > 0 != s_ab) return false;

    return true;
  }
}
