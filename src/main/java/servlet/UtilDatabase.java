package servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtilDatabase {
	   static Connection currentCon = null;
	   static ResultSet rs = null;  
		
		
		
	   public static int updateQuery(String query) {
		   //public static int updateQuery(String query) {
		
	      //preparing some objects for connection 
	      Statement stmt = null;
	      int nRowsUpdated=0;
		    
	   try 
	   {
	      //connect to DB 
	      currentCon = ConnectionManager.getConnection();
	      stmt=currentCon.createStatement();
	      nRowsUpdated = stmt.executeUpdate(query);    

	   }catch (Exception ex) 
	   		{
		   			System.out.println("consulta de actualizacion fallada:" + ex);
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

	return nRowsUpdated;
		
	   }

}
