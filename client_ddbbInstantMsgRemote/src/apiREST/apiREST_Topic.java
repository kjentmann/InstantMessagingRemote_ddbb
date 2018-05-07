package apiREST;

import com.google.gson.Gson;
import entity.Topic;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;

public class apiREST_Topic {

  public static Topic retrieveTopicByName(String name) {
    try {
      URL url = new URL(Cons.SERVER_REST+"/entity.topic/name");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();

      ucon.setRequestMethod("POST");
      ucon.setDoInput(true);
      ucon.setDoOutput(true);
      ucon.setRequestProperty("Content-Type", "application/json");
      ucon.setRequestProperty("Accept", "application/json");
      
      PrintWriter out = new PrintWriter(ucon.getOutputStream(), true);
      Gson gson = new Gson();
      Topic topic = new Topic();
      topic.setName(name);
      String json = gson.toJson(topic);
      System.out.println(json);
      out.println(json);
      out.flush();
      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      topic = gson.fromJson(in, Topic.class);
      return topic;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }
  
  public static List<Topic> allTopics(){
    try {
      URL url = new URL(Cons.SERVER_REST+"/entity.topic/");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();

      ucon.setRequestMethod("GET");
      ucon.setDoInput(true);
      ucon.setRequestProperty("Content-Type", "application/json");
      ucon.setRequestProperty("Accept", "application/json");
      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      Gson gson = new Gson();      
      Topic[] topicArray = gson.fromJson(in, Topic[].class);
      List<Topic> topics = Arrays.asList(topicArray);
      return topics;
      
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
