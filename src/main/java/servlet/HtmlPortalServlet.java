package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet("/HtmlPortal")
public class HtmlPortalServlet extends HttpServlet{
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		Carton carton = null;
		HttpSession htps=req.getSession();
		String idHttpSession = htps.getId();
		String usuario = req.getParameter("usuario");
		String perfil = req.getParameter("perfil");
		String ordCarton = req.getParameter("nCarton");
		String comando = req.getParameter("comando");
		String url="";
		
		if(comando.equals("MostrarLogin")){
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
			res.sendRedirect("Login.html");
		}
		if(comando.equals("MostrarUsuarios")){
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
			res.sendRedirect("PlantillaListaJugadores.html");
		}		
		if(comando.equals("MostrarNuevoUsuario")){
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
			res.sendRedirect("AltaUsuario.html");
		}
		if(comando.equals("Solicitud Bonus")){
		    res.setContentType("text/html");
		    res.setCharacterEncoding("UTF-8");
			res.sendRedirect("CompraBonus.html");
		}
		
		if(comando.equals("ArrayCartonBaseDatos")){

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
}
