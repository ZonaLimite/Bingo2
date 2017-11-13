package servlet;

import java.util.StringTokenizer;
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
        	  //Utilizamos el atributo de sesion usuario para hacer seguimiento de sesiones Http mediante 
        	  //la implementacion de la interface "implements HttpSessionAttributeListener"
        	  
        	  	  session.setAttribute("usuario_"+user.getUsername(),user.getUsername());
            	  //session.setAttribute("perfil",user.getPerfil());
            	  //session.setAttribute("sala",user.getSalonInUse());
        	  	  
        	  //Registramos el usuario a traves de un bean que representa todos los atributos del mismo
        	  //en un singleton con alcance de aplicacion
                  gestorSesions.add(user.getUsername(), user);
                
                  String url="WriterHeaderCarton?usuario="+user.getUsername()+"&sala="+user.getSalonInUse()+"&perfil="+perfil;
                  log.info("Voy ha hacer el response");
                  response.sendRedirect(url); //logged-in page // 
         }

         if(perfil.equals("supervisor")){
        	  session.setAttribute("usuario_"+user.getUsername(),user.getUsername());
        	  ////session.setAttribute("perfil",user.getPerfil());
        	  //session.setAttribute("sala",user.getSalonInUse());
                  gestorSesions.add(user.getUsername(), user);
                  
                 String url="WriterHeaderBingo?usuario="+user.getUsername()+"&sala="+user.getSalonInUse()+"&perfil="+perfil;
                  //String url="bingo.jsp";
                 //mail.sendEmail("javier.boga@yahoo.es", "javier.boga.rioja@gmail.com", "prueba", "Hay contenido");
        	  response.sendRedirect(url); //logged-in page  
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

    if(nameEvent.contains("usuario")){
        StringTokenizer st = new StringTokenizer(nameEvent,"_");
        String atributo = st.nextToken();
        String usuario = st.nextToken();
        //UserBean ub = (UserBean)event.getValue();//
    	//Se elimina el userbean asociado a esta sesion Http ("Se avisara a el usuario para que renove la sesion").
    	gestorSesions.removeUserBean(event.getSession(),usuario);
        //remove(ub.getSesionHttp());
    	System.out.println("Se accede a metodo deteccion atributo removido para "+usuario);
    }
        
}

@Override
public void attributeReplaced(HttpSessionBindingEvent event) {
	 if(event.getName().equals("usuario")){
		 System.out.println("Se detecta atributo de sesion "+event.getName()+" reemplazado");
	 }
}
}
