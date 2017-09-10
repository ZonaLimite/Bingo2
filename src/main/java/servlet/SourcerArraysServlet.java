package servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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
			carton = this.consultaObjetoCarton(ordCarton);
		
		}
		if(comando.equals("ArrayCartonBaseDatosPorNRef")){
			carton = cartonUsuarioPorNRef (usuario, perfil, ordCarton,idHttpSession);
		}
		if(comando.equals("DatosCartones")){
			UserBean userbean = gestorSesions.dameUserBeansPorUser(usuario, perfil, idHttpSession);
			String sala = userbean.getSalonInUse();
			float precioCarton = new Float(gestorSesions.getJugadasSalas(sala).getPrecioCarton());
			float saldoUsuario = userbean.getSaldo();
			int cartonesManuales = new Integer(gestorSesions.getJugadasSalas(sala).getnCartonesManuales());
			int cartonesAutomaticos = gestorSesions.dameSetCartonesEnJuego(sala).size();
			int nCartonesEnJuego = gestorSesions.dameSetCartonesEnJuego(sala).size() + cartonesManuales;
			int porCientoCantaor = new Integer(gestorSesions.getJugadasSalas(sala).getPorcientoCantaor());
			int porCientoLinea = new Integer(gestorSesions.getJugadasSalas(sala).getPorcientoLinea());
			int porCientoBingo = new Integer(gestorSesions.getJugadasSalas(sala).getPorcientoBingo());
			//float premioLinea = gestorSesions.getJugadasSalas(sala).
			//Pasamos los datos a array
			// index 0 = precioCarton - float
			// index 1 = saldoUsuario - float
			// index 2 = cartones en Juego - int (Automaticos) + manuales
			// index 3 = cartones automaticos - int
			// index 4 = cartones Manaules - int
			// index 5 = % Cantaor - int
			// index 6 - % Linea - int
			// index 7 - % Bingo - int
			
			Object datosCartones [] = new Object[10];
			datosCartones[0] = precioCarton;
			datosCartones[1] = saldoUsuario;
			datosCartones[2] = nCartonesEnJuego;
			datosCartones[3] = cartonesAutomaticos;
			datosCartones[4] = cartonesManuales;
			datosCartones[5] = porCientoCantaor;
			datosCartones[6] = porCientoLinea;
			datosCartones[7] = porCientoBingo;
			
			//Ahora a enviarlo como JSON
			 String json = new Gson().toJson(datosCartones);
		   
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    try {
				response.getWriter().write(json);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}		
		if(comando.equals("ArrayNumerosCantados")){
			UserBean userbean = gestorSesions.dameUserBeansPorUser(usuario, perfil, idHttpSession);
			String sala = userbean.getSalonInUse();
			List<Integer> numerosCantados = dameNumerosCantados(sala);
			//Pasamos la lista a array
			int cantidad = numerosCantados.size();
			
			int arrayNumerosCantados[] = new int[cantidad] ;
			for(int i=0;i<cantidad;i++){
				arrayNumerosCantados[i]=numerosCantados.get(i);
			}
			//Ahora a enviarlo como JSON
			 String json = new Gson().toJson(arrayNumerosCantados);
		   
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    try {
				response.getWriter().write(json);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
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
private List<Integer>  dameNumerosCantados(String sala){
	List<Integer> cantados  =null;
		cantados = gestorSesions.getJugadasSalas(sala).getNumerosCalled();
	return cantados;
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
