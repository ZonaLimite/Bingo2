package servlet;

import java.sql.*;
import java.util.logging.Logger;

public class UserDAO 	
{
   static Connection currentCon = null;
   static ResultSet rs = null;  
   static Logger log = Logger.getLogger("UserDao");	
	
	
   public static UserBean login(UserBean bean) {
	
      //preparing some objects for connection 
      Statement stmt = null;    
	
      String username = bean.getUsername();    
      String password = bean.getPassword();   
	    
      String searchQuery =
            "select * from wildfly.usuarios where User='"
                     + username
                     + "' AND Password='"
                     + password
                     + "'";
	    
   // "System.out.println" prints in the console; Normally used to trace the process
   System.out.println("Your user name is " + username);          
   System.out.println("Your password is " + password);
   log.info("Query: "+searchQuery);
	    
   try 
   {
      //connect to DB 
      currentCon = ConnectionManager.getConnection();
      stmt=currentCon.createStatement();
     
      rs = stmt.executeQuery(searchQuery);	        
      boolean more = rs.next();
	       
      // if user does not exist set the isValid variable to false
      if (!more) 
      {
         System.out.println("Sorry, you are not a registered user! Please sign up first");
         bean.setValid(false);
      } 
	        
      //if user exists set the isValid variable to true
      else if (more) 
      {
         String firstName = rs.getString("User");
         String eMail = rs.getString("Email");
         String perfil = rs.getString("Perfil");
         float saldo = rs.getFloat("Saldo");
	     	
         System.out.println("Welcome " + firstName + "Email:" + eMail);
         bean.setUserName(firstName);
         bean.setPerfil(perfil);
         bean.setValid(true);
         bean.setSaldo(saldo);
      }
   } 

   catch (Exception ex) 
   {
      System.out.println("Log In failed: An Exception has occurred! " + ex);
   } 
	    
   //some exception handling
   finally 
   {
      if (rs != null)	{
         try {
            rs.close();
         } catch (Exception e) {}
            rs = null;
         }
	
      if (stmt != null) {
         try {
            stmt.close();
         } catch (Exception e) {}
            stmt = null;
         }
	
      if (currentCon != null) {
         try {
            currentCon.close();
         } catch (Exception e) {
         }

         currentCon = null;
      }
   }

return bean;
	
   }	
}
