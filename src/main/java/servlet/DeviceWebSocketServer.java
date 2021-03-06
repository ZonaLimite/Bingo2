package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//import org.jboss.logging.Logger;


@ServerEndpoint(value="/sala1",configurator=UserAwareConfigurator.class)
public class DeviceWebSocketServer {
static final Logger log = Logger.getLogger("MyLogger");
 
@Inject
private GestorSessions gestorSesions;

@Resource
private ManagedThreadFactory threadFactory;

private PocketBingo pb; 
private Thread hilo3 = null;
private Runnable3 runnable3;//
private Session mySesion;//
private int delay=0;//ms
private String salaInUse;
private String myPerfil;
private UserBean userBean;
@OnOpen
    public void open(Session session, EndpointConfig config) {
	this.mySesion=session;
	String usuario = (String)config.getUserProperties().get("usuario");
	String perfil = (String)config.getUserProperties().get("perfil");
	String idHttpSession = (String)config.getUserProperties().get("idHttpSession");
	userBean = gestorSesions.dameUserBeansPorUser(usuario,perfil,idHttpSession);//
	userBean.setSesionSocket(session);
	//userBean.setStatusPlayer("playingBingo");
	perfil = userBean.getPerfil();
	this.myPerfil = perfil;
	salaInUse = userBean.getSalonInUse();
	HttpSession userHttpSession = userBean.getSesionHttp();
	String idSesionHttp = userHttpSession.getId();
	log.info("Abierta Session WebSocket:"+ session.getId() + "del Usuario "+userBean.getUsername()+" ,perfil="+userBean.getPerfil()+" y nueva sessionHttp:"+idSesionHttp);
	
	//	Manejo perfil supervisor
	if(perfil.equals("supervisor") || perfil.equals("tablero")){
		
		userBean.setStatusPlayer("playingBingo");
		pb=gestorSesions.getJugadasSalas(salaInUse);
		//if(pb==null)pb= new PocketBingo();
		//session.getUserProperties().put("sala1",pb);
		//this.gestorSesions.add(userBean.getUsername(), userBean);
		//Set<UserBean> juegoUserBeans = gestorSesions.dameUserBeans("supervisor");
		mySesion.getUserProperties().put("gestorSesiones",gestorSesions);
		log.info("Grabados userBeans en sesion");
		
	}
	if(perfil.equals("jugador")){
		
		userBean.setStatusPlayer("playingBingo");
		pb=gestorSesions.getJugadasSalas(salaInUse);
		//this.gestorSesions.add(userBean.getUsername(), userBean);
	}
}

@OnClose
    public void close(CloseReason reason) {
	//serializar Pocke33tBingo
	//guardaPocket(salaInUse,mySesion);
	
	try{
		//userBean.setStatusPlayer("notPlayingBingo");
		log.info("Closing a WebSocket due to close web socket 'notPlayingBingo'");
		gestorSesions.remove(mySesion);
	}catch(Exception e){
		//
	}
	
}

@OnError
    public void onError(Throwable error) {
	//guardaPocket(salaInUse,mySesion);

	log.info("Ocurrido error : "+ error.getMessage());
	//error.printStackTrace();
}

@OnMessage
    public void handleMessage(String message, Session session) throws InterruptedException{ 
	log.info("recibido mensaje:"+ message);

	if(message.contains("type")) {
		JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
		String target = jsonObject.get("target").getAsString();
		relayMessageToTarget(message,target);
		return;
	}
	switch(message){
	
	case "resume":
		//if(pb!=null)this.guardaPocket("sala1", session);
		this.enviarMensajeAPerfil("EnciendeVideo","supervisor");
		this.enviarMensajeAPerfil("EnciendeVideo","tablero");
		pb= gestorSesions.getJugadasSalas(salaInUse);
		if(pb==null){
			this.enviarMensajeAPerfil("No registrado Pocket","supervisor");
			this.enviarMensajeAPerfil("No registrado Pocket","tablero");
			break;
		}
		session.getUserProperties().put("sala1",pb);
		if(pb.isLineaCantada()){
			this.enviarMensajeAPerfil("ApagaLinea","supervisor");
			this.enviarMensajeAPerfil("ApagaLinea","tablero");			
		}else{
			
		}
		if(pb.isBingoCantado()){
			this.enviarMensajeAPerfil("ApagaBingo","supervisor");
			this.enviarMensajeAPerfil("ApagaBingo","tablero");
		}else{
			
		}
		runnable3 = new Runnable3(mySesion,delay);
		//runnable2 = new Runnable2(session,delay);
		try {
			threadFactory = InitialContext.doLookup("java:comp/DefaultManagedThreadFactory");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hilo3 = threadFactory.newThread(runnable3);
		gestorSesions.addHiloSala(salaInUse, hilo3);
		log.info("Antes de arrancar hilo3 en resume");
		//pb= (PocketBingo)this.mySesion.getUserProperties().get("sala1");
		
		hilo3.start();
		break;
	case "startGame":
		//Se deberia checkear si hay partidas abiertas,
		//Si hay un singleton inyectado (con scope Session)verificar su IdState.
		//Si no hay inyeccion, leerPocket's en directorio "DATA" 
		// Si las hay: se envia Info_PocketAbierto
		// Si no es un newGame
		this.enviarMensajeAPerfil("Info_PocketAbierto","supervisor");
		break;
	case "InitInterface":
		this.enviarMensajeAPerfil("InitInterface","supervisor");
		this.enviarMensajeAPerfil("InitInterface","tablero");
		break;
	case "autoGame":
		//autoGame Dinamico , permite jugar sin intervencion del supervisor
		//
		break;
	case "newGame"://ojo a prueba
		//this.borraPocket("user", session);vcfb
		this.enviarMensajeAPerfil("EnciendeVideo","supervisor");
		this.enviarMensajeAPerfil("EnciendeVideo","tablero");		
		this.enviarMensajeAPerfil("InitInterface", "jugador");
		this.enviarMensajeAPerfil("InitInterface","tablero");//ojo a prueba
		this.enviarMensajeAPerfil("InitInterface","supervisor");//ojo a prueba	
		//pb= new PocketBingo();
		pb= gestorSesions.getJugadasSalas(salaInUse);
		if(pb==null){
			pb= new PocketBingo();
			pb.setDelay(delay);
		}
		pb.initPocket();
		delay=pb.getDelay();
		//this.guardaPocket("sala1", this.mySesion);
		gestorSesions.setJugadasSalas(salaInUse,pb);
		runnable3 = new Runnable3(this.mySesion,delay);
		//runnable2 = new Runnable2(session,delay);
		try {
			threadFactory = InitialContext.doLookup("java:comp/DefaultManagedThreadFactory");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hilo3 = threadFactory.newThread(runnable3);
		gestorSesions.addHiloSala(salaInUse, hilo3);
		
		log.info("Antes de arrancar hilo3");
		//pb= (PocketBingo)this.mySesion.getUserProperties().get("sala1");
		
		hilo3.start();
		break;
	case "seekingFinished":
		//enviarMensajeAPerfil("EncenderNumero_"+pb.getNewBola());
		pb.setReasonInterrupt("secuenciaAcabada");
		//this.guardaPocket("sala1", session);
		//Thread.sleep(delay);
		
		hilo3.interrupt();
		break;
	
	case "Linea":
		//if(pb.isLineaCantada() || pb.isBingoCantado() || pb.getIdState().equals("ComprobandoBingo") || pb.getIdState().equals("ComprobandoLinea") || pb.getIdState().equals("Bingo") )return;
		if(pb.isLineaCantada() || pb.isBingoCantado() || pb.getIdState().equals("ComprobandoBingo")|| pb.getIdState().equals("Bingo") )return;

		if(gestorSesions.addPeticionPremios(userBean,"Linea")){

			log.info("Si se registra esta peticion revision premio Linea");
			log.info("Status antes de tratar Linea:"+pb.getIdState());
			//Ojo con esto
			if(pb.getIdState().equals("ComprobandoLinea")||pb.getIdState().equals("WaitingResultSuper")){
				pb.setIdState("Linea");
				enviarMensajeAPerfil("Linea_"+userBean.getUsername(),"jugador");
				enviarMensajeAPerfil("LineaCantada_"+userBean.getUsername(),"supervisor");
				enviarMensajeAPerfil("LineaCantada_"+userBean.getUsername(),"tablero");
				gestorSesions.getHiloSala(salaInUse).interrupt();
			}else{
				pb.setIdState("Linea");
				enviarMensajeAPerfil("Linea_"+userBean.getUsername(),"jugador");
				enviarMensajeAPerfil("LineaCantada_"+userBean.getUsername(),"supervisor");
				enviarMensajeAPerfil("LineaCantada_"+userBean.getUsername(),"tablero");
			}
		}
		
		//this.guardaPocket("sala1", session);
		//Hilo2.interrupt();
		break;
	case "LiquidarPremiosLinea":
		//Salida automatizacion comprbacion manual cartones
		log.info("Terminando handshake ...");
				pb.setIdState("PremiosRecopiladosLinea");
				gestorSesions.getHiloSala(salaInUse).interrupt();
		break;
	case "LiquidarPremiosBingo":
		//Salida automatizacion comprbacion manual cartones
		log.info("Terminando handshake ...");
				pb.setIdState("PremiosRecopiladosBingo");
				gestorSesions.getHiloSala(salaInUse).interrupt();
		break;		
	case "RefreshDatosCartones":
			enviarMensajeAPerfil("RefreshDatosCartones","jugador");
		break;
	case "Linea_OK":
		if(pb.getIdState().equals("ComprobandoLinea")){
			pb.setLineaCantada(true);
			pb.setIdState("LineaOk");
			pb.setReasonInterrupt("secuenciaAcabada");
			enviarMensajeAPerfil("ApagaLinea","supervisor");
			enviarMensajeAPerfil("ApagaLinea","tablero");
			enviarMensajeAPerfil("ApagaLinea","jugador");
			enviarMensajeAPerfil("EnciendeVideo","supervisor");
			enviarMensajeAPerfil("EnciendeVideo","tablero");
			//this.guardaPocket("sala1", session);
			hilo3.interrupt();
		}

		break;
	
	case "Bingo":
		if(!pb.isLineaCantada() || pb.isBingoCantado() ||  pb.getIdState().equals("ComprobandoLinea") || pb.getIdState().equals("Linea") || pb.getIdState().equals("PremiosRecopiladosBingo") || pb.getIdState().equals("BingoOk") || pb.getIdState().equals("EndBalls")|| pb.getIdState().equals("Finalized"))return;

		if(gestorSesions.addPeticionPremios(userBean,"Bingo")){

			log.info("Si se registra esta peticion revision premio Bingo. IdState es:"+ pb.getIdState());
			//Ojo con esto
			if(pb.getIdState().equals("ComprobandoBingo")||pb.getIdState().equals("WaitingResultSuper")){
				pb.setIdState("Bingo");
				gestorSesions.getHiloSala(salaInUse).interrupt();
				enviarMensajeAPerfil("Bingo_"+userBean.getUsername(),"jugador");
				enviarMensajeAPerfil("BingoCantado_"+userBean.getUsername(),"supervisor");
				enviarMensajeAPerfil("BingoCantado_"+userBean.getUsername(),"tablero");

			}else if(pb.getIdState().equals("WarningFinalizando")){
				
				pb.setReasonInterrupt("Bingo");
				pb.setIdState("Bingo");
				gestorSesions.getHiloSala(salaInUse).interrupt();
				enviarMensajeAPerfil("Bingo_"+userBean.getUsername(),"jugador");

				//hilo3.interrupt();
			}else {
				pb.setIdState("Bingo");
				enviarMensajeAPerfil("Bingo_"+userBean.getUsername(),"jugador");
				enviarMensajeAPerfil("BingoCantado_"+userBean.getUsername(),"supervisor");
				enviarMensajeAPerfil("BingoCantado_"+userBean.getUsername(),"tablero");
			}
		}

		break;
	
	case "Bingo_OK":
		if(pb.getIdState().equals("ComprobandoBingo")){
			log.info("Status antes de Bingo_Ok"+pb.getIdState());
			pb.setBingoCantado(true);
			
			pb.setIdState("BingoOk");
			pb.setReasonInterrupt("secuenciaAcabada");
			enviarMensajeAPerfil("ApagaBingo","supervisor");
			enviarMensajeAPerfil("ApagaBingo","tablero");
			enviarMensajeAPerfil("ApagaBingo","jugador");
			enviarMensajeAPerfil("EnciendeVideo","supervisor");
			enviarMensajeAPerfil("EnciendeVideo","tablero");

			//this.guardaPocket("sala1", session);
			hilo3.interrupt();
		}
		break;
		
	case "Continue":
		if(pb.getIdState().equals("ComprobandoBingo") || pb.getIdState().equals("ComprobandoLinea")|| pb.getIdState().equals("WaitingResultSuper") ){		
			pb.setIdState("Continue");
			this.enviarMensajeAPerfil("EnciendeVideo","supervisor");
			this.enviarMensajeAPerfil("EnciendeVideo","tablero");
			pb.setReasonInterrupt("continuar");
			//	this.guardaPocket("sala1", session);
			hilo3.interrupt();
		}
		break;
	
	case "Finalize":

			pb.setIdState("Finalized");
			pb.resetNumerosCalled();
			gestorSesions.resetCartones(this.salaInUse);
			enviarMensajeAPerfil("EndBalls","supervisor");
			enviarMensajeAPerfil("EndBalls","tablero");
			enviarMensajeAPerfil("EndBalls","jugador");               			
			//Pendiente interrumpir hilo para que pare ya
		//}
		
		break;
	}
	//manejar POJOS en el formato JSON#comando#dato1#datox....
	if(message.startsWith("JSON")){
		StringTokenizer mySToken= new StringTokenizer(message,"#");
		Vector<String> arrayMessage = new Vector<String>();
		while(mySToken.hasMoreTokens()){
			arrayMessage.add(mySToken.nextToken());
		}
		if(arrayMessage.size()>0){
			String comando=arrayMessage.elementAt(1);
			switch (comando){
			case "SET_DATOS_CARTONES"://JSON#SET_DATOS_CARTONES#.........
				//String precioCarton,nCartones,porCientoLinea,porCientoBingo,porCientoCantaor;
				pb.setPrecioCarton(arrayMessage.elementAt(2));
				pb.setnCartonesManuales(arrayMessage.elementAt(3));
				pb.setPorcientoLinea(arrayMessage.elementAt(4));
				pb.setPorcientoBingo(arrayMessage.elementAt(5));
				pb.setPorcientoCantaor(arrayMessage.elementAt(6));
				//this.guardaPocket("sala1", session);
				comando="GET_DATOS_CARTONES";
			
			case "GET_DATOS_CARTONES"://JSON#GET_DATOS_CARTONES#.........
				String precioCarton,porCientoLinea,porCientoBingo,porCientoCantaor;
				int page_delay;
				precioCarton=pb.getPrecioCarton();
				//Hay que distinguir entre cartones electronicos y manuales
				//EL cuadro de Dialogo debe considerar las dos facetas
				// Por lo tanto debe haber dos variables, uno para cada tipo de faceta de cartones.
				// Implementado ambos tipos de datos
			
				int nCartones = new Integer(pb.calculaNcartonesManuales()) + this.gestorSesions.dameSetCartonesEnJuego(salaInUse).size();
				porCientoLinea=pb.getPorcientoLinea();
				porCientoBingo=pb.getPorcientoBingo();
				porCientoCantaor=pb.getPorcientoCantaor();
				page_delay=pb.getDelay();
				String construirScript="DATOSCARTONES_"+precioCarton+"_"+nCartones+"_"+porCientoLinea+"_"+porCientoBingo+"_"+porCientoCantaor+"_"+page_delay;
				enviarMensajeAPerfil(construirScript,"supervisor");
				enviarMensajeAPerfil(construirScript,"tablero");
				enviarMensajeAPerfil(construirScript,"jugador");
				break;
				
			case "SET_DATOS_DELAY"://JSON#SET_DATOS_DELAY#delay
				this.delay=new Integer(arrayMessage.elementAt(2)).intValue();
				if(pb!=null)pb.setDelay(delay);
			}
		}
	}
}

private PocketBingo leePocket(String sala, Session sesion){
	  	String ruta,fichero;
	  	PocketBingo aux=null;
	  	String uri=sesion.getRequestURI().toString();
	  	if(uri.equals("/wildfly-1.0/sala1")){
	  		ruta="C:\\\\put\\HTML5\\PocketBingo";
	  		fichero=ruta+"\\"+sala;
	  	}else{
  				ruta = System.getenv("OPENSHIFT_DATA_DIR");
  				fichero=ruta+sala;
	  	}
        try
        	{
	            // Se crea un ObjectInputStream
	            ObjectInputStream ois = new ObjectInputStream(
	                    new FileInputStream(fichero));
	            
	            // Se lee el primer objeto
	            aux = (PocketBingo)ois.readObject();
	            
	            ois.close();
	        }catch (Exception e1){
	           log.severe("Problem serializacion File="+fichero);
	           e1.printStackTrace();
	        }
	        
	    
	    return aux; 
  }
  private void enviarMensajeAPerfil(String textMessage,String perfil){
  	try {
		Set<UserBean> myUsersbean = gestorSesions.dameUserBeans(perfil);
		Iterator<UserBean> itBeans= myUsersbean.iterator();
		while (itBeans.hasNext()){
			Session sesionActiva = itBeans.next().getSesionSocket();
			sesionActiva.getBasicRemote().sendText(textMessage);
		}
			log.info("Enviando desde servidor a navegador:"+textMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  private void relayMessageToTarget(String textMessage,String target){
	  	try {
			Set<UserBean> myUsersbean = gestorSesions.dameUserBeansPorUser(target);
			Iterator<UserBean> itBeans= myUsersbean.iterator();
			while (itBeans.hasNext()){
				Session sesionActiva = itBeans.next().getSesionSocket();
				sesionActiva.getBasicRemote().sendText(textMessage);
			}
				//log.info("Relay desde servidor a navegador:"+textMessage);
			} catch (IOException e) {
				log.warning("Error en relayMessage:"+e.getMessage());
			}
	  }
  private void guardaPocket(String sala, Session sesion){
	  	String ruta,fichero;
	  	
	  	String uri=sesion.getRequestURI().toString();
	  	//log.info("la uri es:"+uri);
	  	if(uri.equals("/wildfly-1.0/sala1")){
	  		ruta="C:\\\\put\\HTML5\\PocketBingo";
	  		fichero=ruta+"\\"+sala;
	  	}else{
				ruta = System.getenv("OPENSHIFT_DATA_DIR");
				fichero=ruta+sala;
				log.info("ghuaradndo Pocket"+ fichero);
	  	}
	  try
      {
          ObjectOutputStream oos = new ObjectOutputStream(
                  new FileOutputStream(fichero));
          
              oos.writeObject(pb);
              
          oos.close();
      } catch (Exception e)
      {
          log.severe("Excepcion Guarda Pocket "+ fichero);
    	  e.printStackTrace();
      }  
	  
  }
  private void borraPocket(String user, Session sesion){
	  String ruta,fichero;
	  	
	  	String uri=sesion.getRequestURI().toString();
	  	if(uri.equals("/wildfly-1.0/actions")){
	  		ruta="C:\\\\put\\HTML5\\PocketBingo";
	  		fichero=ruta+"\\"+user;
	  	}else{
				ruta = System.getenv("OPENSHIFT_DATA_DIR");
				fichero=ruta+user;
	  	}
	  	File fileUser= new File(fichero);
	  	fileUser.delete();
	  	
  }
}
