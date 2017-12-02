package servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

/**
 *
 * @author javier Boga
 */
@WebServlet("/GestorComprasBonus")
public class GestionComprasBonos extends HttpServlet {
    @Inject
    private GestorSessions gestorSesions;
    
    //private UserBean user = null;

    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSessionAComparar = req.getSession(false);
        UserBean user_ = null;
        PocketBingo pocketBingoSala= null;
        //Identificacion de la sesion de usuario//
        if(httpSessionAComparar!=null){
            String usuario = req.getParameter("usuario");
            String sala = req.getParameter("sala");
            int nBonus = new Integer(req.getParameter("nBonus"));

            user_= gestorSesions.dameUserBeansPorUser(usuario, "jugador", httpSessionAComparar.getId());
            if(user_==null){
                String mensaje="Error,Usuario no esta registrado o \nno esta On Line ";
                resp.getWriter().print(mensaje);
                return;
            }
            
            pocketBingoSala= gestorSesions.getJugadasSalas(user_.getSalonInUse());
 
            //Comprobacion estado Pocket Bingo en "Finalized"
 
 
           	//realizarCompraTransaccion();
           	registroPeticionCompraCartones(user_,nBonus,sala);

        	 
         	resp.setContentType("application/json");
         	resp.setCharacterEncoding("UTF-8");           	
            String mensaje="!Solicitud de peticion compra cartones, efectuada¡";
			 String json = new Gson().toJson(mensaje);	    	
			 resp.getWriter().write(json);
//
       		System.out.println(gestorSesions.getPeticionBonus().toString()+"Solicitud ok");
            
        }
    }
    private void registroPeticionCompraCartones(UserBean myUser, float nBonus, String sala){
    	//Se puede incluir en un vector en gestorSession, las peticiones de compra Bonus, para cuando pueda el super
    	//atender una lista de peticiones con usuario,nBonus de compra confirmando entonces la compra efectuada
    	PeticionBonus pbonus = new PeticionBonus();
    	pbonus.setSala(sala);
    	pbonus.setUserbean(myUser);
    	pbonus.setBonus(nBonus);
        Calendar cal = Calendar.getInstance();
        Long hora =cal.getTimeInMillis();   	
    	gestorSesions.registraPeticionBonus(hora,pbonus);
    	
    }

    private boolean realizarCompraBonos(float precioCompra, UserBean myUser){
        boolean okeyCompra=false;
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        //Formateador de datos decimales. Limitado a dos digitos.
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#######.##",simbolos);
        ///////////////////////////////////////////////////////
        float saldoRestante = myUser.getSaldo() + precioCompra;
        ///////////////////////////////////////////////////////
        String consultaUsuario = "UPDATE usuarios SET Saldo = "+formateador.format(saldoRestante)+" WHERE User = '"+myUser.getUsername()+"'";
        String consultaCaja = "UPDATE usuarios SET Saldo = "+formateador.format(saldoRestante)+" WHERE User = '"+myUser.getUsername()+"'";


        ///// TRansaccion sobre Saldo y Caja
        UtilDatabase udatabase = new UtilDatabase();
        int result=udatabase.updateQueryCompraBonus(consultaUsuario,consultaCaja);
        if(result>0){
        	myUser.setSaldo(new  Float(formateador.format(saldoRestante)));
        	//Actualizar saldo Caja
        	//Enviar un email de confirmacion
        	okeyCompra=true;
        }
        
        return okeyCompra;
    }


}
