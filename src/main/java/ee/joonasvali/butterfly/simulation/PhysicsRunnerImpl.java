package ee.joonasvali.butterfly.simulation;

import ee.joonasvali.butterfly.simulation.actor.Action;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.actor.vision.ActorVisionHelper;
import ee.joonasvali.butterfly.simulation.actor.vision.VisibleActor;
import ee.joonasvali.butterfly.simulation.actor.vision.VisibleFood;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author Joonas Vali July 2016
 */
public class PhysicsRunnerImpl implements PhysicsRunner {
  public static final int MAX_ROTATION_IMPULSE = 5;
  public static final double IMPULSE_DECAY = 1.1;
  public static final int HEALTH_DECAY = 1;
  public static final int DIAMETER_DETECTION = 100;
  public static final int SIDEWAYS_IMPULSE_MODIFIER = 170;
  private final ActorVisionHelper visionHelper;
  // TODO remove this.
  private final HashMap<Actor, Double> healthToAdd = new HashMap<>();

  public PhysicsRunnerImpl(ActorVisionHelper visionHelper) {
    this.visionHelper = visionHelper;
  }

  @Override
  public SimulationState run(SimulationState original) {
    ArrayList<Actor> actors = original.getActors();
    ArrayList<Actor> newActors = modifyActors(actors, original.getFood(), original.getWidth(), original.getHeight());
    ArrayList<Food> newFood = modifyFood(original.getFood(), newActors, original.getWidth(), original.getHeight());
    return new SimulationState(original.getFrameNumber() + 1, newActors, newFood, original.getWidth(), original.getHeight());
  }

  private ArrayList<Food> modifyFood(ArrayList<Food> food, ArrayList<Actor> actors, int width, int height) {
    ArrayList<Food> result = new ArrayList<>(food);
    ArrayList<Food> added = new ArrayList<>(food.size());
    for (Actor actor : actors) {
      double x = actor.getX();
      double y = actor.getY();
      int diameter = actor.getDiameter();
      if (!added.isEmpty()) {
        result.addAll(added);
        added.clear();
      }
      Iterator<Food> it = result.iterator();
      while (it.hasNext()) {
        Food f = it.next();
        double fx = f.getX();
        double fy = f.getY();
        double frot = f.getRotation();
        int fdiam = f.getDiameter();
        double points = f.getPoints();
        double fXImp = f.getXImpulse();
        double fYImp = f.getYImpulse();

        double fRotImpulse = f.getRotationImpulse();

        double midX = x + diameter / 2;
        double midY = y + diameter / 2;

        double fmidX = f.getX() + (double) fdiam / 2;
        double fmidY = f.getY() + (double) fdiam / 2;

        if (isInRadius(f, x, y, diameter)) {
          Double health = healthToAdd.get(actor);
          if (health == null) {
            healthToAdd.put(actor, f.getPoints());
          } else {
            healthToAdd.put(actor, f.getPoints() + health);
          }
          it.remove();
          continue;
        } else if (isInRadius(f, x - DIAMETER_DETECTION / 2, y - DIAMETER_DETECTION / 2, diameter + DIAMETER_DETECTION)) {
          // If food close to actor, but not in radius.
          double impulse = getImpulseVector(actor);
          fXImp += (fmidX - midX) / (SIDEWAYS_IMPULSE_MODIFIER - Math.abs(actor.getYImpulse() /* Y on purpose */));
          fYImp += (fmidY - midY) / (SIDEWAYS_IMPULSE_MODIFIER - Math.abs(actor.getXImpulse() /* X on purpose */));
        }

        Food newFood = new Food(fx, fy, fdiam, frot, fXImp, fYImp, fRotImpulse, points);
        it.remove();
        added.add(newFood);
      }
    }

    result.addAll(added);
    added.clear();

    // Make the actual movement
    for (Food f : result) {
      double fx = f.getX();
      double fy = f.getY();
      double frot = f.getRotation();
      int fdiam = f.getDiameter();
      double points = f.getPoints();
      double fXImp = f.getXImpulse();
      double fYImp = f.getYImpulse();

      double fRotImpulse = f.getRotationImpulse();

      fx += fXImp;
      fy += fYImp;

          /*
            Limit world boundaries
          */
      fx = Math.min(Math.max(0, fx), width - f.getDiameter());
      fy = Math.min(Math.max(0, fy), height - f.getDiameter());

      fXImp = (fx - f.getX()) / IMPULSE_DECAY;
      fYImp = (fy - f.getY()) / IMPULSE_DECAY;

      Food newFood = new Food(fx, fy, fdiam, frot, fXImp, fYImp, fRotImpulse, points);
      added.add(newFood);
    }

    result.clear();
    result.addAll(added);
    return result;
  }

  private double getImpulseVector(Physical p) {
    return Math.sqrt(Math.pow(p.getXImpulse(), 2) + Math.pow(p.getYImpulse(), 2));
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

  private ArrayList<Actor> modifyActors(List<Actor> actors, List<Food> food, int width, int height) {
    ArrayList<Actor> newActors = new ArrayList<>(actors.size());
    for (Actor actor : actors) {
      act(actor, actors, food, width, height).ifPresent(newActors::add);
    }
    healthToAdd.clear();
    return newActors;
  }

  private Optional<Actor> act(Actor actor, List<Actor> actors, List<Food> foods, int width, int height) {
    double rotation = actor.getRotation();
    int health = actor.getHealth() - HEALTH_DECAY;
    double x = actor.getX();
    double y = actor.getY();
    int diameter = actor.getDiameter();
    double rotationImpulse = actor.getRotationImpulse();
    double xImpulse = actor.getXImpulse();
    double yImpulse = actor.getYImpulse();
    double speed = actor.getSpeed();

    Double healthToAdd = this.healthToAdd.get(actor);
    if (healthToAdd != null) {
      health += healthToAdd;
    }

    List<VisibleActor> visibleActors = getVisibleActors(actor, actors);
    List<VisibleFood> visibleFoods = getVisibleFoods(actor, foods);

    Action action = actor.move(visibleActors, visibleFoods);

    rotationImpulse += action.getRotation();
    rotationImpulse = Math.min(rotationImpulse, MAX_ROTATION_IMPULSE);
    rotationImpulse = Math.max(rotationImpulse, -MAX_ROTATION_IMPULSE);

    double thrust = action.getThrust();

    rotation += rotationImpulse;
    rotationImpulse = (rotation - actor.getRotation()) / IMPULSE_DECAY;

    double yMovement;
    double xMovement;

    xMovement = thrust * Math.cos(Math.toRadians(rotation));
    yMovement = thrust * Math.sin(Math.toRadians(rotation));

    x += xMovement + xImpulse;
    y += yMovement + yImpulse;

    /*
      Limit world boundaries
     */
    x = Math.min(Math.max(0, x), width - actor.getDiameter());
    y = Math.min(Math.max(0, y), height - actor.getDiameter());

    xImpulse = (x - actor.getX()) / IMPULSE_DECAY;
    yImpulse = (y - actor.getY()) / IMPULSE_DECAY;


    if (health > 0) {
      return Optional.of(new Actor(actor.getId(), x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse, health, speed));
    } else {
      return Optional.empty();
    }
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
