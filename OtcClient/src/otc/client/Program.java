/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package otc.client;

import otc.entity.*;

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
            OtcClient client = new OtcClient("59.78.56.5", 8089);
            
            /*ResultInfo result 
              = client.addBroker("broker1");
            System.out.println(result.getErrno() + " " + result.getErrmsg());*/
            
            //QueryResultInfo<String> rs2 = client.getBroker();
            //System.out.println(rs2.getList().length);
            
            /*TradeResultInfo rs3 = client.sell("test4", "trader1", "broker1", 100, 110);
            System.out.println(rs3.getErrno() + " " + rs3.getErrmsg() + " " + 
                               rs3.getOrderId() + " " + (rs3.getTradeRecord() == null));*/
            
            //ResultInfo rs4 = client.cancel("trader1", 1);
            //System.out.println(rs4.getErrno() + " " + rs4.getErrmsg());
            
            //QueryResultInfo<Order> rs5 = client.order(null, null, null, OrderStatus.ALL, OrderType.ALL);
            //System.out.println(rs5.getList().length);
            
            /*TradeResultInfo rs6 = client.buy("test4", "trader2", "broker1", 100, 110);
            System.out.println(rs6.getErrno() + " " + rs6.getErrmsg() + " " + 
                               rs6.getOrderId() + " " + (rs6.getTradeRecord() == null));*/
            
            //QueryResultInfo<History> rs7 = client.history(null, null, null, null);
            //System.out.println(rs7.getList().length);
            
            //PriceResultInfo rs8 = client.lastPrice("abc");
            //System.out.print(rs8.getPrice());
            
            //QueryResultInfo<Order> rs9 = client.orderById(1);
            //System.out.println(rs9.getList().length);
            
            TradeResultInfo rs10 = client.modify("trader2", 4, 10, 200);
            System.out.println(rs10.getErrno() + " " + rs10.getErrmsg() + " " + rs10.getOrderId());
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
