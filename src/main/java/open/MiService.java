package open;


import java.io.IOException;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import servlet.GestorSessions;



@Path("/openvidu")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class MiService {
	
	@Inject
	private UserHandlerOpenVidu uhov;

	@Inject
	private GestorSessions gestorSesions;	
	
	
	@GET
    @Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
    public Response ping() {
        return Response.ok().entity("{Service online:yes}").build();
    }
	
	@GET
	@Path("/get-token-GET")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTokenGet(@QueryParam("sessionNameParam") String sessionNameParam ,@QueryParam("usuario") String user ,@Context HttpServletRequest request) {
		
		//JsonObject jsonObject = new JsonParser().parse(sessionNameParam).getAsJsonObject();
		//String sessionNameP = jsonObject.get("sessionName").getAsString();		
		String token = uhov.getToken(sessionNameParam,user);
		System.out.println("request="+request.getRequestURL());
		return Response.ok().entity(token).build();
	}
	
	@POST
	@Path("/get-token")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getToken(@Context HttpServletRequest request) {
		
		String sessionNameParam="";
		String sessionName="";
		String user="";
		try {
			sessionNameParam = request.getReader().readLine();
			JsonObject sessionJSON = (JsonObject) new JsonParser().parse(sessionNameParam).getAsJsonObject();

			// The video-call to connect
			sessionName = (String) sessionJSON.get("sessionName").getAsString();
			user = (String) sessionJSON.get("usuario").getAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		String token = uhov.getToken(sessionName,user);
		uhov.objetosViduPorSesion();
		Set jugadores = gestorSesions.dameUserBeansEnPortal("jugador");
		System.out.println("Jugadores Bingo="+jugadores);

		return Response.ok().entity(token).build();
	}
	@POST
	@Path("/remove-user")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	public Response removeUser(@Context HttpServletRequest request) {
		Response res = null;
		String sessionNameToken="";
		try {
			sessionNameToken = request.getReader().readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Removing user | {sessionName, token}=" + sessionNameToken);
		// Retrieve the params from BODY
		JsonObject sessionNameTokenJSON = (JsonObject) new JsonParser().parse(sessionNameToken).getAsJsonObject();
		String sessionName = (String) sessionNameTokenJSON.get("sessionName").getAsString();
		String token = (String) sessionNameTokenJSON.get("token").getAsString();
		res = uhov.removeUser(sessionName, token);
		uhov.objetosViduPorSesion();
		return res;
	}
}
