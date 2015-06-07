/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package brokerserver;
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
            return new ResultInfo(0, "成功");
        }
        catch(Exception ex)
        {
            return new ResultInfo(1, ex.getMessage());
        }
    }
}
