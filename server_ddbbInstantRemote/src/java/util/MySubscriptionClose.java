package util;

import entity.Topic;

/**
 *
 * @author juanluis
 */
public class MySubscriptionClose {
  public enum Cause { PUBLISHER, SUBSCRIBER };
  public String topic;
  public Cause cause;
}
