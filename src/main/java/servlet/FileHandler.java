package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FileHandler
 */
@WebServlet(description = "Proporciona acceso a ficheros estaticos en el servidor", urlPatterns = { "/FileHandler" })
public class FileHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mimeType = request.getParameter("mimeType");
		String nameFile = request.getParameter("filename");
		
		
		
		String fichero = "/var/lib/openshift/5616505789f5cf812500001d/app-root/data/" +nameFile;
		File file = new File(fichero);
		byte[] fileInBytes = new byte[(int) file.length()];
	    
	    InputStream inputStream = null;
	    try {
	    
	        inputStream = new FileInputStream(file);
	        
	        inputStream.read(fileInBytes);
	        
	    } finally {
	        inputStream.close();
	    }
	    response.setContentType(mimeType);
	    response.setContentLengthLong(file.length());
	    OutputStream os =response.getOutputStream();
		//String envVar = System.getenv("OPENSHIFT_ENV_VAR");
		os.write(fileInBytes);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
