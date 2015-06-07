/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package brokerclient;

import otc.entity.ResultInfo;
import org.apache.xmlrpc.client.*;
import java.net.*;
import org.apache.xmlrpc.XmlRpcException;

/**
 *
 * @author Wizard
 */
public class BrokerClient 
{
   
    public static XmlRpcClient getConn() 
           throws MalformedURLException
    {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();  
        config.setServerURL(new URL("http://localhost:8089"));  
        config.setEnabledForExtensions(true);    
        config.setConnectionTimeout(60 * 1000);  
        config.setReplyTimeout(60 * 1000);  
        XmlRpcClient client = new XmlRpcClient();  
        client.setConfig(config);  
        return client;
    }
    
    public static String hello(String name) 
           throws MalformedURLException, XmlRpcException
    {
        Object[] params = new Object[]{name};  
        return (String) getConn().execute("otc.hello", params); 
    }
    
    public static ResultInfo sell(
        String traderName, //trader服务器的名字
        String name, //商品名字
        String comment, //商品描述
        int count, //商品数量
        boolean vip //是否设置为vip可见
    ) throws MalformedURLException, XmlRpcException
    {
        Object[] params = new Object[]{traderName, name, comment, count, vip};
        return (ResultInfo) getConn().execute("otc.sell", params); 
    }
    
}
