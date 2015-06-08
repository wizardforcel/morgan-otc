package brokerserver.utility;

import java.io.*;
import java.util.*;

public class DBConfig
{
	private String db_server;
	private String db_port;
	private String db_username;
	private String db_password;
	private String db_name;
	private String db_driver;
	
	public DBConfig()
		   throws IOException
	{
		InputStream in = getClass().getResourceAsStream("/db.properties");
		Properties props = new Properties();   
        props.load(in);   
        in.close();
        db_server = props.getProperty("db_server");
        db_port = props.getProperty("db_port");
        db_username = props.getProperty("db_username");
        db_password = props.getProperty("db_password");
        db_name = props.getProperty("db_name");
        db_driver = props.getProperty("db_driver");
	}
	
	public String GetServer()
	{
		return db_server;
	}
	
	public String GetPort()
	{
		return db_port;
	}
	
	public String GetUsername()
	{
		return db_username;
	}
	
	public String GetPassword()
	{
		return db_password;
	}
	
	public String GetName()
	{
		return db_name;
	}
	
	public String GetDriver()
	{
		return db_driver;
	}
}