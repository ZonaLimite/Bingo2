package servlet;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
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
	pb=leePocket("user",session);
	if( pb==null){
		
	}else if(pb.getIdState().equals("Started")|| pb.getIdState().equals("Paused")){
		this.enviarMensaje("Info_PocketAbierto");
	}
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
    public void handleMessage(String message, Session session) {
	log.info("recibido mensaje:"+ message);
	switch(message){
	case "resume":
		pb= this.leePocket("user", session);
		session.getUserProperties().put("user",pb);
		Hilo2 = new Hilo2(session);
		Hilo2.start();
		break;
	case "startGame":
		pb= new PocketBingo();
		session.getUserProperties().put("user",pb);
		Hilo2 = new Hilo2(session);
		Hilo2.start();
		break;
	case "secuenciaAcabada":
		pb.setReasonInterrupt("secuenciaAcabada");
		Hilo2.interrupt();
		break;
	case "Finalize":
		pb.setIdState("Finalized");
		pb.setReasonInterrupt("offLine");
		Hilo2.interrupt();
		break;
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
  				fichero=ruta+"/"+user;
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  private void guardaPocket(String user, Session sesion){
	  	String ruta,fichero;
	  	
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
          ObjectOutputStream oos = new ObjectOutputStream(
                  new FileOutputStream(fichero));
          
              oos.writeObject(pb);
          
          oos.close();
      } catch (Exception e)
      {
          log.error("Guarda Pocket "+ fichero);
    	  e.printStackTrace();
      }  
	  
  }
}
