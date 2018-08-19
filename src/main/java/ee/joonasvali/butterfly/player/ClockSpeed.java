package ee.joonasvali.butterfly.player;

public enum ClockSpeed {
  FASTEST(10),
  FAST(30),
  NORMAL(50),
  BACKWARDS(-50);

  private final int speed;
  ClockSpeed(int speed) {
    this.speed = speed;
  }

  public int getSpeed() {
    return Math.abs(speed);
  }

  public boolean isForward() {
    return speed >= 0;
  }
}
