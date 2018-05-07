/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apiREST;

/**
 *
 * @author juanluis
 */
public interface Cons {
  //String SERVER_IP = "46.249.225.122:62987"; // Remote Rasperry PI server in Norway
  String SERVER_IP = "localhost:22223";     //  Server running localhost      
  String SERVER_REST = "http://"+SERVER_IP+"/server_ddbbInstantRemote/webresources";
  String SERVER_WEBSOCKET = "ws://"+SERVER_IP+"/server_ddbbInstantRemote/ws";
  
}

