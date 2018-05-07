/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.service;

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

/**
 *
 * @author upcnet
 */
@Stateless
@Path("entity.topic")
public class TopicFacadeREST extends AbstractFacade<Topic> {
  @PersistenceContext(unitName = "PubSubWebServerPU")
  private EntityManager em;

  public TopicFacadeREST() {
    super(Topic.class);
  }
  
  @POST
  @Path("name")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public Topic findByName(Topic topic) {
    System.out.println("findByName: "+topic.getName());
    Query query = em.createQuery("select o from Topic as o where o.name=:name");
    query.setParameter("name", topic.getName());
    List<Topic> result = query.getResultList();
    if(result.isEmpty()) {
      System.out.println("no result for topic name: "+topic.getName());
      return new Topic();}
    else return result.get(0);
  }

  @GET
  @Override
  @Produces({"application/xml", "application/json"})
  public List<Topic> findAll() {
    return super.findAll();
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }
  
}
