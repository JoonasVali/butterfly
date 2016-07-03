package ee.joonasvali;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class Boot extends BasicGame {
  private final static Logger log = LoggerFactory.getLogger(Boot.class);


  public static void main(String[] args) {

    if (GraphicsEnvironment.isHeadless()) {
      log.error("This system is not supported as it appears to be headless. GraphicsEnvironment.isHeadless() == true");
      System.exit(-1);
    }

    try {
           AppGameContainer container = new AppGameContainer(new Boot());
      container.setForceExit(true);
      container.setDisplayMode(Constants.DIMENSION_X, Constants.DIMENSION_Y, false);
      container.setVSync(true);
      container.start();
    } catch (SlickException e) {
      log.error("Unable to launch Mirrors", e);
    }
  }

  public Boot() {
    super("Mirrors");
  }

  @Override
  public void init(GameContainer gameContainer) throws SlickException {

  }

  @Override
  public void update(GameContainer gameContainer, int i) throws SlickException {

  }

  @Override
  public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
    graphics.drawString("Hello world!", 10, 20);
  }

  @Override
  public void keyPressed(int key, char c) {

  }

  @Override
  public void keyReleased(int key, char c) {

  }
}
