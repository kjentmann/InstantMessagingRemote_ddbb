/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.service;

import entity.Message;
import entity.Topic;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import webSocketService.WebSocketServer;

/**
 *
 * @author upcnet
 */
@Stateless
@Path("entity.message")
public class MessageFacadeREST extends AbstractFacade<Message> {
  @PersistenceContext(unitName = "PubSubWebServerPU")
  private EntityManager em;

  public MessageFacadeREST() {
    super(Message.class);
  }

  @POST
  @Override
  @Consumes({"application/xml", "application/json"})
  public void create(Message entity) {
    //we check out if the topic exists for this message:
    Query query = em.createQuery("select t from Topic t where t.name=:name");
    query.setParameter("name", entity.getTopic().getName());
    List list = query.getResultList();
    if(!list.isEmpty()){
      super.create(entity);
      WebSocketServer.notifyNewMessage(entity);
    }
  }
  
  @POST
  @Path("messagesFrom")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public List<Message> messagesFrom(Topic entity) {
    Query query = em.createQuery("select m from Message m where m.topic=:topic");
    query.setParameter("topic", entity);
    return query.getResultList();
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }
  
}
