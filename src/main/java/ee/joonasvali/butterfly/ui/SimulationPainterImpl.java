package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.simulation.Food;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.SimulationState;
import ee.joonasvali.butterfly.simulation.actor.vision.ActorVisionHelper;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joonas Vali July 2016
 */
public class SimulationPainterImpl implements SimulationPainter {
  private static final Logger log = LoggerFactory.getLogger(SimulationPainterImpl.class);
  public static final Color BACKGROUND_COLOR = new Color(10, 10, 50);
  private final int width;
  private final int height;
  private final int actorDiameter;
  private final int foodDiameter;
  private final Image image;
  private final Image actorOk;
  private final Image actorMedium;
  private final Image actorBad;
  private final Image food;
  private final Graphics g;
  private final Font font;
  private final ActorVisionHelper visionHelper;
  private int playerX;
  private int playerY;

  public SimulationPainterImpl(int width, int height, int actorDiameter, int foodDiameter, ActorVisionHelper visionHelper) throws SlickException {
    this.visionHelper = visionHelper;
    this.width = width;
    this.height = height;
    this.actorDiameter = actorDiameter;
    this.foodDiameter = foodDiameter;
    this.image = new Image(width, height);
    this.actorOk = createActor(Color.cyan);
    this.actorMedium = createActor(Color.orange);
    this.actorBad = createActor(Color.red);
    this.food = createFood();

    this.image.setFilter(Image.FILTER_LINEAR);
    this.g = image.getGraphics();
    this.font = createFont();
    g.setAntiAlias(true);
  }

  private Font createFont() {
    java.awt.Font awtFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 24);
    return new TrueTypeFont(awtFont, true);
  }

  private Image createFood() throws SlickException {
    Image image = new Image(foodDiameter, foodDiameter);
    Graphics g = image.getGraphics();
    g.setLineWidth(2);
    g.setColor(Color.green);
    g.drawOval(0, 0, foodDiameter, foodDiameter);
    g.drawLine(foodDiameter / 2, foodDiameter / 2, foodDiameter / 2, 0);
    g.flush();
    return image;
  }

  private Image createActor(Color color) throws SlickException {
    Image image = new Image(actorDiameter, actorDiameter);
    Graphics g = image.getGraphics();
    g.setLineWidth(5);
    g.setColor(color);
    g.drawOval(0, 0, actorDiameter, actorDiameter);
    g.drawLine(actorDiameter / 2, actorDiameter / 2, actorDiameter / 2, 0);
    g.flush();
    return image;
  }

  @Override
  public Image draw(SimulationState state) {
    g.setColor(BACKGROUND_COLOR);
    g.fillRect(0, 0, width, height);
    drawFood(g, state.getFood());
    drawActors(g, state.getActors());
    g.flush();
    return image;
  }

  private void drawFood(Graphics g, List<Food> food) {
    g.setColor(Color.orange);
    for (Food f : food) {
      this.food.setRotation((float) (f.getRotation() + 90));
      g.drawImage(this.food, f.getRoundedX(), f.getRoundedY());
    }
  }

  private void drawActors(Graphics g, List<Actor> actors) {
    for (Actor actor : actors) {
      if (actor.getHealth() > 800) {
        this.actorOk.setRotation((float) actor.getRotation() + 90);
        g.drawImage(this.actorOk, actor.getRoundedX(), actor.getRoundedY());
      } else if (actor.getHealth() > 400) {
        this.actorMedium.setRotation((float) actor.getRotation() + 90);
        g.drawImage(this.actorMedium, actor.getRoundedX(), actor.getRoundedY());
      } else {
        this.actorBad.setRotation((float) actor.getRotation() + 90);
        g.drawImage(this.actorBad, actor.getRoundedX(), actor.getRoundedY());
      }
      Font previous = g.getFont();
      g.setFont(font);
      g.drawString(actor.getId(), actor.getRoundedX() + actorDiameter + 5, actor.getRoundedY() - 5);
      g.setFont(previous);
      drawVision(g, actor);

    }
  }

  private void drawVision(Graphics g, Actor actor) {
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
}
