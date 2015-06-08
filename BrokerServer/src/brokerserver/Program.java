/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package brokerserver;

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
            System.out.println("Attempting to start XML-RPC Server...");

            int port = args.length == 0? 8089: Integer.parseInt(args[0]);
            BrokerServer server = new  BrokerServer(port);
            server.start();

            System.out.println("Started successfully.");
            System.out.println("Accepting requests. (Halt program to stop.)");
      } 
      catch (Exception ex)
      {
          ex.printStackTrace();
      }
    }
    
}
