package ee.joonasvali.butterfly.slick;

import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.player.SimulationPlayer;
import ee.joonasvali.butterfly.simulation.Food;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.PhysicsRunner;
import ee.joonasvali.butterfly.simulation.SimulationContainer;
import ee.joonasvali.butterfly.simulation.SimulationState;
import ee.joonasvali.butterfly.simulation.actor.vision.ActorVisionHelper;
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
  public static final int CLOCK_FASTEST = 10;
  public static final int CLOCK_FAST = 30;
  public static final int CLOCK = 50;

  public static final int TOTAL_FRAMES_IN_SIMULATION = 1000;

  private final int simulationSizeMultiplier;
  private final int actorsInSimulation;
  private final int initialHealth;

  private final UI ui;
  private final ButterFlyConfig config;
  private final PhysicsRunner runner;
  private final ActorVisionHelper visionHelper;

  private volatile SimulationContainer container;
  private volatile SimulationPlayer player;
  private volatile int simulationFrame;
  private volatile int clock = CLOCK;


  private final static Logger log = LoggerFactory.getLogger(ButterFly.class);
  private volatile boolean shutdown;

  public ButterFly(UI ui, ButterFlyConfig config, PhysicsRunner runner, ActorVisionHelper visionHelper) {
    super("ButterFly");
    this.runner = runner;
    this.visionHelper = visionHelper;
    this.ui = ui;
    this.config = config;
    simulationSizeMultiplier = config.getSimulationSizeMultiplier();
    initialHealth = config.getActorInitialHealth();
    actorsInSimulation = config.getActorsInSimulation();
  }

  @Override
  public void init(GameContainer gameContainer) throws SlickException {
    int height = config.getWindowResolutionHeight();
    int width = config.getWindowResolutionWidth();
    int simWidth = simulationSizeMultiplier * width;
    int simHeight = simulationSizeMultiplier * height;
    this.container = new SimulationContainer(
        runner,
        createInitialState(simWidth, simHeight),
        new SimulationPainterImpl(simWidth, simHeight, config.getActorDiameter(), config.getFoodDiameter(), visionHelper),
        simWidth,
        simHeight
    );

    this.player = new SimulationPlayer(this.container, TOTAL_FRAMES_IN_SIMULATION);
    this.player.calculateSimulation();
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
    while (timeInGame >= clock) {
      timeInGame -= clock;
      if (simulationFrame < TOTAL_FRAMES_IN_SIMULATION - 1) {
        simulationFrame++;
      }
    }

  }

  @Override
  public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
    graphics.drawString("Hello world!", 10, 20);
    ui.drawUI(graphics);
    ui.drawSimulation(container.getPainter(), container.getState(simulationFrame));
    ui.drawUITop(graphics, simulationFrame);
    graphics.flush();
  }


  volatile double thrust;
  volatile double rotate;

  @Override
  public void keyPressed(int key, char c) {
    if (Input.KEY_SPACE == key) {
      container.reset();
    }
  }

  @Override
  public void keyReleased(int key, char c) {
    log.info("keycode: " + key + " pressed ");

    if (key == Input.KEY_3) {
      clock = CLOCK_FASTEST;
    }

    if (key == Input.KEY_2) {
      clock = CLOCK_FAST;
    }

    if (key == Input.KEY_1) {
      clock = CLOCK;
    }

    if (key == Input.KEY_ESCAPE) {
      shutdown = true;
    }
  }

  public ArrayList<Actor> getActors(int simWidth, int simHeight) {
    ArrayList<Actor> actors = new ArrayList<>();
    for (int i = 0; i < actorsInSimulation; i++) {
      actors.add(createRandomActor(simWidth - config.getActorDiameter(), simHeight - config.getActorDiameter()));
    }
    return actors;
  }

  private Actor createRandomActor(int simWidth, int simHeight) {
    return new Actor(
        getRandomId(),
        (int) (Math.random() * simWidth),
        (int) (Math.random() * simHeight),
        config.getActorDiameter(),
        Math.random() * 360,
        0,
        0,
        0,
        initialHealth,
        1 + Math.random() * 3
    );
  }

  public String getRandomId() {
    String[] firstNames = {"JAMES", "JOHN", "ROBERT", "MICHAEL", "WILLIAM", "DAVID", "RICHARD", "CHARLES", "JOSEPH"};
    String[] lastNames = {"GATES", "DOE", "HOLMES", "PARK", "SEAGULL", "BEAR", "TARGARYEN", "SNOW", "LANNISTER", "SMITH", "STARK"};
    return firstNames[((int) (Math.random() * firstNames.length))] + " " + lastNames[((int) (Math.random() * lastNames.length))];
  }

}
