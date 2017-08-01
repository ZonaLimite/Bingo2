package servlet;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

//import org.jboss.logging.Logger;

import javax.inject.Inject;

/**
 * Servlet implementation class LoginServlet
 */
@WebListener
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet implements HttpSessionAttributeListener {

static Logger log = Logger.getLogger("LoginServlet");

@Inject
private Mailing mail;

@Inject
private GestorSessions gestorSesions;


@Override
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
     if (user.isValid()){
          
     HttpSession session = request.getSession(true);
     user.setSesionHttp(session);
     PocketBingo pb = gestorSesions.getJugadasSalas(user.getSalonInUse());
     if(pb==null ){
    		  pb= new PocketBingo();
    		  pb.setIdState("Finalized");
                  gestorSesions.setJugadasSalas(user.getSalonInUse(), pb);
                  log.info("Registrando Pockect a gestorSessions");
     }
    	  
          if(perfil.equals("jugador")){
        	  session.setAttribute("usuario",user.getUsername());
        	  session.setAttribute("userBean",user);
                  gestorSesions.add(user.getUsername(), user);
        	  response.sendRedirect("carton.jsp"); //logged-in page  
          }

          
          if(perfil.equals("supervisor")){
        	  session.setAttribute("usuario",user.getUsername());
        	  session.setAttribute("perfil",user.getPerfil());
        	  session.setAttribute("sala",user.getSalonInUse());
                  gestorSesions.add(user.getUsername(), user);
        	  response.sendRedirect("bingo.jsp"); //logged-in page  
          }
          
     }else response.sendRedirect("invalidLogin.jsp"); //error page 
} 
		
		
catch (Throwable theException) 	    
{
     System.out.println(theException); 
}
}
@Override
public void attributeAdded(HttpSessionBindingEvent event) {
    String nameEvent = event.getName();
    
    if(nameEvent.equals("usuario")){
    	String name = (String)event.getValue();
    	
        System.out.println("Se esta detectando el añadido de atributo de un usuario("+name+") a sesion");
    }
}

@Override
public void attributeRemoved(HttpSessionBindingEvent event) {
    String nameEvent = event.getName();
    System.out.println("Se accede a metodo deteccion atributo removido para "+nameEvent);
    if(nameEvent.equals("usuario")){
        //UserBean ub = (UserBean)event.getValue();//
    	gestorSesions.removeUserBean(event.getSession());
        //remove(ub.getSesionHttp());

    }
        
}

@Override
public void attributeReplaced(HttpSessionBindingEvent event) {
	 if(event.getName().equals("usuario")){
		 System.out.println("Se detecta atributo de sesion "+event.getName()+" reemplazado");
	 }
}
}
