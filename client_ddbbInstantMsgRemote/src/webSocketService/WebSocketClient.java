package webSocketService;

import apiREST.Cons;
import com.google.gson.Gson;
import entity.Message;
import entity.Topic;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import subscriber.Subscriber;
import util.MySubscriptionClose;

@ClientEndpoint
public class WebSocketClient {

  static Map<String, Subscriber> subscriberMap;
  static Session session;

  public static void newInstance() {
    subscriberMap = new HashMap<String, Subscriber>();
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      session = container.connectToServer(WebSocketClient.class,
        URI.create(Cons.SERVER_WEBSOCKET));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //only one subscriber per topic allowed:
  public static synchronized void addSubscriber(String topic_name, Subscriber subscriber) {
    
    //...
    
  }

  public static synchronized void removeSubscriber(String topic_name) {
    
    //...
    
  }

  public static void close() {
    try {
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnMessage
  public void onMessage(String message) {

    Gson gson = new Gson();
    System.out.println(message);
    
    //on receiving message, as we do not know if it is a Message
    //or a MyCloseTopic, one of the two try-catch will produce
    //an exception, but it does not matter, the exception is hiden.

    try {
      Message the_message = gson.fromJson(message, Message.class);
      
      //...
      
    } catch (Exception e) {

    }

    try {
      MySubscriptionClose mySubsClose = gson.fromJson(message, MySubscriptionClose.class);
      
      //...
      
    } catch (Exception e) {

    }
  }

}
