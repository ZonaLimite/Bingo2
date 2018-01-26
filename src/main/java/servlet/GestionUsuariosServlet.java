package servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

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
			gestorSesions.getJugadasSalas(sala).AsignaPreferCarton(usuario, 0);
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
			triggerRefreshDatos(sala);
		
	}
	if(comando.equals("AjustarPreferenciasCarton")){
		int valorPreferenciaCarton = new Integer(req.getParameter("prefCarton"));
		String sala = req.getParameter("sala");
		
		gestorSesions.getJugadasSalas(sala).AsignaPreferCarton(usuario, valorPreferenciaCarton);
		String json = new Gson().toJson("Cartones Asignados :"+valorPreferenciaCarton +" a " +usuario);	
	    res.setContentType("text/html");
	    res.setCharacterEncoding("UTF-8");			
		res.getWriter().write(json);
	}
	if(comando.equals("ComprarCartones")){
		int nCarton= new Integer(req.getParameter("nCarton"));
		String sJugador = req.getParameter("usuario");
		String sala = req.getParameter("sala");
		String resultMensaje="";
		resultMensaje = transaccionCompraCartonOffLine(sJugador,sala, nCarton);
		String json = new Gson().toJson(resultMensaje);	
	    res.setContentType("text/html");
	    res.setCharacterEncoding("UTF-8");			
		res.getWriter().write(json);	        
	}
	if(comando.equals("GetPlayersOffLine")){
		String sala = req.getParameter("sala");
		Vector<String> playersConCartones = gestorSesions.getJugadasSalas(sala).getUsuariosManualesEnJuegoConCartones();
		String usersOffLine[] = new String[playersConCartones.size()];
		for(int i=0;i<playersConCartones.size();i++){
		
			usersOffLine[i]=playersConCartones.elementAt(i);
		
		}
		String json = new Gson().toJson(usersOffLine);	
	    res.setContentType("text/html");
	    res.setCharacterEncoding("UTF-8");			
		res.getWriter().write(json);
		triggerRefreshDatos(sala);
	}	
	
	if(comando.equals("ComprarTodosLosCartones")){
		int nCarton=0;
		String sJugador="";
		String resultMensaje="";
		String sala = req.getParameter("sala");
		
		Vector<String> jugadoresOffLine = gestorSesions.getJugadasSalas(sala).getUsuariosManualesEnJuego();
		
		Iterator<String> itKeys = jugadoresOffLine.iterator();
		while(itKeys.hasNext()){
			sJugador = itKeys.next();
			nCarton = gestorSesions.getJugadasSalas(sala).dimePreferCartonDe(sJugador);
			resultMensaje +=transaccionCompraCartonOffLine(sJugador,sala, nCarton)+ "<br>";
		}
		
	    res.setContentType("text/html");
	    res.setCharacterEncoding("UTF-8");			
		res.getWriter().write(resultMensaje);	        
		
	}
	
	}
	private String transaccionCompraCartonOffLine(String sJugador,String sala, int nCarton){
		UtilDatabase udatabase = new UtilDatabase();
		float saldoSJugador = new Float(udatabase.consultaSQLUnica("Select Saldo From usuarios Where User ='"+sJugador+"'"));
		float precioCarton = new Float(gestorSesions.getJugadasSalas(sala).getPrecioCarton());
		float precioCompra = precioCarton * nCarton;
		String statusJuego = gestorSesions.getJugadasSalas(sala).getIdState();
		String resultMensaje="";
		if(precioCompra>saldoSJugador){
				resultMensaje="No hay suficiente Saldo para hacer la compra de cartones para "+sJugador;	
				return resultMensaje;
		}
		if(!(statusJuego.equals("Finalized"))){
				resultMensaje="Compras cartones No permitidas. Partida aun no finalizada";
				return resultMensaje;
		}
			//Formateo datos y TRansaccion compra carton
			float saldoRestante = saldoSJugador - precioCompra;
	        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
	        //Formateador de datos decimales. Limitado a dos digitos.
	        simbolos.setDecimalSeparator('.');
	        DecimalFormat formateador = new DecimalFormat("#######.##",simbolos);

	        String Consulta = "UPDATE usuarios SET Saldo = "+formateador.format(saldoRestante)+" WHERE User = '"+sJugador+"'";
	        System.out.println("Compra Manual carton :" +Consulta);
	       
	        int result=UtilDatabase.updateQuery(Consulta);
	       
	        if(result>0){
	        	gestorSesions.getJugadasSalas(sala).AsignaNCartonesA(sJugador,gestorSesions.getJugadasSalas(sala).dimeCartonesDe(sJugador)+ nCarton);
	    		resultMensaje="Cartones comprados :"+nCarton+" a " +sJugador;
	    		triggerRefreshDatos(sala);
	        }
		return resultMensaje;
	}
	private void triggerRefreshDatos(String salaInUse){
		PocketBingo pb = gestorSesions.getJugadasSalas(salaInUse);
		String precioCarton,porCientoLinea,porCientoBingo,porCientoCantaor;
		precioCarton=pb.getPrecioCarton();
		//Hay que distinguir entre cartones electronicos y manuales
		//EL cuadro de Dialogo debe considerar las dos facetas
		// Por lo tanto debe haber dos variables, uno para cada tipo de faceta de cartones.
		// Implementado ambos tipos de datos
	
		int nCartones = new Integer(pb.calculaNcartonesManuales()) + this.gestorSesions.dameSetCartonesEnJuego(salaInUse).size();
		porCientoLinea=pb.getPorcientoLinea();
		porCientoBingo=pb.getPorcientoBingo();
		porCientoCantaor=pb.getPorcientoCantaor();
		String construirScript="DATOSCARTONES_"+precioCarton+"_"+nCartones+"_"+porCientoLinea+"_"+porCientoBingo+"_"+porCientoCantaor;
		enviarMensajeAPerfil(construirScript,"supervisor");
		enviarMensajeAPerfil("RefreshDatosCartones","jugador");
	}
	
	private void enviarMensajeAPerfil(String textMessage,String perfil){
		  	try {
				Set<UserBean> myUsersbean = gestorSesions.dameUserBeans(perfil);
				Iterator<UserBean> itBeans= myUsersbean.iterator();
				while (itBeans.hasNext()){
					Session sesionActiva = itBeans.next().getSesionSocket();
					sesionActiva.getBasicRemote().sendText(textMessage);
				}
					//log.info("Enviando desde servidor a navegador:"+textMessage);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
