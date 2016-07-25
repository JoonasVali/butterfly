package ee.joonasvali.butterfly.ui;

/**
 * @author Joonas Vali July 2016
 */
public interface MouseDispatcher extends MouseListener {

  void registerMouseListener(MouseListener listener, Area area);
  void mouseClicked(int button, int x, int y, int clickCount);

  enum AreaImpl implements Area {
    SIMULATION, PLAYER, SETTINGS
  }
}
