package servlet;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

//import org.jboss.logging.Logger;


public class ConnectionManager {

   static Connection con;
   static String url;
   static Logger log = Logger.getLogger("ConnectionDatabase");
   //Obtencion de conexion con JNDI      
   static Connection getConnection(){
 
	   String DATASOURCE_CONTEXT = "java:jboss/datasources/MySQLDS";//Probando
	    
	    Connection result = null;
	    try {
	      Context initialContext = new InitialContext();
	      if ( initialContext == null){
	        log.info("JNDI problem. Cannot get InitialContext.");
	      }
	      DataSource datasource = (DataSource)initialContext.lookup(DATASOURCE_CONTEXT);
	      if (datasource != null) {
	        result = datasource.getConnection();
	      }
	      else {
	        log.info("Failed to lookup datasource.");
	      }
	    }
	    catch ( NamingException ex ) {
	      log.info("Cannot get connection: "+"("+ DATASOURCE_CONTEXT+"9"+ ex);
	    }
	    catch(SQLException ex){
	      log.info("Cannot get connection: " + ex);
	    }
	    return result;
    }
   
    public static Connection getConexionMySQL(String ipServer,String user,String pwd, String schema ){
    Connection conSQL = null;
    Properties connectionProps = new Properties();
    connectionProps.put("user", user);
    connectionProps.put("password", pwd);

       try {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            Logger.getLogger(GeneradorCartones.class.getName()).log(Level.SEVERE, null, ex);
        }
           String cadena = "jdbc:mysql://"+ipServer+":3306/"+schema;
            System.out.println("Cadena conexion:"+ cadena);
           conSQL = DriverManager.getConnection(cadena,connectionProps);
           System.out.println("Connected to database");
       } catch (SQLException ex) {
           Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
       }
  
    
    return conSQL;
    }
}
