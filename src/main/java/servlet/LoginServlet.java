package servlet;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import javax.inject.Inject;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

static Logger log = Logger.getLogger("LoginServlet");

@Inject
private Mailing mail;

@Inject
private GestorSessions gestorSesions;


public void doGet(HttpServletRequest request, HttpServletResponse response) 
			           throws ServletException, java.io.IOException {

try
{	    

     UserBean user = new UserBean();
     user.setUserName(request.getParameter("un"));
     user.setPassword(request.getParameter("pw"));
     user.setSalonInUse(request.getParameter("sala"));
     

     user = UserDAO.login(user);
     String perfil = user.getPerfil(); 		    
     if (user.isValid())
     {
          
    	 HttpSession session = request.getSession(true);
    	  user.setSesionHttp(session);
    	  PocketBingo pb = gestorSesions.getJugadasSalas(user.getSalonInUse());
    	  if(pb==null && perfil.equals("supervisor")){
    		  if(pb==null)pb= new PocketBingo();
    		  gestorSesions.setJugadasSalas(user.getSalonInUse(), pb);
    	  }
 
          if(perfil.equals("jugador")){
        	  Carton newcarton = new Carton();
        	  Vector<Carton> vCarton = user.getvCarton();
        	  if(vCarton==null){
        		  //No ha comprado cartones
        		  //rutina compra cartones
        		  log.info("vCarton es null");
        		  vCarton = new Vector();
        		  vCarton.add(newcarton);
        	  }else{
        		  //tiene un carton activo
        	  }
        	  
        	  user.setvCarton(vCarton);
        	  session.setAttribute("userBean",user); 
        	  response.sendRedirect("carton.jsp"); //logged-in page  
          }

          
          if(perfil.equals("supervisor")){
        	  session.setAttribute("userBean",user); 
        	  response.sendRedirect("bingo.jsp"); //logged-in page  
          }
          
          //mail.sendEmail("javier.boga.rioja@gmail.com","javier.boga.rioja@gmail.com", "prueba", "Has accedido al portal.Gracias");
     }
	        
     else 
          response.sendRedirect("invalidLogin.jsp"); //error page 
} 
		
		
catch (Throwable theException) 	    
{
     System.out.println(theException); 
}
       }
	}