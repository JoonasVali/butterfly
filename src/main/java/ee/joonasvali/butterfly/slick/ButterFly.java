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
import ee.joonasvali.butterfly.simulation.generator.InitialStateGenerator;
import ee.joonasvali.butterfly.ui.MouseDispatcher;
import ee.joonasvali.butterfly.ui.MouseListener;
import ee.joonasvali.butterfly.ui.SelectionListener;
import ee.joonasvali.butterfly.simulation.SimulationConfiguration;
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
import java.util.Optional;

public class ButterFly extends BasicGame {
  private static final int TOTAL_TRACKS = 2;

  private final int simulationSizeMultiplier;

  private final UI ui;
  private final ButterFlyConfig config;
  private final PhysicsRunner runner;
  private final ActorVisionHelper visionHelper;
  private final Clock clock;
  private final KeyBoardClockListener clockListener;
  private final InitialStateGenerator generator;
  private final SimulationConfiguration runtimeConfig;

  private volatile SimulationContainer[] containers = new SimulationContainer[2];
  private volatile SimulationPlayer player;
  private volatile SimulationPlayerPainter playerPainter;

  private final List<MouseListener> mouseListenerList = new ArrayList<>();


  private final static Logger log = LoggerFactory.getLogger(ButterFly.class);
  private volatile boolean shutdown;
  private volatile Physical selected;

  public ButterFly(UI ui, ButterFlyConfig config, PhysicsRunner runner, InitialStateGenerator generator, ActorVisionHelper visionHelper) {
    super("ButterFly");
    this.generator = generator;
    this.runner = runner;
    this.visionHelper = visionHelper;
    this.ui = ui;
    this.config = config;
    this.runtimeConfig = new SimulationConfiguration();
    simulationSizeMultiplier = config.getSimulationSizeMultiplier();
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
        ((UIImpl)ui).getSimulationScreenHeight(), config.getActorDiameter(), config.getFoodDiameter(), runtimeConfig, visionHelper);
    SimulationContainer container = new SimulationContainer(
        runner,
        generator.createInitialState(simWidth, simHeight),
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

  @Override
  public void update(GameContainer gameContainer, int i) throws SlickException {
    if (shutdown) {
      gameContainer.exit();
    }

    player.passTime(i);
  }

  @Override
  public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
    ui.drawUI(graphics, player, playerPainter);
    ui.drawSimulation(getActiveTrackContainer().getPainter(), player.getState(), getOriginalState());
    ui.drawUITop(graphics, player.getCurrentFrame(), player.getTrackPlayed());
    graphics.flush();
  }

  /**
   * @return the original state
   */
  private Optional<SimulationState> getOriginalState() {
    if (player.getTrackPlayed() != 0) {
      return Optional.of(player.getContainer(0).getState(player.getCurrentFrame()));
    } else {
      return Optional.empty();
    }
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
      //don't allow changes to first track
      if (player.getTrackPlayed() != 0) {
        Physical selected = this.selected;
        if (selected != null) {
          removePhysical(selected);
        }
      } else {
        ui.displayWarning("Switch to other track (press 'T') to make changes, this is the original track for comparison.");
      }
    }

    if (key == Input.KEY_T) {
      int track = player.getTrackPlayed();
      track++;
      player.setTrackPlayed(track % TOTAL_TRACKS);
      return;
    }

    if (key == Input.KEY_V) {
      runtimeConfig.setPaintVision(!runtimeConfig.isPaintVision());
      return;
    }

    if (key == Input.KEY_B) {
      runtimeConfig.setPaintButterflyEffect(!runtimeConfig.isPaintButterflyEffect());
      ui.displayWarning("ButterFly view: " + (runtimeConfig.isPaintButterflyEffect() ? "ON" : "OFF"));
      return;
    }

    if (key == Input.KEY_H) {
      ui.toggleHelp();
      return;
    }

    clockListener.keyReleased(key);

    if (key == Input.KEY_ESCAPE) {
      if (!ui.isHelpVisible()) {
        shutdown = true;
      } else {
        ui.toggleHelp();
      }
    }
  }

  private void removePhysical(Physical selected) {
    if (selected instanceof Actor) {
      // remove actor
      SimulationState state = getActiveTrackContainer().getState(clock.getFrameIndex());
      List<Actor> actors = new ArrayList<>(state.getActors());
      if (actors.remove(selected)) {
        SimulationState newState = new SimulationState(state.getFrameNumber(), actors, state.getFood(), state.getWidth(), state.getHeight());
        getActiveTrackContainer().alterState(newState);
      } else {
        log.warn("actor " + selected + " is not found from the active track " + player.getTrackPlayed());
      }
    } else if (selected instanceof Food) {
      // remove food
      SimulationState state = getActiveTrackContainer().getState(clock.getFrameIndex());
      List<Food> food = new ArrayList<>(state.getFood());
      if (food.remove(selected)) {
        SimulationState newState = new SimulationState(state.getFrameNumber(), state.getActors(), food, state.getWidth(), state.getHeight());
        getActiveTrackContainer().alterState(newState);
      } else {
        log.warn("Food " + selected + " is not found from the active track " + player.getTrackPlayed());
      }
    }
  }

}
