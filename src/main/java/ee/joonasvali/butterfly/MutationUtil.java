package ee.joonasvali.butterfly;

import ee.joonasvali.butterfly.simulation.ActorBuilder;

import java.util.Random;

/**
 * @author Joonas Vali July 2016
 */
public class MutationUtil {

  /**
   * @param actorBuilder
   * @param random predictable random generator.
   */
  public static void mutate(ActorBuilder actorBuilder, Random random) {
    actorBuilder.setDiameter((int) mutate(actorBuilder.getDiameter(), 30, 5, 500, random));
    actorBuilder.setHealth((int) mutate(actorBuilder.getHealth(), 100, 5, Integer.MAX_VALUE, random));
    actorBuilder.setRotation((int) mutate(actorBuilder.getRotation(), 360, 0, 360, random));
  }

  private static double mutate(double num, double step, int min, int max, Random random) {
    double ans = num + (random.nextDouble() * step - (step / 2));
    return limit(ans, min, max);
  }

  private static double limit(double num, double min, double max) {
    return Math.min(max, Math.max(num, min));
  }


}
