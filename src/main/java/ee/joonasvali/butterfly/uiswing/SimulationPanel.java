package ee.joonasvali.butterfly.uiswing;

import ee.joonasvali.butterfly.ButterFly;
import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.simulation.Food;
import ee.joonasvali.butterfly.simulation.Physical;
import ee.joonasvali.butterfly.simulation.PhysicalUID;
import ee.joonasvali.butterfly.simulation.SimulationConfiguration;
import ee.joonasvali.butterfly.simulation.SimulationState;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.actor.vision.ActorVisionHelper;
import ee.joonasvali.butterfly.uiswing.listener.DispatchToParentMouseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationPanel {
  private static final Logger log = LoggerFactory.getLogger(SimulationPanel.class);
  private static final Color ACTOR_HEALTHY_COLOR = new Color(50, 129, 236);
  private static final Color ACTOR_UNHEALTY_COLOR = new Color(188, 225, 250);
  private static final Color ACTOR_ID_COLOR = new Color(0, 0, 0);
  private static final Color VISION_COLOR = new Color(110, 108, 105);
  private static final Color FOOD_COLOR = new Color(22, 87, 0);
  private static final Color SELECTION_COLOR = new Color(246, 250, 255);
  private static final int TIMER_DELAY = 1;
  private static final String BAD_TRACK_ERROR = "You are trying to delete object from the original track. Switch to alternative track first.";
  private static final String NO_PHYSICAL_IN_TIME_ERROR = "Selected physical appears to already be missing from the current time.";
  private static final Color BUTTERFLY_EFFECT_COLOR = new Color(175, 0, 212);
  private static final Color BACKGROUND_COLOR = new Color(203, 203, 203);
  public static final int SELECTION_PADDING = 5;

  private final ButterFly butterFly;
  private final ButterFlyConfig config;
  private final JPanel panel;
  private final Timer timer;
  private final ActorVisionHelper visionHelper;
  private final SimulationConfiguration simulationConfiguration;
  private final Map<Integer, Color> actorHealthColorCache = new HashMap<>();

  private PhysicalUID selectedPhysical;

  public SimulationPanel(ButterFly butterFly, ButterFlyConfig config) {
    this.butterFly = butterFly;
    this.config = config;
    panel = new Panel();
    visionHelper = butterFly.getVisionHelper();
    simulationConfiguration = butterFly.getSimulationConfiguration();

    panel.setPreferredSize(new Dimension(butterFly.getSimulationWidth(), butterFly.getSimulationHeight()));
    panel.setBackground(BACKGROUND_COLOR);
    panel.addMouseListener(createMouseListener());
    panel.addMouseMotionListener(new DispatchToParentMouseListener());
    timer = new Timer(TIMER_DELAY, createTimerListener());
    timer.start();
  }


  private MouseListener createMouseListener() {
    return new DispatchToParentMouseListener() {
      @Override
      public void mouseReleased(MouseEvent e) {
        SimulationState state = butterFly.getActiveTrackContainer().getState(butterFly.getPlayer().getCurrentFrame());
        List<Actor> actors = state.getActors();
        List<Food> foods = state.getFoods();
        for (Food food : foods) {
          if (food.getRoundedX() < e.getX() && food.getRoundedX() + food.getDiameter() > e.getX()
              && food.getRoundedY() < e.getY() && food.getRoundedY() + food.getDiameter() > e.getY()) {
            selectedPhysical = food.getUID();
            break;
          }
        }

        for (Actor a : actors) {
          if (a.getRoundedX() < e.getX() && a.getRoundedX() + a.getDiameter() > e.getX()
              && a.getRoundedY() < e.getY() && a.getRoundedY() + a.getDiameter() > e.getY()) {
            selectedPhysical = a.getUID();
            break;
          }
        }
        super.mouseReleased(e);
      }
    };
  }

  public List<KeyListener> createKeyListeners() {
    List<KeyListener> listeners = new ArrayList<>();
    listeners.add(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE && selectedPhysical != null) {
          if (butterFly.getPlayer().getTrackPlayed() != 1) {
            log.info("Unable to modify track " + butterFly.getPlayer().getTrackPlayed());
            JOptionPane.showMessageDialog(new JFrame(), BAD_TRACK_ERROR);
            return;
          }

          Physical physical = findPhysicalById(selectedPhysical);
          if (physical == null) {
            log.info("No physical found in time for selected physicalUID" + selectedPhysical);
            JOptionPane.showMessageDialog(new JFrame(), NO_PHYSICAL_IN_TIME_ERROR);
            return;
          }
          log.info("Attempting to remove physical " + physical);
          butterFly.removePhysical(physical);
        }
      }
    });
    return listeners;
  }

  private ActionListener createTimerListener() {
    return (e) -> panel.repaint();
  }

  public JPanel getPanel() {
    return panel;
  }

  private long lastTime = 0;

  private class Panel extends JPanel {

    @Override
    public void paint(Graphics g) {
      super.paint(g);
      if (simulationConfiguration.isPaintButterflyEffect() && butterFly.getPlayer().getTrackPlayed() != 0) {
        paintWithButterflyEffect(g);
      } else {
        paintNormal(g);
      }
    }

    private void paintWithButterflyEffect(Graphics g) {
      advanceSimulationClockFromTimePassed();
      SimulationState originalState = butterFly.getPlayer().getContainer(0).getState(butterFly.getPlayer().getCurrentFrame());
      SimulationState state = butterFly.getPlayer().getState();
      List<Actor> originalActors = originalState.getActors();
      List<Food> originalFood = originalState.getFoods();
      List<Actor> actors = state.getActors();
      List<Food> foods = state.getFoods();
      for (Actor actor : actors) {
        // To clarify: it is present if it is identical in all properties. (Check actors equals() and hashcode() methods)
        if (originalActors.contains(actor)) {
          paintActor(g, actor, getActorColor(actor.getHealth()));
        } else {
          paintActor(g, actor, BUTTERFLY_EFFECT_COLOR);
        }
        paintVision(g, actor);
      }
      for (Food food : foods) {
        // To clarify: it is present if it is identical in all properties. (Check food equals() and hashcode() methods)
        if (originalFood.contains(food)) {
          paintFood(g, food, FOOD_COLOR);
        } else {
          paintFood(g, food, BUTTERFLY_EFFECT_COLOR);
        }
      }
      paintSelection(g, findPhysicalById(selectedPhysical));
    }

    public void paintNormal(Graphics g) {
      advanceSimulationClockFromTimePassed();
      SimulationState state = butterFly.getPlayer().getState();
      List<Actor> actors = state.getActors();
      List<Food> foods = state.getFoods();
      for (Actor actor : actors) {
        paintActor(g, actor, getActorColor(actor.getHealth()));
        paintVision(g, actor);
      }
      for (Food food : foods) {
        paintFood(g, food, FOOD_COLOR);
      }
      paintSelection(g, findPhysicalById(selectedPhysical));
    }

    private void paintSelection(Graphics g, Physical selectionArea) {
      if (selectionArea == null) {
        return;
      }
      int x = (int) selectionArea.getX();
      int y = (int) selectionArea.getY();
      g.setColor(SELECTION_COLOR);
      g.drawRect(
          x - SELECTION_PADDING,
          y - SELECTION_PADDING,
          selectionArea.getDiameter() + SELECTION_PADDING * 2,
          selectionArea.getDiameter() + SELECTION_PADDING * 2
      );
    }

    private Color getActorColor(int health) {
      Color color = actorHealthColorCache.get(health);
      if (color != null) {
        return color;
      }

      int initHealth = config.getActorInitialHealth();
      float healthFraction = (float) health / (float) initHealth;
      int r = (int) (ACTOR_HEALTHY_COLOR.getRed() * healthFraction + ACTOR_UNHEALTY_COLOR.getRed() * (1 - healthFraction));
      int g = (int) (ACTOR_HEALTHY_COLOR.getGreen() * healthFraction + ACTOR_UNHEALTY_COLOR.getGreen() * (1 - healthFraction));
      int b = (int) (ACTOR_HEALTHY_COLOR.getBlue() * healthFraction + ACTOR_UNHEALTY_COLOR.getBlue() * (1 - healthFraction));
      color = new Color(limitColorRange(r), limitColorRange(g), limitColorRange(b));
      actorHealthColorCache.put(health, color);
      return color;
    }

    private int limitColorRange(int colorValue) {
      return Math.min(Math.max(0, colorValue), 255);
    }

    private void advanceSimulationClockFromTimePassed() {
      if (lastTime == 0) {
        lastTime = System.currentTimeMillis();
      } else {
        long now = System.currentTimeMillis();
        long passed = now - lastTime;
        butterFly.tick(passed);
        lastTime = now;
      }
    }

    private void paintActor(Graphics g, Actor actor, Color color) {
      int diameter = actor.getDiameter();
      int x = actor.getRoundedX();
      int y = actor.getRoundedY();
      g.setColor(color.darker());
      g.fillOval(x, y, diameter, diameter);
      g.setColor(color);
      g.drawOval(x, y, diameter, diameter);
      int radius = diameter / 2;
      int xCenter = x + radius;
      int yCenter = y + radius;

      double rads = Math.toRadians(actor.getRotation());

      int xPosy = Math.round((float) (x + Math.cos(rads) * radius)) + radius;
      int yPosy = Math.round((float) (y + Math.sin(rads) * radius)) + radius;

      g.drawLine(xCenter, yCenter, xPosy, yPosy);
      g.setColor(ACTOR_ID_COLOR);
      g.drawString(actor.getId(), x, y - 10);
    }

    private void paintVision(Graphics g, Actor actor) {
      if (!simulationConfiguration.isPaintVision()) {
        return;
      }
      g.setColor(VISION_COLOR);
      int ax = visionHelper.getActorVisionAX(actor);
      int ay = visionHelper.getActorVisionAY(actor);
      int bx = visionHelper.getActorVisionBX(actor);
      int by = visionHelper.getActorVisionBY(actor);
      int cx = visionHelper.getActorVisionCX(actor);
      int cy = visionHelper.getActorVisionCY(actor);
      g.drawLine(ax, ay, cx, cy);
      g.drawLine(bx, by, cx, cy);
      g.drawLine(ax, ay, bx, by);
    }

    private void paintFood(Graphics g, Food food, Color foodColor) {
      int foodDiameter = food.getDiameter();
      int x = food.getRoundedX();
      int y = food.getRoundedY();
      g.setColor(foodColor);
      g.drawOval(x, y, foodDiameter, foodDiameter);

      double rads = Math.toRadians(food.getRotation());

      int radius = food.getDiameter() / 2;
      int xPosy = Math.round((float) (x + Math.cos(rads) * radius)) + radius;
      int yPosy = Math.round((float) (y + Math.sin(rads) * radius)) + radius;
      int xCenter = x + radius;
      int yCenter = y + radius;
      g.drawLine(xCenter, yCenter, xPosy, yPosy);
    }
  }

  private Physical findPhysicalById(PhysicalUID lookedUpPhysical) {
    SimulationState state = butterFly.getActiveTrackContainer().getState(butterFly.getPlayer().getCurrentFrame());
    return state.findPhysicalByUUID(lookedUpPhysical);
  }
}
