package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.inject.Inject;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

@Inject
private Mailing mail;

public void doGet(HttpServletRequest request, HttpServletResponse response) 
			           throws ServletException, java.io.IOException {

try
{	    

     UserBean user = new UserBean();
     user.setUserName(request.getParameter("un"));
     user.setPassword(request.getParameter("pw"));

     user = UserDAO.login(user);
	   		    
     if (user.isValid())
     {
          
    	 HttpSession session = request.getSession(true);
    	  user.setSesionHttp(session);
          session.setAttribute("userBean",user); 
          response.sendRedirect("bingo.jsp"); //logged-in page 
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