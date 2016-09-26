package ee.joonasvali.butterfly.slick;

import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.player.Clock;
import ee.joonasvali.butterfly.player.ClockImpl;
import ee.joonasvali.butterfly.player.KeyBoardClockListener;
import ee.joonasvali.butterfly.player.SimulationPlayer;
import ee.joonasvali.butterfly.simulation.Food;
import ee.joonasvali.butterfly.simulation.Physical;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.PhysicsRunner;
import ee.joonasvali.butterfly.simulation.SimulationContainer;
import ee.joonasvali.butterfly.simulation.SimulationState;
import ee.joonasvali.butterfly.simulation.actor.vision.ActorVisionHelper;
import ee.joonasvali.butterfly.ui.MouseDispatcher;
import ee.joonasvali.butterfly.ui.MouseListener;
import ee.joonasvali.butterfly.ui.SelectionListener;
import ee.joonasvali.butterfly.ui.SimulationPainterImpl;
import ee.joonasvali.butterfly.ui.SimulationPlayerPainter;
import ee.joonasvali.butterfly.ui.SimulationPlayerPainterImpl;
import ee.joonasvali.butterfly.ui.UI;
import ee.joonasvali.butterfly.ui.UIImpl;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ButterFly extends BasicGame {
  public static final int TOTAL_TRACKS = 2;

  private final int simulationSizeMultiplier;
  private final int actorsInSimulation;
  private final int initialHealth;

  private final UI ui;
  private final ButterFlyConfig config;
  private final PhysicsRunner runner;
  private final ActorVisionHelper visionHelper;
  private final Clock clock;
  private final KeyBoardClockListener clockListener;

  private volatile SimulationContainer[] containers = new SimulationContainer[2];
  private volatile SimulationPlayer player;
  private volatile SimulationPlayerPainter playerPainter;

  private final List<MouseListener> mouseListenerList = new ArrayList<>();


  private final static Logger log = LoggerFactory.getLogger(ButterFly.class);
  private volatile boolean shutdown;
  private volatile Physical selected;

  public ButterFly(UI ui, ButterFlyConfig config, PhysicsRunner runner, ActorVisionHelper visionHelper) {
    super("ButterFly");
    this.runner = runner;
    this.visionHelper = visionHelper;
    this.ui = ui;
    this.config = config;
    simulationSizeMultiplier = config.getSimulationSizeMultiplier();
    initialHealth = config.getActorInitialHealth();
    actorsInSimulation = config.getActorsInSimulation();
    clock = new ClockImpl(config.getFramesInSimulation());
    clockListener = ((ClockImpl)clock).getListener();
  }

  @Override
  public void init(GameContainer gameContainer) throws SlickException {
    int height = config.getWindowResolutionHeight();
    int width = config.getWindowResolutionWidth();
    int simWidth = simulationSizeMultiplier * width;
    int simHeight = simulationSizeMultiplier * height;
    SimulationPainterImpl painter = new SimulationPainterImpl(simWidth, simHeight, ((UIImpl)ui).getSimulationScreenWidth(),
        ((UIImpl)ui).getSimulationScreenHeight(), config.getActorDiameter(), config.getFoodDiameter(), visionHelper);
    SimulationContainer container = new SimulationContainer(
        runner,
        createInitialState(simWidth, simHeight),
        painter,
        simWidth,
        simHeight
    );
    this.containers = new SimulationContainer[]{container, container.copy()};

    painter.addSelectionListener(createSelectionListener());

    this.player = new SimulationPlayer(
        containers,
        config.getFramesInSimulation(), clock
    );

    this.player.calculateSimulation();
    this.playerPainter = new SimulationPlayerPainterImpl(config.getWindowResolutionWidth(), config.getWindowResolutionHeight());

    MouseDispatcher dispatcher = this.ui.getMouseDispatcher();
    dispatcher.registerMouseListener(((SimulationPlayerPainterImpl)playerPainter).createMouseListener(player, clock), MouseDispatcher.AreaImpl.PLAYER);
    dispatcher.registerMouseListener(createSimulationMouseListener(), MouseDispatcher.AreaImpl.SIMULATION);
    mouseListenerList.add(dispatcher);
  }

  private SelectionListener createSelectionListener() {
    return physical -> selected = physical;
  }

  private MouseListener createSimulationMouseListener() {
    return ((SimulationPainterImpl)getActiveTrackContainer().getPainter()).createMouseListener(clock);
  }

  private SimulationContainer getActiveTrackContainer() {
    return containers[player.getTrackPlayed()];
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
        1500
    );
  }

  @Override
  public void update(GameContainer gameContainer, int i) throws SlickException {
    if (shutdown) {
      gameContainer.exit();
    }

    player.passTime(i);
  }

  @Override
  public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
    graphics.drawString("Hello world!", 10, 20);
    ui.drawUI(graphics, player, playerPainter);
    ui.drawSimulation(getActiveTrackContainer().getPainter(), player.getState());
    ui.drawUITop(graphics, player.getCurrentFrame(), player.getTrackPlayed());
    graphics.flush();
  }

  @Override
  public void mouseClicked(int button, int x, int y, int clickCount) {
    mouseListenerList.forEach(ml -> ml.mouseClicked(button, x, y, clickCount));
  }

  @Override
  public void keyPressed(int key, char c) {

  }

  @Override
  public void keyReleased(int key, char c) {
    log.info("keycode: " + key + " pressed ");

    if (key == Input.KEY_DELETE) {
      Physical selected = this.selected;
      if (selected != null) {
        removePhysical(selected);
      }
    }

    if (key == Input.KEY_T) {
      int track = player.getTrackPlayed();
      track++;
      player.setTrackPlayed(track % TOTAL_TRACKS);
      return;
    }

    clockListener.keyReleased(key);

    if (key == Input.KEY_ESCAPE) {
      shutdown = true;
    }
  }

  private void removePhysical(Physical selected) {
    if (selected instanceof Actor) {
      // remove actor
      SimulationState state = getActiveTrackContainer().getState(clock.getFrameIndex());
      List<Actor> actors = new ArrayList<>(state.getActors());
      actors.remove(selected);
      SimulationState newState = new SimulationState(state.getFrameNumber(), actors, state.getFood(), state.getWidth(), state.getHeight());
      getActiveTrackContainer().alterState(newState);
    } else if (selected instanceof Food) {
      // remove food
      SimulationState state = getActiveTrackContainer().getState(clock.getFrameIndex());
      List<Food> food = new ArrayList<>(state.getFood());
      food.remove(selected);
      SimulationState newState = new SimulationState(state.getFrameNumber(), state.getActors(), food, state.getWidth(), state.getHeight());
      getActiveTrackContainer().alterState(newState);
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
        (int) (50 + (Math.random() * (config.getActorDiameter() - 50))),
        Math.random() * 360,
        0,
        0,
        0,
        initialHealth,
        1 + Math.random() * 3
    );
  }

  public String getRandomId() {
    String[] firstNames = {"JAMES", "JOHN", "ROBERT", "MICHAEL", "WILLIAM", "DAVID", "RICHARD", "CHARLES", "JOSEPH", "JOFFREY", "BRAN", "LEIA", "LUKE"};
    String[] lastNames = {"GATES", "DOE", "HOLMES", "PARK", "SEAGULL", "BEAR", "TARGARYEN", "SNOW", "LANNISTER", "SMITH", "STARK", "BARATHEON"};
    return firstNames[((int) (Math.random() * firstNames.length))] + " " + lastNames[((int) (Math.random() * lastNames.length))];
  }

}
