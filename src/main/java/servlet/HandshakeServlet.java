package servlet;

import java.io.IOException;
import java.util.Map;

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
	
	@Inject
	private GestorSessions gestorSesions;
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String sala, usuario, nRef, comando;
		sala= req.getParameter("sala");
		usuario = req.getParameter("sala");
		nRef= req.getParameter("nRef");
		comando  = req.getParameter("comandoHandshake");
		
		if(comando=="_ComprobarCartonLinea"){
			
		}
		//,"_ComprobarCartonBingo" o "_NohayMas
		
		String objetoaConvertir = "sala="+sala+ "\nusuario="+usuario+"\nnref="+nRef+"\ncomando="+comando;
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
