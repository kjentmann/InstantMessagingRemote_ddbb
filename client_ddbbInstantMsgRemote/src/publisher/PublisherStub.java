package publisher;
import apiREST.apiREST_Message;
import apiREST.apiREST_Topic;
import entity.Message;
import entity.Topic;

public class PublisherStub implements Publisher {
  Topic topic;
  public PublisherStub(Topic topic) {
    this.topic = topic;
  }

public void publish(String topic, String event) {
  entity.Message message = new Message();
  message.setTopic(apiREST_Topic.retrieveTopicByName(topic));
  message.setContent(event);
  apiREST_Message.createMessage(message);
  //...
}

  public String topicName() {
    return topic.getName();
  }
}
