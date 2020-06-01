package open;

//import java.util.Base64;
import org.apache.commons.codec.binary.Base64;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class RestClient{
	public static void main(String args[]) {
		RestClient restC = new RestClient();
        restC.apiOpenvidu("GET","https://bogaservice.es:4443/api/sessions",null,null);
	}
	public Map<String,String> apiOpenvidu(String operation ,String url, Map<String,String> headers, String bodyJSON) 
	{
		Map<String,String> myEntity = new ConcurrentHashMap<>();
		Response response = null;
		
		Client client = ClientBuilder.newClient();
		WebTarget resourceTarget = client.target(url);
		//WebTarget resourceTarget = client.target("https://bogaservice.es:4443/api/sessions/SessionA");
		// obtener el Invocacion.Builder
		Invocation.Builder invocation = (Builder) resourceTarget.request();
		
        String originalInput = "OPENVIDUAPP:ntmanager";
        byte[] encodedBytes = Base64.encodeBase64(originalInput.getBytes());
        System.out.println("encodedBytes " + new String(encodedBytes));
       
        String encodedString = "Basic "+new String(encodedBytes);
        System.out.println("Encode String :" + new String(encodedString));
        ((Builder) invocation).header("Authorization",encodedString);
        
		//aplicar Headers
        if(headers!=null) {
        	Set<String> keyHeaders = headers.keySet();
        	Iterator<String> itKeys = keyHeaders.iterator();      
        	while(itKeys.hasNext()) {
        		String key = itKeys.next();
        		((Builder) invocation).header(key,headers.get(key));
        	}
        }
        
		//Solicitar ejecucion
        //Invoke the request using generic interface
        switch(operation) {
        	case "GET":
        		response = ((Builder) invocation).buildGet().invoke();
        		break;
        	case "POST":
        		Entity<String> entity = Entity.text(bodyJSON);
        		response=((Builder) invocation).buildPost(entity).invoke();
        		break;
        	case "DELETE":	
        		response =((Builder) invocation).buildDelete().invoke();
        }
	
		
		String status = response.getStatus()+"";
		String body = response.readEntity(String.class);
		
		System.out.println("Status:"+status);
		System.out.println("Response:"+body);
        //JsonObject jSonObject = (JsonObject) new JsonParser().parse(body).getAsJsonObject();
        
        //System.out.println("numeroSesionesOpen"+jSonObject.get("numberOfElements").getAsString());
        response.close();  // You should close connections!
	   
        myEntity.put("status", status);
        myEntity.put("body", body);
        
        return myEntity;
	}
}