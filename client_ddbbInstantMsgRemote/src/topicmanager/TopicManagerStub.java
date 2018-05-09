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
  public String reconnect(){
    this.server_status = WebSocketClient.newInstance();
    return server_status;
  }
  public Publisher addPublisherToTopic(String topic) {
    Topic newtopic = new Topic();
    newtopic.setName(topic);
    publisher.Publisher localPub = new PublisherStub(newtopic);
    entity.Publisher remotePub = new entity.Publisher();
    remotePub.setTopic(newtopic);
    remotePub.setUser(user);
     if (apiREST_Publisher.create_and_return_Publisher(remotePub)==null){
         return null;
     }
    return localPub;
  }

  public int removePublisherFromTopic(String topic) {
      entity.Publisher removePub = new entity.Publisher();
      removePub.setTopic(apiREST_Topic.retrieveTopicByName(topic));
      removePub.setUser(user);
      System.out.println("Info -> TOpicMangager-> Request to remove: " + removePub);
      try{
      apiREST_Publisher.deletePublisher(removePub);
      }
      catch (Exception e){
        System.out.println("ERROR -> TOpicMangager-> Failed to remove: " + removePub);
      }
    return -1;
  }

  public boolean isTopic(String topic_name) {
      if (topics().contains(topic_name))
          return true;
      else
          return false;
  }

  public Set<String> topics() {
    Set<String> topic_names = new HashSet<String>();
    for ( Topic topic : apiREST_Topic.allTopics() ){
        topic_names.add(topic.getName());
    }
    return topic_names;
  }

  public boolean subscribe(String topic, Subscriber subscriber) {
    WebSocketClient.addSubscriber(topic, subscriber);
    entity.Subscriber sub = new entity.Subscriber();
    sub.setUser(user);
    sub.setTopic(apiREST_Topic.retrieveTopicByName(topic));
    try{
        apiREST_Subscriber.create_and_return_Subscriber(sub);
        return true;
    }
    catch (Exception e){
        System.out.println("ERROR -> TopicMangager-> Failed to subscribe: " + topic);
        return false;
    }
  }

  public boolean unsubscribe(String topic, Subscriber subscriber) {
      entity.Subscriber removeSub = new entity.Subscriber();
      removeSub.setTopic(apiREST_Topic.retrieveTopicByName(topic));
      removeSub.setUser(user);
      return apiREST_Subscriber.deleteSubscriber(removeSub);
  }
  
  public Publisher publisherOf() {
     try{
     return new PublisherStub(apiREST_Publisher.PublisherOf(user).getTopic()); 
     }
     catch(Exception e){
         return null;
     }
  }

  public List<entity.Subscriber> mySubscriptions() {
      return apiREST_Subscriber.mySubscriptions(user);
  }

  public List<Message> messagesFrom(entity.Topic topic) {
      return apiREST_Message.messagesFrom(topic);
  }

}