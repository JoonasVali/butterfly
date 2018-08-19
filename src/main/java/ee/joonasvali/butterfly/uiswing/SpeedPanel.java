package ee.joonasvali.butterfly.uiswing;

import ee.joonasvali.butterfly.player.ClockSpeed;
import ee.joonasvali.butterfly.player.SimulationPlayer;

import javax.swing.*;
import java.awt.*;

public class SpeedPanel extends JPanel {
  private final SimulationPlayer player;
  private final JButton pause;
  private final JButton backwards;
  private final JButton normal;
  private final JButton fast;
  private final JButton fastest;

  public SpeedPanel(SimulationPlayer player) {
    this.player = player;
    this.setLayout(new GridLayout(1, 0));
    this.pause = new JButton("Pause");
    this.backwards = new JButton("Backwards");
    this.normal = new JButton("Normal");
    this.fast = new JButton("Fast");
    this.fastest = new JButton("Fastest");
    this.add(pause);
    this.add(backwards);
    this.add(normal);
    this.add(fast);
    this.add(fastest);

    fast.addActionListener(a -> setSpeed(ClockSpeed.FAST));
    fastest.addActionListener(a -> setSpeed(ClockSpeed.FASTEST));
    normal.addActionListener(a -> setSpeed(ClockSpeed.NORMAL));
    pause.addActionListener(a -> {
      player.getClock().pause(!player.getClock().isPause());
      adjustButtonsToClockState();
    });
    backwards.addActionListener(a -> setSpeed(ClockSpeed.BACKWARDS));
    adjustButtonsToClockState();
  }

  public void flipPause() {
    player.getClock().pause(!player.getClock().isPause());
    adjustButtonsToClockState();
  }

  public void setSpeed(ClockSpeed clockSpeed) {
    player.getClock().pause(false);
    player.getClock().setSpeed(clockSpeed);
    adjustButtonsToClockState();
  }

  public void adjustButtonsToClockState() {
    ClockSpeed speed = player.getClock().getSpeed();

    if (speed == ClockSpeed.NORMAL) {
      normal.setEnabled(false);
      fast.setEnabled(true);
      fastest.setEnabled(true);
      backwards.setEnabled(true);
    }

    if (speed == ClockSpeed.FAST) {
      normal.setEnabled(true);
      fast.setEnabled(false);
      fastest.setEnabled(true);
      backwards.setEnabled(true);
    }

    if (speed == ClockSpeed.FASTEST) {
      normal.setEnabled(true);
      fast.setEnabled(true);
      fastest.setEnabled(false);
      backwards.setEnabled(true);
    }

    if (speed == ClockSpeed.BACKWARDS) {
      normal.setEnabled(true);
      fast.setEnabled(true);
      fastest.setEnabled(true);
      backwards.setEnabled(false);
    }
  }

}
