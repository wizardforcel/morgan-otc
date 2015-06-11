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
            /*IdResultInfo result 
              = client.sell("test", "test", "test2", "test", 100, 100, false);
            System.out.println(result.getErrno() + " " + result.getErrmsg() + " " + result.getId());*/
            
            QueryResultInfo<Good> rs2 = client.query(true, true);
            System.out.println(rs2.getList().length);
            
            //QueryResultInfo<History> rs3 = client.history();
            //System.out.println(rs3.getList().length);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
