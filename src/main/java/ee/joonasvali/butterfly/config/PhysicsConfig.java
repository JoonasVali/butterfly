package ee.joonasvali.butterfly.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Joonas Vali September 2016
 */
public class PhysicsConfig {
  @Value("${i.physics.rotationimpulse.max}")
  private int maxRotationImpulse;
  @Value("${d.physics.impulse.decay}")
  private double impulseDecay;
  @Value("${i.physics.health.decay}")
  private int healthDecay;
  @Value("${i.physics.food.detection.diameter}")
  private int foodDetectionDiameter;
  @Value("${i.physics.sideways.impulse.modifier}")
  private int sideWaysImpulseModifier;
  @Value("${d.physics.collision.force.modifier}")
  private double collisionForceModifier;
  @Value("${i.physics.food.consume.impulse.diff}")
  private int actorFoodConsumeImpulseDiff;

  public int getMaxRotationImpulse() {
    return maxRotationImpulse;
  }

  public double getImpulseDecay() {
    return impulseDecay;
  }

  public int getHealthDecay() {
    return healthDecay;
  }

  public int getFoodDetectionDiameter() {
    return foodDetectionDiameter;
  }

  public int getSidewaysImpulseModifier() {
    return sideWaysImpulseModifier;
  }

  public double getCollisionForceModifier() {
    return collisionForceModifier;
  }

  public int getActorFoodConsumeImpulseDiff() {
    return actorFoodConsumeImpulseDiff;
  }
}
