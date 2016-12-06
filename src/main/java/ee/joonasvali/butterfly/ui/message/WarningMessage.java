package ee.joonasvali.butterfly.ui.message;

/**
 * @author Joonas Vali October 2016
 */
public class WarningMessage implements Message {
  private static final int DEFAULT_DURATION = 5000;
  private final long duration;
  private final String message;
  private final long startTime;

  public WarningMessage(long duration, String message) {
    this(duration, message, System.currentTimeMillis());
  }

  public WarningMessage(long duration, String message, long startTime) {
    this.duration = duration;
    this.message = message;
    this.startTime = startTime;
  }

  public WarningMessage(String message) {
    this(DEFAULT_DURATION, message);
  }

  public long getDuration() {
    return duration;
  }

  public String getMessage() {
    return message;
  }

  public long getStartTime() {
    return startTime;
  }

  public boolean isExpired() {
    return startTime + duration < System.currentTimeMillis();
  }
}
