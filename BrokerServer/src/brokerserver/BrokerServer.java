/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package brokerserver;
import brokerserver.utility.*;
import java.sql.*;
import java.util.Calendar;
import otc.entity.ResultInfo;

/**
 *
 * @author Wizard
 */
public class BrokerServer 
{
    public String hello(String name)
    {
        System.out.println("method hello is called...");
        return "hello " + name;
    }
    
    public ResultInfo sell(
        String traderName, //trader服务器的名字
        String name, //商品名字
        String comment, //商品描述
        int count, //商品数量
        boolean vip //是否设置为vip可见
    )
    {
        try
        {
            System.out.println("method sell is called...");
            long timestamp = Calendar.getInstance().getTimeInMillis();
            Connection conn = DBConn.getDbConn();
            String sql 
              = "INSERT INTO good (trader_name, name, comment, count, status, vip, time) VALUES (?,?,?,?,1,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, traderName);
            stmt.setString(2, name);
            stmt.setString(3, comment);
            stmt.setInt(4, count);
            stmt.setInt(5, vip? 1: 0);
            stmt.setLong(6, timestamp);
            stmt.executeUpdate();
            return new ResultInfo(0, "成功");
        }
        catch(SQLException sqlex)
        {
            String errmsg = "数据库错误：" + sqlex.getMessage();
            return new ResultInfo(1024 + sqlex.getErrorCode(), errmsg);
        }
        catch(Exception ex)
        {
            return new ResultInfo(1, ex.getMessage());
        }
    }
    
    public ResultInfo regTrader(
        String traderName, //trader服务器的名字
        String host, //主机域名或者ip
        int port, //端口号
        String appName //应用名称
    )
    {
        try
        {
            System.out.println("method regTrader is called...");
            Connection conn = DBConn.getDbConn();
            String sql = "REPLACE INTO trader VALUES (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, traderName);
            stmt.setString(2, host);
            stmt.setInt(3, port);
            stmt.setString(4, appName);
            stmt.executeUpdate();
            return new ResultInfo(0, "成功");
        }
        catch(SQLException sqlex)
        {
            String errmsg = "数据库错误：" + sqlex.getMessage();
            return new ResultInfo(1024 + sqlex.getErrorCode(), errmsg);
        }
        catch(Exception ex)
        {
            return new ResultInfo(1, ex.getMessage());
        }
    }
}
