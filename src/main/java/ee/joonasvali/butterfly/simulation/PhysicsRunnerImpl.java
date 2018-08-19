package ee.joonasvali.butterfly.simulation;

import ee.joonasvali.butterfly.MutationUtil;
import ee.joonasvali.butterfly.code.Immutable;
import ee.joonasvali.butterfly.config.PhysicsConfig;
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
  private static final int ACTOR_DIAMETER_HEALTH_CLONE_RATIO = 15;
  private final int maxRotationImpulse;
  private final double impulseDecay;
  private final int healthDecay;
  private final int foodDetectionDiameter;
  private final int sidewaysImpulseModifier;
  private final double collisionForceModifier;
  private final int foodActorImpulseDiff;
  private final ActorVisionHelper visionHelper;

  public PhysicsRunnerImpl(ActorVisionHelper visionHelper, PhysicsConfig config) {
    this.visionHelper = visionHelper;
    maxRotationImpulse = config.getMaxRotationImpulse();
    impulseDecay = config.getImpulseDecay();
    healthDecay = config.getHealthDecay();
    foodDetectionDiameter = config.getFoodDetectionDiameter();
    sidewaysImpulseModifier = config.getSidewaysImpulseModifier();
    collisionForceModifier = config.getCollisionForceModifier();
    foodActorImpulseDiff = config.getActorFoodConsumeImpulseDiff();
  }

  @Override
  public SimulationState run(SimulationState original) {
    List<Actor> actors = original.getActors();
    List<ActorBuilder> actorBuilders = modifyActors(actors, original.getFoods(), original.getWidth(), original.getHeight());
    List<Food> newFood = modifyFood(original.getFoods(), actorBuilders, original.getWidth(), original.getHeight());
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

        // Check that actor is on food && food and actor impulses difference is low enough
        if (isInRadius(f, x, y, diameter) && Math.abs(f.getXImpulse() - actor.getXImpulse()) < foodActorImpulseDiff && Math.abs(f.getYImpulse() - actor.getYImpulse()) < foodActorImpulseDiff) {
          actor.setHealth((int) (actor.getHealth() + Math.round(f.getPoints() / actor.getDiameter())));
          double foodImpulseDecay = Math.max(0, (0.8 - (10 * f.getDiameter() / diameter)));
          actor.setXImpulse(actor.getXImpulse() * foodImpulseDecay);
          actor.setYImpulse(actor.getYImpulse() * foodImpulseDecay);
          it.remove();
        } else if (isInRadius(f, x - foodDetectionDiameter / 2, y - foodDetectionDiameter / 2, diameter + foodDetectionDiameter)) {
          // If food close to actor, but not in radius.
          f.setXImpulse(f.getXImpulse() + (fmidX - midX) / (sidewaysImpulseModifier - Math.abs(actor.getYImpulse() /* Y on purpose */)));
          f.setYImpulse(f.getYImpulse() + (fmidY - midY) / (sidewaysImpulseModifier - Math.abs(actor.getXImpulse() /* X on purpose */)));
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

      f.setXImpulse((f.getX() - f.getOriginal().getX()) / impulseDecay);
      f.setYImpulse((f.getY() - f.getOriginal().getY()) / impulseDecay);
    }

    return result.stream().map(FoodBuilder::build).collect(Collectors.toList());
  }

  private boolean isCollision(Physical one, Physical two) {
    return isInRadius(one, two.getX(), two.getY(), two.getDiameter());
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
      ActorBuilder builder = act(actor, actors, food, width, height);
      newActors.add(builder);
      // Split actor into two
      if (builder.getHealth() > builder.getDiameter() * ACTOR_DIAMETER_HEALTH_CLONE_RATIO) {
        builder.setChildren(builder.getChildren() + 1);
        builder.setHealth(builder.getHealth() / 2);
        ActorBuilder clone = new ActorBuilder(builder.build());
        /* Can't just create a 'new PhysicalUID()' here, as separate tracks would end up
           creating non-identical UID's for clones, which means for all purposes the clone
           created in track A is not the same actor, as the clone created in track B, even if they would
           end up being indifferent. Instead we try to derive it from parent deterministically.
        */
        clone.setUid(derive(actor));
        MutationUtil.mutate(clone, actor.getPredictableRandom());
        newActors.add(clone);
      }
    }

    modifyCollisions(newActors);
    return newActors;
  }

  private PhysicalUID derive(Actor actor) {
    PhysicalUID uid = actor.getUID();
    /* We pass the number of children this actor has as a seed, which logically means that n-th child for actor
    * in one track has matching PhysicalUID for the n-th child in other track. This solves the issue that
    * even slight butterfly effect will cause the children in separate tracks not to be linked at all, as would be the
    * case if we'd try to generate the PhysicalUID based on the properties of the parent.
    * */
    return uid.getDerivativeUID(actor.getChildren());
  }

  private void modifyCollisions(List<ActorBuilder> actors) {
    for (ActorBuilder actor : actors) {
      for (ActorBuilder actor2 : actors) {
        if (actor == actor2) {
          continue;
        }
        if (isCollision(actor, actor2)) {
          double xdelta = actor.getX() - actor2.getX();
          double ydelta = actor.getY() - actor2.getY();
          double xForce = xdelta / collisionForceModifier;
          double yForce = ydelta / collisionForceModifier;
          xForce = xForce * ((double)actor2.getDiameter() / (double)actor.getDiameter());
          yForce = yForce * ((double)actor2.getDiameter() / (double)actor.getDiameter());
          actor.setXImpulse(actor.getXImpulse() + xForce);
          actor.setYImpulse(actor.getYImpulse() + yForce);
          int force = (int) Math.sqrt(Math.pow(xForce, 2) + Math.pow(yForce, 2));
          actor.setHealth(Math.max(0, actor.getHealth() - force));
        }
      }
    }
  }

  private ActorBuilder act(Actor actor, List<Actor> actors, List<Food> foods, int width, int height) {
    ActorBuilder builder = new ActorBuilder(actor);
    builder.setHealth(builder.getHealth() - healthDecay);

    List<VisibleActor> visibleActors = getVisibleActors(actor, actors);
    List<VisibleFood> visibleFoods = getVisibleFoods(actor, foods);

    Action action = actor.move(visibleActors, visibleFoods);

    builder.setRotationImpulse(builder.getRotationImpulse() + action.getRotation());
    builder.setRotationImpulse(Math.min(builder.getRotationImpulse(), maxRotationImpulse));
    builder.setRotationImpulse(Math.max(builder.getRotationImpulse(), -maxRotationImpulse));

    double thrust = action.getThrust();

    builder.setRotation(builder.getRotation() + builder.getRotationImpulse());
    builder.setRotationImpulse((builder.getRotation() - actor.getRotation()) / impulseDecay);

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

    builder.setXImpulse((builder.getX() - actor.getX()) / impulseDecay);
    builder.setYImpulse((builder.getY() - actor.getY()) / impulseDecay);

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
