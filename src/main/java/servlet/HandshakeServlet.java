package servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/Handshake")
public class HandshakeServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger("Handshake");
	
	@Inject
	private GestorSessions gestorSesions;
	private ComprobadorPremios cp;
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String objetoaConvertir="";
		String sala, usuario, nRef, comando;
		sala= req.getParameter("sala");
		usuario = req.getParameter("usuario");
		String numero= req.getParameter("nRef");
		int iNumero = new Integer(numero);
		nRef=""+iNumero;
		comando  = req.getParameter("comandoHandshake");
		cp = new ComprobadorPremios(gestorSesions);
		if(comando.equals("_ComprobarCartonLinea")){
			Set<UserBean> setUserBean = gestorSesions.dameUserBeans("supervisor", sala);
			Iterator<UserBean> itUserBean = setUserBean.iterator();
			UserBean user = (UserBean)itUserBean.next();
			UserBean myUSER = new UserBean();
			myUSER.setSalonInUse(user.getSalonInUse()) ;
			myUSER.setSesionSocket(user.getSesionSocket());
			myUSER.setUserName("Carton_"+nRef);

			if(cp.comprobarLineaDeCarton(sala, nRef, myUSER)){
				objetoaConvertir = "! Carton "+ nRef+ " premiado para Linea ¡\n ¿Hay algun carton mas a comprobar?";

			}else{
				objetoaConvertir = "Se Siente ..., Carton no premiado o ya esta registrado el premio\n ¿Hay algun carton mas a comprobar?";
			}
		}
		if(comando.equals("_ComprobarCartonBingo")){
			Set<UserBean> setUserBean = gestorSesions.dameUserBeans("supervisor", sala);
			Iterator<UserBean> itUserBean = setUserBean.iterator();
			UserBean user = (UserBean)itUserBean.next();
			UserBean myUSER = new UserBean();
			myUSER.setSalonInUse(user.getSalonInUse()) ;
			myUSER.setSesionSocket(user.getSesionSocket());
			myUSER.setUserName("Carton_"+nRef);

			if(cp.comprobarBingoDeCarton(sala, nRef, myUSER)){
				objetoaConvertir = "! Carton "+ nRef+ " premiado para Bingo ¡\n ¿Hay algun carton mas a comprobar?";

			}else{
				objetoaConvertir = "Se Siente ..., Carton no premiado o ya esta registrado este premio\n ¿Hay algun carton mas a comprobar?";
			}
		}		
		
		//,"_ComprobarCartonBingo" o "_NohayMas
		
		//objetoaConvertir = "sala="+sala+ "\nusuario="+usuario+"\nnref="+nRef+"\ncomando="+comando;
		//pendiente comprobar carton con parametro nREF recibido
		//si correcto Crear un nuevo Objeto PeticionPremio y add a private Map<PeticionPremio,Carton>  pilaAnunciaPremios;
		// con el carton correspondiente
		
		//Ahora enviamos una respuesta al Dialogo Handshake,si era correcto o no
		//y le preguntamos si hay mas.
		
		//Si la respuesta es NoHayMas cambiamos el estado a "PremiosRecopilados"
		
		//Ahora a enviarlo como JSON
		 String json = new Gson().toJson(objetoaConvertir);
	   
	    res.setContentType("application/json");
	    res.setCharacterEncoding("UTF-8");
	    try {
			res.getWriter().write(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //gestorSesions.getJugadasSalas(sala).setIdState("PremiosRecopilados");
	    //gestorSesions.getHiloSala(sala).interrupt();
	}
}
