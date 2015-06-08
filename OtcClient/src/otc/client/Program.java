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
            OtcClient client = new OtcClient("localhost", 8089);
            /*ResultInfo result 
              = client.sell("test", "test", "test", "test", 100, 100, false);
            System.out.println(result.getErrno() + " " + result.getErrmsg());*/
            
            //QueryResultInfo<Good> rs2 = client.query(true, true);
            //System.out.println(rs.getList().length);
            
            QueryResultInfo<History> rs3 = client.history();
            System.out.println(rs3.getList().length);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
