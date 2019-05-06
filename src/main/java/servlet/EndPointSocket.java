	package servlet;

	 
	import java.io.IOException;

import javax.websocket.*;
	import javax.websocket.server.ServerEndpoint;
	 
	  
	@ServerEndpoint("/hello")
	public class EndPointSocket {
	 
	     
	    @OnMessage
	    public String hello(String message) {
	        System.out.println("Received : "+ message);
	        return message;
	    }
	 
	    @OnOpen
	    public void myOnOpen(Session session) {
	        System.out.println("WebSocket opened: " + session.getId());
	        System.out.println("enviado1");
	        try {
				session.getBasicRemote().sendText("Enhorabuena");//
				System.out.println("enviado2");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	 
	    @OnClose
	    public void myOnClose(CloseReason reason) {
	        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());
	    }
	 
	}

