/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package brokerclient;

import otc.entity.ResultInfo;

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
            ResultInfo result = BrokerClient.sell("test", "test", "test", 100, false);
            System.out.println(result.getErrno() + " " + result.getErrmsg());  
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
