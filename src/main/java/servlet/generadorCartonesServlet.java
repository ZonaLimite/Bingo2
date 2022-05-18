package servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/generadorCartones")
public class generadorCartonesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//HttpSession sesion = req.getSession();
		HashMap totalCartones;
		int cuantos=0;
		String series = req.getParameter("nSeries");
		int numeroSeriesAGenerar = new Integer(series).intValue();//obtenemos valor int
		GeneradorCartones gen = new GeneradorCartones();
		gen.seriesRequeridas(numeroSeriesAGenerar);
		totalCartones= gen.generar();
		cuantos=gen.registraCartones(totalCartones);
		Writer wr = resp.getWriter();
		wr.write(cuantos+"");
		
	}
	

}
