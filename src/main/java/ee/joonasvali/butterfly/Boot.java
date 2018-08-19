package ee.joonasvali.butterfly;

import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.spring.SpringConfig;
import ee.joonasvali.butterfly.uiswing.ButterflyWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;
import java.awt.*;

public class Boot {
  private final static Logger log = LoggerFactory.getLogger(Boot.class);

  public static void main(String[] args) {
    log.info("Starting system up.");

    try {
      AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
      ButterFly fly = ctx.getBean(ButterFly.class);

      if (GraphicsEnvironment.isHeadless()) {
        log.error("This system is not supported as it appears to be headless. GraphicsEnvironment.isHeadless() == true");
        System.exit(-1);
      }

      ButterFlyConfig config = ctx.getBean(ButterFlyConfig.class);

      SwingUtilities.invokeAndWait(() -> {
        fly.init();
        ButterflyWindow window = new ButterflyWindow(fly, config);
        window.show();
        window.start();
      });

    } catch (Throwable t) {
      log.error("Unable to launch Butterfly Effect Simulator", t);
    }

  }
}
