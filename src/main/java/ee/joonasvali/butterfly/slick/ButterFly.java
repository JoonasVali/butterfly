package ee.joonasvali.butterfly.slick;

import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.simulation.Food;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.PhysicsRunner;
import ee.joonasvali.butterfly.simulation.SimulationContainer;
import ee.joonasvali.butterfly.simulation.SimulationState;
import ee.joonasvali.butterfly.ui.SimulationPainterImpl;
import ee.joonasvali.butterfly.ui.UI;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Demo class to start up the slick, replace it with what ever you need.
 */
public class ButterFly extends BasicGame {
  public static final int SIMULATION_SIZE_MULTIPLIER = 2;
  public static final int CLOCK = 50;

  private final UI ui;
  private final ButterFlyConfig config;
  private final PhysicsRunner runner;

  private SimulationContainer container;


  private final static Logger log = LoggerFactory.getLogger(ButterFly.class);
  private volatile boolean shutdown;

  public ButterFly(UI ui, ButterFlyConfig config, PhysicsRunner runner) {
    super("ButterFly");
    this.runner = runner;
    this.ui = ui;
    this.config = config;
  }

  @Override
  public void init(GameContainer gameContainer) throws SlickException {
    int height = config.getWindowResolutionHeight();
    int width = config.getWindowResolutionWidth();
    int simWidth = SIMULATION_SIZE_MULTIPLIER * width;
    int simHeight = SIMULATION_SIZE_MULTIPLIER * height;
    container = new SimulationContainer(
        runner,
        createInitialState(simWidth, simHeight),
        new SimulationPainterImpl(simWidth, simHeight, config.getActorDiameter(), config.getFoodDiameter()),
        simWidth,
        simHeight
    );

  }

  private SimulationState createInitialState(int simWidth, int simHeight) {
    return new SimulationState(getActors(simWidth, simHeight), getFood(simWidth, simHeight), simWidth, simHeight);
  }

  private ArrayList<Food> getFood(int simWidth, int simHeight) {
    ArrayList<Food> food = new ArrayList<>();
    for (int i = 0; i < (simWidth * simHeight) / 10000 ; i++) {
      food.add(createRandomFood(simWidth, simHeight));
    }
    return food;
  }

  private Food createRandomFood(int simWidth, int simHeight) {
    return new Food(
        (int)(Math.random() * simWidth),
        (int)(Math.random() * simHeight),
        config.getFoodDiameter(),
        Math.random() * 360,
        0,
        0,
        0
    );
  }

  public volatile long timeInGame;
  @Override
  public void update(GameContainer gameContainer, int i) throws SlickException {
    if (shutdown) {
      gameContainer.exit();
    }
    timeInGame += i;
    while (timeInGame >= CLOCK) {
      timeInGame -= CLOCK;
      container.nextState();
    }

  }

  @Override
  public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
    graphics.drawString("Hello world!", 10, 20);
    ui.drawUI(graphics);
    ui.drawSimulation(container.getPainter(), container.getState());
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

  public ArrayList<Actor> getActors(int simWidth, int simHeight) {
    ArrayList<Actor> actors = new ArrayList<>();
    actors.add(randomActor(simWidth - config.getActorDiameter(), simHeight - config.getActorDiameter()));
    actors.add(randomActor(simWidth - config.getActorDiameter(), simHeight - config.getActorDiameter()));
    actors.add(randomActor(simWidth - config.getActorDiameter(), simHeight - config.getActorDiameter()));
    return actors;
  }

  private Actor randomActor(int simWidth, int simHeight) {
    return new Actor(
        (int)(Math.random() * simWidth),
        (int)(Math.random() * simHeight),
        config.getActorDiameter(),
        Math.random() * 360,
        0,
        0,
        0,
        (int)(Math.random() * 90 + 10),
        Math.random() * 5
    );
  }
}
