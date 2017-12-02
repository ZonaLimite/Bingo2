package servlet;

import java.io.IOException;

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
}
}
