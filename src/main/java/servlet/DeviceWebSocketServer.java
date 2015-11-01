package servlet;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/actions")
public class DeviceWebSocketServer {
@OnOpen
    public void open(Session session) {
	System.out.print("Websocket abierto session =" + session.getId()) ;
}

@OnClose
    public void close(CloseReason reason) {
	System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());
}

@OnError
    public void onError(Throwable error) {
}

@OnMessage
    public void handleMessage(String message, Session session) {
	System.out.println("recibido mensaje:"+ message);
}
}