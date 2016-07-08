package ee.joonasvali.butterfly;

import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.slick.ButterFly;
import ee.joonasvali.butterfly.spring.SpringConfig;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.awt.*;

public class Boot {
  private final static Logger log = LoggerFactory.getLogger(Boot.class);

  /**
   * Right place to read program args for deciding what to start up. Currently only default behavior set up, no args read.
   *
   * Fullscreen and similar parameters could be taken from program args, then runners under 'bin' folder would be needed to modify accordingly.
   */
  public static void main(String[] args) {
    log.info("Starting system up.");

    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
    ButterFly fly = ctx.getBean(ButterFly.class);

    if (GraphicsEnvironment.isHeadless()) {
      log.error("This system is not supported as it appears to be headless. GraphicsEnvironment.isHeadless() == true");
      System.exit(-1);
    }

    ButterFlyConfig config = ctx.getBean(ButterFlyConfig.class);
    try {
      AppGameContainer container = new AppGameContainer(fly);
      container.setForceExit(true);
      container.setDisplayMode(config.getWindowResolutionWidth(), config.getWindowResolutionHeight(), config.isFullscreen());
      container.setVSync(true);
      container.start();
    } catch (SlickException e) {
      log.error("Unable to launch Mirrors", e);
    }
  }

}
