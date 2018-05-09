package webSocketService;

import apiREST.Cons;
import apiREST.apiREST_Subscriber;
import apiREST.apiREST_Topic;
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

    public static String newInstance() { // Note: called when new clent created
    subscriberMap = new HashMap<String, Subscriber>();
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      session = container.connectToServer(WebSocketClient.class,
        URI.create(Cons.SERVER_WEBSOCKET));
        System.out.println("INFO -> Websocket -> Connected to server.");
        return "Connected to " + Cons.SERVER_WEBSOCKET;

    } catch (Exception e) {
        System.out.println("ERROR -> WebSocket -> No connection to the remote server!.");
        return "ERROR. Can't connect to " + Cons.SERVER_WEBSOCKET + "\nPlease ensure that server is running and restart this client to connect.";
        //e.printStackTrace();
    }
  }
    
  //only one subscriber per topic allowed:
  public static synchronized void addSubscriber(String topic_name, Subscriber subscriber) {
    subscriberMap.put(topic_name,subscriber);
     //...
    
  }

  public static synchronized void removeSubscriber(String topic_name) {
      subscriberMap.remove(topic_name);
    
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
    System.out.println("DEBUG -> Websocket -> Got message");
    Gson gson = new Gson();
    System.out.println(message);
    
    //on receiving message, as we do not know if it is a Message
    //or a MyCloseTopic, one of the two try-catch will produce
    //an exception, but it does not matter, the exception is hiden.

    try {
      Message the_message = gson.fromJson(message, Message.class);
      subscriberMap.get(the_message.getTopic().getName()).onEvent(the_message.getTopic().getName(), the_message.getContent());
      
      //...
      
    } catch (Exception e) {
        
        System.out.println("DEBUG -> Websocket -> Got message, was not ordinary text");

    }

    try {
      MySubscriptionClose mySubsClose = gson.fromJson(message, MySubscriptionClose.class);
      subscriberMap.get(mySubsClose.topic).onClose(mySubsClose.topic, mySubsClose.cause.toString());
      //...
      
    } catch (Exception e) {
        
        System.out.println("DEBUG -> Websocket -> Got message, was not a subscription close message");

    }
  }

}
