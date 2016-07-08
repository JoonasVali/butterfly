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
  private final Image image;
  private final Image actor;
  private final Image food;
  private final Graphics g;

  public SimulationPainterImpl(int width, int height, int actorDiameter) throws SlickException {
    this.width = width;
    this.height = height;
    this.actorDiameter = actorDiameter;
    this.image = new Image(width, height);
    this.actor = createActor();
    this.food = createFood();

    this.image.setFilter(Image.FILTER_LINEAR);
    this.g = image.getGraphics();
    g.setAntiAlias(true);
  }

  private Image createFood() throws SlickException {
    Image image = new Image(4, 4);
    Graphics g = image.getGraphics();
    g.setLineWidth(2);
    g.setColor(Color.green);
    g.drawOval(0, 0, 4, 4);
    g.drawLine(2, 2, 2, 0);
    g.flush();
    return image;
  }

  private Image createActor() throws SlickException {
    Image image = new Image(actorDiameter, actorDiameter);
    Graphics g = image.getGraphics();
    g.setLineWidth(5);
    g.setColor(Color.orange);
    g.drawOval(5, 5, actorDiameter - 5, actorDiameter - 5);
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
      this.actor.setRotation((float)actor.getRotation() + 90);
      g.drawImage(this.actor, actor.getX(), actor.getY());
    }
  }
}
