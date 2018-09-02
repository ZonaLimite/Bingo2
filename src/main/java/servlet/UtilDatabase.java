package servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtilDatabase {
	   static Connection currentCon = null;
	   static ResultSet rs = null;  
		
		
	   //Ejecucion de una consulta de actualizacion, devuelve nº filas afectadas
	   public static int updateQuery(String query) {
		
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
public synchronized String consultaSQLUnica(String querySQL){
	
    //preparing some objects for connection 
    Statement stmt = null;
    ResultSet rs = null;
    String valor="";
  try {
    //connect to DB 
    currentCon = ConnectionManager.getConnection();
    stmt=currentCon.createStatement();
    if(stmt.execute(querySQL)){
    	rs=stmt.getResultSet();
    	if(rs.next())valor = rs.getString(1);
    }; 

 }catch (Exception ex) 
 		{
	   			System.out.println("consulta SQlUnica fallada:"+querySQL +" "+ ex);
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

return valor;	
}


public Vector<String[]> dameVectorResultsSQL(String querySQL,int nCampos ){
	Vector<String[]> vectorResult = new Vector<String[]>();
    //preparing some objects for connection 
    Statement stmt = null;
    ResultSet rs = null;

  try {
    //connect to DB 
    currentCon = ConnectionManager.getConnection();
    stmt=currentCon.createStatement();
    if(stmt.execute(querySQL)){
    	rs=stmt.getResultSet();
			int contOrden=0;
    		while(rs.next()){
    			String campos[] = new String[nCampos];
    			for(int i = 0; i <nCampos; i++){
    				campos[i]=rs.getString(i+1);
    				
    			}
    			vectorResult.add(contOrden,campos);
    			contOrden++;
    		}
    }; 

 }catch (Exception ex) 
 		{
	   			System.out.println("consulta SQlUnica fallada:"+querySQL +" "+ ex);
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
  
return vectorResult;	
}

//Ejecucion de una consulta de Inserccion, devuelve un int con resultado operacion
public int queryAlta(String query) {
		   ResultSet rs=null;
		   Statement stmt=null;
		   int nFilas=0;
		    
	   try 
	   {
	      //connect to DB 
	      currentCon = ConnectionManager.getConnection();
	      stmt=currentCon.createStatement();
	      //"INSERT INTO usuarios VALUES ('"+usuario+"','"+ password +"','"+ email+"', 'jugador', 0)"
	      nFilas =stmt.executeUpdate(query);

	   }catch (Exception ex) 
	   		{
		   			System.out.println("consulta de actualizacion fallada:" + ex);
		   			if(ex.toString().contains("Duplicate entry"))nFilas = -1;
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
	   return nFilas;

		
	}
public synchronized int updateQueryCompraBonus(String consultaUsuario,String consultaCaja){

		  //Consulta doble de actualizacion con transaccion
	      //preparing some objects for connection 
	      Statement stmt = null;
	      int nRowsUpdated=0;
		    
	   try 
	   {
	      //connect to DB 
	      currentCon = ConnectionManager.getConnection();
	      currentCon.setAutoCommit(false);
	      stmt=currentCon.createStatement();
	      nRowsUpdated = stmt.executeUpdate(consultaUsuario);  
	      
	      nRowsUpdated += stmt.executeUpdate(consultaCaja);  
	      currentCon.commit();

	   }catch (Exception ex) 
	   		{
		   			try {
						currentCon.rollback();
					} catch (SQLException e) {
						System.out.println("Rollback fallada:" + e);
					}
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
