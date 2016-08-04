package ee.joonasvali.butterfly;

import ee.joonasvali.butterfly.simulation.Food;
import ee.joonasvali.butterfly.simulation.SimulationState;
import ee.joonasvali.butterfly.simulation.actor.Actor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joonas Vali July 2016
 */
public class MutationUtil {

  public static SimulationState mutate(SimulationState simulationState) {
    List<Actor> actorList = simulationState.getActors();
    List<Food> foodList = removeOneFood(simulationState.getFood());
    int w = simulationState.getWidth();
    int h = simulationState.getHeight();
    return new SimulationState(simulationState.getFrameNumber(), actorList, foodList, w, h);
  }

  private static List<Actor> removeOne(List<Actor> actors) {
    List<Actor> n = new ArrayList<>(actors);
    n.remove((int)(Math.random() * n.size()));
    return n;
  }

  private static List<Food> removeOneFood(List<Food> food) {
    List<Food> n = new ArrayList<>(food);
    for (int i = 0; i < 1 ; i++)
      n.remove((int)(Math.random() * n.size()));
    return n;
  }
}
