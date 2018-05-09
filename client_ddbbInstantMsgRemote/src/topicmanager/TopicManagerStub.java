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
      //PublisherStub newPub;
      Topic newtopic = new Topic();
      newtopic.setName(topic);
      //newtopic = apiREST_Topic.retrieveTopicByName(topic); // NO NEED
      //newPub = new PublisherStub(newtopic);
   //  publisher.Publisher newPubl = new publisher.Publisher();
    // Publisher newPub = 
    publisher.Publisher pub2 = new PublisherStub(newtopic);//newPub.Publisher();
    entity.Publisher pub3 = new entity.Publisher();//Stub(newtopic);//newPub.Publisher();pub3.s
    pub3.setTopic(newtopic);
    pub3.setUser(user);
    //Publisher pub1 = new PublisherStub(newtopic);         
     if (apiREST_Publisher.create_and_return_Publisher(pub3)==null){
         return null;
     }
    return pub2;
  }

  public int removePublisherFromTopic(String topic) {
      entity.Publisher removePub = new entity.Publisher();
      removePub.setTopic(apiREST_Topic.retrieveTopicByName(topic));
      removePub.setUser(user);
      System.out.println("DEBUG->TOpicMangager->Request to delete: " + removePub);
      apiREST_Publisher.deletePublisher(removePub);
    //...
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
    apiREST_Subscriber.create_and_return_Subscriber(sub);

    
      
    //...
    return true;

  }

  public boolean unsubscribe(String topic, Subscriber subscriber) {
      entity.Subscriber removeSub = new entity.Subscriber();
      removeSub.setTopic(apiREST_Topic.retrieveTopicByName(topic));
      removeSub.setUser(user);
      return apiREST_Subscriber.deleteSubscriber(removeSub);
    //...
  //  return true;

  }
  
  public Publisher publisherOf() {
     //entity.Publisher pub= apiREST_Publisher.PublisherOf(user);
     //Topic topic = pub.getTopic();
     //Publisher pub2 = new PublisherStub(topic); 
     //Publisher pub3 = new PublisherStub(apiREST_Publisher.PublisherOf(user).getTopic()); 
     try{
     return new PublisherStub(apiREST_Publisher.PublisherOf(user).getTopic()); 
     }
     catch(Exception e){
         return null;
     }
     //return pub2;
// return (Publisher) apiREST_Publisher.PublisherOf(user);
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
