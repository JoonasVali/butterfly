package ee.joonasvali.butterfly.config;

import org.springframework.beans.factory.annotation.Value;

import java.awt.Toolkit;

public class ButterFlyConfig {
  public static final int REAL_SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
  @Value("${i.window.width}")
  private int windowResolutionWidth;
  @Value("${i.window.height}")
  private int windowResolutionHeight;
  @Value("${b.window.fullscreen}")
  private boolean fullscreen;
  @Value("${i.actor.diameter}")
  private int actorDiameter;
  @Value("${i.food.diameter}")
  private int foodDiameter;
  @Value("${i.actor.vision.wideness.degrees}")
  private int actorVisionWidnessDegrees;
  @Value("${i.actor.vision.distance}")
  private int actorVisionDistance;
  @Value("${i.simulation.size.multiplier}")
  private int simulationSizeMultiplier;
  @Value("${i.actor.count}")
  private int actorsInSimulation;
  @Value("${i.actor.initialhealth}")
  private int actorInitialHealth;
  @Value("${i.simulation.length}")
  private int framesInSimulation;

  public int getWindowResolutionWidth() {
    if (windowResolutionWidth <= 0) {
      return Toolkit.getDefaultToolkit().getScreenSize().width;
    }
    return windowResolutionWidth;
  }

  public int getWindowResolutionHeight() {
    if (windowResolutionHeight <= 0) {
      return REAL_SCREEN_HEIGHT;
    }
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

  public int getFramesInSimulation() {
    return framesInSimulation;
  }
}
