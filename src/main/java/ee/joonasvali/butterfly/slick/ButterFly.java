package ee.joonasvali.butterfly.slick;

import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.simulation.Food;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.PhysicsRunner;
import ee.joonasvali.butterfly.simulation.SimulationContainer;
import ee.joonasvali.butterfly.simulation.SimulationState;
import ee.joonasvali.butterfly.simulation.actor.vision.ActorVisionHelper;
import ee.joonasvali.butterfly.simulation.actor.demo.PlayerActor;
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
  public static final int ACTORS_IN_SIMULATION = 10;
  public static final int INITIAL_HEALTH = 1000;

  private final UI ui;
  private final ButterFlyConfig config;
  private final PhysicsRunner runner;
  private final ActorVisionHelper visionHelper;

  private SimulationContainer container;


  private final static Logger log = LoggerFactory.getLogger(ButterFly.class);
  private volatile boolean shutdown;

  public ButterFly(UI ui, ButterFlyConfig config, PhysicsRunner runner, ActorVisionHelper visionHelper) {
    super("ButterFly");
    this.runner = runner;
    this.visionHelper = visionHelper;
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
        new SimulationPainterImpl(simWidth, simHeight, config.getActorDiameter(), config.getFoodDiameter(), visionHelper),
        simWidth,
        simHeight
    );

  }

  private SimulationState createInitialState(int simWidth, int simHeight) {
    return new SimulationState(0, getActors(simWidth, simHeight), getFood(simWidth, simHeight), simWidth, simHeight);
  }

  private ArrayList<Food> getFood(int simWidth, int simHeight) {
    ArrayList<Food> food = new ArrayList<>();
    for (int i = 0; i < (simWidth * simHeight) / 10000; i++) {
      food.add(createRandomFood(simWidth, simHeight));
    }
    return food;
  }

  private Food createRandomFood(int simWidth, int simHeight) {
    return new Food(
        (int) (Math.random() * simWidth),
        (int) (Math.random() * simHeight),
        config.getFoodDiameter(),
        Math.random() * 360,
        0,
        0,
        0,
        10
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
    PlayerActor playerActor = container.getActor();
    if (playerActor != null) {
      playerActor.setMove(thrust, rotate);
    }

  }

  @Override
  public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
    graphics.drawString("Hello world!", 10, 20);
    ui.drawUI(graphics);
    ui.drawSimulation(container.getPainter(), container.getState());
    ui.drawUITop(graphics, container.getTotalFrames());
    graphics.flush();
  }


  volatile double thrust;
  volatile double rotate;

  @Override
  public void keyPressed(int key, char c) {
    if (Input.KEY_D == key) {
      rotate = 2;
    } else if (Input.KEY_A == key) {
      rotate = -2;
    }
    if (Input.KEY_W == key) {
      thrust = 2;
    } else if (Input.KEY_S == key) {
      thrust = -2;
    }
  }

  @Override
  public void keyReleased(int key, char c) {
    log.info("keycode: " + key + " pressed ");
    if (key == Input.KEY_D || key == Input.KEY_A) {
      rotate = 0;
    }
    if (key == Input.KEY_W || key == Input.KEY_S) {
      thrust = 0;
    }
    if (key == Input.KEY_ESCAPE) {
      shutdown = true;
    }
  }

  public ArrayList<Actor> getActors(int simWidth, int simHeight) {
    ArrayList<Actor> actors = new ArrayList<>();
    for (int i = 0; i < ACTORS_IN_SIMULATION; i++) {
      actors.add(createRandomActor(simWidth - config.getActorDiameter(), simHeight - config.getActorDiameter()));
    }
    actors.add(new PlayerActor(10, 10, config.getActorDiameter(), 0, 0, 0, 0, INITIAL_HEALTH, 3));
    return actors;
  }

  private Actor createRandomActor(int simWidth, int simHeight) {
    return new Actor(
        (int) (Math.random() * simWidth),
        (int) (Math.random() * simHeight),
        config.getActorDiameter(),
        Math.random() * 360,
        0,
        0,
        0,
        INITIAL_HEALTH,
        Math.random() * 3
    );
  }
}
