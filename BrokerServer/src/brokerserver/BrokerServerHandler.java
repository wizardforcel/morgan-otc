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
    
    public TradeResultInfo sell(
        String good,
        String trader,
        String broker,
        int count,
        int price
    )
    {
        try
        {
            System.out.println("method sell is called...");
            long timestamp = Calendar.getInstance().getTimeInMillis();
            Connection conn = DBConn.getDbConn();
            
            //增加卖家信息
            String sql = "INSERT INTO order_t " + 
                         "(name, trader, broker, count, price, status, time, type) " + 
                         "VALUES (?,?,?,?,?,1,?,0)";
            PreparedStatement stmt 
              = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, good);
            stmt.setString(2, trader);
            stmt.setString(3, broker);
            stmt.setInt(4, count);
            stmt.setInt(5, price);
            stmt.setLong(6, timestamp);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int sellerId = rs.getInt(1);
            
            //获取所有匹配的买家
            sql = "SELECT id, price, count, time " +
                  "FROM order_t WHERE name=? AND broker=? AND price>=? AND status=1 AND type=1";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, good);
            stmt.setString(2, broker);
            stmt.setInt(3, price);
            rs = stmt.executeQuery();
            ArrayList<Order> li = new ArrayList<>();
            while(rs.next())
            {
                Order o = new Order();
                o.setId(rs.getInt(1));
                o.setPrice(rs.getInt(2));
                o.setCount(rs.getInt(3));
                o.setTime(rs.getLong(4));
                li.add(o);
            }
            
            //筛选最优买家
            if(li.isEmpty())
                return new TradeResultInfo(0, "成功", sellerId, null);
            Collections.sort(li, new Comparator<Order>(){
                @Override
                public int compare(Order o1, Order o2) {
                    if(o1.getPrice() != o2.getPrice())
                        return o1.getPrice() - o2.getPrice();
                    else if(o1.getCount() != o2.getCount())
                        return o1.getCount() - o2.getCount();
                    else
                        return (int)(o2.getTime() - o1.getTime());
                }
            });
            Order bestBuyer = li.get(li.size() - 1);
            
            //增加成交信息
            int tradeCount = (count>bestBuyer.getCount())? bestBuyer.getCount(): count;
            sql = "INSERT INTO history (seller, buyer, count, time)" + 
                  "VALUES (?,?,?,?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, sellerId);
            stmt.setInt(2, bestBuyer.getId());
            stmt.setInt(3, tradeCount);
            stmt.setLong(4, timestamp);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            rs.next();
            int historyId = rs.getInt(1);
            
            //更新卖家买家信息
            int sellerStatus;
            if(bestBuyer.getCount() == count)
            {
                sellerStatus = 2;
                sql = "UPDATE order_t SET status=2 WHERE id IN (?,?)";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, sellerId);
                stmt.setInt(2, bestBuyer.getId());
                stmt.executeUpdate();
            }
            else
            {
                int smaller = (count>bestBuyer.getCount())? bestBuyer.getId(): sellerId;
                int bigger = (count>bestBuyer.getCount())? sellerId: bestBuyer.getId();
                int remain = Math.abs(count - bestBuyer.getCount());
                sellerStatus = (smaller == sellerId)? 2: 1;
                sql = "UPDATE order_t SET status=2 WHERE id=?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, smaller);
                stmt.executeUpdate();
                sql = "UPDATE order_t SET count=? WHERE id=?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, remain);
                stmt.setInt(2, bigger);
                stmt.executeUpdate();
            }
            
            Order seller = new Order();
            seller.setId(sellerId);
            seller.setTrader(trader);
            seller.setBroker(broker);
            seller.setPrice(price);
            seller.setCount(count);
            seller.setStatus(sellerStatus);
            seller.setTime(timestamp);
            seller.setType(OrderType.SELL);
            
            History h = new  History();
            h.setId(historyId);
            h.setSeller(seller);
            h.setBuyer(bestBuyer);
            h.setCount(tradeCount);
            h.setTime(timestamp);
            
            return new TradeResultInfo(0, "成功", sellerId, h);
        }
        catch(SQLException sqlex)
        {
            String errmsg = "数据库错误：" + sqlex.getMessage();
            return new TradeResultInfo(1024 + sqlex.getErrorCode(), sqlex.getMessage(), 0, null);
        }
        catch(Exception ex)
        {
            return new TradeResultInfo(1, ex.getMessage(), 0, null);
        }
    }
    
    public ResultInfo addBroker(String broker)
    {
        try
        {
            System.out.println("method addBroker is called...");
            Connection conn = DBConn.getDbConn();
            String sql = "INSERT IGNORE INTO broker VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, broker);
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
    
    public QueryResultInfo<String> getBroker()
    {
        try
        {
            System.out.println("method getBroker is called...");
            Connection conn = DBConn.getDbConn();
            
            String sql = "SELECT * FROM broker";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<String> li = new ArrayList<>();
            while(rs.next())
                li.add(rs.getString(1));
            rs.close();
            
            return new QueryResultInfo<String>(0, "成功", li.toArray(new String[0]));
        }
        catch(SQLException sqlex)
        {
            String errmsg = "数据库错误：" + sqlex.getMessage();
            return new QueryResultInfo<String>(1024 + sqlex.getErrorCode(), errmsg, null);
        }
        catch(Exception ex)
        {
            return new QueryResultInfo<String>(1, ex.getMessage(), null);
        }
    }
    
    public TradeResultInfo buy(
        String good,
        String trader,
        String broker,
        int count,
        int price
    )
    {
        try
        {
            System.out.println("method sell is called...");
            long timestamp = Calendar.getInstance().getTimeInMillis();
            Connection conn = DBConn.getDbConn();
            
            //增加买家信息
            String sql = "INSERT INTO order_t " + 
                         "(name, trader, broker, count, price, status, time, type) " + 
                         "VALUES (?,?,?,?,?,1,?,1)";
            PreparedStatement stmt 
              = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, good);
            stmt.setString(2, trader);
            stmt.setString(3, broker);
            stmt.setInt(4, count);
            stmt.setInt(5, price);
            stmt.setLong(6, timestamp);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int buyerId = rs.getInt(1);
            
            //获取所有匹配的卖家
            sql = "SELECT id, price, count, time " +
                  "FROM order_t WHERE name=? AND broker=? AND price<=? AND status=1 AND type=0";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, good);
            stmt.setString(2, broker);
            stmt.setInt(3, price);
            rs = stmt.executeQuery();
            ArrayList<Order> li = new ArrayList<>();
            while(rs.next())
            {
                Order o = new Order();
                o.setId(rs.getInt(1));
                o.setPrice(rs.getInt(2));
                o.setCount(rs.getInt(3));
                o.setTime(rs.getLong(4));
                li.add(o);
            }
            
            //筛选最优卖家
            if(li.isEmpty())
                return new TradeResultInfo(0, "成功", buyerId, null);
            Collections.sort(li, new Comparator<Order>(){
                @Override
                public int compare(Order o1, Order o2) {
                    if(o1.getPrice() != o2.getPrice())
                        return o2.getPrice() - o1.getPrice();
                    else if(o1.getCount() != o2.getCount())
                        return o1.getCount() - o2.getCount();
                    else
                        return (int)(o2.getTime() - o1.getTime());
                }
            });
            Order bestSeller = li.get(li.size() - 1);
            
            //增加成交信息
            int tradeCount = (count>bestSeller.getCount())? bestSeller.getCount(): count;
            sql = "INSERT INTO history (seller, buyer, count, time)" + 
                  "VALUES (?,?,?,?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, bestSeller.getId());
            stmt.setInt(2, buyerId);
            stmt.setInt(3, tradeCount);
            stmt.setLong(4, timestamp);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            rs.next();
            int historyId = rs.getInt(1);
            
            //更新卖家买家信息
            int buyerStatus;
            if(bestSeller.getCount() == count)
            {
                buyerStatus = 2;
                sql = "UPDATE order_t SET status=2 WHERE id IN (?,?)";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, buyerId);
                stmt.setInt(2, bestSeller.getId());
                stmt.executeUpdate();
            }
            else
            {
                int smaller = (count>bestSeller.getCount())? bestSeller.getId(): buyerId;
                int bigger = (count>bestSeller.getCount())? buyerId: bestSeller.getId();
                int remain = Math.abs(count - bestSeller.getCount());
                buyerStatus = (smaller == buyerId)? 2: 1;
                sql = "UPDATE order_t SET status=2 WHERE id=?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, smaller);
                stmt.executeUpdate();
                sql = "UPDATE order_t SET count=? WHERE id=?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, remain);
                stmt.setInt(2, bigger);
                stmt.executeUpdate();
            }
            
            Order buyer = new Order();
            buyer.setId(buyerId);
            buyer.setTrader(trader);
            buyer.setBroker(broker);
            buyer.setPrice(price);
            buyer.setCount(count);
            buyer.setStatus(buyerStatus);
            buyer.setTime(timestamp);
            buyer.setType(OrderType.SELL);
            
            History h = new  History();
            h.setId(historyId);
            h.setSeller(bestSeller);
            h.setBuyer(buyer);
            h.setCount(tradeCount);
            h.setTime(timestamp);
            
            return new TradeResultInfo(0, "成功", buyerId, h);
        }
        catch(SQLException sqlex)
        {
            String errmsg = "数据库错误：" + sqlex.getMessage();
            return new TradeResultInfo(1024 + sqlex.getErrorCode(), errmsg, -1, null);
        }
        catch(Exception ex)
        {
            return new TradeResultInfo(1, ex.getMessage(), -1, null);
        }
    }
    
    public ResultInfo cancel(
        String trader, //用户名称
        int id //商品id
    )
    {
        try
        {
            System.out.println("method cancel is called...");
            Connection conn = DBConn.getDbConn();
            
            String sql = "SELECT 1 FROM order_t WHERE id=? AND status=1 AND trader=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, trader);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next())
            {
                return new ResultInfo(1, "此记录不存在");
            }
            
            sql = "UPDATE order_t SET status=3 WHERE id=?";
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
    
    public TradeResultInfo modify(
        String trader, //trader服务器的名字
        int id, //商品id      
        int count,
        int price
    )
    {
        System.out.println("method modify is called...");

        QueryResultInfo<Order> rs1 = orderById(id);
        if(rs1.getErrno() != 0)
            return new TradeResultInfo(rs1.getErrno(), rs1.getErrmsg(), 0, null);
        if(rs1.getList().length == 0)
            return new TradeResultInfo(1, "此条记录不存在", 0, null);
        Order o = rs1.getList()[0];
        if(o.getStatus() != OrderStatus.OPEN ||
           !o.getTrader().equals(trader))
            return new TradeResultInfo(1, "此条记录不存在", 0, null);
            
        ResultInfo rs2 = cancel(trader, id);
        if(rs2.getErrno() != 0)
            return new TradeResultInfo(rs2.getErrno(), rs2.getErrmsg(), 0, null);
            
        if(o.getType() == OrderType.SELL)
            return sell(o.getName(), trader, o.getBroker(), count, price);
        else
            return buy(o.getName(), trader, o.getBroker(), count, price);
    }
   
    public QueryResultInfo<Order> orderById(int id)
    {
        try
        {
            System.out.println("method orderById is called...");
            Connection conn = DBConn.getDbConn();
            
            String sql = "SELECT id, name, trader, broker, " + 
                         "count, price, status, time, type " + 
                         "FROM order_t WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Order> li = new ArrayList<>();
            while(rs.next())
            {
                Order o = new Order();
                o.setId(rs.getInt(1));
                o.setName(rs.getString(2));
                o.setTrader(rs.getString(3));
                o.setBroker(rs.getString(4));
                o.setCount(rs.getInt(5));
                o.setPrice(rs.getInt(6));
                o.setStatus(rs.getInt(7));
                o.setTime(rs.getLong(8));
                o.setType(rs.getInt(9));
                li.add(o);
            }
            rs.close();
            
            return new QueryResultInfo<Order>(0, "成功", li.toArray(new Order[0]));
        }
        catch(SQLException sqlex)
        {
            String errmsg = "数据库错误：" + sqlex.getMessage();
            return new QueryResultInfo<Order>(1024 + sqlex.getErrorCode(), errmsg, null);
        }
        catch(Exception ex)
        {
            return new QueryResultInfo<Order>(1, ex.getMessage(), null);
        }
    }
    
    
    public QueryResultInfo<Order> order(
        String good,
        String trader,
        String broker,
        int status,
        int type
    )
    {
        try
        {
            System.out.println("method order is called...");
            Connection conn = DBConn.getDbConn();
            
            ArrayList<String> params = new ArrayList<>();
            String sql = "SELECT id, name, trader, broker, " + 
                         "count, price, status, time, type " + 
                         "FROM order_t WHERE 1=1";
            if(good != null)
            {
                sql += " AND name=?";
                params.add(good);
            }
            if(trader != null)
            {
                sql += " AND trader=?";
                params.add(trader);
            }
            if(broker != null)
            {
                sql += " AND broker=?";
                params.add(broker);
            }
            if(status != OrderStatus.ALL)
            {
                sql += " AND status=?";
                params.add(String.valueOf(status));
            }
            if(type != OrderType.ALL)
            {
                sql += " AND type=?";
                params.add(String.valueOf(type));
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            for(int i = 0; i < params.size(); i++)
                stmt.setString(i + 1, params.get(i));
            ResultSet rs = stmt.executeQuery();
            
            ArrayList<Order> list = new ArrayList<>();
            while(rs.next())
            {
                Order o = new Order();
                o.setId(rs.getInt(1));
                o.setName(rs.getString(2));
                o.setTrader(rs.getString(3));
                o.setBroker(rs.getString(4));
                o.setCount(rs.getInt(5));
                o.setPrice(rs.getInt(6));
                o.setStatus(rs.getInt(7));
                o.setTime(rs.getLong(8));
                o.setType(rs.getInt(9));
                list.add(o);
            }
            rs.close();
            
            return new QueryResultInfo<Order>(0, "成功", list.toArray(new Order[0]));
        }
        catch(SQLException sqlex)
        {
            String errmsg = "数据库错误：" + sqlex.getMessage();
            return new QueryResultInfo<Order>(1024 + sqlex.getErrorCode(), errmsg, null);
        }
        catch(Exception ex)
        {
            return new QueryResultInfo<Order>(1, ex.getMessage(), null);
        }
    }
        
    public QueryResultInfo<History> history(
        String good,
        String broker,
        String seller,
        String buyer
    )
    {
        try   
        {
            System.out.println("method history is called...");
            Connection conn = DBConn.getDbConn();
            
            ArrayList<String> params = new ArrayList<>();
            String sql = "SELECT history.id, history.count, " + 
                         "history.time, so.id, so.name, so.trader, " + 
                         "so.broker, so.count, so.price, so.status, " + 
                         "so.time, bo.id, bo.trader, bo.count, bo.price, " + 
                         "bo.status, bo.time " + 
                         "FROM history " + 
                         "JOIN order_t AS so ON history.seller=so.id " + 
                         "JOIN order_t AS bo ON history.buyer=bo.id " +
                         "WHERE 1=1";
            if(good != null)
            {
                sql += " AND so.name=?";
                params.add(good);
            }
            if(seller != null)
            {
                sql += " AND so.trader=?";
                params.add(seller);
            }
            if(buyer != null)
            {
                sql += " AND bo.trader=?";
                params.add(buyer);
            }
            if(broker != null)
            {
                sql += " AND so.broker=?";
                params.add(broker); 
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            for(int i = 0; i < params.size(); i++)
                stmt.setString(i + 1, params.get(i));
            ResultSet rs = stmt.executeQuery();
            
            ArrayList<History> list = new ArrayList<>();
            while(rs.next())
            {
                Order so = new Order();
                so.setId(rs.getInt(4));
                so.setName(rs.getString(5));
                so.setTrader(rs.getString(6));
                so.setBroker(rs.getString(7));
                so.setCount(rs.getInt(8));
                so.setPrice(rs.getInt(9));
                so.setStatus(rs.getInt(10));
                so.setTime((rs.getLong(11)));
                so.setType(OrderType.SELL);
                
                Order bo = new Order();
                bo.setId(rs.getInt(12));
                bo.setName(rs.getString(5));
                bo.setTrader(rs.getString(13));
                bo.setBroker(rs.getString(7));
                bo.setCount(rs.getInt(14));
                bo.setPrice(rs.getInt(15));
                bo.setStatus(rs.getInt(16));
                bo.setTime(rs.getLong(17));
                bo.setType(OrderType.BUY);
                
                History h = new History();
                h.setId(rs.getInt(1));
                h.setSeller(so);
                h.setBuyer(bo);
                h.setCount(rs.getInt(2));
                h.setTime(rs.getLong(3));
                
                list.add(h);
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
    
    public PriceResultInfo lastPrice(String good)
    {
        try
        {
            System.out.println("method lastPrice is called...");
            Connection conn = DBConn.getDbConn();
            
            String sql = "SELECT order_t.price " + 
                         "FROM history JOIN order_t ON history.buyer=order_t.id " + 
                         "WHERE order_t.name=? " +
                         "ORDER BY history.time DESC LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, good);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next())
                return new PriceResultInfo(0, "成功", -1);
            
            int price = rs.getInt(1);
            return new PriceResultInfo(0, "成功", price);
        }
        catch(SQLException sqlex)
        {
            String errmsg = "数据库错误：" + sqlex.getMessage();
            return new PriceResultInfo(1024 + sqlex.getErrorCode(), errmsg, -1);
        }
        catch(Exception ex)
        {
            return new PriceResultInfo(1, ex.getMessage(), -1);
        }
    }
}
