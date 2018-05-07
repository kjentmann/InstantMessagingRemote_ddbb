/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.service;

import entity.Subscriber;
import entity.User;
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

/**
 *
 * @author upcnet
 */
@Stateless
@Path("entity.subscriber")
public class SubscriberFacadeREST extends AbstractFacade<Subscriber> {
  @PersistenceContext(unitName = "PubSubWebServerPU")
  private EntityManager em;

  public SubscriberFacadeREST() {
    super(Subscriber.class);
  }
  
  @POST
  @Path("create")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public Subscriber create_and_return(Subscriber entity) {
    Query query = em.createQuery("select s from Subscriber s where s.user=:user and s.topic=:topic");
    query.setParameter("user", entity.getUser());
    query.setParameter("topic", entity.getTopic());
    List list = query.getResultList();
    if(list.isEmpty()){
      em.persist(entity);
      em.flush();
      return entity;
    }
    else{
      return (Subscriber)list.get(0);
    }
  }
  
  @POST
  @Path("delete")
  @Consumes({"application/xml", "application/json"})
  public void remove(Subscriber entity) {
    Query query = em.createQuery("select s from Subscriber s where s.user=:user and s.topic=:topic");
    query.setParameter("user", entity.getUser());
    query.setParameter("topic", entity.getTopic());
    List list = query.getResultList();
    if(list.isEmpty()){
      return;
    }
    else{
      super.remove((Subscriber)list.get(0));
    }
  }
  
  @POST
  @Path("subscriptions")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public List<Subscriber> subscriptions(User entity) {
    Query query = em.createQuery("select s from Subscriber s where s.user=:user");
    query.setParameter("user", entity);
    return query.getResultList();
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }
  
}
