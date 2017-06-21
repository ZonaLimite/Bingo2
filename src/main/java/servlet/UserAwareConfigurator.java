package servlet;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class UserAwareConfigurator extends Configurator {

	    @Override
	    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
	        HttpSession httpSession = (HttpSession) request.getHttpSession();
	        UserBean user = (UserBean) httpSession.getAttribute("userBean");
	        
	        config.getUserProperties().put("userBean", user);
	        config.getUserProperties().put("httpSession", httpSession.getId());
	    }

	}

