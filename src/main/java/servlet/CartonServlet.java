package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/WriterHeaderCarton")
public class CartonServlet extends HttpServlet{
	private String user,sala,perfil;
	//DNS Amazon VPS (linkado por dominio bogaservice.es)
	private String ipWebServer ;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String url="Portal.jsp";
		HttpSession mySession = req.getSession(false);
		String protocol = req.getScheme();
		String host = req.getServerName();
		int port= req.getServerPort();
		ipWebServer = protocol+"://"+host+":"+port+"/wildfly";					
		if(mySession!=null){
			
			//Comprobacion de HttpSession registrada aqui
			
			user = req.getParameter("usuario");//
			if(mySession.getAttribute("usuario")==null) {
				res.sendRedirect(url); //logged-in page //
				System.out.println("Usuario no registrado");
				return;
			}
			System.out.println("la mySesion "+ mySession.getId());
			sala = req.getParameter("sala");
			perfil =req.getParameter("perfil");
			
		}else{
			System.out.print("la sesion es null");
	        
	        res.sendRedirect(url); //logged-in page // 
	        return;
		}

		res.setContentType("text/html; charset=utf-8");
		
		PrintWriter out;
		
		//HTML Carton
		  out = res.getWriter();
		  
	      out.write("<!doctype html>\r\n");
	      out.write("<html>\r\n");
	      out.write("<head>\r\n");
	      out.write("<meta charset=\"utf-8\">\r\n");
	      out.write("<meta name=\"vieport\" content=\"width=800, initial-scale=1, orientation=landscape\">\r\n");
	      out.write("<title>Bingo 2016</title>\r\n");
	      out.write("  <link href=\"css/Carton.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
	      out.write("  <link rel=\"stylesheet\" href=\"//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css\">\r\n");
	      out.write("  <script src=\"//code.jquery.com/jquery-1.12.4.js\"></script>\r\n");
	      out.write("  <script src=\"//code.jquery.com/ui/1.12.1/jquery-ui.js\"></script>\r\n");
	      out.write("  <script src=\"javascript/canvasNumeros.js\"></script>\r\n");
	      out.write("  <script src=\"javascript/carton.js\"></script>\r\n");
	      out.write("\r\n");
	      out.write("</head>\r\n");		
		//<Body>
	      out.write("\r\n");
	      out.write("<body class=\"pagina\" id=\"content\">\r\n");
	      out.write("<div id=\"agrupar\">\r\n");
	      out.write("<header id=\"cabecera\">\r\n");
	      out.write("\r\n");
	      out.write("<table width=\"100%\" border=\"1\" cellspacing=\"2\" class=\"ano\">\r\n");
	      out.write("  <tr>\r\n");
	      out.write("  <td width=\"12%\" height=\"109\" padding=0>\r\n");
	      out.write("  \t<label class=\"labelUser\">Saldo : <label class=\"saldo\" id=\"saldo\">0</label> €</label>\r\n");
	      out.write("  \t<a href=Portal.jsp><img id=\"logo\" src=\"images/IconoBola.jpg\" width=\"56\" height=\"45\" longdesc=\"file:///C|/Users/boga/git/wildfly/src/main/webapp/images/IconoBola.jpg\" ></a>\r\n");
	      out.write("  \t<label id=\"sala\">");
	out.print(sala);
	      out.write("</label>\r\n");
	      out.write("    <span class=\"labelUser\">\r\n");
	      out.write("    <label class=\"labelUser\">");
	out.print(user);
	      out.write("</label>\r\n");
	      out.write("  \r\n");
	      out.write(" \r\n");
	      out.write("    </span></td>\r\n");
	      out.write("  \r\n");
	      out.write(" <td width=\"60%\" class=\"tablaInfo\" ><table width=\"100%\"  align=\"center\" class=\"tablaInfo\">\r\n");
	      out.write("   <tr>\r\n");
	      out.write("     <td width=\"97\" class=\"celdaInfo\"><span class=\"Comander\">BOLA N:</span></td>\r\n");
	      out.write("     <td width=\"37\" id=\"datoOrdenBola\" class=\"celdaInfo\"  ><span class=\"Comander\">\r\n");
	      out.write("       <label id=\"labelOrden\" class=\"valorInfo\">0</label>\r\n");
	      out.write("     </span></td>\r\n");
	      out.write("     <td width=\"118\" id=\"datoLinea\" class=\"celdaInfo\" ><label id=\"labelLinea\">LINEA:</label></td>\r\n");
	      out.write("     <td width=\"176\" class=\"celdaInfo\"><span class=\"Comander\">\r\n");
	      out.write("       <label id=\"valorLinea\" class=\"valorInfo\">0 €</label>\r\n");
	      out.write("     </span></td>\r\n");
	      out.write("   </tr>\r\n");
	      out.write("   <tr>\r\n");
	      out.write("     <td class=\"celdaInfo\"><span class=\"Comander\"> CARTONES</span></td>\r\n");
	      out.write("     <td id=\"datoCartones\" class=\"celdaInfo\" ><span class=\"Comander\">\r\n");
	      out.write("       <label id=\"valorCartones\" class=\"valorInfo\">0</label>\r\n");
	      out.write("     </span></td>\r\n");
	      out.write("     <td class=\"celdaInfo\"><label id=\"labelBingo\">BINGO:</label></td>\r\n");
	      out.write("     <td  id=\"datoBingo\" class=\"celdaInfo\" ><span class=\"Comander\">\r\n");
	      out.write("       <label id=\"valorBingo\" class=\"valorInfo\">0 €</label>\r\n");
	      out.write("     </span></td>\r\n");
	      out.write("   </tr>\r\n");
	      out.write("\r\n");
	      out.write("   <tr>\r\n");
	      out.write("     <td colspan=\"4\" class=\"valorInfo\" >\r\n");
	      out.write("     <article id=\"articulo\">\t\r\n");
	      out.write("\t\t<div align=\"center\">  \r\n");
	      out.write("\t\t<label id=\"boton_Linea\" CLASS=\"menu_li2\" >LINEA</label>\r\n");
	      out.write("\t\t<label id=\"boton_Bingo\" CLASS=\"menu_li2\" >BINGO</label>\r\n");
	      out.write("\t\t<label id=\"boton_Jugar\" CLASS=\"menu_li2\" >FULL</label>\r\n");
	      out.write("\t\t<label id=\"boton_Carton\" CLASS=\"menu_li2\" >CARD</label>\r\n");
	      out.write("\t\t</div>  \r\n");
	      out.write("\t</article>\r\n");
	      out.write("\t</td>\r\n");
	      out.write("   </tr>\r\n");
	      out.write("\t   <tr>\r\n");
	      out.write("     <td colspan=\"4\" class=\"valorInfo\" id=\"comboTexto\">\r\n");
	      out.write("     <label  id=\"labelTexto\" >combotexto</label>\r\n");
	      out.write("\t </td>\r\n");
	      out.write("   </tr>      \r\n");
	      out.write(" </table>\r\n");
	      out.write(" \r\n");
	      out.write("  \r\n");
	      out.write("\t\r\n");
	      out.write("    <td id=\"CajaDcha\" width=\"38%\">\t          \r\n");
	      out.write("    <canvas id=\"canvas_bola\" class=\"canvasBola\">\r\n");
	      out.write("\t           \r\n");
	      out.write("    </canvas></td>\r\n");
	      out.write("  </tr>\r\n");
	      out.write("   \r\n");
	      out.write("</table>\r\n");
	      out.write("</header>\r\n");
	      out.write("<article id=\"innerHTMLCartones\">\r\n");
	      out.write("\t\t<!--Espacio para cartones via Servlet (WriterCartonesServlet)  -->\r\n");
	      out.write("</article>\r\n");
	      out.write("</div>\r\n");
	      out.write("<footer id=\"footer\" class=\"footerClass\">");
	      out.write("<div id=\"cartones\" title=\"Compra de cartones\"> \r\n");
	      out.write("<form id=\"requestForm\">\r\n");
	      out.write("\r\n");
	      out.write("<label >Numero de cartones a jugar?:</label><input id=\"spinner\" type=\"text\" width=\"20\" value=\"1\" name=\"nCartones\" style=\" width : 27px;\">\r\n");
	      out.write("<input type=\"hidden\" id=\"sala\" name=\"sala\" value=\"");
	out.print(sala); 
	      out.write("\">\r\n");
	      out.write("<input type=\"hidden\" id=\"usuario\"  name=\"usuario\" value=\"");
	out.print(user); 
	      out.write("\">\r\n");
	      out.write("<input type=\"hidden\" id=\"perfil\"  name=\"perfil\" value=\"");
	out.print(perfil); 
	      out.write("\">\r\n");
	      
	      out.write("<br>\r\n");
	      out.write("<label id=\"feedback\" style=\" width : 100%;\"></label>\r\n");
	      out.write("<img id=\"Loto2\" class=\"hiddenImage\" src=\"./images/Loto2.png\">\r\n");
	      out.write("</form>\r\n");
	      out.write("</div>\r\n");
	      out.write("<div id=\"welcome\" title=\"¡BIENVENIDOS!\"> \r\n");
	      out.write("<form >\r\n");
	      out.write("<label >Pulsa \"START\" para comenzar</label>\r\n");
	      out.write("</form>\t\r\n");
	      out.write("</div>\r\n");
	      out.write("\r\n");
	      out.write("<span class=\"audioClass\">\r\n");
	      out.write("\r\n");
	      out.write("</span>\r\n");
	      out.write("<audio id=\"audioWeb\" class=\"audioClass\" controls >\r\n");
	      out.write("  \t<source src=\""+ipWebServer+"/audio/AudioLinea1.mp3\" type=\"audio/mpeg\">\r\n");
	      out.write("\tYour browser does not support the audio element....\r\n");
	      out.write("</audio>\r\n");
	      out.write("</footer>");
	      out.write("</body>\r\n");
	      out.write("\r\n");
	      out.write("</html>\r\n");
	}
}
