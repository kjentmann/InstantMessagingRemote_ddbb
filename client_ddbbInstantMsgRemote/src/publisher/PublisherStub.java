package publisher;

import apiREST.apiREST_Message;
import entity.Message;
import entity.Topic;

public class PublisherStub implements Publisher {

  Topic topic;

  public PublisherStub(Topic topic) {
    this.topic = topic;
  }

  public void publish(String topic, String event) {

      //...
  }

  public String topicName() {
    return topic.getName();
  }

}
