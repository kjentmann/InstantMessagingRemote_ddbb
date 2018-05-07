package main;

import apiREST.apiREST_Topic;
import entity.Message;
import subscriber.SubscriberImpl;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import publisher.Publisher;
import subscriber.Subscriber;
import topicmanager.TopicManager;
import topicmanager.TopicManagerStub;
import webSocketService.WebSocketClient;

public class ClientSwing {

  public Map<String, Subscriber> my_subscriptions;
  Publisher publisher;
  String publisherTopic;
  TopicManager topicManager;

  JFrame frame;
  JTextArea topic_list_TextArea;
  public JTextArea messages_TextArea;
  public JTextArea my_subscriptions_TextArea;
  JTextArea publisher_TextArea;
  JTextField argument_TextField;

  public ClientSwing(TopicManager topicManager) {
    this.topicManager = topicManager;
    publisher = null;
    my_subscriptions = new HashMap<String, Subscriber>();
  }

  public void createAndShowGUI() {

    String login = ((TopicManagerStub) topicManager).user.getLogin();
    frame = new JFrame("Publisher/Subscriber demo, user : " + login);
    frame.setSize(900,350);
    frame.addWindowListener(new CloseWindowHandler());

    topic_list_TextArea = new JTextArea(5, 10);
    messages_TextArea = new JTextArea(10, 20);
    my_subscriptions_TextArea = new JTextArea(5, 10);
    publisher_TextArea = new JTextArea(1, 10);
    argument_TextField = new JTextField(20);

    JButton show_topics_button = new JButton("show Topics");
    JButton new_publisher_button = new JButton("new Publisher");
    JButton new_subscriber_button = new JButton("new Subscriber");
    JButton to_unsubscribe_button = new JButton("to unsubscribe");
    JButton to_post_an_event_button = new JButton("post an event");
    JButton to_close_the_app = new JButton("close app.");

    show_topics_button.addActionListener(new showTopicsHandler());
    new_publisher_button.addActionListener(new newPublisherHandler());
    new_subscriber_button.addActionListener(new newSubscriberHandler());
    to_unsubscribe_button.addActionListener(new UnsubscribeHandler());
    to_post_an_event_button.addActionListener(new postEventHandler());
    to_close_the_app.addActionListener(new CloseAppHandler());

    JPanel buttonsPannel = new JPanel(new FlowLayout());
    buttonsPannel.add(show_topics_button);
    buttonsPannel.add(new_publisher_button);
    buttonsPannel.add(new_subscriber_button);
    buttonsPannel.add(to_unsubscribe_button);
    buttonsPannel.add(to_post_an_event_button);
    buttonsPannel.add(to_close_the_app);

    JPanel argumentP = new JPanel(new FlowLayout());
    argumentP.add(new JLabel("Write content to set a new_publisher / new_subscriber / unsubscribe / post_event:"));
    argumentP.add(argument_TextField);

    JPanel topicsP = new JPanel();
    topicsP.setLayout(new BoxLayout(topicsP, BoxLayout.PAGE_AXIS));
    topicsP.add(new JLabel("Topics:"));
    topicsP.add(topic_list_TextArea);
    topicsP.add(new JScrollPane(topic_list_TextArea));
    topicsP.add(new JLabel("My Subscriptions:"));
    topicsP.add(my_subscriptions_TextArea);
    topicsP.add(new JScrollPane(my_subscriptions_TextArea));
    topicsP.add(new JLabel("I'm Publisher of topic:"));
    topicsP.add(publisher_TextArea);
    topicsP.add(new JScrollPane(publisher_TextArea));

    JPanel messagesPanel = new JPanel();
    messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.PAGE_AXIS));
    messagesPanel.add(new JLabel("Messages:"));
    messagesPanel.add(messages_TextArea);
    messagesPanel.add(new JScrollPane(messages_TextArea));

    Container mainPanel = frame.getContentPane();
    mainPanel.add(buttonsPannel, BorderLayout.PAGE_START);
    mainPanel.add(messagesPanel, BorderLayout.CENTER);
    mainPanel.add(argumentP, BorderLayout.PAGE_END);
    mainPanel.add(topicsP, BorderLayout.LINE_START);

    //this is where you restore the user profile
    clientSetup();
    messages_TextArea.setEditable(false);
    topic_list_TextArea.setEditable(false);
    my_subscriptions_TextArea.setEditable(false);
    publisher_TextArea.setEditable(false);

    
    //frame.pack();
    frame.setVisible(true);
     messages_TextArea.append(getTime() + "SERVER: Client "+((TopicManagerStub)topicManager).user.getLogin()+ " "+((TopicManagerStub)topicManager).server_status + "\n"); 
     argument_TextField.grabFocus();

  }
  
  private static String getTime() {
     SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
     return sdfTime.format(new Date());
  }
  
  private String getArg(){
      
      String arg = argument_TextField.getText();
      argument_TextField.setText(null);
      argument_TextField.grabFocus();
      return arg;
  }
  
  private void clientSetup() {
    publisher=topicManager.publisherOf();
      for (entity.Subscriber registeredSub : topicManager.mySubscriptions()){
           SubscriberImpl restoredSub = new SubscriberImpl(ClientSwing.this);
           my_subscriptions.put(registeredSub.getTopic().getName(), restoredSub);
           for( Message message : topicManager.messagesFrom(registeredSub.getTopic())){
               restoredSub.onEvent(registeredSub.getTopic().getName(), message.getContent());
           }
      }
  }//this is where you restore the user profile

  
  private void updateTopics(){
     topic_list_TextArea.setText(null);
                for (String topic : topicManager.topics()){
                    topic_list_TextArea.append(topic + "\n");
                }
}
  
  class showTopicsHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    if (topicManager.topics()!=null){
               updateTopics();
            }
            else{
                topic_list_TextArea.setText(null);
                topic_list_TextArea.append("No topics yet\n");
            }
                argument_TextField.grabFocus();
            }
        }

  class newPublisherHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
     String topic = getArg();
            if (topic.isEmpty()){
                messages_TextArea.append(getTime() + "SYSTEM: Missing input.\n"); 
            }
            else{
                 if (topic.equals(publisherTopic))
                        System.out.println("WARNING -> ClientsWing -> Already publishing on topic");
                 else if (publisherTopic!=null){
                        System.out.println("INFO -> Clientswing -> Trying to remove : " + publisherTopic);
                        topicManager.removePublisherFromTopic(publisherTopic);

                    }
                publisher = topicManager.addPublisherToTopic(topic);
                publisherTopic=topic;
                publisher_TextArea.setText(null);
                publisher_TextArea.append(topic + "\n");
                messages_TextArea.append(getTime() + "SYSTEM: You are publisher of topic '"+ topic + "̈́'.\n"); 
            }
            updateTopics();
        }
    }
  
  class newSubscriberHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
           String topic = getArg();
            if (topic.isEmpty() || my_subscriptions.containsKey(topic) || topic == publisherTopic)
                messages_TextArea.append(getTime() + "SYSTEM: Invalid operation.\n"); 
            else if (topicManager.isTopic(topic)){
                Subscriber newsubscriber;
                newsubscriber = new SubscriberImpl(ClientSwing.this);
                topicManager.subscribe(topic, newsubscriber);              // Note: adding subscriber to the actual publishers list
                my_subscriptions.put(topic, newsubscriber);                // Note: adding subscriber to clientSwing subscribers list
                my_subscriptions_TextArea.append(topic + "\n");
                messages_TextArea.append(getTime() + "SYSTEM: Subscribed on topic '"+ topic + "̈́'.\n"); 
            }
            else{
                messages_TextArea.append(getTime() + "SYSTEM: Topic '"+ topic + "̈́' does not exist.\n");
                System.out.println("WARNING -> Clientswing -> Topic '"+ topic + "̈́' does not exist.");
            }
            updateTopics();
        }
    }
  
  class UnsubscribeHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String topic = getArg();
            try{
                if (topicManager.unsubscribe(topic,my_subscriptions.get(topic)) ){
                        my_subscriptions.remove(topic);
                        my_subscriptions_TextArea.setText(null);
                       for (String otherTopics : my_subscriptions.keySet()){
                             my_subscriptions_TextArea.append(otherTopics+"\n");
                       }
                        System.out.println("INFO -> Clientswing -> Unsubscribed from '"+topic+"'.");
                }
                else{
                messages_TextArea.append(getTime() + "SYSTEM: Topic '"+ topic + "̈́' does not exist.\n");
                }
            }
            catch(Exception ex){
               System.out.println("ERROR -> Clientswing -> Error, no active subscribers to remove.");
            }
            updateTopics();
        }
    }
  
    class postEventHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (publisher != null){
                String event = getArg();
                updateTopics();
                if (!event.isEmpty()){
                    publisher.publish(publisherTopic, event);
                    messages_TextArea.append(getTime() + "You published: " + event + "\n");
                }
            }
            else{
                messages_TextArea.append(getTime() + "SYSTEM: No publisher exist.\n");
            }
        }
    }

  class CloseAppHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      messages_TextArea.append("Client ending... \n");
      ((TopicManagerStub) topicManager).close();
      System.out.println("app closed");
      System.exit(0);
    }
  }

  class CloseWindowHandler implements WindowListener {
    public void windowDeactivated(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {
      messages_TextArea.append("Client ending... \n");
      ((TopicManagerStub) topicManager).close();
      System.out.println("app closed");
      System.exit(0);
    }
  }
}
