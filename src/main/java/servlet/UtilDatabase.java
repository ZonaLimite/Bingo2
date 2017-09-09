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
	public synchronized Carton consultaObjetoCarton(String nrefCarton){
		    Carton carton = new Carton();
		    //Connection con = ConnectionManager.getConexionMySQL("localhost", "paco", "ntmanager","wildfly");
		    Connection con = ConnectionManager.getConnection();
		    Statement st=null;
		    ResultSet rs=null;
		    try {
		        int matrizNumeros[][]=null;
		        int idCarton;
		        String nCarton;
		        String nSerie;
		        Blob blob;
		         st = con.createStatement();
		         rs = st.executeQuery("Select idCarton, nCarton, nSerie, arrayCarton from cartonesbingo where idCarton="+nrefCarton);
		         if(rs.next()){
		             idCarton= rs.getInt(1);
		             nCarton = rs.getString(2);
		             nSerie = rs.getString(3);
		               // Se obtiene el campo blob
		             blob = rs.getBlob(4);

		            // Se reconstruye el objeto con un ObjectInputStream
		            
		            try {
		                ObjectInputStream ois = new ObjectInputStream(blob.getBinaryStream());
		                 try {
		                     matrizNumeros = (int[][]) ois.readObject();
		                 } catch (ClassNotFoundException ex) {
		                     Logger.getLogger(gestorComprasCartones.class.getName()).log(Level.SEVERE, null, ex);
		                 }
		                carton.setnRef(idCarton);
		                carton.setnCarton(nCarton);
		                carton.setnSerie(nSerie);
		                carton.setNumeros(matrizNumeros);
		                carton.setBingoCantado(false);
		                carton.setLineaCantado(false);
		               
		            } catch (IOException ex) {
		                Logger.getLogger(gestorComprasCartones.class.getName()).log(Level.SEVERE, null, ex);
		            }
		            
		            //matrizArrayCarton int [][] 
		         }
		    } catch (SQLException ex) {
		        Logger.getLogger(gestorComprasCartones.class.getName()).log(Level.SEVERE, null, ex);
		    }finally{
		        try {
		            if(con!=null)con.close();
		            if(st!=null)st.close();
		        } catch (SQLException ex) {
		            Logger.getLogger(gestorComprasCartones.class.getName()).log(Level.SEVERE, null, ex);
		        }
		       
		    }
		    return carton;//
		}	   
}
