package ee.joonasvali.butterfly.simulation;

import ee.joonasvali.butterfly.simulation.actor.Action;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.actor.WorldView;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Joonas Vali July 2016
 */
public class PhysicsRunnerImpl implements PhysicsRunner {
  public static final int MAX_ROTATION_IMPULSE = 5;
  public static final double IMPULSE_DECAY = 1.1;


  @Override
  public SimulationState run(SimulationState original) {
    ArrayList<Actor> actors = original.getActors();
    ArrayList<Actor> newActors = modifyActors(actors, original.getWidth(), original.getHeight());
    ArrayList<Food> newFood = modifyFood(original.getFood(), original.getWidth(), original.getHeight());
    return new SimulationState(newActors, newFood, original.getWidth(), original.getHeight());
  }

  private ArrayList<Food> modifyFood(ArrayList<Food> food, int width, int height) {
    return food; // TODO
  }

  private ArrayList<Actor> modifyActors(ArrayList<Actor> actors, int width, int height) {
    ArrayList<Actor> newActors = new ArrayList<>(actors.size());
    for (Actor actor : actors) {
      act(actor, width, height).ifPresent(newActors::add);
    }
    return newActors;
  }

  private Optional<Actor> act(Actor actor, int width, int height) {
    double rotation = actor.getRotation();
    int health = actor.getHealth();
    int x = actor.getX();
    int y = actor.getY();
    int diameter = actor.getDiameter();
    double rotationImpulse = actor.getRotationImpulse();
    double xImpulse = actor.getXImpulse();
    double yImpulse = actor.getYImpulse();
    double speed = actor.getSpeed();

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

    x+= xMovement + xImpulse;
    y+= yMovement + yImpulse;

    /*
      Limit world boundaries
     */
    x = Math.min(Math.max(0, x), width - actor.getDiameter());
    y = Math.min(Math.max(0, y), height - actor.getDiameter());

    xImpulse = (x - actor.getX()) / IMPULSE_DECAY;
    yImpulse = (y - actor.getY()) / IMPULSE_DECAY;

    return Optional.of(new Actor(x, y, diameter, rotation, xImpulse, yImpulse, rotationImpulse, health, speed));
  }
}
