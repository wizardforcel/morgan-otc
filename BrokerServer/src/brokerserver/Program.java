/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package brokerserver;

import org.apache.xmlrpc.webserver.*;
import org.apache.xmlrpc.server.*;
import java.util.*;

/**
 *
 * @author Wizard
 */
public class Program {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try 
        {
            System.out.println("Attempting to start XML-RPC Server...");

            WebServer server = new WebServer(8089);
            XmlRpcServer xmlRpcServer = server.getXmlRpcServer();
            PropertyHandlerMapping phm = new PropertyHandlerMapping();
            phm.addHandler("otc", BrokerServer.class);
            XmlRpcServerConfigImpl config = new XmlRpcServerConfigImpl();
            config.setEnabledForExtensions(true);
            xmlRpcServer.setHandlerMapping(phm);
            xmlRpcServer.setConfig(config);
            server.start();

            System.out.println("Started successfully.");
            System.out.println("Accepting requests. (Halt program to stop.)");
      } 
      catch (Exception ex)
      {
          ex.printStackTrace();
      }
    }
    
}
