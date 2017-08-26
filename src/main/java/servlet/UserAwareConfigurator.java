package servlet;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class UserAwareConfigurator extends Configurator {

	    @Override
	    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
	        HttpSession httpSession = (HttpSession) request.getHttpSession();
	        //UserBean user = (UserBean) httpSession.getAttribute("userBean");//
	        Logger log = Logger.getLogger("Aware");
	        String usuario,perfil,sala;
	        usuario="";
	        perfil="";
	        sala="";

	        Map<String, List<String>> parametros = request.getParameterMap();
	        Set claves = parametros.keySet();
	        Iterator porcadaclave = claves.iterator();
	        while(porcadaclave.hasNext()){
	        	String clave = (String)porcadaclave.next();
	        	log.info("-------------");
	        	log.info("Clave:"+clave);

	        	
	        	List<String> valores = parametros.get(clave);
	        	Iterator<String> itListavalores= valores.iterator();
	        		while(itListavalores.hasNext()){

	        			String valor = itListavalores.next();
	    	        	if(clave.equals("usuario"))usuario=valor;
	    	        	if(clave.equals("perfil"))perfil=valor;
	    	        	if(clave.equals("sala"))sala=valor;
	    	        	
	        			log.info(valor);
	        		}
	        	
	        }
	        
	        log.info("Usuario en award es:"+usuario);

	        config.getUserProperties().put("perfil", perfil);
	        config.getUserProperties().put("usuario", usuario);		
	        //config.getUserProperties().put("userBean", user);
	        config.getUserProperties().put("idHttpSession", httpSession.getId());
	    }

	}

