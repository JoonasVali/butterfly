package ee.joonasvali.butterfly.simulation.generator;

import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.simulation.Food;
import ee.joonasvali.butterfly.simulation.SimulationState;
import ee.joonasvali.butterfly.simulation.actor.Actor;

import java.util.ArrayList;

/**
 * @author Joonas Vali October 2016
 */
public class DefaultInitialStateGenerator implements InitialStateGenerator {
  private static final int FOOD_HEALTH_RECOVER_MODIFIER = 2500;
  private final int actorsInSimulation;
  private final int initialHealth;
  private final int actorDiameter;
  private final int foodDiameter;

  public DefaultInitialStateGenerator(ButterFlyConfig config) {
    actorsInSimulation = config.getActorsInSimulation();
    initialHealth = config.getActorInitialHealth();
    actorDiameter = config.getActorDiameter();
    foodDiameter = config.getFoodDiameter();
  }

  @Override
  public SimulationState createInitialState(int simWidth, int simHeight) {
    return new SimulationState(0, getActors(simWidth, simHeight), getFood(simWidth, simHeight), simWidth, simHeight);
  }

  private ArrayList<Actor> getActors(int simWidth, int simHeight) {
    ArrayList<Actor> actors = new ArrayList<>();
    for (int i = 0; i < actorsInSimulation; i++) {
      actors.add(createRandomActor(simWidth - actorDiameter, simHeight - actorDiameter));
    }
    return actors;
  }

  private Actor createRandomActor(int simWidth, int simHeight) {
    return new Actor(
        getRandomId(),
        (int) (Math.random() * simWidth),
        (int) (Math.random() * simHeight),
        (int) (50 + (Math.random() * (actorDiameter - 50))),
        Math.random() * 360,
        0,
        0,
        0,
        initialHealth,
        1 + Math.random() * 3
    );
  }

  private String getRandomId() {
    String[] firstNames = {"JAMES", "JOHN", "ROBERT", "MICHAEL", "WILLIAM", "DAVID", "RICHARD", "CHARLES", "JOSEPH", "JOFFREY", "BRAN", "LEIA", "LUKE"};
    String[] lastNames = {"GATES", "DOE", "HOLMES", "PARK", "SEAGULL", "BEAR", "TARGARYEN", "SNOW", "LANNISTER", "SMITH", "STARK", "BARATHEON"};
    return firstNames[((int) (Math.random() * firstNames.length))] + " " + lastNames[((int) (Math.random() * lastNames.length))];
  }

  private ArrayList<Food> getFood(int simWidth, int simHeight) {
    ArrayList<Food> food = new ArrayList<>();
    for (int i = 0; i < (simWidth * simHeight) / 10000; i++) {
      food.add(createRandomFood(simWidth, simHeight));
    }
    return food;
  }

  private Food createRandomFood(int simWidth, int simHeight) {
    return new Food(
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

}
