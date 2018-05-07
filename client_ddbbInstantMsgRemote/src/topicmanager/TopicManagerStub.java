package topicmanager;

import apiREST.apiREST_Message;
import apiREST.apiREST_Publisher;
import apiREST.apiREST_Subscriber;
import apiREST.apiREST_Topic;
import entity.Message;
import entity.Topic;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import publisher.Publisher;
import publisher.PublisherStub;
import subscriber.Subscriber;
import webSocketService.WebSocketClient;

public class TopicManagerStub implements TopicManager {

  public entity.User user;
  public String server_status;
  
  public TopicManagerStub(entity.User user) {
    this.server_status = WebSocketClient.newInstance();
    this.user = user;
  }

  public void close() {
    WebSocketClient.close();
  }

  public Publisher addPublisherToTopic(String topic) {

    //...
    return null;

  }

  public int removePublisherFromTopic(String topic) {

    //...
    return -1;

  }

  public boolean isTopic(String topic_name) {

    //...
    return false;

  }

  public Set<String> topics() {
    
    Set<String> topic_names = new HashSet<String>();
    for ( Topic topic : apiREST_Topic.allTopics() ){
        topic_names.add(topic.getName());
    }
    //...
    return topic_names;
  }

  public boolean subscribe(String topic, Subscriber subscriber) {

    //...
    return true;

  }

  public boolean unsubscribe(String topic, Subscriber subscriber) {

    //...
    return true;

  }
  
  public Publisher publisherOf() {
      return (Publisher) apiREST_Publisher.PublisherOf(user);
    //...
  }

  public List<entity.Subscriber> mySubscriptions() {
      return apiREST_Subscriber.mySubscriptions(user);
    //...
  }

  public List<Message> messagesFrom(entity.Topic topic) {
      return apiREST_Message.messagesFrom(topic);
    //...
  }

}
