package ee.joonasvali.butterfly.spring;


import ee.joonasvali.butterfly.ButterFly;
import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.config.PhysicsConfig;
import ee.joonasvali.butterfly.simulation.PhysicsRunner;
import ee.joonasvali.butterfly.simulation.PhysicsRunnerImpl;
import ee.joonasvali.butterfly.simulation.actor.vision.ActorVisionHelper;
import ee.joonasvali.butterfly.simulation.generator.DefaultInitialStateGenerator;
import ee.joonasvali.butterfly.simulation.generator.InitialStateGenerator;
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
  public InitialStateGenerator getInitialStateGenerator(ButterFlyConfig config) {
    return new DefaultInitialStateGenerator(config);
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
  public ButterFly getButterFly(ButterFlyConfig config, PhysicsRunner runner, InitialStateGenerator generator, ActorVisionHelper helper) {
    return new ButterFly(config, runner, generator, helper);
  }
}
