package ee.joonasvali.butterfly.player;

/**
 * @author Joonas Vali July 2016
 */
public interface Clock {
  void passTime(int i);
  int getFrameIndex();
  void setFrameIndex(int index);
  void pause(boolean pause);
  boolean isPause();
}
