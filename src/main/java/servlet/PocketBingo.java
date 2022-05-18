package servlet;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.ObjectChangeListener;

/**
 *
 * @author hormigueras
 */

public class PocketBingo implements Serializable {
    
    public PocketBingo() {
    	//this.mapaUsuarioCarton.put("super",0);
    	//AsignaPreferCarton("super", 0);
    }
	/**
	 * 
	 */
	private static final long serialVersionUID = -5953174463423977451L;
	private int numeroOrden = 0;
    private String idPlayer = "";
 
    
    private Vector<Integer> numerosCalled = new Vector<Integer>() ;
    private int lastNumber;
    private int newBola;
    private String reasonInterrupt;
    private boolean secuenciaAcabada=true;

    private boolean lineaCantada=false;
    private boolean bingoCantado=false;
    private String IdState = "Finalized";
    private String precioCarton="1";
    private int nCartonesElectronicos=0;


    private String porcientoLinea="20";//Por defecto
    private String porcientoBingo="80";
    private String porcientoCantaor="0";
    private int delay = 1500;
    private String nCartonesManuales="0"; 

    //Mapa de Asignacion premios manuales cantados
    //          nombre usuario,Objeto PremiosUsuario
    private Map<String,PremiosUsuario>  cartonesManualesPremiados= new LinkedHashMap<>();
    
    //Mapa de Asignacion cartones a usuarios manuales
    private Map<String,Integer>  mapaUsuarioCarton= new LinkedHashMap<>();
    
    //Mapa de preferencias cartones de usuario
    private Map<String,Integer>  mapaPrefsCarton= new LinkedHashMap<>();
    
    //---------------------------------------------------------------------
    
    public void AsignaPreferCarton(String usuario, int nCartones){
    	mapaPrefsCarton.put(usuario, nCartones);
    }
    public Vector<Carton> getCartonesManualesPremiados(String usuario) {
    	if(this.cartonesManualesPremiados==null)cartonesManualesPremiados= new LinkedHashMap<>();
    	if(this.cartonesManualesPremiados.get(usuario)==null) {
    		return null;
    	}else {
    		return this.cartonesManualesPremiados.get(usuario).getPremios();
    	}
    
    }
    public void deleteCartonesManualesPremiados(String user) {
    	this.cartonesManualesPremiados.remove(user);
    }
    public boolean estaJugandoAManual(String user) {
    	if (this.mapaUsuarioCarton.get(user)==null) {
    		return false;
    	}else {
    		return true;
    	}
    }
    public void AsignaNCartonesA(String usuario, int nCartones){
    	mapaUsuarioCarton.put(usuario, nCartones);
    }
    public void registraCartonPremiado(PeticionPremio petPremio, Carton carton, float valorPremiado, int numeroCartonesEnJuego) {
    	//Pendiente aqui
    	String usuario = petPremio.getUserbean().getUsername();
    	PremiosUsuario pu = this.cartonesManualesPremiados.get(usuario);
    	if(pu==null)pu=new PremiosUsuario();
    	Vector<Carton> v = pu.getPremios();
    	if(v==null)v = new Vector<Carton>(); 
  
			if(v.contains(carton)) {
					if(petPremio.getPremio().equals("Linea"))v.get(v.indexOf(carton)).setLineaCantado(true);
					if(petPremio.getPremio().equals("Bingo"))v.get(v.indexOf(carton)).setBingoCantado(true);
					float valorSuma =v.get(v.indexOf(carton)).getPremiosAcumulados()+valorPremiado;
					v.get(v.indexOf(carton)).setPremiosAcumulados(valorSuma);
					System.out.println("Si habia vector,se va a modificar un carton con premio de:"+valorSuma);
					pu.setCantidadJugada(numeroCartonesEnJuego);
					
			}else {
				
				if(petPremio.getPremio().equals("Linea"))carton.setLineaCantado(true);
				if(petPremio.getPremio().equals("Bingo"))carton.setBingoCantado(true);
				float valorSuma = carton.getPremiosAcumulados()+valorPremiado;
				System.out.println("Si habia vector, pero no el carton.Se va a añadir un carton con premio de "+valorSuma);
				carton.setPremiosAcumulados(valorSuma);
				v.add(carton);
				pu.setCantidadJugada(numeroCartonesEnJuego);
				pu.setPremios(v);
				this.cartonesManualesPremiados.put(usuario, pu);
			}
    	
    }

    public int dimePreferCartonDe(String usuario){
    	return mapaPrefsCarton.get(usuario);
    	
    }
    public void resetCartonesUsuariosOffLine(){
     	this.ajustarCajaPorJugadaFinalizada();

    	//poner a 0 mapaPremosmaualesCantados y inicializar
	    Set<String> jugadoresAhora = this.mapaUsuarioCarton.keySet();
	    Iterator<String> itJugadoresAhora = jugadoresAhora.iterator();
	    while(itJugadoresAhora.hasNext()) {
	    	mapaUsuarioCarton.put(itJugadoresAhora.next(), 0);
	    }
    	cartonesManualesPremiados= new LinkedHashMap<>();
        this.setBingoCantado(false);
        this.setLineaCantada(false);
    }
    private void traspasoDeCartonesASuper(String usuario) {
    	
    	int numeroCartonesSocio = this.dimeCartonesDe(usuario);
    	int numeroCartonesSuper = this.dimeCartonesDe("super");
    	AsignaNCartonesA("super",numeroCartonesSuper+numeroCartonesSocio);
    }
    private void ajustarCajaPorJugadaFinalizada() {
		//Modo 
    	if(this.isBingoCantado())return;
	  	float xValorADescontar = 0;

	    float xCuantoHeGanado = 0;
	   
	    Set<String> jugadoresAhora = this.mapaUsuarioCarton.keySet();
	    Iterator<String> itJugadoresAhora = jugadoresAhora.iterator();
	    while(itJugadoresAhora.hasNext()) {
	    	String usuario = itJugadoresAhora.next();
	    	float xCuantoHasJugado = 0;
	    	xCuantoHeGanado = 0;
	    	Vector<Carton> cartonesPremiados = this.getCartonesManualesPremiados(usuario);
	    	if(!(cartonesPremiados==null)){
	    		//xCuantoHasJugado = this.cartonesManualesPremiados.get(usuario).getCantidadJugada()*new Float(this.getPrecioCarton());;
	    		Iterator<Carton> itVectorCarton = cartonesPremiados.iterator();
	    		while(itVectorCarton.hasNext()) {
	    			Carton c = itVectorCarton.next();
	  			
	    			xCuantoHeGanado =+ c.getPremiosAcumulados();
	    		}
	    		
	    	}else {
	    		
	    	}
	    	xCuantoHasJugado= this.dimeCartonesDe(usuario)*new Float(this.getPrecioCarton());;
	    	xValorADescontar = xCuantoHasJugado - xCuantoHeGanado;
	    	//	Saldo de caja Actual=
	    	UtilDatabase udatabase = new UtilDatabase();
	    	float saldoActualUser = new Float(udatabase.consultaSQLUnica("Select Saldo From usuarios Where User='"+usuario+"'"));
	    	//	///////////////////////////////////////////////////
	    	float saldoActualizadoUser = saldoActualUser + xValorADescontar;
	    	/////////////////////////////////////////////////////
	    	DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
	    	simbolos.setDecimalSeparator('.');
	    	DecimalFormat formateador = new DecimalFormat("#######.##",simbolos);
    
	    	String Consulta = "UPDATE usuarios SET Saldo = "+ formateador.format(saldoActualizadoUser )+ " Where User='"+usuario+"'" ;
    
	    	if(UtilDatabase.updateQuery(Consulta)>0) {	           			        
	    		System.out.println("Cantidad ajustada Caja para jugador Elec. :'"+usuario+" Ha sido :"+xValorADescontar);
	    		System.out.println("El valor de caja ahora es :"+saldoActualizadoUser);
	    	}
	    	this.cartonesManualesPremiados.remove(usuario);
	    	mapaUsuarioCarton.put(usuario, 0);
	    }	

    }
    private void ajustarCajaPorJugadaFinalizada(String usuario) {
		//Modo 
    	if(this.isBingoCantado())return;
	  	float xValorADescontar = 0;

	    float xCuantoHeGanado = 0;
	   

	    	float xCuantoHasJugado = 0;
	    	xCuantoHeGanado = 0;
	    	Vector<Carton> cartonesPremiados = this.getCartonesManualesPremiados(usuario);
	    	if(!(cartonesPremiados==null)){
	    		//xCuantoHasJugado = this.cartonesManualesPremiados.get(usuario).getCantidadJugada()*new Float(this.getPrecioCarton());;
	    		Iterator<Carton> itVectorCarton = cartonesPremiados.iterator();
	    		while(itVectorCarton.hasNext()) {
	    			Carton c = itVectorCarton.next();
	  			
	    			xCuantoHeGanado =+ c.getPremiosAcumulados();
	    		}
	    		
	    	}else {
	    		
	    	}
	    	xCuantoHasJugado= this.dimeCartonesDe(usuario)*new Float(this.getPrecioCarton());;
	    	xValorADescontar = xCuantoHasJugado - xCuantoHeGanado;
	    	//	Saldo de caja Actual=
	    	UtilDatabase udatabase = new UtilDatabase();
	    	float saldoActualUser = new Float(udatabase.consultaSQLUnica("Select Saldo From usuarios Where User='"+usuario+"'"));
	    	//	///////////////////////////////////////////////////
	    	float saldoActualizadoUser = saldoActualUser + xValorADescontar;
	    	/////////////////////////////////////////////////////
	    	DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
	    	simbolos.setDecimalSeparator('.');
	    	DecimalFormat formateador = new DecimalFormat("#######.##",simbolos);
    
	    	String Consulta = "UPDATE usuarios SET Saldo = "+ formateador.format(saldoActualizadoUser )+ " Where User='"+usuario+"'" ;
    
	    	if(UtilDatabase.updateQuery(Consulta)>0) {	           			        
	    		System.out.println("Cantidad ajustada Caja para jugador Elec. :'"+usuario+" Ha sido :"+xValorADescontar);
	    		System.out.println("El valor de caja ahora es :"+saldoActualizadoUser);
	    	}
	    	this.cartonesManualesPremiados.remove(usuario);
	    	mapaUsuarioCarton.put(usuario, 0);
	    	

    }
	public int dimeCartonesDe(String usuario){
    	return mapaUsuarioCarton.get(usuario);
    	
    }
    public int calculaNcartonesManuales(){
    	int nCM=0;
    	Collection<Integer> c = mapaUsuarioCarton.values();
    	Iterator<Integer> itC = c.iterator();
    	while(itC.hasNext()){
    		nCM+=itC.next();
    	}
    	this.setnCartonesManuales(""+nCM);
    	return nCM;
    }
    
	public Vector<String> getUsuariosManualesEnJuego() {
	
		Set<String> setUsuarios = mapaUsuarioCarton.keySet();
		Iterator<String> itUsuarios = setUsuarios.iterator();
		Vector<String> v = new Vector<String>();
		while(itUsuarios.hasNext()){
			v.add(itUsuarios.next());
		}
		return v;
	}
	public Vector<String> getUsuariosManualesEnJuegoConCartones() {
		
		Set<String> setUsuarios = mapaUsuarioCarton.keySet();
		Iterator<String> itUsuarios = setUsuarios.iterator();
		Vector<String> v = new Vector<String>();
		while(itUsuarios.hasNext()){
			String usuario = itUsuarios.next();
			if(mapaUsuarioCarton.get(usuario)>0)v.add(usuario);
			
		}
		return v;
	}	
	

	public void removerUsuariosManualesEnJuego(String sEnJuego) {
		//No se pueden quitar en juego
		//this.traspasoDeCartonesASuper(sEnJuego);
		String stateSala = this.getIdState();
		if(stateSala.equals("Finalized")) {
			if(!(this.mapaUsuarioCarton.get(sEnJuego)==null)) {
				this.ajustarCajaPorJugadaFinalizada(sEnJuego);
				this.mapaUsuarioCarton.remove(sEnJuego);
			}
			
		}
		//this.removePremioJugador(sEnJuego);
	}
	public void añadirUsuariosManualesEnJuego(String sEnJuego) {
		this.mapaUsuarioCarton.put(sEnJuego, 0);
	}
	public void removePremioJugador(String jugador) {
		this.cartonesManualesPremiados.remove(jugador);
	}
	
	
    
    /* 
     * IdState :
     *  NewGame,Linea,Bingo,Started,Finalized.
     */
    public void initPocket(){
    	numeroOrden=0;
    	idPlayer="";
    	this.resetNumerosCalled();
       	lastNumber=0;
    	newBola=0;
    	reasonInterrupt="";
    	secuenciaAcabada=true;
    	lineaCantada=false;
    	bingoCantado=false;
    	IdState="NewGame";
        //BORRA LOS CARTONES MANUALES DE LOS JUGADORES PRESENTES es competencia del proceso de
    	//resetear cartones
    
    	//cartonesManualesPremiados= new LinkedHashMap<>();
    	//super esta presente,sin cartones
    	//this.mapaUsuarioCarton.put("super",0);
    	//AsignaPreferCarton("super", 0);
    }
   
    
    public int getnCartonesElectronicos() {
		return nCartonesElectronicos;
	}
	public void setnCartonesElectronicos(int nCartonesElectronicos) {
		this.nCartonesElectronicos = nCartonesElectronicos;
	}
    public boolean isLineaCantada() {
		return lineaCantada;
	}
	public void setLineaCantada(boolean lineaCantada) {
		this.lineaCantada = lineaCantada;
	}
	public void resetNumerosCalled(){
		numerosCalled= new Vector<Integer>();
	}
	public boolean isBingoCantado() {
		return bingoCantado;
	}
	public void setBingoCantado(boolean bingoCantado) {
		this.bingoCantado = bingoCantado;
	}
    public boolean getSecuenciaAcabada(){
        return secuenciaAcabada;
    }
    public String getIdState() {
		
    	return IdState;
	}
	public void setIdState(String idState) {
		IdState = idState;
	}
	public void setSecuenciaAcabada(boolean secAca){
        this.secuenciaAcabada = secAca;
    }
    public int getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(int numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }

    public List<Integer> getNumerosCalled() {
        return numerosCalled;
    }

    public void addNumerosCalled(int addNumero) {
        numerosCalled.add(addNumero);
    }

    public int getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(int lastNumber) {
        this.lastNumber = lastNumber;
    }

    public int getNewBola() {
        return newBola;
    }

    public void setNewBola(int newBola) {
        this.newBola = newBola;
    }
	public String getReasonInterrupt() {
		return reasonInterrupt;
	}
	public void setReasonInterrupt(String reasonInterrupt) {
		this.reasonInterrupt = reasonInterrupt;
	}
	public String getPrecioCarton() {
		return precioCarton;
	}
	public void setPrecioCarton(String precioCarton) {
		this.precioCarton = precioCarton;
	}
	public String getnCartonesManuales() {
		
		return nCartonesManuales;
	}
	public void setnCartonesManuales(String nCartones) {
		this.nCartonesManuales = nCartones;
	}
	public String getPorcientoLinea() {
		return porcientoLinea;
	}
	public void setPorcientoLinea(String porcientoLinea) {
		this.porcientoLinea = porcientoLinea;
	}
	public String getPorcientoBingo() {
		return porcientoBingo;
	}
	public void setPorcientoBingo(String porcientoBingo) {
		this.porcientoBingo = porcientoBingo;
	}
	public String getPorcientoCantaor() {
		return porcientoCantaor;
	}
	public void setPorcientoCantaor(String porcientoCantaor) {
		this.porcientoCantaor = porcientoCantaor;
	}
	public int getDelay() {
		return delay;
	}
	public void setDelay(int delay) {
		this.delay = delay;
	}


}
