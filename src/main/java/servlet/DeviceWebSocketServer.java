package servlet;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.jboss.logging.Logger;

@ServerEndpoint("/actions")
public class DeviceWebSocketServer {
Logger log = Logger.getLogger("MyLogger");
@Resource
private ManagedThreadFactory threadFactory;
private PocketBingo pb; 
private Thread Hilo2 = null;
private Session mySesion;

@OnOpen
    public void open(Session session) {
	log.info("Abierta Session :"+ session.getId());
	this.mySesion=session;
	pb=this.leePocket("user", session);
	if(pb==null)pb= new PocketBingo();
	session.getUserProperties().put("user",pb);
}

@OnClose
    public void close(CloseReason reason) {
	//serializar Pocke33tBingo
	guardaPocket("user",mySesion);
	log.info("Closing a WebSocket due to " + reason.getReasonPhrase());
	
}

@OnError
    public void onError(Throwable error) {
	log.info("Ocurrido error : "+ error.getMessage());
	error.printStackTrace();
}

@OnMessage
    public void handleMessage(String message, Session session){ 
	log.info("recibido mensaje:"+ message);
	switch(message){
	case "resume":
		if(pb!=null)this.guardaPocket("user", session);
		this.enviarMensaje("EnciendeVideo");
		pb= this.leePocket("user", session);
		session.getUserProperties().put("user",pb);
		if(pb.isLineaCantada()){
			this.enviarMensaje("ApagaLinea");
		}else{
			
		}
		if(pb.isBingoCantado()){
			this.enviarMensaje("ApagaBingo");
		}else{
			
		}
		
		Hilo2 = new Hilo2(session);
		Hilo2.start();
		break;
	case "startGame":
		//Se deberia checkear si hay partidas abiertas,
		//Si hay un singleton inyectado (con scope Session)verificar su IdState.
		//Si no hay inyeccion, leerPocket's en directorio "DATA" 
		// Si las hay: se envia Info_PocketAbierto
		// Si no es un newGame
		this.enviarMensaje("Info_PocketAbierto");
		break;
	case "newGame":
		//this.borraPocket("user", session);
		this.enviarMensaje("EnciendeVideo");
		//pb= new PocketBingo();
		pb= this.leePocket("user", session);
		pb.initPocket();
		this.guardaPocket("user", session);
		session.getUserProperties().put("user",pb);
		Hilo2 = new Hilo2(session);
		Hilo2.start();
		break;
	case "seekingFinished":
		pb.setReasonInterrupt("secuenciaAcabada");
		this.guardaPocket("user", session);
		Hilo2.interrupt();
		break;
	
	case "Linea":
		pb.setIdState("Linea");
		this.guardaPocket("user", session);
		break;

	case "Linea_OK":
		pb.setLineaCantada(true);
		pb.setIdState("LineaOk");
		pb.setReasonInterrupt("secuenciaAcabada");
		this.guardaPocket("user", session);
		Hilo2.interrupt();
		break;
	
	case "Bingo":
		pb.setIdState("Bingo");
		this.guardaPocket("user", session);
		break;
	
	case "Bingo_OK":
		pb.setBingoCantado(true);
		pb.setIdState("BingoOk");
		pb.setReasonInterrupt("secuenciaAcabada");
		this.guardaPocket("user", session);
		Hilo2.interrupt();
		break;
	case "Continue":
		pb.setIdState("Started");
		this.enviarMensaje("EnciendeVideo");
		pb.setReasonInterrupt("secuenciaAcabada");
		this.guardaPocket("user", session);
		Hilo2.interrupt();
		break;
	case "Finalize":
		pb.setIdState("Finalized");
		pb.setReasonInterrupt("offLine");
		Hilo2.interrupt();
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
				this.guardaPocket("user", session);
				break;
			
			case "GET_DATOS_CARTONES"://JSON#GET_DATOS_CARTONES#.........
				String precioCarton,nCartones,porCientoLinea,porCientoBingo,porCientoCantaor;
				precioCarton=pb.getPrecioCarton();
				nCartones=pb.getnCartones();
				porCientoLinea=pb.getPorcientoLinea();
				porCientoBingo=pb.getPorcientoBingo();
				porCientoCantaor=pb.getPorcientoCantaor();
				String construirScript="DATOSCARTONES_"+precioCarton+"_"+nCartones+"_"+porCientoLinea+"_"+porCientoBingo+"_"+porCientoCantaor;
				enviarMensaje(construirScript);
			}
		

		}
	}
	
	
}
  private PocketBingo leePocket(String user, Session sesion){
	  	String ruta,fichero;
	  	PocketBingo aux=null;
	  	String uri=sesion.getRequestURI().toString();
	  	if(uri.equals("/wildfly/actions")){
	  		ruta="C:\\\\put\\HTML5\\PocketBingo";
	  		fichero=ruta+"\\"+user;
	  	}else{
  				ruta = System.getenv("OPENSHIFT_DATA_DIR");
  				fichero=ruta+user;
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
  private void enviarMensaje(String textMessage){
  	try {
			mySesion.getBasicRemote().sendText(textMessage);
			log.info("Enviando desde servidor a navegador:"+textMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  private void guardaPocket(String user, Session sesion){
	  	String ruta,fichero;
	  	
	  	String uri=sesion.getRequestURI().toString();
	  	//log.info("la uri es:"+uri);
	  	if(uri.equals("/wildfly/actions")){
	  		ruta="C:\\\\put\\HTML5\\PocketBingo";
	  		fichero=ruta+"\\"+user;
	  	}else{
				ruta = System.getenv("OPENSHIFT_DATA_DIR");
				fichero=ruta+user;
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
	  	if(uri.equals("/wildfly/actions")){
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
