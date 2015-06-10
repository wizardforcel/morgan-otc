/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package brokerserver;
import otc.entity.*;
import brokerserver.utility.*;
import java.sql.*;
import java.util.*;

/**
 *
 * @author Wizard
 */
public class BrokerServerHandler 
{
    public String hello(String name)
    {
        System.out.println("method hello is called...");
        return "hello " + name;
    }
    
    public IdResultInfo sell(
        String traderName, //trader服务器的名字
        String userName, //卖家名字
        String name, //商品名字
        String comment, //商品描述
        int count, //商品数量
        int price, //商品价格
        boolean vip //是否设置为vip可见
    )
    {
        try
        {
            System.out.println("method sell is called...");
            long timestamp = Calendar.getInstance().getTimeInMillis();
            Connection conn = DBConn.getDbConn();
            String sql 
              = "INSERT INTO good " + 
                "(trader_name, user_name, name, comment, count, price, status, vip, time) " + 
                "VALUES (?,?,?,?,?,?,1,?,?)";
            PreparedStatement stmt 
              = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, traderName);
            stmt.setString(2, userName);
            stmt.setString(3, name);
            stmt.setString(4, comment);
            stmt.setInt(5, count);
            stmt.setInt(6, price);
            stmt.setInt(7, vip? 1: 0);
            stmt.setLong(8, timestamp);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            return new IdResultInfo(0, "成功", id);
        }
        catch(SQLException sqlex)
        {
            String errmsg = "数据库错误：" + sqlex.getMessage();
            return new IdResultInfo(1024 + sqlex.getErrorCode(), errmsg, 0);
        }
        catch(Exception ex)
        {
            return new IdResultInfo(1, ex.getMessage(), 0);
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
    
    public ResultInfo buy(
        String traderName, //trader服务器的名字
        String userName, //用户名称
        int id, //商品id
        int count, //数量
        int price //价格
    )
    {
        try
        {
            System.out.println("method buy is called...");
            Connection conn = DBConn.getDbConn();
            
            String sql = "SELECT price, count, trader_name, user_name " + 
                         "FROM good WHERE id=? AND status=1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next())
            {
                return new ResultInfo(1, "此商品不存在");
            }
            int originPrice = rs.getInt(1);
            int originCount = rs.getInt(2);
            String sellerTrader = rs.getString(3);
            String sellerUser = rs.getString(4);
            
            if(originPrice > price)
            {
                return new ResultInfo(1, "出价不能低于商品价格");
            }
            if(originCount < count)
            {
                return new ResultInfo(1, "商品库存不足");
            }
            if(traderName.equals(sellerTrader) &&
               userName.equals(sellerUser))
            {
                return new ResultInfo(1, "买方和卖方不能为同一个人");
            }
            
            sql = "UPDATE good SET count=?";
            if(originCount == count)
                sql += ", status=2";
            sql += "WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, originCount - count);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            
            long timestamp = Calendar.getInstance().getTimeInMillis();
            sql = "INSERT history (gid, trader_name, user_name, count, price, time) " + 
                   "VALUES (?,?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, traderName);
            stmt.setString(3, userName);
            stmt.setInt(4, count);
            stmt.setInt(5, price);
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
    
    public ResultInfo cancel(
        String traderName, //trader服务器的名字
        String userName, //用户名称
        int id //商品id
    )
    {
        try
        {
            System.out.println("method cancel is called...");
            Connection conn = DBConn.getDbConn();
            
            String sql = "SELECT trader_name, user_name " + 
                         "FROM good WHERE id=? AND status=1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next())
            {
                return new ResultInfo(1, "此商品不存在");
            }
            String sellerTrader = rs.getString(1);
            String sellerUser = rs.getString(2);
            if(!traderName.equals(sellerTrader) ||
               !userName.equals(sellerUser))
            {
                return new ResultInfo(1, "不可以取消别人的商品");
            }
            
            sql = "UPDATE good SET status=3 WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
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
    
    public ResultInfo modify(
        String traderName, //trader服务器的名字
        String userName, //用户名称
        int id, //商品id      
        String name,
        String comment,
        int count,
        int price,
        boolean vip
    )
    {
        try
        {
            System.out.println("method modify is called...");
            Connection conn = DBConn.getDbConn();
            
            String sql = "SELECT trader_name, user_name " + 
                         "FROM good WHERE id=? AND status=1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next())
            {
                return new ResultInfo(1, "此商品不存在");
            }
            String sellerTrader = rs.getString(1);
            String sellerUser = rs.getString(2);
            if(!traderName.equals(sellerTrader) ||
               !userName.equals(sellerUser))
            {
                return new ResultInfo(1, "不可以编辑别人的商品");
            }
            
            sql = "UPDATE good SET name=?, comment=?, count=?, price=?, vip=? WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, comment);
            stmt.setInt(3, count);
            stmt.setInt(4, price);
            stmt.setInt(5, vip? 1: 0);
            stmt.setInt(6, id);
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
    
    public QueryResultInfo<Good> query(
        boolean vip, //如果该选项为false，则不显示vip可见的商品
        boolean onSale //如果该选项为false，则不显示在售中之外的商品
    )
    {
        try
        {
            System.out.println("method query is called...");
            Connection conn = DBConn.getDbConn();
            
            String sql = "SELECT id, trader_name, user_name, name, " + 
                         "comment, count, price, status, vip, time " + 
                         "FROM good WHERE 1=1";
            if(!vip)
                sql += " AND vip=0";
            if(onSale)
                sql += " AND status=1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            ArrayList<Good> list = new ArrayList<>();
            while(rs.next())
            {
                Good g = new Good();
                g.setId(rs.getInt(1));
                g.setTrader(rs.getString(2));
                g.setUser(rs.getString(3));
                g.setName(rs.getString(4));
                g.setComment(rs.getString(5));
                g.setCount(rs.getInt(6));
                g.setPrice(rs.getInt(7));
                g.setStatus(rs.getInt(8));
                g.setVip(rs.getInt(9) != 0);
                g.setTimestamp(rs.getLong(10));
                list.add(g);
            }
            rs.close();
            
            return new QueryResultInfo<Good>(0, "成功", list.toArray(new Good[0]));
        }
        catch(SQLException sqlex)
        {
            String errmsg = "数据库错误：" + sqlex.getMessage();
            return new QueryResultInfo<Good>(1024 + sqlex.getErrorCode(), errmsg, null);
        }
        catch(Exception ex)
        {
            return new QueryResultInfo<Good>(1, ex.getMessage(), null);
        }
    }
    
    public QueryResultInfo<History> history()
    {
        try   
        {
            System.out.println("method history is called...");
            Connection conn = DBConn.getDbConn();
            
            String sql = "SELECT history.id, history.trader_name, " + 
                         "history.user_name, history.count, history.price, " + 
                         "history.time, good.id, good.trader_name, good.user_name, " + 
                         "good.name, good.comment, good.count, good.price, " + 
                         "good.status, good.vip, good.time " + 
                         "FROM good JOIN history ON good.id=history.gid";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            ArrayList<History> list = new ArrayList<>();
            while(rs.next())
            {
                History h = new History();
                h.setId(rs.getInt(1));
                h.setTrader(rs.getString(2));
                h.setUser(rs.getString(3));
                h.setCount(rs.getInt(4));
                h.setPrice(rs.getInt(5));
                h.setTimestamp(rs.getLong(6));
                
                Good g = new Good();
                g.setId(rs.getInt(7));
                g.setTrader(rs.getString(8));
                g.setUser(rs.getString(9));
                g.setName(rs.getString(10));
                g.setComment(rs.getString(11));
                g.setCount(rs.getInt(12));
                g.setPrice(rs.getInt(13));
                g.setStatus(rs.getInt(14));
                g.setVip(rs.getInt(15) != 0);
                g.setTimestamp(rs.getLong(16));
                h.setGood(g);
            }
            rs.close();
            
            return new QueryResultInfo<History>(0, "成功", list.toArray(new History[0]));
        }
        catch(SQLException sqlex)
        {
            String errmsg = "数据库错误：" + sqlex.getMessage();
            return new QueryResultInfo<History>(1024 + sqlex.getErrorCode(), errmsg, null);
        }
        catch(Exception ex)
        {
            return new QueryResultInfo<History>(1, ex.getMessage(), null);
        }
    }
}
