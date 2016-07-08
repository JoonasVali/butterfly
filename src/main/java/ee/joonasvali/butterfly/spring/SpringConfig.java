package ee.joonasvali.butterfly.spring;


import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.simulation.PhysicsRunner;
import ee.joonasvali.butterfly.simulation.PhysicsRunnerImpl;
import ee.joonasvali.butterfly.slick.ButterFly;
import ee.joonasvali.butterfly.ui.SimulationPainter;
import ee.joonasvali.butterfly.ui.SimulationPainterImpl;
import ee.joonasvali.butterfly.ui.UI;
import ee.joonasvali.butterfly.ui.UIImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:butterfly.properties")
public class SpringConfig {
  @Bean
  public ButterFlyConfig getConfiguration() {
    return new ButterFlyConfig();
  }

  @Bean
  public PhysicsRunner getPhysics() {
    return new PhysicsRunnerImpl();
  }

  @Bean
  public ButterFly getButterFly(UI ui, ButterFlyConfig config, PhysicsRunner runner) {
    return new ButterFly(ui, config, runner);
  }

  @Bean
  public UI getUI(ButterFlyConfig config) {
    return new UIImpl(config);
  }

}
