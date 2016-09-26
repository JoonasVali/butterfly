package ee.joonasvali.butterfly.spring;


import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.config.PhysicsConfig;
import ee.joonasvali.butterfly.simulation.PhysicsRunner;
import ee.joonasvali.butterfly.simulation.PhysicsRunnerImpl;
import ee.joonasvali.butterfly.simulation.actor.vision.ActorVisionHelper;
import ee.joonasvali.butterfly.slick.ButterFly;
import ee.joonasvali.butterfly.ui.UI;
import ee.joonasvali.butterfly.ui.UIImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
    @PropertySource("classpath:butterfly.properties"),
    @PropertySource(value = "file:${butterfly.properties.path}", ignoreResourceNotFound = true)
})
public class SpringConfig {
  @Bean
  public ButterFlyConfig getConfiguration() {
    return new ButterFlyConfig();
  }

  @Bean
  public PhysicsConfig getPhysicsConfiguration() {
    return new PhysicsConfig();
  }

  @Bean
  public ActorVisionHelper getActorVisionHelper(ButterFlyConfig config) {
    return new ActorVisionHelper(config);
  }

  @Bean
  public PhysicsRunner getPhysics(ActorVisionHelper helper, PhysicsConfig config) {
    return new PhysicsRunnerImpl(helper, config);
  }

  @Bean
  public ButterFly getButterFly(UI ui, ButterFlyConfig config, PhysicsRunner runner, ActorVisionHelper helper) {
    return new ButterFly(ui, config, runner, helper);
  }

  @Bean
  public UI getUI(ButterFlyConfig config) {
    return new UIImpl(config);
  }

}
