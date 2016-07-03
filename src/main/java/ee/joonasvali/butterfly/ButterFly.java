package ee.joonasvali.butterfly;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demo class to start up the slick, replace it with what ever you need.
 */
public class ButterFly extends BasicGame {
  private final static Logger log = LoggerFactory.getLogger(ButterFly.class);
  private volatile boolean shutdown;

  public ButterFly() {
    super("ButterFly");
  }

  @Override
  public void init(GameContainer gameContainer) throws SlickException {

  }

  @Override
  public void update(GameContainer gameContainer, int i) throws SlickException {
    if (shutdown) {
      gameContainer.exit();
    }
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
    log.info("keycode: " + key + " pressed ");
    if (key == Input.KEY_ESCAPE) {
      shutdown = true;
    }
  }
}
