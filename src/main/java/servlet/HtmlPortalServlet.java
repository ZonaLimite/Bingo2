package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet("/HtmlPortal")
public class HtmlPortalServlet extends HttpServlet{
    @Inject
    private GestorSessions gestorSesions;
    
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter pw = null;
		String comando = req.getParameter("comando");
		
		
		if(comando.equals("MostrarLogin")){
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
			res.sendRedirect("Login.html");
		}
		if(comando.equals("MostrarUsuarios")){
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
			res.sendRedirect("GamerStatus.jsp");
		}		
		if(comando.equals("MostrarNuevoUsuario")){
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
			res.sendRedirect("AltaUsuario.html");
		}
		if(comando.equals("Solicitud Bonus")){
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
		    String usuario = req.getParameter("usuario");
			res.sendRedirect("CompraBonus.jsp?usuario='"+usuario+"'");
		}
		if(comando.equals("Solicitud Liquidacion")){
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
		    String usuario = req.getParameter("usuario");
			res.sendRedirect("SolicitudLiquidacionBonus.jsp?usuario='"+usuario+"'");
		}
		if(comando.equals("VolcadoPeticionesBonus")){
			//res.sendRedirect("PlantillaVolcadoBonus.jsp");}
			String sala = req.getParameter("sala");
		    try {
				pw= res.getWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    writeHTMLVolcadoPeticionesBonus(pw,sala);
		}
		if(comando.equals("VolcadoPeticionesLiquidacionBonus")){
			//res.sendRedirect("PlantillaVolcadoBonus.jsp");}
			String sala = req.getParameter("sala");
		    try {
				pw= res.getWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    writeHTMLVolcadoPeticionesLiquidacionBonus(pw,sala);
		}		
		if(comando.equals("ConfigurarJugadores")){
			
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
		    //res.sendRedirect("ConfiguracionUsuariosPlaying.jsp");
			String sala = req.getParameter("sala");
		    try {
				pw= res.getWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    writeHTMLConfiguradorJugadores(pw,sala);	    
		}
		if(comando.equals("ConfigurarCartones")){
			
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
		    //res.sendRedirect("ConfiguracionCartonesPlaying.jsp");
		  
			String sala = req.getParameter("sala");
		    try {
				pw= res.getWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    writeHTMLConfiguradorCartones(pw,sala);	 
		}
		if(comando.equals("MostrarGamerStatus")){
			
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
		    //res.sendRedirect("ConfiguracionCartonesPlaying.jsp");2
		  
			String sala = req.getParameter("sala");
		    try {
				pw= res.getWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    writeHTMLGamerStatus(pw, sala );
		}
		if(comando.equals("MostrarGamerStatusWithPlayers")){
			
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
		    //res.sendRedirect("ConfiguracionCartonesPlaying.jsp");2
		  
			String sala = req.getParameter("sala");
		    try {
				pw= res.getWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    writeHTMLGamerStatusWithPlayers(pw, sala );
		}		
		
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String comando = req.getParameter("comando");
		if(comando.equals("CerrarSesion")){
			req.getSession().invalidate();
			//req.getSession().removeAttribute("jugador");
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
			res.sendRedirect("Portal.jsp");
		}					
	}
	private void writeHTMLConfiguradorJugadores(PrintWriter out, String sala){
		UtilDatabase udatabase = new UtilDatabase();
		Vector<String[]> vectorResultsSQL1 = null;
		PocketBingo pb = gestorSesions.getJugadasSalas(sala);
		Vector<String> vJugadores = pb.getUsuariosManualesEnJuego();
		
        //arrayCampos[1] = 1.User
	  vectorResultsSQL1 = udatabase.dameVectorResultsSQL("Select User From usuarios Order by idUsuarios", 1);


      out.write("");
      out.write("<!doctype html>");
      out.write("<html>");
      out.write("<head>");
      out.write("<meta charset=\"utf-8\">");
      out.write("<title>Documento sin título</title>");
      out.write("<link href=\"css/estilosDialogos.css\" rel=\"stylesheet\" type=\"text/css\">");
      out.write("<script src=\"javascript/utilDialogos.js\"></script>");
      out.write("</head>");
      out.write("");
      out.write("<body>");
      out.write("");
      
      out.write("<table id=\"resultLines\" width=\"97%\" border=\"1\" align=\"center\" class=\"Tabla\">");
      out.write("  <tr bgcolor=\"#003399\">");
      out.write("    <td  width=\"50%\" height=\"72\" class=\"Cabecera\" ><p>Usuarios Registrados</p>");
      out.write("      <form name=\"form1\" method=\"post\" action=\"\">");
      out.write("        <select class=\"Caja\" name=\"registeredPlayers\" id=\"registeredPlayers\">");
      out.write("          ");
      
        	for(int i=0; i < vectorResultsSQL1.size();i++){
				String array[] = (String[])vectorResultsSQL1.elementAt(i);
        		String user = array[0];	
        		
      out.write("");
      out.write("        <option value=\"");
      out.print(user );
      out.write('"');
      out.write('>');
      out.print(user );
      out.write("</option>");
      out.write("        ");
} 
      out.write("");
      out.write("      </select>");
      out.write("      </form>");
      out.write(" ");
      out.write("    <td width=\"25%\">");
      out.write("      <p align=\"center\"><input type=\"button\" width=\"50px\" name=\"botonJugar\" id=\"botonJugar\" value=\"Añadir Jugador\" onclick=\"registraJugador()\">");
      out.write("      </p>");
      out.write("   ");
      out.write("    </td>");
      out.write("<td >");
      out.write("  <label class=\"soloCaja\" >SALA :</label >");
      out.write("");
      out.write("    <input  class=\"Caja\" type=\"text\" Id=\"sala\" value=\"sala1\"/p></td>    ");
      out.write("    </tr>");
      out.write("  <tr>");
      out.write("    <td class=\"Cabecera\">Usuarios en Juego</td>");
      out.write("    <td  class=\"Cabecera\">Saldo</td>");
      out.write("    <td  class=\"Cabecera\">Opciones</td>");
      out.write("  </tr>");
      out.write("");
      //vJugadores.add("guest");
  
      for(int i = 0 ;i<vJugadores.size();i++){
    	  String sJugador = (String)vJugadores.get(i);
    	  String saldoSJugador = udatabase.consultaSQLUnica("Select Saldo From usuarios Where User ='"+sJugador+"'");
    	  
    	  out.write("<tr class=\"fondosLineas\" >");
    	  out.write("    <td class=\"otro\"><label class=\"AIzquierdas\">"+sJugador+"</label></td>");
    	  out.write("    <td class=\"otro\"><label class=\"AIzquierdas\">"+saldoSJugador+" €</label></td>");
    	  out.write("    <td class=\"otro\"><label class=\"DatosCentrados\"><input type=\"button\" width=\"50px\" name=\"Quitar\" id=\"Quitar\" value=\"Quitar\" onclick=\"removeJugador('"+ sJugador+"')\" ></td>");
    	  out.write("         ");
    	  out.write("</tr></article>");
    	  
      }

      out.write("</table>");
      out.write("</body>");
      out.write("</html>");		
	}
	private void writeHTMLConfiguradorCartones(PrintWriter out, String sala){
		 Vector<String> jEnJuego =null;
		 UtilDatabase udatabase = new UtilDatabase();	      out.write("<!doctype html>");
	    
	      out.write("<!doctype html>");
	      out.write("<html>");
	      out.write("<head>");
	      out.write("<meta charset=\"utf-8\">");
	      out.write("<title>Documento sin título</title>");
	      out.write("<link href=\"css/estilosDialogos.css\" rel=\"stylesheet\" type=\"text/css\">");
	      out.write("<script src=\"javascript/utilDialogos.js\"></script>");
	      out.write("</head>");
	    
	      out.write("<body>");
	      out.write("<table id=\"resultLines\" width=\"97%\" border=\"1\" align=\"center\" class=\"Tabla\">");
	      out.write("  <tr bgcolor=\"#003399\" id=\"fila\">");
	      out.write("    <td  width=\"43%\" height=\"72\" class=\"Cabecera\" ><p>Asignacion Cartones</p>");
	      out.write("      <form name=\"form1\" method=\"post\" action=\"\">");
	      out.write("        <select  onChange=\"valorCombo(this.value)\" value=\"\" class=\"Caja\" name=\"OnLinePlayers\" id=\"OnLinePlayers\">");
	      out.write("          ");
		  jEnJuego = gestorSesions.getJugadasSalas(sala).getUsuariosManualesEnJuego();
		      		
		        for(int i=0; i < jEnJuego.size();i++){
		        String user= jEnJuego.elementAt(i);
		        		
		      out.write("");
		      out.write("        <option value=\"");
		      out.print(user );
		      out.write('"');
		      out.write('>');
		      out.print(user );
		      out.write("</option>");

		} 
	      out.write("");
	      out.write("      </select>");
	      out.write("      </form>");
	      out.write(" ");
	      out.write("    <td colspan=\"2\"   width=\"42%\"><table width=\"100%\" border=\"1\">");
	      out.write("  <tr>");
	      out.write("    <td><input onClick=\"valorCarton('1')\" type=\"button\" class=\"cantidadCartones\" value=\"1\"></td>");
	      out.write("    <td><input onClick=\"valorCarton('2')\" type=\"button\" class=\"cantidadCartones\" value=\"2\"></td>");
	      out.write("    <td><input onClick=\"valorCarton('3')\" type=\"button\" class=\"cantidadCartones\" value=\"3\"></td>");
	      out.write("  </tr>");
	      out.write("  <tr>");
	      out.write("    <td><input onClick=\"valorCarton('4')\" type=\"button\" class=\"cantidadCartones\" value=\"4\" ></td>");
	      out.write("    <td><input onClick=\"valorCarton('5')\" type=\"button\" class=\"cantidadCartones\" value=\"5\"></td>");
	      out.write("    <td><input onClick=\"valorCarton('6')\" id=\"valorCarton6\" type=\"button\" class=\"cantidadCartones\" value=\"6\"></td>");
	      out.write("");
	      out.write("  </tr>");
	      out.write("</table>");
	      out.write("</td>");
	      out.write("    ");
	      out.write("<td  width=\"15%\">");
	      out.write("  <label class=\"soloCaja\" >Precio Carton<br>");
	      out.write("      </label ><input  class=\"Caja\" type=\"text\" Id=\"precioCarton\" value=\""+gestorSesions.getJugadasSalas(sala).getPrecioCarton()+" €\"/p>");
	      out.write("  <br>");
	      out.write("  <input type=\"button\" onclick=\"comprarTodosLosCartones()\" class=\"soloRojo\" name=\"Quitar2\" id=\"Quitar2\" value=\"Comprar Todos\">");
	      out.write(" </td>    ");
	      out.write("    </tr>");
	      out.write("  <tr>");
	      out.write("    <td class=\"Cabecera\">Usuarios</td>");
	      out.write("    <td  class=\"Cabecera\">Saldo</td>");
	      out.write("    <td  class=\"Cabecera\">Cartones</td>");
	      out.write("    <td  class=\"Cabecera\">Comprar</td>");
	      out.write("  </tr>");
	     // out.write("<----------------------------------------------------------------------->");
	      	jEnJuego = gestorSesions.getJugadasSalas(sala).getUsuariosManualesEnJuego();
      		
	        for(int i=0; i < jEnJuego.size();i++){
	        	String usuarioEnJuego = jEnJuego.elementAt(i);
	        
	      out.write("<tr onclick=\"valorCombo('"+usuarioEnJuego +"')\" class=\"fondosLineas\" id=\""+usuarioEnJuego +"\">");
	      out.write("    <td class=\"otro\"><label class=\"AIzquierdas\">"+usuarioEnJuego+"</label></td>");
	      out.write("    <td class=\"otro\"><label class=\"AIzquierdas\">");
	      out.write(udatabase.consultaSQLUnica("Select Saldo From usuarios Where User ='"+usuarioEnJuego+"'")+" </label></td>");
	      out.write("    <td class=\"otroCentro\">"+gestorSesions.getJugadasSalas(sala).dimeCartonesDe(usuarioEnJuego)+"<span class=\"AIzquierdas\">");
	      out.write("      ");
	      out.write("    </span></td>");
	      out.write("    <td class=\"otroCentro\">");
	      //gestorSesions.getJugadasSalas(sala).AsignaPreferCarton(usuarioEnJuego, 1);
	      out.write("      <input type=\"button\"  class=\"soloRojo\" onclick=\"comprarCartones('"+usuarioEnJuego+"','"+gestorSesions.getJugadasSalas(sala).dimePreferCartonDe(usuarioEnJuego)+"')\"  value=\"Comprar "+gestorSesions.getJugadasSalas(sala).dimePreferCartonDe(usuarioEnJuego)+"\">");
	      out.write("   </td>");
	      out.write("</tr>");
	      }
	      //out.write("<----------------------------------------------------------------------->");
	      out.write(" ");
	      out.write("</table>");
	      out.write("");
	      out.write("</body>");
	      out.write("</html>");
	}
	private void writeHTMLGamerStatus(PrintWriter out, String sala){
		  Vector<String> jEnJuego =null;	
		  UtilDatabase udatabase = new UtilDatabase();
			//arrayCampos[1] =1.User
			String vCaja = udatabase.consultaSQLUnica("Select SaldoCaja From caja");

	      out.write("\r\n");
	      out.write("<!doctype html>\r\n");
	      out.write("<html>\r\n");
	      out.write("<head>\r\n");
	      out.write("<meta charset=\"utf-8\">\r\n");
	      out.write("<title>Documento sin título</title>\r\n");
	      out.write("<link href=\"css/estilosDialogos.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
	      out.write("<script src=\"javascript/utilDialogos.js\"></script>\r\n");
	      out.write("</head>\r\n");
	      out.write("\r\n");
	      out.write("<body>\r\n");
	     
	      out.write("<table id=\"resultLines\" width=\"97%\" border=\"1\" align=\"center\" class=\"Tabla\">\r\n");
	      out.write("  <tr bgcolor=\"#003399\" id=\"fila\">\r\n");
	      out.write("    <td  width=\"50%\" height=\"72\"  ><span class=\"soloCaja\">CAJA BINGO</span>\r\n");
	      out.write("      <form name=\"form1\" method=\"post\" action=\"\">\r\n");
	      out.write("        <span class=\"soloCaja\">\r\n");
	      out.write("        <input  class=\"Caja\" type=\"text\" Id=\"precioCarton2\" value=\""+vCaja+" €\"/p>\r\n");
	      out.write("        </span>\r\n");
	      out.write("      </form>\r\n");
	      out.write(" \r\n");
	      out.write("    <td colspan=\"1\">\r\n");
	      out.write("      <label class=\"soloCaja\" >CARTON<br>\r\n");
	      out.write("      </label ><input  class=\"Caja\" type=\"text\" Id=\"precioCarton\" value=\""+gestorSesions.getJugadasSalas(sala).getPrecioCarton() +"€\"/p>\r\n");
	      out.write("    <br></td>\r\n");
	      out.write("   <td colspan=\"2\">\r\n");
	      out.write("      <label class=\"soloCaja\" >TOT CARTONES<br>\r\n");
			int cartonesManuales = new Integer(gestorSesions.getJugadasSalas(sala).calculaNcartonesManuales());
			int cartonesAutomaticos = gestorSesions.dameSetCartonesEnJuego(sala).size();
			int nCartonesEnJuego = cartonesAutomaticos + cartonesManuales;
	      out.write("      </label ><input  class=\"Caja\" type=\"text\" Id=\"totalCartones\" value=\""+nCartonesEnJuego+"\"/p>\r\n");
	      out.write("    <br></td> \r\n");
	      out.write("\t</tr>\r\n");
	      out.write("  <tr>\r\n");
	      out.write("    <td class=\"Cabecera\">USUARIOS</td>\r\n");
	      out.write("    <td width=\"30%\"  class=\"Cabecera\">SALDO</td>\r\n");
	      out.write("    <td  class=\"Cabecera\">CARDs</td>\r\n");
	      out.write("    <td width=\"30%\"  class=\"Cabecera\">PERFIL</td>\r\n");
	      out.write("  </tr>\r\n");
	      out.write("\r\n");
	      
	      //UsuariosManuales
	      jEnJuego = gestorSesions.getJugadasSalas(sala).getUsuariosManualesEnJuego();
	      for(int i=0; i < jEnJuego.size();i++){
	    	  String user= jEnJuego.elementAt(i);
	    	  out.write("<tr class=\"fondosLineas\" id=\"fila\">\r\n");
	    	  out.write("    <td class=\"otro\"><label class=\"AIzquierdas\">"+user+"</label></td>\r\n");
	    	  out.write("    <td class=\"otro\"><label class=\"AIzquierdas\">\r\n");
	    	  
	    	  out.write(udatabase.consultaSQLUnica("Select Saldo From usuarios Where User ='"+user+"'")+" </label></td>");
	    	  out.write("    <td class=\"otroCentro\">"+gestorSesions.getJugadasSalas(sala).dimeCartonesDe(user)+"<span class=\"AIzquierdas\">\r\n");
	    	  out.write("      \r\n");
	    	  out.write("    </span></td>\r\n");
	    	  out.write("    <td class=\"otroCentro\">"+"MANUAL"+"</td>\r\n");
	    	  out.write("</tr>\r\n");
	      }
	      //Usuarios electronicos
	      Set<UserBean> jElectronicos = gestorSesions.dameUserBeansEnPortal("jugador");
	      Iterator<UserBean> itjElectronicos = jElectronicos.iterator();
	     while(itjElectronicos.hasNext()){
	    	  UserBean userbean = itjElectronicos.next();
	    	  if(userbean.getSalonInUse().equals(sala)){
	    		  String user= userbean.getUsername();
	    		  out.write("<tr class=\"fondosLineas\" id=\"fila\">\r\n");
	    		  out.write("    <td class=\"otro\"><label class=\"AIzquierdas\">"+user+"</label></td>\r\n");
	    		  out.write("    <td class=\"otro\"><label class=\"AIzquierdas\">\r\n");
	    		  
	    		  out.write(udatabase.consultaSQLUnica("Select Saldo From usuarios Where User ='"+user+"'")+" </label></td>");
	    		  out.write("    <td class=\"otroCentro\">"+userbean.getvCarton().size()+"<span class=\"AIzquierdas\">\r\n");
	    		  out.write("      \r\n");
	    		  out.write("    </span></td>\r\n");
	    		  out.write("    <td class=\"otroCentro\">"+"DIGITAL"+"</td>\r\n");
	    		  out.write("</tr>\r\n");
	    	  }
	     }
	      out.write("\r\n");
	      out.write(" \r\n");
	      out.write("</table>\r\n");
	      
	      out.write("\r\n");
	      out.write("</body>\r\n");
	      out.write("</html>\r\n");		
	}
	private void writeHTMLGamerStatusWithPlayers(PrintWriter out, String sala){
		  Vector<String> jEnJuego =null;	
		  UtilDatabase udatabase = new UtilDatabase();
			//arrayCampos[1] =1.User
			String vCaja = udatabase.consultaSQLUnica("Select SaldoCaja From caja");

	      out.write("\r\n");
	      out.write("<!doctype html>\r\n");
	      out.write("<html>\r\n");
	      out.write("<head>\r\n");
	      out.write("<meta charset=\"utf-8\">\r\n");
	      out.write("<title>Documento sin título</title>\r\n");
	      out.write("<link href=\"css/estilosDialogos.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
	      out.write("<script src=\"javascript/utilDialogos.js\"></script>\r\n");
	      out.write("</head>\r\n");
	      out.write("\r\n");
	      out.write("<body>\r\n");
	      
	      out.write("<div class=\"content2\">\r\n");
	      out.write("<table id=\"resultLines\" width=\"97%\" border=\"1\" align=\"center\" class=\"Tabla\">\r\n");
	      out.write("  <tr bgcolor=\"#003399\" id=\"fila\">\r\n");
	      out.write("    <td  width=\"50%\" height=\"72\"  ><span class=\"soloCaja\">CAJA</span>\r\n");
	      out.write("      <form name=\"form1\" method=\"post\" action=\"\">\r\n");
	      out.write("        <span class=\"soloCaja\">\r\n");
	      out.write("        <input  class=\"Caja\" type=\"text\" Id=\"precioCarton2\" value=\""+vCaja+" €\"/p>\r\n");
	      out.write("        </span>\r\n");
	      out.write("      </form>\r\n");
	      out.write(" \r\n");
	      out.write("    <td colspan=\"1\">\r\n");
	      out.write("      <label class=\"soloCaja\" >CARTON<br>\r\n");
	      out.write("      </label ><input  class=\"Caja\" type=\"text\" Id=\"precioCarton\" value=\""+gestorSesions.getJugadasSalas(sala).getPrecioCarton() +"€\"/p>\r\n");
	      out.write("    <br></td>\r\n");
	      out.write("   <td colspan=\"2\">\r\n");
	      out.write("      <label class=\"soloCaja\" >JUGADORES<br>\r\n");
	      
	      out.write("      </label ><input  class=\"Caja\" type=\"text\" Id=\"totalJugadores\" value=\""+(gestorSesions.getJugadasSalas(sala).getUsuariosManualesEnJuegoConCartones().size()+gestorSesions.dameNumeroJugadoresConCartones(sala))+"\"/p>\r\n");
	      out.write("    <br></td> \r\n");
	      out.write("\t</tr>\r\n");
	      out.write("  <tr>\r\n");
	      out.write("    <td class=\"Cabecera\">USUARIOS</td>\r\n");
	      out.write("    <td width=\"30%\"  class=\"Cabecera\">SALDO</td>\r\n");
	      out.write("    <td  class=\"Cabecera\">CARDs</td>\r\n");
	      out.write("    <td width=\"30%\"  class=\"Cabecera\">PERFIL</td>\r\n");
	      out.write("  </tr>\r\n");
	      out.write("\r\n");
	      out.write("</table>\r\n");
	      out.write("</div>\r\n");
	      out.write("\r\n");
	      
	      out.write("<div class=\"flotador\">\r\n");
	      out.write("<div class=\"content2\">\r\n");
	      out.write("<table width=\"97%\" id=\"resultLines2\" border=\"1\" align=\"center\" class=\"Tabla\">\r\n");   
	      //UsuariosManuales
	      jEnJuego = gestorSesions.getJugadasSalas(sala).getUsuariosManualesEnJuego();
	      for(int i=0; i < jEnJuego.size();i++){
	    	  String user= jEnJuego.elementAt(i);
	    	  out.write("<tr class=\"fondosLineas\" id=\"fila\">\r\n");
	    	  out.write("    <td class=\"otro\"><label class=\"AIzquierdas\">"+user+"</label></td>\r\n");
	    	  out.write("    <td class=\"otro\"><label class=\"AIzquierdas\">\r\n");
	    	  
	    	  out.write(udatabase.consultaSQLUnica("Select Saldo From usuarios Where User ='"+user+"'")+" </label></td>");
	    	  out.write("    <td class=\"otroCentro\">"+gestorSesions.getJugadasSalas(sala).dimeCartonesDe(user)+"<span class=\"AIzquierdas\">\r\n");
	    	  out.write("      \r\n");
	    	  out.write("    </span></td>\r\n");
	    	  out.write("    <td class=\"otroCentro\">"+"MANUAL"+"</td>\r\n");
	    	  out.write("</tr>\r\n");
	      }
	      //Usuarios electronicos
	      Set<UserBean> jElectronicos = gestorSesions.dameUserBeansEnPortal("jugador");
	      Iterator<UserBean> itjElectronicos = jElectronicos.iterator();
	     while(itjElectronicos.hasNext()){
	    	  UserBean userbean = itjElectronicos.next();
	    	  if(userbean.getSalonInUse().equals(sala)){
	    		  String user= userbean.getUsername();
	    		  out.write("<tr class=\"fondosLineas\" id=\"fila\">\r\n");
	    		  out.write("    <td class=\"otro\"><label class=\"AIzquierdas\">"+user+"</label></td>\r\n");
	    		  out.write("    <td class=\"otro\"><label class=\"AIzquierdas\">\r\n");
	    		  
	    		  out.write(udatabase.consultaSQLUnica("Select Saldo From usuarios Where User ='"+user+"'")+" </label></td>");
	    		  out.write("    <td class=\"otroCentro\">"+userbean.getvCarton().size()+"<span class=\"AIzquierdas\">\r\n");
	    		  out.write("      \r\n");
	    		  out.write("    </span></td>\r\n");
	    		  out.write("    <td class=\"otroCentro\">"+"DIGITAL"+"</td>\r\n");
	    		  out.write("</tr>\r\n");
	    	  }
	     }
	      out.write("\r\n");
	      out.write(" \r\n");
	      out.write("</table>\r\n");
	      out.write("</div>\r\n");
	      out.write("</div>\r\n");
	      
	      out.write("\r\n");
	      out.write("</body>\r\n");
	      out.write("</html>\r\n");		
	}
	private void writeHTMLVolcadoPeticionesBonus(PrintWriter out, String sala){
    	Set<Long> keysHora =gestorSesions.getPeticionBonus();
    	Iterator<Long> itLongHora = keysHora.iterator();
		UtilDatabase udatabase = new UtilDatabase();
        //arrayCampos[1] =1.User
		String vCaja = udatabase.consultaSQLUnica("Select SaldoCaja From caja");

	      out.write("");
	      out.write("<!doctype html>");
	      out.write("<html>");
	      out.write("<head>");
	      out.write("<meta charset=\"utf-8\">");
	      out.write("<title>Documento sin título</title>");
	      out.write("<link href=\"css/estilosDialogos.css\" rel=\"stylesheet\" type=\"text/css\">");
	      out.write("<script src=\"javascript/utilDialogos.js\"></script>");
	      out.write("");
	      out.write("</head>");
	      out.write("");
	      out.write("<body>");
	      out.write("");
	      out.write("<table id=\"resultLines\" width=\"97%\" border=\"1\" align=\"center\" class=\"Tabla\" >");
	      out.write("  <tr bgcolor=\"#003399\">");
	      out.write("    <td  width=\"50%\" height=\"72\" class=\"Cabecera\" >Peticiones compra Bonus Pendientes</td>");
	      out.write("    <td width=\"25%\">");
	      out.write("      <p align=\"center\"><input type=\"button\" width=\"30px\" name=\"Mostrar Peticiones\" value=\"Mostrar Peticiones\" onclick=\"volcarPeticionesBonus()\"></p>");
	      out.write("    </td>");
	      out.write("<td >");
	      out.write("  <label class=\"soloCaja\" >CAJA :</label >");
	      out.write("");
	      out.write("    <input  class=\"Caja\" type=\"text\" value=\"");
	      out.print(vCaja );
	      out.write("\"></td>    ");
	      out.write("    </tr>");
	      out.write("  <tr>");
	      out.write("    <td class=\"Cabecera\">Usuario</td>");
	      out.write("    <td  class=\"Cabecera\">Bonus</td>");
	      out.write("    <td  class=\"Cabecera\">Checking</td>");
	      out.write("  </tr>");

	  while(itLongHora.hasNext()){
	  		
	  		Long hora = (Long)itLongHora.next();
	  		PeticionBonus pBonus = gestorSesions.getPeticionBonus(hora);
	  		if(pBonus.getSala().equals(sala)){
	  			UserBean uBean = pBonus.getUserbean();

	  			out.write("<tr class='fondosLineas'><td  class='otro'><label class='AIzquierdas'>"+uBean.getUsername()+"</label></td>");
	  			out.write("<td class='otro'><label class='AIzquierdas'>"+pBonus.getBonus()+"</label></td>");
	  			out.write("<td class='otro'><input type='button' value='Pagado' onclick=\"realizarPagoBonus('"+hora+"')\"><label> </label><input type='button' value='Eliminar' onclick=\"borrarRegistroPeticionBonus('"+hora+"')\"></td></tr>");
	  		
	  		}
	  }	        

      out.write("</table>");
      out.write("</body>");
      out.write("</html>");
      out.flush();
	}
	private void writeHTMLVolcadoPeticionesLiquidacionBonus(PrintWriter out, String sala){

		UtilDatabase udatabase = new UtilDatabase();
        //arrayCampos[1] =1.User
		String vCaja = udatabase.consultaSQLUnica("Select SaldoCaja From caja");

	      out.write("");
	      out.write("<!doctype html>");
	      out.write("<html>");
	      out.write("<head>");
	      out.write("<meta charset=\"utf-8\">");
	      out.write("<title>Liquidacion Bonus</title>");
	      out.write("<link href=\"css/estilosDialogos.css\" rel=\"stylesheet\" type=\"text/css\">");
	      out.write("<script src=\"javascript/utilDialogos.js\"></script>");
	      out.write("");
	      out.write("</head>");
	      out.write("");
	      out.write("<body>");
	      out.write("");
	      out.write("<table id=\"resultLines\" width=\"97%\" border=\"1\" align=\"center\" class=\"Tabla\" >");
	      out.write("  <tr bgcolor=\"#003399\">");
	      out.write("    <td  width=\"50%\" height=\"72\" class=\"Cabecera\" >Peticiones Liquidacion Bonus Pendientes</td>");
	      out.write("    <td width=\"25%\">");
	      out.write("      <p align=\"center\"><input type=\"button\" width=\"30px\" name=\"Mostrar Peticiones\" value=\"Mostrar Peticiones\" onclick=\"volcarPeticionesLiquidacionBonus()\"></p>");
	      out.write("    </td>");
	      out.write("<td >");
	      out.write("  <label class=\"soloCaja\" >CAJA :</label >");
	      out.write("");
	      out.write("    <input  class=\"Caja\" type=\"text\" value=\"");
	      out.print(vCaja );
	      out.write("\"></td>    ");
	      out.write("    </tr>");
	      out.write("  <tr>");
	      out.write("    <td class=\"Cabecera\">Usuario</td>");
	      out.write("    <td  class=\"Cabecera\">Bonus</td>");
	      out.write("    <td  class=\"Cabecera\">Checking</td>");
	      out.write("  </tr>");
	      Set<Long> keysHora =gestorSesions.getPeticionLiquidacionBonus();
	      Iterator<Long> itLongHora = keysHora.iterator();
	      while(itLongHora.hasNext()){
	  		
	  		Long Keyhora = (Long)itLongHora.next();
	  		PeticionLiquidacionBonus pBonus = gestorSesions.getPeticionLiquidacionBonus(Keyhora);
	  		if(pBonus.getSala().equals(sala)){
	  			UserBean uBean = pBonus.getUserbean();

	  			out.write("<tr class='fondosLineas'><td  class='otro'><label class='AIzquierdas'>"+uBean.getUsername()+"</label></td>");
	  			out.write("<td class='otro'><label class='AIzquierdas'>"+pBonus.getBonus()+"</label></td>");
	  			out.write("<td class='otro'><input type='button' value='Pagado' onclick=\"realizarLiquidacionBonus('"+Keyhora+"')\"><label> </label><input type='button' value='Eliminar' onclick=\"borrarRegistroPeticionLiquidacionBonus('"+Keyhora+"')\"></td></tr>");
	  		
	  		}
	  }	        

      out.write("</table>");
      out.write("</body>");
      out.write("</html>");
      out.flush();
	}
}
