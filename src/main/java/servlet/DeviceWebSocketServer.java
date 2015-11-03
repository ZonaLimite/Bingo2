package servlet;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.jboss.logging.Logger;

@ServerEndpoint("/actions")
public class DeviceWebSocketServer {
Logger log = Logger.getLogger("MyLogger");
@Resource
private ManagedExecutorService mes;

@OnOpen
    public void open(Session session) {
	log.info("Abierta Session :"+ session.getId());
	
	mes.execute(new Runnable() {
		@Override
		public void run() {				
			try {
				for (int i=0;i<3;i++) {
				 Thread.sleep(10000);
				 session.getBasicRemote().sendText("Message temporizado :" + (new Date()).toString());
				} 
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}			
	});
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
}
}