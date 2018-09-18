package servlet;

import java.io.IOException;
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

import com.google.gson.Gson;

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
public void doPost(HttpServletRequest request, HttpServletResponse response) 
			           throws ServletException, java.io.IOException {

try
{	    
	 String okLogin="No";
     UserBean user = new UserBean();
     user.setUserName(request.getParameter("un"));
     user.setPassword(request.getParameter("pw"));
     user.setSalonInUse(request.getParameter("sala"));
 
     user = UserDAO.login(user);
     String perfil = user.getPerfil(); 		    
     if (user.isValid()){
          

    	 PocketBingo pb = gestorSesions.getJugadasSalas(user.getSalonInUse());
    	 if(pb==null ){
    		  pb= new PocketBingo();
    		  pb.setIdState("Finalized");
              gestorSesions.setJugadasSalas(user.getSalonInUse(), pb);
              log.info("Registrando Pockect a gestorSessions");
    	 }
    	  
         if(perfil.equals("jugador")){
        	 //Un control para verificar si este jugador ya esta jugando en modo manual y tiene cartones
        	 
        	 if(pb.estaJugandoAManual(user.getUsername())==true && !(pb.getIdState().equals("Finalized"))){
        		 okLogin="2Users";
        	 }else {
        		 //Utilizamos el atributo de sesion usuario para hacer seguimiento de sesiones Http mediante 
        		 //la implementacion de la interface "implements HttpSessionAttributeListener"
            	 HttpSession session = request.getSession(true);
            	 user.setSesionHttp(session);
        		 user.setType("Digital");
        		 //session.setAttribute("usuario_"+user.getUsername(),user.getUsername());
        		 session.setAttribute("usuario",user.getUsername());
        		 session.setAttribute("perfil",user.getPerfil());
        		 session.setAttribute("sala",user.getSalonInUse());
        	  	  
        		 //Registramos el usuario a traves de un bean que representa todos los atributos del mismo
        		 //en un singleton con alcance de aplicacion
                  gestorSesions.add(user.getUsername(), user);
                  okLogin="Si";
                  //String url="WriterHeaderCarton?usuario="+user.getUsername()+"&sala="+user.getSalonInUse()+"&perfil="+perfil;
                  //String url="Portal.jsp";
                  //log.info("Voy ha hacer el response");
                  //response.sendRedirect(url); //logged-in page // 
        	 }
         }

         if(perfil.equals("supervisor")){
        	  //session.setAttribute("usuario_"+user.getUsername(),user.getUsername());
        	  HttpSession session = request.getSession(true);
        	  user.setSesionHttp(session);
        	  session.setAttribute("usuario",user.getUsername());
        	  session.setAttribute("perfil",user.getPerfil());
        	  session.setAttribute("sala",user.getSalonInUse());
                  gestorSesions.add(user.getUsername(), user);
                  
                 //String url="WriterHeaderBingo?usuario="+user.getUsername()+"&sala="+user.getSalonInUse()+"&perfil="+perfil;
                 // String url="Portal.jsp";
                 //mail.sendEmail("javier.boga@yahoo.es", "javier.boga.rioja@gmail.com", "prueba", "Hay contenido");
        	     //response.sendRedirect(url); //logged-in page 
                  okLogin="Si";
         }
          
     }else{
    	 	okLogin="No";

     }
		//Ahora a enviarlo como JSON
     	String json = new Gson().toJson(okLogin);
 
     	response.setContentType("application/json");
     	response.setCharacterEncoding("UTF-8");
     	try {
     		response.getWriter().write(json);
     	} catch (IOException e) {
		e.printStackTrace();
     	}

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


    //if(nameEvent.contains("usuario")){

    /*    	
    StringTokenizer st = new StringTokenizer(nameEvent,"_");
        String atributo = st.nextToken();
        String usuario = st.nextToken(); */
        //UserBean ub = (UserBean)event.getValue();//
    	//Se elimina el userbean asociado a esta sesion Http ("Se avisara a el usuario para que renove la sesion").
    	//remove(ub.getSesionHttp());
    String nameEvent = event.getName();
	if(nameEvent.equals("usuario")){
    gestorSesions.removeUserBean(event.getSession(),(String)event.getValue());

    	System.out.println("Se accede a metodo deteccion atributo removido para "+(String)event.getValue());
    }
        
}

@Override
public void attributeReplaced(HttpSessionBindingEvent event) {
	 if(event.getName().equals("usuario")){
		 System.out.println("Se detecta atributo de sesion "+event.getName()+" reemplazado");
	 }
}
}
