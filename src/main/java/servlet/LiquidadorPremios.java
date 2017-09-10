package servlet;

import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;

public class LiquidadorPremios {

	@Inject
	private GestorSessions gestorSesions;

	static Logger log = Logger.getLogger("LiquidadorPremios");
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
		Map<PeticionPremio,Carton> pilaPremios = gestorSesions.getPilaAnunciaPremios();
		int numeroPremios = pilaPremios.size();
		float precioCarton =  new Float(pb.getPrecioCarton());
		float sumaCaja = precioCarton*numeroPremios;
		float porCientoLinea = new Float(pb.getPorcientoLinea());
		float porCientoBingo = new Float(pb.getPorcientoBingo());
		float porCientoCantaor = new Float(pb.getPorcientoCantaor());
		float sumaTantos = porCientoLinea+porCientoBingo + porCientoCantaor;
		// 
		float xLinea = (new Float((sumaCaja*porCientoLinea)/sumaTantos)/numeroPremios);
		
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
		Map<PeticionPremio,Carton> pilaPremios = gestorSesions.getPilaAnunciaPremios();
		int numeroPremios = pilaPremios.size();
		float precioCarton =  new Float(pb.getPrecioCarton());
		float sumaCaja = precioCarton*numeroPremios;
		float porCientoLinea = new Float(pb.getPorcientoLinea());
		float porCientoBingo = new Float(pb.getPorcientoBingo());
		float porCientoCantaor = new Float(pb.getPorcientoCantaor());
		float sumaTantos = porCientoLinea+porCientoBingo + porCientoCantaor;
		// 
		float xBingo = (new Float((sumaCaja*porCientoBingo)/sumaTantos)/numeroPremios);
		
		if(this.realizarLiquidacionTransaccion(xBingo, user)){
			premio = xBingo;
		}
				
		return premio;
		//float xBingo = new Float((sumaCaja*porCientoBingo)/sumaTantos);
		//float zCantaor = new Float((sumaCaja*porCientoCantaor)/sumaTantos);
		

	}	
	
    private boolean realizarLiquidacionTransaccion(float valorPremio, UserBean myUser){
        boolean okeyCompra=false;
        float saldoRestante = myUser.getSaldo() + valorPremio;
        String Consulta = "UPDATE usuarios SET Saldo = "+ saldoRestante+" WHERE User = '"+myUser.getUsername()+"'";
        log.info(Consulta);
        int result=UtilDatabase.updateQuery(Consulta);
        if(result>0){
        	myUser.setSaldo(saldoRestante);
        	okeyCompra=true;
        }
        
        return okeyCompra;
    }
}
