package ee.joonasvali.butterfly.ui;

import ee.joonasvali.butterfly.player.SimulationPlayer;
import org.newdawn.slick.Image;

/**
 * @author Joonas Vali July 2016
 */
public interface SimulationPlayerPainter {
  Image getPlayerImage(SimulationPlayer player);
}
