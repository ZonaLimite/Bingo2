package servlet;

import java.io.IOException;
import java.util.Vector;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import com.google.gson.Gson;

@WebServlet("/GestionUsuarios")
public class GestionUsuariosServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Mailing mail;

	@Inject
	private GestorSessions gestorSesions;
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {	
		Logger log = Logger.getLogger("GestionUsuarios");
		
		HttpSession htps=req.getSession();
		String idHttpSession = htps.getId();
		String usuario = req.getParameter("usuario");
		String password = req.getParameter("password");
		String eMail = req.getParameter("email");
		String comando = req.getParameter("comando");

	if(comando.equals("AltaUsuario")){
	    res.setContentType("application/json");
	    res.setCharacterEncoding("UTF-8");
	    try {
	    	 UtilDatabase ud = new UtilDatabase();
	    	 String query = "INSERT INTO usuarios VALUES (null,'"+usuario+"','"+ password +"','"+ eMail + "', 'jugador', 0)";
	    	 int result = ud.queryAlta(query);
	    	 String sResult="";
	    	 if (result >0){
	    		 String message = "Enhorabuena "+usuario+ " , has sido dado de alta en el Portal Bingo Home. Tus datos de acceso son: \n Usuario = "+ usuario+"\n Password = "+ password;
	    		mail.sendEmail(eMail, "javier.boga.rioja@gmail.com", "Alta en Bingo Home", message);
	    		 sResult = "Usuario dado de alta OK";
	    	 }
	    	 if (result==0)sResult = "Ha habido error en alta Usuario";
	    	 if(result==-1)sResult = "Usuario Ya existe, elija otro";
			 String json = new Gson().toJson(sResult);	    	
			 res.getWriter().write(json);
			 /*
			 ejecutamos una consulta de insercion, comprobando si ya existe el usuario o el email ya esta en uso
			 	Si no existen,
			 				(De momento no,solo dar de alta e informar)-
			 				 mandamos un email de confirmacion con un enlace que complete el proceso de alta.
			 				(Aqui podemos mandar un codigo de confirmacion que sera escrito por el usuario y comparado)
			    Si ya existen
			    	Respondemos que alta incorrecta o existente.*/
			 
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	if(comando.equals("RegistraJugador")){
		    
			String sala = req.getParameter("sala");
			gestorSesions.getJugadasSalas(sala).añadirUsuariosManualesEnJuego(usuario);
			String HTML = HTMLTablaJugadores(sala) ;
			String json = new Gson().toJson(HTML);	
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");			
			res.getWriter().write(json);
	}
	if(comando.equals("RemoveJugador")){
			String sala = req.getParameter("sala");
			gestorSesions.getJugadasSalas(sala).removerUsuariosManualesEnJuego(usuario);
			String HTML = HTMLTablaJugadores(sala) ;
			String json = new Gson().toJson(HTML);	
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");			
			res.getWriter().write(json);
		
	}
	}
	
	private String HTMLTablaJugadores(String sala){
		   Vector<String> vJugadores = gestorSesions.getJugadasSalas(sala).getUsuariosManualesEnJuego();
		   UtilDatabase udatabase= new UtilDatabase();
		   String HTML ="";

		      HTML+=("  <tr bgcolor=\"#003399\">\r\n");
		      HTML+=("    <td  width=\"50%\" height=\"72\" class=\"Cabecera\" ><p>Usuarios Registrados</p>\r\n");
		      HTML+=("      <form name=\"form1\" method=\"post\" action=\"\">\r\n");
		      HTML+=("        <select class=\"Caja\" name=\"registeredPlayers\" id=\"registeredPlayers\">\r\n");
		      HTML+=("          ");
			  		Vector vectorResultsSQL1 = udatabase.dameVectorResultsSQL("Select User From usuarios Order by idUsuarios", 1);

		        	for(int i=0; i < vectorResultsSQL1.size();i++){
						String array[] = (String[])vectorResultsSQL1.elementAt(i);
		        		String user = array[0];	
		        		
		      HTML+=("\r\n");
		      HTML+=("        \t\t<option value=\"");
		      HTML+=(user );
		      HTML+=('"');
		      HTML+=('>');
		      HTML+=(user );
		      HTML+=("</option>\r\n");
		      HTML+=("      \t  ");
		} 
		      HTML+=("\r\n");
		      HTML+=("      </select>\r\n");
		      HTML+=("      </form>\r\n");
		      HTML+=(" \r\n");
		      HTML+=("    <td width=\"25%\">\r\n");
		      HTML+=("      <p align=\"center\"><input type=\"button\" width=\"50px\" name=\"botonJugar\" id=\"botonJugar\" value=\"Añadir Jugador\" onclick=\"registraJugador()\">\r\n");
		      HTML+=("      </p>\r\n");
		      HTML+=("   \r\n");
		      HTML+=("    </td>\r\n");
		      HTML+=("\t<td >\r\n");
		      HTML+=("\t  <label class=\"soloCaja\" >SALA :</label >\r\n");
		      HTML+=("\t\r\n");
		      HTML+=("    <input  class=\"Caja\" type=\"text\" Id=\"sala\" value=\"sala1\"/p></td>    \r\n");
		      HTML+=("    </tr>\r\n");
		      HTML+=("  <tr>\r\n");
		      HTML+=("    <td class=\"Cabecera\">Usuarios en Juego</td>\r\n");
		      HTML+=("    <td  class=\"Cabecera\">Saldo</td>\r\n");
		      HTML+=("    <td  class=\"Cabecera\">Opciones</td>\r\n");
		      HTML+=("  </tr>\r\n");
		      HTML+=("\r\n");
		   for(int i = 0 ;i<vJugadores.size();i++){
		    	  String sJugador = (String)vJugadores.get(i);
		    	  String saldoSJugador = udatabase.consultaSQLUnica("Select Saldo From usuarios Where User ='"+sJugador+"'");
		    	  
		    	  HTML+=("<tr class=\"fondosLineas\" >\r\n");
		    	  HTML+=("    <td class=\"otro\"><label class=\"AIzquierdas\">"+sJugador+"</label></td>\r\n");
		    	  HTML+=("    <td class=\"otro\"><label class=\"AIzquierdas\">"+saldoSJugador+" €</label></td>\r\n");
		    	  HTML+=("    <td class=\"otro\"><label class=\"DatosCentrados\"><input type=\"button\" width=\"50px\" name=\"Quitar\" id=\"Quitar\" value=\"Quitar\" onclick=\"removeJugador('"+ sJugador+"')\" ></label></td>\r\n");
		    	  HTML+=("         \r\n");
		    	  HTML+=("</tr>\r\n");
		    	  
		   }
		   return HTML;
	}

}
