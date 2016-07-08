package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.simulation.SimulationState;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * @author Joonas Vali July 2016
 */
public interface SimulationPainter {
  Image draw(SimulationState state);
}
