package servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;




public class LiquidadorPremio {
	static Logger log = Logger.getLogger("LiquidadorPremios");


	private GestorSessions gestorSesions;
	
	Map<PeticionPremio, Carton> pilaAnunciaPremios;
	
	public LiquidadorPremio(GestorSessions gs){
		this.gestorSesions=gs;
	}
    public boolean liquidacionPremios(String sala){
		//Tratamiento comprobacion peticiones premios
    	boolean hayPremios=false;
    	pilaAnunciaPremios= gestorSesions.getPilaAnunciaPremios(sala);
		Set<PeticionPremio> userBeansPremiados = pilaAnunciaPremios.keySet();
		Iterator<PeticionPremio> itPremiados = userBeansPremiados.iterator();
		log.info("Liquidando premios ... tamaño en Pila("+userBeansPremiados.size()+")");
		while(itPremiados.hasNext()){
			log.info("En el iterador hay objetos premio");
			PeticionPremio pp =  itPremiados.next();
			UserBean ubPremiado =pp.getUserbean();
			
			if(ubPremiado.getSalonInUse().equals(sala)){
				Carton carton  = pilaAnunciaPremios.get(pp);
				try {
					//Si la transaccion de liquidacion se completa. lo anunciamos
					float premioCobrado = this.saldarPremio(sala, ubPremiado, pp.getPremio());
					if(premioCobrado>0){
						ubPremiado.getSesionSocket().getBasicRemote().sendText("RefreshDatosCartones");
						ubPremiado.getSesionSocket().getBasicRemote().sendText("PremioLiquidado");
						ubPremiado.getSesionSocket().getBasicRemote().sendText("!Premio "+pp.getPremio()+"("+premioCobrado+" €)¡ Carton:"+carton.getnRef()+"\n ! Bien " +pp.getUserbean().getUsername()+" ¡");
					}
 					
					hayPremios=true;
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(4500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		gestorSesions.borrarListaPeticionPremios(sala);
		gestorSesions.borrarListaPremiosLiquidados(sala);
		log.info("Hay premios :" + hayPremios);
		return hayPremios;
    }
	public float saldarPremio(String sala, UserBean user, String tipoPremio){
		float valorPremio=0;
		if(tipoPremio.equals("Linea")){
			valorPremio = this.saldarPremioLinea(sala, user);
		}
		if(tipoPremio.equals("Bingo")){
			valorPremio = this.saldarPremioBingo(sala, user);
		}
		return valorPremio;
	}
	private float saldarPremioLinea(String sala,UserBean user){
		float premio = 0;//
		PocketBingo pb = gestorSesions.getJugadasSalas(sala);
		//Lo cogemos de aqui
		int cartonesAutomaticos = gestorSesions.dameSetCartonesEnJuego(sala).size();
		int numeroCartonesEnJuego = cartonesAutomaticos + new Integer(pb.getnCartonesManuales());
		Map<PeticionPremio,Carton> pilaPremios = gestorSesions.getPilaAnunciaPremios(sala);
		int numeroPremios = pilaPremios.size();
		log.info("Numero de premios (saldar premio Linea):"+numeroPremios);
		float precioCarton =  new Float(pb.getPrecioCarton());
		float sumaCaja = precioCarton*numeroCartonesEnJuego;
		float porCientoLinea = new Float(pb.getPorcientoLinea());
		float porCientoBingo = new Float(pb.getPorcientoBingo());
		float porCientoCantaor = new Float(pb.getPorcientoCantaor());
		float sumaTantos = porCientoLinea+porCientoBingo + porCientoCantaor;
		// 
		float xLinea = (new Float(((sumaCaja*porCientoLinea)/sumaTantos))/numeroPremios);
		if(user.getUsername().contains("Carton"))return xLinea;
		if(this.realizarLiquidacionTransaccion(xLinea, user)){
			premio = xLinea;
		}
				
		return premio;
		//float xBingo = new Float((sumaCaja*porCientoBingo)/sumaTantos);
		//float zCantaor = new Float((sumaCaja*porCientoCantaor)/sumaTantos);
		

	}
	
	private float saldarPremioBingo(String sala,UserBean user){
		float premio = 0;
		PocketBingo pb = gestorSesions.getJugadasSalas(sala);
		int cartonesAutomaticos = gestorSesions.dameSetCartonesEnJuego(sala).size();
		int numeroCartonesEnJuego = cartonesAutomaticos + new Integer(pb.getnCartonesManuales());
		Map<PeticionPremio,Carton> pilaPremios = gestorSesions.getPilaAnunciaPremios(sala);
		int numeroPremios = pilaPremios.size();
		float precioCarton =  new Float(pb.getPrecioCarton());
		float sumaCaja = precioCarton*numeroCartonesEnJuego;
		float porCientoLinea = new Float(pb.getPorcientoLinea());
		float porCientoBingo = new Float(pb.getPorcientoBingo());
		float porCientoCantaor = new Float(pb.getPorcientoCantaor());
		float sumaTantos = porCientoLinea+porCientoBingo + porCientoCantaor;
		// 
		
		float xBingo = (new Float(((sumaCaja*porCientoBingo)/sumaTantos))/numeroPremios);
		if(user.getUsername().contains("Carton"))return xBingo;
		if(this.realizarLiquidacionTransaccion(xBingo, user)){
			premio = xBingo;
		}
		log.info("Valor premio:"+premio);		
		return premio;
		//float xBingo = new Float((sumaCaja*porCientoBingo)/sumaTantos);
		//float zCantaor = new Float((sumaCaja*porCientoCantaor)/sumaTantos);
		

	}	
	
    private boolean realizarLiquidacionTransaccion(float valorPremio, UserBean myUser){
        boolean okeyCompra=false;
        UtilDatabase udatabase = new UtilDatabase();
        float saldoActual = new Float(udatabase.consultaSQLUnica("Select Saldo from usuarios Where User = '"+myUser.getUsername()+"'"));
        /////////////////////////////////////////////////////
        float saldoRestante = saldoActual + valorPremio;
        /////////////////////////////////////////////////////
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#######.##",simbolos);
        
        String Consulta = "UPDATE usuarios SET Saldo = "+ formateador.format(saldoRestante)+" WHERE User = '"+myUser.getUsername()+"'";
        log.info(Consulta);
        int result=UtilDatabase.updateQuery(Consulta);
        if(result>0){
        	myUser.setSaldo(new Float(formateador.format(saldoRestante)));
        	okeyCompra=true;
        }
        
        return okeyCompra;
    }
}
    

