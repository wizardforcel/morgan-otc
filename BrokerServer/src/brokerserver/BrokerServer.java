/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package brokerserver;

import java.io.IOException;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

/**
 *
 * @author Wizard
 */
public class BrokerServer 
{
    private WebServer server;
    
    public BrokerServer(int port) 
           throws XmlRpcException
    {
        server = new WebServer(port);
        XmlRpcServer xmlRpcServer = server.getXmlRpcServer();
        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        phm.addHandler("otc", BrokerServerHandler.class);
        XmlRpcServerConfigImpl config = new XmlRpcServerConfigImpl();
        config.setEnabledForExtensions(true);
        xmlRpcServer.setHandlerMapping(phm);
        xmlRpcServer.setConfig(config);
    }
    
    public BrokerServer() 
           throws XmlRpcException
    {
        this(80);
    }
    
    public void start() 
           throws IOException
    {
        server.start();
    }
    
    
}
