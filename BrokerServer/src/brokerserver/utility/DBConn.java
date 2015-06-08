package brokerserver.utility;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {

	private static DBConfig cfg;
	
	static
	{
		try {
			cfg = new DBConfig();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Connection getDbConn() 
		   throws ClassNotFoundException, SQLException
	{
		Class.forName(cfg.GetDriver());
		String url = "jdbc:mysql://" + cfg.GetServer() + ":" + cfg.GetPort() + 
		             "/" + cfg.GetName() + "?user=" + cfg.GetUsername() + 
		             "&password=" + cfg.GetPassword();
	    Connection conn = DriverManager.getConnection(url);
	    return conn;
	}
	
}
