package servlet;

import java.io.IOException;
import java.util.Date;

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

@OnOpen
    public void open(Session session) {
	log.info("Abierta Session :"+ session.getId());
	
	
	
	
}

@OnClose
    public void close(CloseReason reason) {
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
	case "offLine":
		pb.setReasonInterrupt("offLine");
		Hilo2.interrupt();
	}
	
}
}