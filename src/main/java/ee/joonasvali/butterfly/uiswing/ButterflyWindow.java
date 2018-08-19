package ee.joonasvali.butterfly.uiswing;

import ee.joonasvali.butterfly.ButterFly;
import ee.joonasvali.butterfly.config.ButterFlyConfig;
import ee.joonasvali.butterfly.uiswing.listener.DelegatingKeyListener;
import ee.joonasvali.butterfly.uiswing.listener.ScrollPaneHandScrollListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ButterflyWindow {
  private static final String WINDOW_TITLE = "Butterfly";
  private final ButterFly butterFly;
  private final JFrame frame;
  private final ButterFlyConfig config;
  private final SimulationPanel simulationPanel;
  private final ControlPanel controlPanel;
  private final BorderLayout layout = new BorderLayout();

  public ButterflyWindow(ButterFly butterFly, ButterFlyConfig config) throws HeadlessException {
    this.butterFly = butterFly;
    this.config = config;
    frame = new JFrame(WINDOW_TITLE);
    DelegatingKeyListener centralKeyListener = new DelegatingKeyListener();
    centralKeyListener.addListener(createExitListener());

    this.simulationPanel = new SimulationPanel(butterFly, config);
    this.controlPanel = new ControlPanel(butterFly.getSimulationConfiguration(), butterFly.getPlayer());
    centralKeyListener.addListener(controlPanel.createKeyListener());
    frame.setSize(config.getWindowResolutionWidth(), config.getWindowResolutionHeight());
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setUndecorated(true);

    frame.getContentPane().setLayout(layout);
    frame.setFocusable(true);

    JPanel simPanel = simulationPanel.getPanel();
    simulationPanel.createKeyListeners().forEach(centralKeyListener::addListener);
    JScrollPane scrollPane = new JScrollPane(simPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    ScrollPaneHandScrollListener scrollListener = new ScrollPaneHandScrollListener(scrollPane.getViewport(), simPanel);
    scrollPane.getViewport().addMouseMotionListener(scrollListener);
    scrollPane.getViewport().addMouseListener(scrollListener);

    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    frame.getContentPane().add(controlPanel.getPanel(), BorderLayout.SOUTH);
  }

  private KeyListener createExitListener() {
    return new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
          butterFly.shutdown();
        }
      }
    };
  }

  public void show() {
    frame.setVisible(true);
  }

  public void start() {


  }
}
