package servlet;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.jboss.logging.Logger;

@ServerEndpoint(value="/sala1",configurator=UserAwareConfigurator.class)
public class DeviceWebSocketServer {
Logger log = Logger.getLogger("MyLogger");
 
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

@OnOpen
    public void open(Session session, EndpointConfig config) {
	UserBean userBean = (UserBean) config.getUserProperties().get("userBean");
	//Comprobar si la corriente sesionHttp ya esta presente(cosa que no deberia porque al morir el websocket capturamos ese evento y kill la sesion
	//vinculada, pero por si acaso.
	//Lo que si puede ocurrir es que se repita una sesionHttp de un usuario que utilice mas de un recurso del contenedor, entonces tendra la misma
	//Http Session, distinto perfil, y distinta WebsocketSession.
	//Que como identificamos un usuario, por su nombre de usuario, independientemente de su perfil,y sessiones asociadas.
	//Solo habra un univoco usuario en juego.
	//vincular sesion web socket a UserBean.
	/*Aqui, sabemos el  nombre del user
						el perfil del user
						su Http Session
						su WebSockeSession	
	*/
	userBean.setSesionSocket(session);
	
	String perfil = userBean.getPerfil();
	salaInUse = userBean.getSalonInUse();
	HttpSession userHttpSession = userBean.getSesionHttp();
	String idSesionHttp = userHttpSession.getId();
	log.info("Abierta Session WebSocket:"+ session.getId() + "del Usuario "+userBean.getUsername()+" ,perfil="+userBean.getPerfil()+" y nueva sessionHttp:"+idSesionHttp);

	//	Manejo perfil supervisor
	if(perfil.equals("supervisor")){
		this.mySesion=session;
		pb=this.gestorSesions.getJugadasSalas(salaInUse);
		//if(pb==null)pb= new PocketBingo();
		//session.getUserProperties().put("sala1",pb);
		this.gestorSesions.add(userBean.getUsername(), userBean);
		//Set<UserBean> juegoUserBeans = gestorSesions.dameUserBeans("supervisor");
		mySesion.getUserProperties().put("gestorSesiones",gestorSesions);
		log.info("Grabados userBeans en sesion");//
		
	}
	if(perfil.equals("jugador")){
		this.mySesion=session;
		userBean.setStatusPlayer("OnLine");
		pb=this.gestorSesions.getJugadasSalas(salaInUse);
		this.gestorSesions.add(userBean.getUsername(), userBean);

	}
}

@OnClose
    public void close(CloseReason reason) {
	//serializar Pocke33tBingo
	guardaPocket(salaInUse,mySesion);
	log.info("Closing a WebSocket due to " + reason.getReasonPhrase());
	//gestorSesions.remove(mySesion);
	//gestorSesions.guardaContext();
	
}

@OnError
    public void onError(Throwable error) {
	guardaPocket(salaInUse,mySesion);
	gestorSesions.guardaContext();
	log.info("Ocurrido error : "+ error.getMessage());
	error.printStackTrace();
}

@OnMessage
    public void handleMessage(String message, Session session) throws InterruptedException{ 
	log.info("recibido mensaje:"+ message);
	switch(message){
	case "resume":
		//if(pb!=null)this.guardaPocket("sala1", session);
		this.enviarMensajeAPerfil("EnciendeVideo","supervisor");
		pb= gestorSesions.getJugadasSalas(salaInUse);
		if(pb==null){
			this.enviarMensajeAPerfil("No registrado Pocket","supervisor");
			break;
		}
		session.getUserProperties().put("sala1",pb);
		if(pb.isLineaCantada()){
			this.enviarMensajeAPerfil("ApagaLinea","supervisor");
		}else{
			
		}
		if(pb.isBingoCantado()){
			this.enviarMensajeAPerfil("ApagaBingo","supervisor");
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
	case "newGame":
		//this.borraPocket("user", session);vcfb
		this.enviarMensajeAPerfil("EnciendeVideo","supervisor");
		this.enviarMensajeAPerfil("InitInterface", "jugador");
		//pb= new PocketBingo();
		pb= gestorSesions.getJugadasSalas(salaInUse);
		if(pb==null){
			pb= new PocketBingo();
		}
		pb.initPocket();
		pb.setDelay(delay);
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
		if(pb.isLineaCantada() || pb.isBingoCantado() || pb.getIdState().equals("ComprobandoBingo") || pb.getIdState().equals("ComprobandoLinea") || pb.getIdState().equals("Bingo") || pb.getIdState().equals("Linea"))return;
		pb.setIdState("Linea");
		enviarMensajeAPerfil("Linea","jugador");
		//this.guardaPocket("sala1", session);
		//Hilo2.interrupt();
		break;

	case "Linea_OK":
		if(pb.getIdState().equals("ComprobandoLinea")){
			pb.setLineaCantada(true);
			pb.setIdState("LineaOk");
			pb.setReasonInterrupt("secuenciaAcabada");
			enviarMensajeAPerfil("ApagaLinea","supervisor");
			enviarMensajeAPerfil("ApagaLinea","jugador");
			enviarMensajeAPerfil("EnciendeVideo","supervisor");
			//this.guardaPocket("sala1", session);
			hilo3.interrupt();
		}

		break;
	
	case "Bingo":
		if(!pb.isLineaCantada() || pb.isBingoCantado() || pb.getIdState().equals("ComprobandoBingo") || pb.getIdState().equals("ComprobandoLinea") || pb.getIdState().equals("Bingo") || pb.getIdState().equals("Linea"))return;
		pb.setIdState("Bingo");
		enviarMensajeAPerfil("Bingo","jugador");		
		pb.setReasonInterrupt("Bingo");
		//this.guardaPocket("sala1", session);
		//Hilo2.interrupt();
		break;
	
	case "Bingo_OK":
		if(pb.getIdState().equals("ComprobandoBingo")){
			pb.setBingoCantado(true);
			pb.setIdState("BingoOk");
			pb.setReasonInterrupt("secuenciaAcabada");
			enviarMensajeAPerfil("ApagaBingo","supervisor");
			enviarMensajeAPerfil("ApagaBingo","jugador");
			enviarMensajeAPerfil("EnciendeVideo","supervisor");

			//this.guardaPocket("sala1", session);
			hilo3.interrupt();
		}
		break;
		
	case "Continue":
		if(pb.getIdState().equals("ComprobandoBingo") || pb.getIdState().equals("ComprobandoLinea") ){		
			pb.setIdState("Continue");
			this.enviarMensajeAPerfil("EnciendeVideo","supervisor");
			pb.setReasonInterrupt("continuar");
			//	this.guardaPocket("sala1", session);
			hilo3.interrupt();
		}
		break;
	
	case "Finalize":
		pb.setIdState("Finalized");
		pb.setReasonInterrupt("offLine");
		hilo3.interrupt();
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
				pb.setnCartones(arrayMessage.elementAt(3));
				pb.setPorcientoLinea(arrayMessage.elementAt(4));
				pb.setPorcientoBingo(arrayMessage.elementAt(5));
				pb.setPorcientoCantaor(arrayMessage.elementAt(6));
				//this.guardaPocket("sala1", session);
				break;
			
			case "GET_DATOS_CARTONES"://JSON#GET_DATOS_CARTONES#.........
				String precioCarton,nCartones,porCientoLinea,porCientoBingo,porCientoCantaor;
				precioCarton=pb.getPrecioCarton();
				nCartones=pb.getnCartones();
				porCientoLinea=pb.getPorcientoLinea();
				porCientoBingo=pb.getPorcientoBingo();
				porCientoCantaor=pb.getPorcientoCantaor();
				String construirScript="DATOSCARTONES_"+precioCarton+"_"+nCartones+"_"+porCientoLinea+"_"+porCientoBingo+"_"+porCientoCantaor;
				enviarMensajeAPerfil(construirScript,"supervisor");
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
	           log.error("Problem serializacion File="+fichero);
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
          log.error("Excepcion Guarda Pocket "+ fichero);
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
