/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author juanluis
 */
public class MySubscriptionRequest {
  public enum Type { ADD, REMOVE };
  public Type type;
  public String topic;
}
