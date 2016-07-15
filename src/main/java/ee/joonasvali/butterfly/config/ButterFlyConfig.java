package ee.joonasvali.butterfly.config;

import org.springframework.beans.factory.annotation.Value;

public class ButterFlyConfig {
  @Value("${window.width}")
  private int windowResolutionWidth;
  @Value("${window.height}")
  private int windowResolutionHeight;
  @Value("${window.fullscreen}")
  private boolean fullscreen;
  @Value("${actor.diameter}")
  private int actorDiameter;
  @Value("${food.diameter}")
  private int foodDiameter;
  @Value("${actor.vision.wideness.degrees}")
  private int actorVisionWidnessDegrees;
  @Value("${actor.vision.distance}")
  private int actorVisionDistance;
  @Value("${simulation.size.multiplier}")
  private int simulationSizeMultiplier;
  @Value("${actor.count}")
  private int actorsInSimulation;
  @Value("${actor.initialhealth}")
  private int actorInitialHealth;

  public int getWindowResolutionWidth() {
    return windowResolutionWidth;
  }

  public int getWindowResolutionHeight() {
    return windowResolutionHeight;
  }

  public boolean isFullscreen() {
    return fullscreen;
  }

  public int getActorDiameter() {
    return actorDiameter;
  }

  public int getFoodDiameter() {
    return foodDiameter;
  }

  public int getActorVisionWidnessDegrees() {
    return actorVisionWidnessDegrees;
  }

  public int getActorVisionDistance() {
    return actorVisionDistance;
  }

  public int getSimulationSizeMultiplier() {
    return simulationSizeMultiplier;
  }

  public int getActorsInSimulation() {
    return actorsInSimulation;
  }

  public int getActorInitialHealth() {
    return actorInitialHealth;
  }
}
