package ee.joonasvali.butterfly.simulation.generator;

import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.simulation.Food;
import ee.joonasvali.butterfly.simulation.PhysicalUID;
import ee.joonasvali.butterfly.simulation.SimulationState;
import ee.joonasvali.butterfly.simulation.actor.Actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Joonas Vali October 2016
 */
public class DefaultInitialStateGenerator implements InitialStateGenerator {
  private static final int FOOD_HEALTH_RECOVER_MODIFIER = 2500;
  private final int actorsInSimulation;
  private final int initialHealth;
  private final int maxActorDiameter;
  private final int foodDiameter;
  private final static String[] FIRST_NAMES = {"JAMES", "JOHN", "ROBERT", "MICHAEL", "WILLIAM", "DAVID", "RICHARD", "CHARLES", "JOSEPH", "JOFFREY", "BRAN", "LEIA", "LUKE", "HARRY"};
  private final static String[] LAST_NAMES = {"GATES", "DOE", "HOLMES", "PARK", "SEAGULL", "BEAR", "TARGARYEN", "SNOW", "LANNISTER", "SMITH", "STARK", "BARATHEON", "POTTER"};
  private final NameGenerator nameGen;


  public DefaultInitialStateGenerator(ButterFlyConfig config) {
    actorsInSimulation = config.getActorsInSimulation();
    initialHealth = config.getActorInitialHealth();
    maxActorDiameter = config.getActorDiameter();
    foodDiameter = config.getFoodDiameter();
    nameGen = new NameGenerator(FIRST_NAMES, LAST_NAMES);
    if (nameGen.getSize() < actorsInSimulation) {
      throw new IllegalStateException("Name generator is able to provide less unique names than actors in simulation. Reduce the actor count.");
    }
  }

  @Override
  public SimulationState createInitialState(int simWidth, int simHeight) {
    return new SimulationState(0, createActors(simWidth, simHeight), createFood(simWidth, simHeight), simWidth, simHeight);
  }

  private ArrayList<Actor> createActors(int simWidth, int simHeight) {
    ArrayList<Actor> actors = new ArrayList<>();
    for (int i = 0; i < actorsInSimulation; i++) {
      actors.add(createRandomActor(simWidth, simHeight));
    }
    return actors;
  }

  private Actor createRandomActor(int simWidth, int simHeight) {
    String id = nameGen.next();
    int diameter = (int) (50 + (Math.random() * (maxActorDiameter - 50)));
    return new Actor(
        new PhysicalUID(),
        id,
        (int) (Math.random() * (simWidth - diameter)),
        (int) (Math.random() * (simHeight - diameter)),
        diameter,
        Math.random() * 360,
        0,
        0,
        0,
        initialHealth,
        1 + Math.random() * 3,
        0
    );
  }

  private ArrayList<Food> createFood(int simWidth, int simHeight) {
    ArrayList<Food> food = new ArrayList<>();
    for (int i = 0; i < (simWidth * simHeight) / 10000; i++) {
      food.add(createRandomFood(simWidth, simHeight));
    }
    return food;
  }

  private Food createRandomFood(int simWidth, int simHeight) {
    return new Food(
        new PhysicalUID(),
        (int) (Math.random() * simWidth),
        (int) (Math.random() * simHeight),
        foodDiameter,
        Math.random() * 360,
        0,
        0,
        0,
        FOOD_HEALTH_RECOVER_MODIFIER
    );
  }

  private static class NameGenerator {
    private List<String> names = new ArrayList<>();

    NameGenerator(String[] firstNames, String[] lastNames) {
      for (String firstName : firstNames) {
        for (String lastName : lastNames) {
          names.add(firstName + " " + lastName);
        }
      }
      Collections.shuffle(names);
    }

    int getSize() {
      return names.size();
    }

    String next() {
      return names.remove(names.size() - 1);
    }
  }

}
