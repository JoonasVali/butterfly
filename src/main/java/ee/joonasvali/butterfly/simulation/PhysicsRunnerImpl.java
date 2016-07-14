package ee.joonasvali.butterfly.simulation;

import ee.joonasvali.butterfly.simulation.actor.Action;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.actor.WorldView;
import ee.joonasvali.butterfly.simulation.actor.demo.PlayerActor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author Joonas Vali July 2016
 */
public class PhysicsRunnerImpl implements PhysicsRunner {
  public static final int MAX_ROTATION_IMPULSE = 5;
  public static final double IMPULSE_DECAY = 1.1;
  public static final int HEALTH_DECAY = 1;
  public static final int DIAMETER_DETECTION = 100;
  public static final int SIDEWAYS_IMPULSE_MODIFIER = 70;


  private HashMap<Actor, Double> healthToAdd = new HashMap<>();

  @Override
  public SimulationState run(SimulationState original) {
    ArrayList<Actor> actors = original.getActors();
    ArrayList<Actor> newActors = modifyActors(actors, original.getWidth(), original.getHeight());
    ArrayList<Food> newFood = modifyFood(original.getFood(), newActors, original.getWidth(), original.getHeight());
    return new SimulationState(newActors, newFood, original.getWidth(), original.getHeight());
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
      while(it.hasNext()) {
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

        double fmidX = f.getX() + (double)fdiam / 2;
        double fmidY = f.getY() + (double)fdiam / 2;

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
    for (Food f: result) {
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

  private boolean isInRadius(Physical f, double x, double y, int diameter) {
    double radius = diameter / 2;
    double midX = x + radius;
    double midY = y + radius;

    int fRadius = f.getDiameter() / 2;
    double fXmid = f.getX() + (double)fRadius;
    double fYmid = f.getY() + (double)fRadius;
    double dist = Math.sqrt(Math.pow(midX - fXmid, 2) + Math.pow(midY - fYmid, 2));
    return dist < radius + fRadius;



  }

  private ArrayList<Actor> modifyActors(ArrayList<Actor> actors, int width, int height) {
    ArrayList<Actor> newActors = new ArrayList<>(actors.size());
    for (Actor actor : actors) {
      act(actor, width, height).ifPresent(newActors::add);
    }
    healthToAdd.clear();
    return newActors;
  }

  private Optional<Actor> act(Actor actor, int width, int height) {
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

    Action action = actor.move(new WorldView(/* TODO */));

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
      if (actor instanceof PlayerActor) {
        return Optional.of(new PlayerActor(x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse, health, speed));
      } else {
        return Optional.of(new Actor(x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse, health, speed));
      }
    } else {
      return Optional.empty();
    }
  }
}
