package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.simulation.Food;
import ee.joonasvali.butterfly.simulation.Physical;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.SimulationState;
import ee.joonasvali.butterfly.simulation.actor.vision.ActorVisionHelper;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Joonas Vali July 2016
 */
public class SimulationPainterImpl implements SimulationPainter {
  private static final Logger log = LoggerFactory.getLogger(SimulationPainterImpl.class);
  public static final Color BACKGROUND_COLOR = new Color(10, 10, 50);
  private final int width;
  private final int height;
  private final int foodDiameter;
  private final Image image;
  private final Image actor;
  private final Graphics actorGraphics;
  private final Image food;
  private final Graphics g;
  private final Font font;
  private final ActorVisionHelper visionHelper;
  private final float simulationScale;
  private volatile List<Actor> lastActors;
  private volatile List<Food> lastFood;

  private volatile Physical selected;

  public SimulationPainterImpl(int width, int height, int onScreenWidth, int onScreenHeight, int maxActorDiameter, int foodDiameter, ActorVisionHelper visionHelper) throws SlickException {
    this.visionHelper = visionHelper;
    this.width = width;
    this.height = height;
    this.simulationScale = calcScale(width, height, onScreenWidth, onScreenHeight);
    this.foodDiameter = foodDiameter;
    this.image = new Image(width, height);
    this.food = createFood();
    this.actor = new Image(maxActorDiameter, maxActorDiameter);
    this.actorGraphics = this.actor.getGraphics();
    this.image.setFilter(Image.FILTER_LINEAR);
    this.g = image.getGraphics();
    this.font = createFont();
    g.setAntiAlias(true);
  }

  private float calcScale(int width, int height, int onScreenWidth, int onScreenHeight) {
    float simulationScale;
    if (onScreenWidth > onScreenHeight) {
      simulationScale = (float) onScreenHeight / (float) height;
    } else {
      // Not sure if this should ever happen, but still..
      simulationScale = (float) width / (float) width;
    }
    return simulationScale;
  }

  private Font createFont() {
    java.awt.Font awtFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 14);
    return new TrueTypeFont(awtFont, true);
  }

  private Image createFood() throws SlickException {
    Image image = new Image(foodDiameter, foodDiameter);
    Graphics g = image.getGraphics();
    g.setLineWidth(2 / simulationScale);
    g.setColor(Color.green);
    g.drawOval(0, 0, foodDiameter, foodDiameter);
    g.drawLine(foodDiameter / 2, foodDiameter / 2, foodDiameter / 2, 0);
    g.flush();
    return image;
  }

  private void paintActor(Graphics g, int diameter, Color color) {
    g.setLineWidth(2 / simulationScale);

    int x = (this.actor.getWidth() - diameter) / 2;
    int y = (this.actor.getHeight() - diameter) / 2;
    g.setColor(color.darker(0.3f));
    g.fillOval(x, y, diameter, diameter);
    g.setColor(color);
    g.drawOval(x, y, diameter, diameter);
    g.drawLine(x + diameter / 2, y + diameter / 2, x + diameter / 2, y);
    g.flush();
  }

  @Override
  public void draw(int screenX, int screenY, SimulationState state) {
    g.setColor(BACKGROUND_COLOR);
    g.fillRect(0, 0, width, height);
    drawFood(g, state.getFood());
    drawActors(g, state.getActors());

    g.flush();
    image.draw(screenX, screenY, simulationScale);
    drawStrings(screenX, screenY, simulationScale, state);
  }

  private void drawStrings(int screenX, int screenY, float simulationScale, SimulationState state) {
    for (Actor actor : state.getActors()) {
      font.drawString((screenX + actor.getRoundedX() + actor.getDiameter() / 2) * simulationScale,
          (screenY + actor.getRoundedY() + actor.getDiameter() / 2) * simulationScale,
          String.valueOf(actor.getHealth()) + " hp", Color.white);

      font.drawString((screenX + actor.getRoundedX() + actor.getDiameter() + 5) * simulationScale,
          (screenY + actor.getRoundedY() - 5) * simulationScale,
          actor.getId(), Color.white);
    }
  }


  private void drawFood(Graphics g, List<Food> food) {
    this.lastFood = food;
    g.setColor(Color.orange);
    g.setLineWidth(2 / simulationScale);
    for (Food f : food) {
      this.food.setRotation((float) (f.getRotation() + 90));
      g.drawImage(this.food, f.getRoundedX(), f.getRoundedY());
      drawSelectedIfMatch(g, f);
    }

  }

  private void drawSelectedIfMatch(Graphics g, Physical f) {
    if (f == selected) {
      g.setColor(Color.white);
      g.setLineWidth(2 / simulationScale);
      g.drawRect((int)f.getX(), (int)f.getY(), f.getDiameter(), f.getDiameter());
    }
  }

  private void drawActors(Graphics g, List<Actor> actors) {
    this.lastActors = actors;
    for (Actor actor : actors) {
      actorGraphics.clear();
      paintActor(actorGraphics, actor.getDiameter(), getActorColor(actor.getHealth()));
      this.actor.setRotation((float) actor.getRotation() + 90);
      int x = actor.getRoundedX() - ((this.actor.getWidth() - actor.getDiameter()) / 2);
      int y = actor.getRoundedY() - ((this.actor.getHeight() - actor.getDiameter()) / 2);
      g.drawImage(this.actor, x, y);
      drawSelectedIfMatch(g, actor);
      drawVision(g, actor);
    }
  }

  private Color getActorColor(int health) {
    return new Color(100, 100, Math.min(Math.max(health / 5, 0), 255));
  }

  private void drawVision(Graphics g, Actor actor) {
    g.setLineWidth(2 / simulationScale);
    g.setColor(Color.pink);
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

  public MouseListener createMouseListener() {
    return new MListener();
  }

  private class MListener implements MouseListener {
    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
      x = (int) (x / simulationScale);
      y = (int) (y / simulationScale);
      if (Input.MOUSE_LEFT_BUTTON == button) {
        for (Actor a : lastActors) {
          if (x > a.getX() && x < a.getX() + a.getDiameter() && y > a.getY() && y < a.getY() + a.getDiameter()) {
            selected = a;
            return;
          }
        }

        for (Food f : lastFood) {
          if (x > f.getX() && x < f.getX() + f.getDiameter() && y > f.getY() && y < f.getY() + f.getDiameter()) {
            selected = f;
            return;
          }
        }

        selected = null;
      }
    }
  }
}
