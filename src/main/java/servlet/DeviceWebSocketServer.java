package servlet;

import java.io.IOException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.jboss.logging.Logger;

@ServerEndpoint("/actions")
public class DeviceWebSocketServer {
Logger log = Logger.getLogger("MyLogger");
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
}

@OnMessage
    public void handleMessage(String message, Session session) {
	log.info("recibido mensaje:"+ message);
}
}