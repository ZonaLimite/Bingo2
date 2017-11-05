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
		Carton carton = null;
		HttpSession htps=req.getSession();
		UserBean userbean=null;
		String usuario = req.getParameter("usuario");
		String perfil= req.getParameter("perfil");
		String idHttp = htps.getId();
		res.setContentType("text/html");
		PrintWriter out;
	   
	   ////Solo hay un Userbean que requiera el carton a la vez
	   //UserBean userbean = gestorSesions.dameUserBeansPorUser(usuario,perfil,idHttp);
	
		//Necesitaria mejor un vector de UserBeans,para luego registrarlo modificado a el mapa del usuario.
		Vector<UserBean> vUserBean = gestorSesions.dameVectorUserBeansUsuario(usuario);
		Iterator<UserBean> itUserBean = vUserBean.iterator();
		while(itUserBean.hasNext()){
			userbean= itUserBean.next();
			HttpSession htpsBean = userbean.getSesionHttp();
			String idHttpSessionBean = htpsBean.getId();
			if(idHttpSessionBean.equals(idHttp)){
				Vector<Carton> vCarton= userbean.getvCarton();
				int nCartones = vCarton.size();
	   
				out = res.getWriter();
	   
				out.write("<input id=\"numeroCartonesComprados\" type=\"hidden\" name=\"cuantosCartones\" value=\"");
				out.print(""+nCartones );
				out.write("\">\r\n");
				
				for(int nC=1;nC <= nCartones; nC++){
					carton = (Carton)vCarton.elementAt(nC-1);
			  //	Para luego poder identificarlo en el check de carton
					carton.setnOrden(nC);
					vCarton.set(nC-1, carton);
					
					String nRef = carton.getnRef()+"";
					out.write("<table class=\"tablero\">\r\n");
					out.write("    <tr>\r\n");
					out.write("         <td  class=\"panel\"> \r\n");
					out.write("          <canvas class=\"canvasNumero\" id=\"");
					out.print(""+nC) ;
					out.write("F1C1\" ></canvas>             \r\n");
					out.write("        </td>\r\n");
					out.write("    <td class=\"panel\" >\t          \r\n");
					out.write("  \t\t  <canvas class=\"canvasNumero\" id=\"");
					out.print(""+nC) ;
					out.write("F1C2\" ></canvas>\r\n");
					out.write("    </td>\r\n");
					out.write("      <td class=\"panel\"> \r\n");
					out.write("     \t<canvas class=\"canvasNumero\" id=\"");
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
					out.write("     \t<canvas class=\"canvasNumero\" id=\"");
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
					out.write("          <canvas class=\"canvasNumero\" id=\"");
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
					out.write("        <td  class=\"panel\">\r\n");
					out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
					out.print(""+nC) ;
					out.write("F3C8\"></canvas>          \r\n");
					out.write("        </td >\r\n");
					out.write("        <td  class=\"panel\">\r\n");
					out.write("\t\t<canvas class=\"canvasNumero\" id=\"");
					out.print(""+nC) ;
					out.write("F3C9\"></canvas>          \r\n");
					out.write("        </td >\r\n");
					out.write("\r\n");
					out.write("    </tr>\r\n");
					out.write("    <tr>\r\n");
					out.write("    \t<td colspan=\"9\" class=\"letraCarton\">CARTON n:<Label id=\"refCarton");
					out.print(""+nC);
					out.write('"');
					out.write('>');
					out.print(""+nRef);
					out.write("</label></td>\r\n");
					out.write("    </tr>\r\n");
					out.write("</table>\r\n");
					out.flush();
					
				}
				//	registramos los cambios hechos en los cartones, a nivel del numero de Orden del carton
				// 	que luego nos sirve para identificar el carton en el check de Linea, Bingo
				userbean.setvCarton(vCarton);
			}
			
		}
		gestorSesions.setVectorUserBeansUsuario(usuario, vUserBean);
	}
}
