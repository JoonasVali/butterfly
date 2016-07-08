package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.simulation.Food;
import ee.joonasvali.butterfly.simulation.actor.Actor;
import ee.joonasvali.butterfly.simulation.SimulationState;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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

  public SimulationPainterImpl(int width, int height, int actorDiameter, int foodDiameter) throws SlickException {
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
    g.setAntiAlias(true);
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

  private void drawFood(Graphics g, ArrayList<Food> food) {
    g.setColor(Color.orange);
    for (Food f : food) {
      this.food.setRotation((float) (f.getRotation() + 90));
      g.drawImage(this.food, f.getX(), f.getY());
    }
  }

  private void drawActors(Graphics g, ArrayList<Actor> actors) {
    for (Actor actor : actors) {
      if (actor.getHealth() > 800) {
        this.actorOk.setRotation((float) actor.getRotation() + 90);
        g.drawImage(this.actorOk, actor.getX(), actor.getY());
      } else if (actor.getHealth() > 400) {
        this.actorMedium.setRotation((float) actor.getRotation() + 90);
        g.drawImage(this.actorMedium, actor.getX(), actor.getY());
      } else {
        this.actorBad.setRotation((float) actor.getRotation() + 90);
        g.drawImage(this.actorBad, actor.getX(), actor.getY());
      }
    }
  }
}