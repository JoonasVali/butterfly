package ee.joonasvali.butterfly.uiswing;

import ee.joonasvali.butterfly.player.ClockSpeed;
import ee.joonasvali.butterfly.player.SimulationPlayer;
import ee.joonasvali.butterfly.simulation.SimulationConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControlPanel {
  private static final int HEIGHT = 100;
  private static final int SLIDER_WIDTH = 800;
  private static final int SLIDER_HEIGHT = 35;
  private final JPanel panel;
  private final JSlider slider;
  private final JCheckBox displayButterflyEffectBox;
  private final JCheckBox displayVisionBox;
  private final JButton helpButton;
  private final SimulationPlayer player;
  private final JLabel frameValue;
  private final JComboBox<String> trackBox;
  private final SpeedPanel speedPanel;
  private final SimulationConfiguration simulationConf;

  public static final String[] HELP_CONTENT = new String[]{
      "Press '1' '2' '3' to choose speed. '0' for reverse. P for pause",
      "Press 'Tab' to switch track. The alternative track is the one you can edit.",
      "While on alternative track, click on screen to select actor or food.",
      "Press 'delete' to alter timeline and remove the actor or food from existence.",
      "Press 'B' to toggle Butterfly Effect view on / off. (Works if you've altered the timeline.)",
      "Press 'Esc' to exit."
  };

  private final Timer timer;

  public ControlPanel(SimulationConfiguration simulationConfiguration, SimulationPlayer player) {
    this.panel = new Panel();
    this.player = player;
    this.slider = new JSlider(JSlider.HORIZONTAL, 0, player.getTotalFrames() - 1, player.getCurrentFrame());
    this.frameValue = new JLabel(String.valueOf(player.getCurrentFrame()));
    this.simulationConf = simulationConfiguration;
    slider.setMajorTickSpacing(100);
    slider.setMinorTickSpacing(10);
    slider.setPaintTicks(true);
    slider.setPreferredSize(new Dimension(SLIDER_WIDTH, SLIDER_HEIGHT));
    slider.addChangeListener(e -> player.setFrameIndex(slider.getValue()));
    trackBox = new JComboBox<>(new String[] { "Original track", "Alternative track" });
    trackBox.addActionListener(e -> player.setTrackPlayed(trackBox.getSelectedIndex()));

    displayButterflyEffectBox = new JCheckBox("Visualize butterfly effect");
    displayVisionBox = new JCheckBox("Display actor vision range");
    displayButterflyEffectBox.addActionListener(e -> simulationConf.setPaintButterflyEffect(displayButterflyEffectBox.isSelected()));
    displayVisionBox.addActionListener(e -> simulationConf.setPaintVision(displayVisionBox.isSelected()));

    helpButton = new JButton("?");
    helpButton.addActionListener(createHelpAction());

    speedPanel = new SpeedPanel(player);
    this.panel.add(speedPanel);

    this.panel.add(trackBox);
    this.panel.add(slider);
    this.panel.add(frameValue);
    this.panel.add(new JSeparator(JSeparator.HORIZONTAL));
    this.panel.add(displayButterflyEffectBox);
    this.panel.add(displayVisionBox);
    this.panel.add(helpButton);

    this.panel.setPreferredSize(new Dimension(0, HEIGHT));
    this.timer = new Timer(100, e -> panel.repaint());
    timer.start();
  }

  private ActionListener createHelpAction() {
    return e -> JOptionPane.showMessageDialog(new JFrame("Help"), String.join("\n", HELP_CONTENT));
  }

  public void adaptCheckBoxesToState() {
    displayButterflyEffectBox.setSelected(simulationConf.isPaintButterflyEffect());
    displayVisionBox.setSelected(simulationConf.isPaintVision());
  }

  public JPanel getPanel() {
    return panel;
  }

  private class Panel extends JPanel {
    @Override
    public void paint(Graphics g) {
      super.paint(g);
      slider.setValue(player.getCurrentFrame());
      frameValue.setText(String.valueOf(player.getCurrentFrame()));
    }
  }

  public KeyListener createKeyListener() {
    return new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_TAB) {
          player.setTrackPlayed((player.getTrackPlayed() + 1) % 2);
          trackBox.setSelectedIndex(player.getTrackPlayed());
        }

        if (e.getKeyCode() == KeyEvent.VK_1) {
          speedPanel.setSpeed(ClockSpeed.NORMAL);
        }

        if (e.getKeyCode() == KeyEvent.VK_2) {
          speedPanel.setSpeed(ClockSpeed.FAST);
        }

        if (e.getKeyCode() == KeyEvent.VK_3) {
          speedPanel.setSpeed(ClockSpeed.FASTEST);
        }

        if (e.getKeyCode() == KeyEvent.VK_0) {
          speedPanel.setSpeed(ClockSpeed.BACKWARDS);
        }

        if (e.getKeyCode() == KeyEvent.VK_P) {
          speedPanel.flipPause();
        }

        // Press 'V' to change if actor's vision is displayed.
        if (e.getKeyCode() == KeyEvent.VK_V) {
          simulationConf.setPaintVision(!simulationConf.isPaintVision());
          adaptCheckBoxesToState();
        }

        // Press 'B' to change if butterfly effect is displayed
        if (e.getKeyCode() == KeyEvent.VK_B) {
          simulationConf.setPaintButterflyEffect(!simulationConf.isPaintButterflyEffect());
          adaptCheckBoxesToState();
        }
      }
    };
  }
}
