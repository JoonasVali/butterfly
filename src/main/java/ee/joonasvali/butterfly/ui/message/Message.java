package ee.joonasvali.butterfly.ui.message;

/**
 * @author Joonas Vali October 2016
 */
public interface Message {
  String getMessage();
  boolean isExpired();
}
