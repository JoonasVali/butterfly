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
  private volatile long timeInGame;
  private volatile boolean forward = true;


  public ClockImpl(int maxFrame) {
    this.maxFrame = maxFrame;
    this.listener = new KeyBoardClockListener() {
      @Override
      public void keyReleased(int keycode) {
        if (keycode == Input.KEY_3) {
          forward = true;
          clock = CLOCK_FASTEST;
        }

        if (keycode == Input.KEY_2) {
          forward = true;
          clock = CLOCK_FAST;
        }

        if (keycode == Input.KEY_1) {
          forward = true;
          clock = CLOCK;
        }

        if (keycode == Input.KEY_0) {
          timeInGame = 0;

          if (forward) {
            pause();
          } else if (clock == 0) {
            clock = CLOCK;
          } else if (clock == CLOCK) {
            clock = CLOCK_FAST;
          } else if (clock == CLOCK_FAST) {
            clock = CLOCK_FASTEST;
          }

          forward = false;
        }
      }
    };
  }

  public void pause() {
    clock = 0;
  }

  public void passTime(int i) {
    if (clock == 0) {
      // pause
      return;
    }
    timeInGame += i;
    while (timeInGame >= clock) {
      timeInGame -= clock;
      if (forward) {
        if (currentFrame < maxFrame - 1) {
          currentFrame++;
        }
      } else {
        if (currentFrame > 0) {
          currentFrame--;
        }
      }
    }
  }

  @Override
  public int getFrameIndex() {
    return currentFrame;
  }

  @Override
  public void setFrameIndex(int index) {
    if (index < 0 || index >= maxFrame) {
      throw new IllegalArgumentException("index " + index + " out of bounds.");
    }
    this.currentFrame = index;
  }

  public KeyBoardClockListener getListener() {
    return listener;
  }
}
