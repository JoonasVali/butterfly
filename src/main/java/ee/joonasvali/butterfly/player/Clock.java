package ee.joonasvali.butterfly.player;

/**
 * @author Joonas Vali July 2016
 */
public class Clock {

  private final int maxFrame;

  private int currentFrame;
  private ClockSpeed speed = ClockSpeed.NORMAL;
  private long timeInGame;
  private boolean pause;


  public Clock(int maxFrame) {
    this.maxFrame = maxFrame;
  }

  public void pause(boolean pause) {
    this.pause = pause;
  }

  public boolean isPause() {
    return pause;
  }

  public void passTime(long i) {
    if (pause) {
      // pause
      return;
    }
    timeInGame += i;
    while (timeInGame >= speed.getSpeed()) {
      timeInGame -= speed.getSpeed();
      if (speed.isForward()) {
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

  public int getFrameIndex() {
    return currentFrame;
  }

  public void setFrameIndex(int index) {
    if (index < 0 || index >= maxFrame) {
      throw new IllegalArgumentException("index " + index + " out of bounds.");
    }
    this.currentFrame = index;
  }

  public void setSpeed(ClockSpeed speed) {
    this.speed = speed;
  }

  public ClockSpeed getSpeed() {
    return speed;
  }
}
