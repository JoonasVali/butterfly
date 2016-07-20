package ee.joonasvali.butterfly.player;

import org.newdawn.slick.Input;

/**
 * @author Joonas Vali July 2016
 */
public class ClockImpl implements Clock {
  public static final int CLOCK_FASTEST = 10;
  public static final int CLOCK_FAST = 30;
  public static final int CLOCK = 50;

  private final KeyBoardClockListener listener;
  private final int maxFrame;

  private volatile int currentFrame;
  private volatile int clock = CLOCK;
  public volatile long timeInGame;


  public ClockImpl(int maxFrame) {
    this.maxFrame = maxFrame;
    this.listener = new KeyBoardClockListener() {
      @Override
      public void keyReleased(int keycode) {
        if (keycode == Input.KEY_3) {
          clock = CLOCK_FASTEST;
        }

        if (keycode == Input.KEY_2) {
          clock = CLOCK_FAST;
        }

        if (keycode == Input.KEY_1) {
          clock = CLOCK;
        }
      }
    };
  }

  public void passTime(int i) {
    timeInGame += i;
    while (timeInGame >= clock) {
      timeInGame -= clock;
      if (currentFrame < maxFrame - 1) {
        currentFrame++;
      }
    }
  }

  @Override
  public int getFrameIndex() {
    return currentFrame;
  }

  public KeyBoardClockListener getListener() {
    return listener;
  }
}
