package servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

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
        HttpSession hS = req.getSession(false);
        UserBean user_ = null;

        String comando = req.getParameter("comando");
        //Identificacion de la sesion de usuario//
        if(hS!=null ){
        	if(comando.equals("PeticionCompraBonus")){
        		String usuario = req.getParameter("usuario");
        		String sala = req.getParameter("sala");
        		String sBonus = req.getParameter("nBonus");
        		if(sBonus.contains(",")){
        			sBonus=sBonus.replaceAll(",", ".");
        			System.out.println("sBonus="+sBonus);
        		}
        
        		float nBonus = new Float(sBonus);

        		user_= gestorSesions.dameUserBeansPorUser(usuario, "jugador", hS.getId());
        		if(user_==null){//Son jugadores offline

        			/*String mensaje="Error,Usuario no esta registrado o \nno esta On Line ";
        			String json = new Gson().toJson(mensaje);	    	
            		resp.getWriter().write(json);
        			return;*/
        		UserBean ubOffLine = new UserBean();
        		ubOffLine.setUsername(usuario);
        		
        		user_=ubOffLine;
        		}

        		//realizarCompraTransaccion();
        		registroPeticionCompraBonus(user_,nBonus,sala);
  	 
        		resp.setContentType("application/json");
        		resp.setCharacterEncoding("UTF-8");           	
        		String mensaje="!Solicitud de peticion compra bonus, efectuada¡";
        		String json = new Gson().toJson(mensaje);	    	
        		resp.getWriter().write(json);
        		System.out.println(gestorSesions.getPeticionBonus().toString()+"Solicitud ok(compra Bonus):"+user_+",Bonus:"+nBonus);
        	}
        	if(comando.equals("PeticionLiquidacionBonus")){
        		String usuario = req.getParameter("usuario");
        		String sala = req.getParameter("sala");
        		String sBonus = req.getParameter("nBonus");
        		if(sBonus.contains(",")){
        			sBonus=sBonus.replaceAll(",", ".");
        			System.out.println("sBonus="+sBonus);
        		}
        
        		float nBonus = new Float(sBonus);

        		user_= gestorSesions.dameUserBeansPorUser(usuario, "jugador", hS.getId());
        		if(user_==null){//Son jugadores offline

        			/*String mensaje="Error,Usuario no esta registrado o \nno esta On Line ";
        			String json = new Gson().toJson(mensaje);	    	
            		resp.getWriter().write(json);
        			return;*/
        		UserBean ubOffLine = new UserBean();
        		ubOffLine.setUsername(usuario);
        		
        		user_=ubOffLine;
        		}
        		//Comprobacion valor solicitado no excede saldo de usuario
        		String mensaje="";
        		float vSolicitado = new Float(sBonus);
        		UtilDatabase udatabase = new UtilDatabase();
        		float saldoActual= new Float(udatabase.consultaSQLUnica("Select Saldo From usuarios Where User='"+ user_.getUsername()+"'"));
        		if((saldoActual-vSolicitado)<0){
        			mensaje="!No es posible registrar un valor mayor que Saldo¡";
        		}else{
            		//realizarCompraTransaccion();
            		registroPeticionLiquidacionBonus(user_,nBonus,sala);
        			mensaje="!Solicitud de liquidacion Bonus, REGISTRADA¡";
        			System.out.println(gestorSesions.getPeticionLiquidacionBonus().toString()+"Solicitud ok(Liquidacion Bonus):"+user_+",Bonus:"+nBonus);
        		}
        		resp.setContentType("application/json");
        		resp.setCharacterEncoding("UTF-8");           	
        		
        		String json = new Gson().toJson(mensaje);	    	
        		resp.getWriter().write(json);
        		
        	}        	
        	
           	if(comando.equals("VolcarListadoCompraBonus")){ 
           		String sala = req.getParameter("sala");
           		
        		resp.setContentType("application/json");
        		resp.setCharacterEncoding("UTF-8");           	
        		String mensaje = volcarRegistroPeticionesBonus(sala);
        		String json = new Gson().toJson(mensaje);	    	
        		resp.getWriter().write(json);
        		//System.out.println(gestorSesions.getPeticionBonus().toString()+"Volcado hecho:\n"+mensaje);           		
           	}
           	if(comando.equals("VolcarListadoliquidacionBonus")){ 
           		String sala = req.getParameter("sala");
           		
        		resp.setContentType("application/json");
        		resp.setCharacterEncoding("UTF-8");           	
        		String mensaje = volcarRegistroPeticionesLiquidacionesBonus(sala);
        		String json = new Gson().toJson(mensaje);	    	
        		resp.getWriter().write(json);
        		//System.out.println(gestorSesions.getPeticionBonus().toString()+"Volcado hecho:\n"+mensaje);           		
           	}          	
           	           	
           	if(comando.equals("RealizarPagoCompraBonus")){ 
           		if(hS.getAttribute("usuario")==null){
        			String mensaje="Error,Usuario no esta registrado o \nno esta On Line ";
        			resp.getWriter().print(mensaje);
        			return;
        		}           		
           		String sala = req.getParameter("sala");
           		String idBonus = req.getParameter("idBonus");
           		
        		resp.setContentType("application/json");
        		resp.setCharacterEncoding("UTF-8");           	
        		String mensaje = realizarPagoCompraBonus(idBonus);
        		String json = new Gson().toJson(mensaje);	    	
        		resp.getWriter().write(json);
        		triggerRefreshDatos(sala);
        		//System.out.println(gestorSesions.getPeticionBonus().toString()+"Volcado hecho:\n"+mensaje);           		
           	}
           	if(comando.equals("RealizarLiquidacionBonus")){ 
           		if(hS.getAttribute("usuario")==null){
        			String mensaje="Error,Usuario no esta registrado o \nno esta On Line ";
        			resp.getWriter().print(mensaje);
        			return;
        		}           		
           		String sala = req.getParameter("sala");
           		String idBonus = req.getParameter("idBonus");
           		
        		resp.setContentType("application/json");
        		resp.setCharacterEncoding("UTF-8");           	
        		String mensaje = realizarLiquidacionBonus(idBonus);
        		String json = new Gson().toJson(mensaje);	    	
        		resp.getWriter().write(json);
        		triggerRefreshDatos(sala);
        		//System.out.println(gestorSesions.getPeticionBonus().toString()+"Volcado hecho:\n"+mensaje);           		
           	}           	
           	if(comando.equals("BorrarRegistroPeticionBonus")){
           		String idBonus = req.getParameter("idBonus");
           		Long lIdtransaccion = new Long(idBonus);
           		gestorSesions.borraPeticionBonus(lIdtransaccion);
        		String mensaje = "Linea registro de Bonus borrada para id="+idBonus;
        		String json = new Gson().toJson(mensaje);	    	
        		resp.getWriter().write(json);          		
           	}
           	if(comando.equals("BorrarRegistroPeticionLiquidacionBonus")){
           		String idBonus = req.getParameter("idBonus");
           		Long lIdtransaccion = new Long(idBonus);
           		gestorSesions.borraPeticionLiquidacionBonus(lIdtransaccion);
        		String mensaje = "Linea registro de peticion Liquidacion Bonus borrada para id="+idBonus;
        		String json = new Gson().toJson(mensaje);	    	
        		resp.getWriter().write(json);          		
           	}          	

        }
    }
    public String volcarRegistroPeticionesBonus(String sala){
    	Set<Long> keysHora =gestorSesions.getPeticionBonus();
    	Iterator<Long> itLongHora = keysHora.iterator();
    	UtilDatabase udatabase = new UtilDatabase();
    	String volcado="<table width='97%' border='1' align='center' class='Tabla'><tr bgcolor='#003399'>";
    	volcado+="<td width=\"50%\" height=\"72\" class=\"Cabecera\" >Peticiones compra Bonus Pendientes</td>";
    	volcado+="<td width=\"25%\"><p align=\"center\"><input type=\"button\" width=\"30px\" name=\"Mostrar Peticiones\" value=\"Mostrar Peticiones\" onclick=\"volcarPeticionesBonus()\"></p></td>";
    	volcado+="<td ><label class=\"soloCaja\" >CAJA : </label ><input  class=\"Caja\" type=\"text\" value='"+udatabase.consultaSQLUnica("Select SaldoCaja From caja")+"'/p></td></tr>";
    	volcado+="<tr><td class=\"Cabecera\">Usuario</td><td  class=\"Cabecera\">Bonus</td> <td  class=\"Cabecera\">Checking</td></tr>";
    	while(itLongHora.hasNext()){
    		
    		Long hora = (Long)itLongHora.next();
    		PeticionBonus pBonus = gestorSesions.getPeticionBonus(hora);
    		if(pBonus.getSala().equals(sala)){
    			UserBean uBean = pBonus.getUserbean();
    			float bonus = pBonus.getBonus();
    			volcado+="<tr class='fondosLineas'><td  class='otro'><label class='AIzquierdas'>"+uBean.getUsername()+"</label></td>";
    		    volcado+="<td class='otro'><label class='AIzquierdas'>"+pBonus.getBonus()+"</label></td>";
    		    volcado+="<td class='otro'><input type='button' value='Pagado' onclick=\"realizarPagoBonus('"+hora+"')\"><label> </label><input type='button' value='Eliminar' onclick=\"borrarRegistroPeticionBonus('"+hora+"')\"></td></tr>";
    		
    		}
    	}
    	return volcado;
    	
    }
    public String volcarRegistroPeticionesLiquidacionesBonus(String sala){
    	Set<Long> keysHora =gestorSesions.getPeticionLiquidacionBonus();
    	Iterator<Long> itLongHora = keysHora.iterator();
    	UtilDatabase udatabase = new UtilDatabase();
    	String volcado="<table width='97%' border='1' align='center' class='Tabla'><tr bgcolor='#003399'>";
    	volcado+="<td width=\"50%\" height=\"72\" class=\"Cabecera\" >Peticiones Liquidacion Bonus Pendientes</td>";
    	volcado+="<td width=\"25%\"><p align=\"center\"><input type=\"button\" width=\"30px\" name=\"Mostrar Peticiones\" value=\"Mostrar Peticiones\" onclick=\"volcarPeticionesBonus()\"></p></td>";
    	volcado+="<td ><label class=\"soloCaja\" >CAJA : </label ><input  class=\"Caja\" type=\"text\" value='"+udatabase.consultaSQLUnica("Select SaldoCaja From caja")+"'/p></td></tr>";
    	volcado+="<tr><td class=\"Cabecera\">Usuario</td><td  class=\"Cabecera\">Bonus</td> <td  class=\"Cabecera\">Checking</td></tr>";
    	while(itLongHora.hasNext()){
    		
    		Long hora = (Long)itLongHora.next();
    		PeticionLiquidacionBonus pLiqBonus = gestorSesions.getPeticionesLiquidacionBonus(hora);
    		if(pLiqBonus.getSala().equals(sala)){
    			UserBean uBean = pLiqBonus.getUserbean();
    			float bonus = pLiqBonus.getBonus();
    			volcado+="<tr class='fondosLineas'><td  class='otro'><label class='AIzquierdas'>"+uBean.getUsername()+"</label></td>";
    		    volcado+="<td class='otro'><label class='AIzquierdas'>"+pLiqBonus.getBonus()+"</label></td>";
	  			volcado+=("<td class='otro'><input type='button' value='Pagado' onclick=\"realizarLiquidacionBonus('"+hora+"')\"><label> </label><input type='button' value='Eliminar' onclick=\"borrarRegistroPeticionLiquidacionBonus('"+hora+"')\"></td></tr>");
    		
    		}
    	}
    	return volcado;
    	
    }    
    private void registroPeticionCompraBonus(UserBean myUser, float nBonus, String sala){
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
    private void registroPeticionLiquidacionBonus(UserBean myUser, float nBonus, String sala){
    	//Se puede incluir en un vector en gestorSession, las peticiones de compra Bonus, para cuando pueda el super
    	//atender una lista de peticiones con usuario,nBonus de compra confirmando entonces la compra efectuada
    	PeticionLiquidacionBonus pbonus = new PeticionLiquidacionBonus();
    	pbonus.setSala(sala);
    	pbonus.setUserbean(myUser);
    	pbonus.setBonus(nBonus);
        Calendar cal = Calendar.getInstance();
        Long hora =cal.getTimeInMillis();   	
    	gestorSesions.registraPeticionLiquidacionBonus(hora,pbonus);
    	
    }    
   

    private String realizarPagoCompraBonus(String idTransaccion){
    	Long lIdtransaccion = new Long(idTransaccion);
    	PeticionBonus pb = gestorSesions.getPeticionBonus(lIdtransaccion);
    	String result="";
    	boolean okeyCompra=false;
        float precioCompra = pb.getBonus();
        UserBean ub = pb.getUserbean();
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        UtilDatabase udatabase = new UtilDatabase();
        //Formateador de datos decimales. Limitado a dos digitos.
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#######.##",simbolos);
        ///////////////////////////////////////////////////////
        float saldoRestanteUser = new Float(udatabase.consultaSQLUnica("Select Saldo From usuarios Where User='"+ ub.getUsername()+"'")) + precioCompra;
        float valorActualCaja = new Float(udatabase.consultaSQLUnica("Select SaldoCaja From caja"));
        float valorFinalCaja = valorActualCaja + precioCompra;
        ///////////////////////////////////////////////////////
        String consultaUsuario = "UPDATE usuarios SET Saldo = "+formateador.format(saldoRestanteUser)+" WHERE User = '"+pb.getUserbean().getUsername()+"'";
        String consultaCaja = "UPDATE caja SET SaldoCaja = "+formateador.format(valorFinalCaja);


        //Transaccion sobre Saldo y Caja
 
        int updates=udatabase.updateQueryCompraBonus(consultaUsuario,consultaCaja);
        if(updates>0){

        	//Actualizar datos de saldo de usuario en tiempo real
        	ub.setSaldo(new  Float(formateador.format(saldoRestanteUser)));

        	//borrar registro Peticion Bonus
        	gestorSesions.borraPeticionBonus(lIdtransaccion);

        	//Actualizar saldo Caja(pendiente)
        	//Enviar un email de confirmacion(Pendiente)
        	result="TransaccionOK para Jugador:" + ub.getUsername() +" (Añadir Saldo:"+ precioCompra+")";
        }
        
        return result;
    }
    private String realizarLiquidacionBonus(String idTransaccion){
    	Long lIdtransaccion = new Long(idTransaccion);
    	PeticionLiquidacionBonus pb = gestorSesions.getPeticionesLiquidacionBonus(lIdtransaccion);
    	String result="";
    	boolean okeyCompra=false;
        float precioLiquidacion = pb.getBonus();
        UserBean ub = pb.getUserbean();
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        UtilDatabase udatabase = new UtilDatabase();
        //Formateador de datos decimales. Limitado a dos digitos.
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#######.##",simbolos);
        ///////////////////////////////////////////////////////
        float saldoRestanteUser = new Float(udatabase.consultaSQLUnica("Select Saldo From usuarios Where User='"+ ub.getUsername()+"'")) - precioLiquidacion;

        float valorActualCaja = new Float(udatabase.consultaSQLUnica("Select SaldoCaja From caja"));
        float valorFinalCaja = valorActualCaja - precioLiquidacion;
        ///////////////////////////////////////////////////////
        String consultaUsuario = "UPDATE usuarios SET Saldo = "+formateador.format(saldoRestanteUser)+" WHERE User = '"+pb.getUserbean().getUsername()+"'";
        String consultaCaja = "UPDATE caja SET SaldoCaja = "+formateador.format(valorFinalCaja);

        
        //Transaccion sobre Saldo y Caja
 
        int updates=udatabase.updateQueryCompraBonus(consultaUsuario,consultaCaja);
        if(updates>0){

        	//Actualizar datos de saldo de usuario en tiempo real
        	ub.setSaldo(new  Float(formateador.format(saldoRestanteUser)));

        	//borrar registro Peticion Bonus
        	gestorSesions.borraPeticionLiquidacionBonus(lIdtransaccion);

        	
        	//Enviar un email de confirmacion(Pendiente)
        	result="TransaccionOK para Jugador:" + ub.getUsername() +" (Saldo liquidado:"+ precioLiquidacion+")";
        }
        
        return result;
    }    
    
	private void triggerRefreshDatos(String salaInUse){
		PocketBingo pb = gestorSesions.getJugadasSalas(salaInUse);
		String precioCarton,porCientoLinea,porCientoBingo,porCientoCantaor;
		precioCarton=pb.getPrecioCarton();
		//Hay que distinguir entre cartones electronicos y manuales
		//EL cuadro de Dialogo debe considerar las dos facetas
		// Por lo tanto debe haber dos variables, uno para cada tipo de faceta de cartones.
		// Implementado ambos tipos de datos
	
		int nCartones = new Integer(pb.calculaNcartonesManuales()) + this.gestorSesions.dameSetCartonesEnJuego(salaInUse).size();
		porCientoLinea=pb.getPorcientoLinea();
		porCientoBingo=pb.getPorcientoBingo();
		porCientoCantaor=pb.getPorcientoCantaor();
		String construirScript="DATOSCARTONES_"+precioCarton+"_"+nCartones+"_"+porCientoLinea+"_"+porCientoBingo+"_"+porCientoCantaor;
		enviarMensajeAPerfil(construirScript,"supervisor");
		enviarMensajeAPerfil("RefreshDatosCartones","jugador");
	}
	private void enviarMensajeAPerfil(String textMessage,String perfil){
	  	try {
			Set<UserBean> myUsersbean = gestorSesions.dameUserBeans(perfil);
			Iterator<UserBean> itBeans= myUsersbean.iterator();
			while (itBeans.hasNext()){
				Session sesionActiva = itBeans.next().getSesionSocket();
				sesionActiva.getBasicRemote().sendText(textMessage);
			}
				//log.info("Enviando desde servidor a navegador:"+textMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
}
}
