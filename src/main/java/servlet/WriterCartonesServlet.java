package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Vector;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/WriterCartonesServlet")
public class WriterCartonesServlet extends HttpServlet{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
@Inject
private GestorSessions gestorSesions;	

protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		HttpSession htps=req.getSession();
		UserBean userbean=null;
		
		String usuario = req.getParameter("usuario");
		String comando= req.getParameter("comando");
		String idHttp = htps.getId();
		
		res.setContentType("text/html");
		PrintWriter out;

		if(comando.equals("CartonesJuego")){
			//Los Cartones se obtienen de un vector de UserBeans para luego registrarlo, modificado ,a el mapa del usuario.
			Vector<UserBean> vUserBean = gestorSesions.dameVectorUserBeansUsuario(usuario);
		
			Iterator<UserBean> itUserBean = vUserBean.iterator();
			//Iteramos todas posibles conexiones o userbeans de usuario activas
			while(itUserBean.hasNext()){
				userbean= itUserBean.next();
				HttpSession htpsBean = userbean.getSesionHttp();
				String idHttpSessionBean = htpsBean.getId();
				if(idHttpSessionBean.equals(idHttp)){
					//	Por cada userbean obtenemos su juego de cartones adquiridos
					Vector<Carton> vCarton= userbean.getvCarton();
					//	fillCanvas es un metodo generico de relleno estructura HTML del carton.
					//	Necesita un PrintWriter (Normalmente obtenido del Servlet de servicio corriente.) y un Vector de cartones a imprimir
					out = res.getWriter();
					fillCanvasTable(out,vCarton);
					//	registramos los cambios hechos en los cartones, a nivel del numero de Orden del carton
					// 	que luego nos sirve para identificar el carton en el check de Linea, Bingo.
					userbean.setvCarton(vCarton);
				}
			
			}
			gestorSesions.setVectorUserBeansUsuario(usuario, vUserBean);
		}
		if(comando.equals("ImprimirCartones")){
			//Los Cartones son proporcionados aleatoriamente en el rango de los ultimos 100 cartones de la base de datos de cartones.
			//Se consigue el objeto Carton a partir de la Clase Carton y su metodo consultaObjetoCarton(String nrefCarton)
			//En la request obtenemos el numero de cartones a imprimr (nCartones)

				Carton utilCarton = new Carton();
				Vector<Carton> cartonesPrinting = new Vector<Carton>();
				int nCartones = new Integer(req.getParameter("nCartones"));
				for(int n=1001; n < 1001 + nCartones; n++){
				//for(int n=1; n < 1 + nCartones; n++){
					Carton myCarton = utilCarton.consultaObjetoCarton(n+"");
					cartonesPrinting.add(myCarton);
				}
				out = res.getWriter();
				
			      out.write("<!doctype html>\r\n");
			      out.write("<html>\r\n");
			      out.write("<head>\r\n");
			      out.write("<meta charset=\"utf-8\">\r\n");
			      out.write("<meta name=\"vieport\" content=\"width=800, initial-scale=1, orientation=landscape\">\r\n");
			      out.write("<title>Plantilla Impresion Cartones</title>\r\n");
			      out.write("  <link href=\"css/Carton.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
			      out.write("  <link rel=\"stylesheet\" href=\"//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css\">\r\n");
			      out.write("  <script src=\"//code.jquery.com/jquery-1.12.4.js\"></script>\r\n");
			      out.write("  <script src=\"//code.jquery.com/ui/1.12.1/jquery-ui.js\"></script>\r\n");
			      out.write("  <script src=\"javascript/canvasNumeros.js\"></script>\r\n");
			      out.write("  <script src=\"javascript/carton.js\"></script>\r\n");
			      out.write("\r\n");
			      out.write("</head>\r\n");	
			      out.write("\r\n");
			      out.write("<body class=\"pagina\" id=\"content\">\r\n");

				this.fillCanvasTable(out, cartonesPrinting);
			      out.write("<img id=\"Loto2\" class=\"hiddenImage\" src=\"./images/Loto2.png\">\r\n");					      
			      out.write("</body>\r\n");	
			      out.flush();

		}
		
	}

	private void fillCanvasTable(PrintWriter out,Vector<Carton> vCarton){
		Carton carton = null;
		int nCartones = vCarton.size();
		out.write("<input id=\"numeroCartonesComprados\" type=\"hidden\" name=\"cuantosCartones\" value=\"");
		out.print(""+nCartones );
		out.write("\">\r\n");
		//HTML pàra rejilla Cartones.
		for(int nC=1;nC <= nCartones; nC++){
			carton = (Carton)vCarton.elementAt(nC-1);
	  //	Para luego poder identificarlo en el check de carton
			carton.setnOrden(nC);
			vCarton.set(nC-1, carton);
			int [][]maskCellVideo = carton.getNumeros();
			
			out.write("<table class=\"tablero\">\r\n");
			for(int fila=0;fila<3;fila++) {
				out.write("    <tr>\r\n");
				for (int col=0; col < 9; col++) {
					if(maskCellVideo[fila][col]==0) {
						out.write("    </td>\r\n");
						out.write("      <td class=\"panel\"> \r\n");
						out.write("     \t<video autoplay playsinline=\"true\" class=\"canvasNumero\" id=\"");
						out.print(""+nC) ;
						out.write("F"+(fila+1)+"C"+(col+1)+"\"></video>   \r\n");
	
					}else {
						out.write("         <td  class=\"panel\"> \r\n");
						out.write("          <canvas class=\"canvasNumero\" id=\"");
						out.print(""+nC) ;
						out.write("F"+(fila+1)+"C"+(col+1)+"\" ></canvas>             \r\n");
					}

					
				}
			}
			String nRef = carton.getnRef()+"";
			

            /*
			out.write("        </td>\r\n");
			out.write("    <td class=\"panel\" >\t          \r\n");
			out.write("  \t\t  <canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F1C2\" ></canvas>\r\n");
			out.write("    </td>\r\n");
			out.write("      <td class=\"panel\"> \r\n");
			out.write("     \t<video autoplay playsinline=\"true\" class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F1C3\"></canvas>   \r\n");
			out.write("        </td>\r\n");
			out.write("      <td class=\"panel\"> \r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F1C4\"></canvas> \r\n");
			out.write("        </td>\r\n");
			out.write("      <td class=\"panel\"> \r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F1C5\"></canvas>            \r\n");
			out.write("        </td>\r\n");
			out.write("      <td class=\"panel\">\r\n");
			out.write("     \t <canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F1C6\"></canvas> \r\n");
			out.write("      </td >\r\n");
			out.write("      <td  class=\"panel\">\r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F1C7\"></canvas>               \r\n");
			out.write("     </td >\r\n");
			out.write("        <td  class=\"panel\">\r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F1C8\"></canvas>          \r\n");
			out.write("        </td >\r\n");
			out.write("        <td  class=\"panel\">\r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F1C9\"></canvas>          \r\n");
			out.write("        </td >\r\n");
			out.write("\r\n");
			out.write("    </tr>\r\n");
			out.write("   <tr>\r\n");
			out.write("         <td class=\"panel\"> \r\n");
			out.write("          <canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F2C1\"></canvas>             \r\n");
			out.write("        </td>\r\n");
			out.write("    <td class=\"panel\">\t          \r\n");
			out.write("    <canvas class=\"canvasNumero\"  id=\"");
			out.print(""+nC) ;
			out.write("F2C2\">\r\n");
			out.write("\t           \r\n");
			out.write("    </canvas></td>\r\n");
			out.write("      <td class=\"panel\"> \r\n");
			out.write("     \t<video autoplay playsinline=\"true\" class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F2C3\"></canvas>   \r\n");
			out.write("        </td>\r\n");
			out.write("      <td class=\"panel\"> \r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F2C4\"></canvas> \r\n");
			out.write("        </td>\r\n");
			out.write("      <td class=\"panel\"> \r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F2C5\"></canvas>            \r\n");
			out.write("        </td>\r\n");
			out.write("      <td class=\"panel\">\r\n");
			out.write("     \t <canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F2C6\"></canvas> \r\n");
			out.write("      </td >\r\n");
			out.write("      <td  class=\"panel\">\r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F2C7\"></canvas>               \r\n");
			out.write("     </td >\r\n");
			out.write("        <td  class=\"panel\">\r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F2C8\"></canvas>          \r\n");
			out.write("        </td >\r\n");
			out.write("        <td  class=\"panel\">\r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F2C9\"></canvas>          \r\n");
			out.write("        </td >\r\n");
			out.write("\r\n");
			out.write("    </tr>\r\n");
			out.write("   <tr>\r\n");
			out.write("         <td class=\"panel\"> \r\n");
			out.write("          <video autoplay playsinline=\"true\" class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F3C1\"></canvas>             \r\n");
			out.write("        </td>\r\n");
			out.write("    <td class=\"panel\" >\t          \r\n");
			out.write("    <canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F3C2\">\r\n");
			out.write("\t           \r\n");
			out.write("    </canvas></td>\r\n");
			out.write("      <td class=\"panel\"> \r\n");
			out.write("     \t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F3C3\"></canvas>   \r\n");
			out.write("        </td>\r\n");
			out.write("      <td class=\"panel\"> \r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F3C4\"></canvas> \r\n");
			out.write("        </td>\r\n");
			out.write("      <td class=\"panel\"> \r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F3C5\"></canvas>            \r\n");
			out.write("        </td>\r\n");
			out.write("      <td class=\"panel\">\r\n");
			out.write("     \t <canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F3C6\"></canvas> \r\n");
			out.write("      </td >\r\n");
			out.write("      <td  class=\"panel\">\r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F3C7\"></canvas>               \r\n");
			out.write("     </td >\r\n");
			o			out.write("    </td>\r\n");
			out.write("      <td class=\"panel\"> \r\n");
			out.write("     \t<video autoplay playsinline=\"true\" class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F1C3\"></canvas>   \r\n");
ut.write("        <td  class=\"panel\">\r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F3C8\"></canvas>          \r\n");
			out.write("        </td >\r\n");
			out.write("        <td  class=\"panel\">\r\n");
			out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
			out.print(""+nC) ;
			out.write("F3C9\"></canvas>          \r\n");
			out.write("        </td >\r\n");
			*/
			out.write("\r\n");
			out.write("    </tr>\r\n");
			out.write("    <tr>\r\n");
			out.write("    \t<td colspan=\"7\" class=\"letraCarton\">CARTON n:<Label align=\"center\" id=\"refCarton");
			out.print(""+nC);
			out.write('"');
			out.write('>');
			out.print(""+nRef);
			out.write("</label></td>\r\n");
			out.write("<td colspan=\"2\" class=\"letraCarton\"><label align=\"right\">Copyright@Boga</label></td>\r\n");
			out.write("    </tr>\r\n");
			out.write("</table>\r\n");
			if(nC%4==0)out.write("<div style=\"page-break-before: always;\"> </div>");
			
			
			
		}
	}
}
