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
}
