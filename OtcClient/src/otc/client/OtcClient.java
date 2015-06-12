/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package otc.client;

import otc.entity.ResultInfo;
import org.apache.xmlrpc.client.*;
import java.net.*;
import org.apache.xmlrpc.XmlRpcException;
import otc.entity.*;

/**
 *
 * @author Wizard
 */
public class OtcClient 
{
    private URL url;
    
    public OtcClient(String host) 
           throws MalformedURLException
    {
        this(host, 80);
    }
    
    public OtcClient(String host, int port) 
           throws MalformedURLException
    {
        this(host, port, "");
    }
    
    public OtcClient(String host, int port, String appName) 
           throws MalformedURLException
    {
        String urlStr = "http://" + host + ":" + String.valueOf(port);
        if(!appName.equals(""))
            urlStr += "/" + appName;
        url = new URL(urlStr);
    }
   
    private XmlRpcClient getConn() 
           throws MalformedURLException
    {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();  
        config.setServerURL(url);  
        config.setEnabledForExtensions(true);    
        config.setConnectionTimeout(60 * 1000);  
        config.setReplyTimeout(60 * 1000);  
        XmlRpcClient client = new XmlRpcClient();  
        client.setConfig(config);  
        return client;
    }
    
    public String hello(String name) 
           throws MalformedURLException, XmlRpcException
    {
        Object[] params = new Object[]{name};  
        return (String) getConn().execute("otc.hello", params); 
    }
    
    public TradeResultInfo modify(
        String trader, //trader服务器的名字
        int id, //商品id      
        int count,
        int price
    ) throws MalformedURLException, XmlRpcException
    {
        Object[] params = new Object[]{trader, id, count, price};
        return (TradeResultInfo) getConn().execute("otc.modify", params);
    }
    
    public QueryResultInfo<Order> orderById(int id)
           throws MalformedURLException, XmlRpcException
    {
        Object[] params = new Object[]{id};
        return (QueryResultInfo<Order>) getConn().execute("otc.orderById", params);
    }
    
    public TradeResultInfo sell(
        String good,
        String trader,
        String broker,
        int count,
        int price
    ) throws MalformedURLException, XmlRpcException
    {
        Object[] params 
          = new Object[]{good, trader, broker, count, price};
        return (TradeResultInfo) getConn().execute("otc.sell", params); 
    }
    
    public ResultInfo addBroker(String broker)
           throws MalformedURLException, XmlRpcException
    {
        Object[] params 
          = new Object[]{broker};
        return (ResultInfo) getConn().execute("otc.addBroker", params);
    }
    
    public QueryResultInfo<String> getBroker()
           throws MalformedURLException, XmlRpcException
    {
        return (QueryResultInfo<String>) getConn().execute("otc.getBroker", new Object[0]);
    }
    
    public TradeResultInfo buy(
        String good,
        String trader,
        String broker,
        int count,
        int price
    ) throws MalformedURLException, XmlRpcException
    {
        Object[] params = new Object[]{good, trader, broker, count, price};
        return (TradeResultInfo) getConn().execute("otc.buy", params);
    }
   
    
    public QueryResultInfo<History> history(
        String good,
        String broker,
        String seller,
        String buyer
    ) throws MalformedURLException, XmlRpcException
    {
        Object[] params = new Object[]{good, broker, seller, buyer};
        return (QueryResultInfo<History>) getConn().execute("otc.history", params);
    }
    
    public QueryResultInfo<Order> order(
        String good,
        String trader,
        String broker,
        int status,
        int type
    ) throws MalformedURLException, XmlRpcException
    {
        Object[] params = new Object[]{good, trader, broker, status, type};
        return (QueryResultInfo<Order>) getConn().execute("otc.order", params);
    }
    
    public ResultInfo cancel(
        String trader, //用户名称
        int id //商品id
    ) throws MalformedURLException, XmlRpcException
    {
        Object[] params = new Object[]{trader, id};
        return (ResultInfo) getConn().execute("otc.cancel", params);
    }
    
    public PriceResultInfo lastPrice(String good)
           throws MalformedURLException, XmlRpcException
    {
         Object[] params = new Object[]{good};
        return (PriceResultInfo) getConn().execute("otc.lastPrice", params); 
    }
    
}
