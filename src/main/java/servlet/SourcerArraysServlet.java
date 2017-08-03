package servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet("/SourcerArraysCarton")
public class SourcerArraysServlet extends HttpServlet{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
@Inject
private GestorSessions gestorSesions;
HttpServletResponse response;
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.response=res;
		Carton carton = null;
		HttpSession htps=req.getSession();
		String idHttpSession = htps.getId();
		String usuario = req.getParameter("usuario");
		String perfil = req.getParameter("perfil");
		String ordCarton = req.getParameter("nCarton");
		String comando = req.getParameter("comando");
		if(comando.equals("ArrayCartonUsuario")){
			carton = cartonUsuario (usuario, perfil, ordCarton, idHttpSession);
		}
		if(comando.equals("ArrayCartonBaseDatos")){
			carton = consultaObjetoCarton(ordCarton);
		}
		if(comando.equals("ArrayCartonBaseDatosPorNRef")){
			carton = cartonUsuarioPorNRef (usuario, perfil, ordCarton,idHttpSession);
		}		
		
		int numeros[][]= carton.getNumeros();
		
		//Ahora a enviarlo como JSON
		 String json = new Gson().toJson(numeros);
	   
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    try {
			response.getWriter().write(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}
private Carton cartonUsuario (String usuario, String perfil, String ordCarton, String idHttpSession){
	Carton carton=null;
	int index = new Integer(ordCarton);
	UserBean userbean = gestorSesions.dameUserBeansPorUser(usuario, perfil,idHttpSession);
	Vector<Carton> vCarton = userbean.getvCarton();
	if(vCarton.size()>0){
		carton= vCarton.get(index-1);
	}
	
	return carton;

}
private Carton cartonUsuarioPorNRef (String usuario, String perfil, String nRef, String idHttpSession){
	Carton cartonBuscado=null;
	//
	UserBean userbean = gestorSesions.dameUserBeansPorUser(usuario, perfil,idHttpSession);
	Vector<Carton> vCarton = userbean.getvCarton();//
	for(int c=0;c<vCarton.size();c++){
		Carton cart = vCarton.elementAt(c);//
		String nrefComp= cart.getnRef()+"";
		if(nRef.equals(nrefComp)){
			cartonBuscado=cart;
		}
	}
	return cartonBuscado;

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
