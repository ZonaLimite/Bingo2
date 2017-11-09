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

public class Carton {
	
	private int nRef;
	private String nCarton;
	private String nSerie;
	private String PrecioCarton;
	//3 lineas de 5 numeros cada una y 9 Columnas nunca 3 a la vez
	//6 cartones cada serie.
	//Fuente Letra Arial condensada tamaño 28
	
	private int[][] numeros = new int[3][9] ;
	private String numeroPantalla;
	private boolean lineaCantado=false;//
	private boolean bingoCantado=false;
	private int nOrden=0;
	
	public int getnOrden() {
		return nOrden;
	}
	public void setnOrden(int nOrden) {
		this.nOrden = nOrden;
	}
	public int getnRef() {
        return nRef;
    }
    public void setnRef(int nRef) {
        this.nRef = nRef;
    }
	public String getnCarton() {
		return nCarton;
	}
	public void setnCarton(String nCarton) {
		this.nCarton = nCarton;
	}
	public String getnSerie() {
		return nSerie;
	}
	public void setnSerie(String nSerie) {
		this.nSerie = nSerie;
	}
	public String getPrecioCarton() {
		return PrecioCarton;
	}
	public void setPrecioCarton(String precioCarton) {
		PrecioCarton = precioCarton;
	}
	public String getNumeroPantalla() {
		return numeroPantalla;
	}
	public void setNumeroPantalla(String numeroPantalla) {
		this.numeroPantalla = numeroPantalla;
	}
	public boolean isLineaCantado() {
		return lineaCantado;
	}
	public void setLineaCantado(boolean lineaCantado) {
		this.lineaCantado = lineaCantado;
	}
	public boolean isBingoCantado() {
		return bingoCantado;
	}
	public void setBingoCantado(boolean bingoCantado) {
		this.bingoCantado = bingoCantado;
	}
	public int[][] getNumeros() {
		return numeros;
	}
	public void setNumeros(int[][] numeros) {
		this.numeros = numeros;
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
	    return carton;
	}
}
