package servlet;
 
import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.jboss.logging.Logger;

@ServerEndpoint("/Endpoint")
@Stateless
public class WSEndpoint {
	Logger log = Logger.getLogger(this.getClass());
	@Resource
	ManagedExecutorService mes;
	
	@OnMessage
	public void receiveMessage(String message, Session session) {
		log.info("Received : "+ message + ", session:" + session.getId());
		//return "Response from the server";
	}
	
	@OnOpen
	public void open(Session session) {
		log.info("Open session:" + session.getId());
		final Session s = session;
		/*
			mes.execute(new Runnable() {
			@Override
			public void run() {				
				try {
					for (int i=0;i<3;i++) {
					 Thread.sleep(10000);
					 s.getBasicRemote().sendText("Message temporizado :" + (new Date()).toString());
					} 
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}			
		});*/
	}
	
	@OnClose
	public void close(Session session, CloseReason c) {
		log.info("Closing:" + session.getId());
	}
}