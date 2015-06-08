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
import otc.entity.Good;
import otc.entity.History;
import otc.entity.QueryResultInfo;

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
    
    public ResultInfo sell(
        String traderName, //trader服务器的名字
        String userName, //卖家名字
        String name, //商品名字
        String comment, //商品描述
        int count, //商品数量
        int price, //商品价格
        boolean vip //是否设置为vip可见
    ) throws MalformedURLException, XmlRpcException
    {
        Object[] params 
          = new Object[]{traderName, userName, name, comment, count, price, vip};
        return (ResultInfo) getConn().execute("otc.sell", params); 
    }
    
    public ResultInfo regTrader(
        String traderName, //trader服务器的名字
        String host, //主机域名或者ip
        int port, //端口号
        String appName //应用名称
    ) throws MalformedURLException, XmlRpcException
    {
        Object[] params = new Object[]{traderName, host, port, appName};
        return (ResultInfo) getConn().execute("otc.regTrader", params); 
    }
    
    public ResultInfo buy(
        String traderName, //trader服务器的名字
        String userName, //用户名称
        int id, //商品id
        int count, //数量
        int price //价格
    ) throws MalformedURLException, XmlRpcException
    {
        Object[] params = new Object[]{traderName, userName, id, count, price};
        return (ResultInfo) getConn().execute("otc.buy", params);
    }
    
    public QueryResultInfo<History> history()
           throws MalformedURLException, XmlRpcException
    {
        return (QueryResultInfo<History>) getConn().execute("otc.history", new Object[0]);
    }
    
    public QueryResultInfo<Good> query(
        boolean vip, //如果该选项为false，则不显示vip可见的商品
        boolean onSale //如果该选项为false，则不显示在售中之外的商品
    ) throws MalformedURLException, XmlRpcException
    {
        Object[] params = new Object[]{vip, onSale};
        return (QueryResultInfo<Good>) getConn().execute("otc.query", params);
    }
    
    public ResultInfo modify(
        String traderName, //trader服务器的名字
        String userName, //用户名称
        int id, //商品id      
        String name,
        String comment,
        int count,
        int price,
        boolean vip
    ) throws MalformedURLException, XmlRpcException
    {
        Object[] params 
          = new Object[]{traderName, userName, id, name, comment, count, price, vip};
        return (ResultInfo) getConn().execute("otc.modify", params);
    }
    
    public ResultInfo cancel(
        String traderName, //trader服务器的名字
        String userName, //用户名称
        int id //商品id
    ) throws MalformedURLException, XmlRpcException
    {
        Object[] params = new Object[]{traderName, userName, id};
        return (ResultInfo) getConn().execute("otc.cancel", params);
    }
    
}
