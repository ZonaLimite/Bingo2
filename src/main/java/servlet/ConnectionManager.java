package servlet;

import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.jboss.logging.Logger;


public class ConnectionManager {

   static Connection con;
   static String url;
   static Logger log = Logger.getLogger("ConnectionDatabase");
         
   static Connection getConnection(){
 
	   String DATASOURCE_CONTEXT = "java:jboss/datasources/MySQLDS";
	    
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
	      log.info("Cannot get connection: " + ex);
	    }
	    catch(SQLException ex){
	      log.info("Cannot get connection: " + ex);
	    }
	    return result;
	  }
}
