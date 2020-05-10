package open;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.OpenViduRole;
import io.openvidu.java.client.Session;
import io.openvidu.java.client.TokenOptions;
import servlet.GestorSessions;

@Startup
@Singleton
public class UserHandlerOpenVidu {

	
	// OpenVidu object as entrypoint of the SDK
	private OpenVidu openVidu = null;

	// Collection to pair session names and OpenVidu Session objects
	private Map<String, Session> mapSessions = new ConcurrentHashMap<>();
	// Collection to pair session names and tokens (the inner Map pairs tokens and
	// role associated)
	private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();

	// URL where our OpenVidu server is listening
	//Cambiar a localhost cuando vaya al servidor
	private String OPENVIDU_URL = "https://18.221.239.217:4443";
	// Secret shared with our OpenVidu server
	private String SECRET ="ntmanager";
	//private HttpSession httpSesion=null;

    
	
	 public UserHandlerOpenVidu() {
		 //initOpenVidu();
		 
	 }
	 private void initOpenVidu() {
		 if(openVidu==null) {
			 openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
			 System.out.println("Nueva sesion OpenVidu"+openVidu.toString());
		 }else {
			 System.out.println("OpenVidu Ya esta instanciado"); 
		 }
	 }
	 
	 public String getToken(String nameSession,String user) {
		 initOpenVidu();
		String sessionName = nameSession;
		Session session=null;
		String token=null;
		System.out.println("Getting a token from OpenVidu Server | {sessionName}=" + sessionName);


		// The video-call to connect
	

		// Role associated to this user
		//OpenViduRole role = new OpenViduRole();

		// Optional data to be passed to other users when this user connects to the
		// video-call. In this case, a JSON with the value we stored in the HttpSession
		// object on login
		String serverData = "{\"serverData\": \""+user+"\"}";

		// Build tokenOptions object with the serverData and the role
		TokenOptions tokenOptions = new TokenOptions.Builder().data(serverData).role(OpenViduRole.PUBLISHER).build();
		
			if (this.mapSessions.get(sessionName) != null) {
				// Session already exists
				System.out.println("Existing session " + sessionName);
				

				// Generate a new token with the recently created tokenOptions
				try {
					token = this.mapSessions.get(sessionName).generateToken(tokenOptions);
				} catch (OpenViduJavaClientException | OpenViduHttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Update our collection storing the new token
				this.mapSessionNamesTokens.get(sessionName).put(token, OpenViduRole.PUBLISHER);
				return token;
			}

		// New session
			System.out.println("New session " + sessionName);
		

			// Create a new OpenVidu Session
			

				try {
					session = this.openVidu.createSession();
				} catch (OpenViduJavaClientException | OpenViduHttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			// Generate a new token with the recently created tokenOptions
			try {
				token = session.generateToken(tokenOptions);
			} catch (OpenViduJavaClientException | OpenViduHttpException e) {
				// TODO Auto-generated catch block
				System.err.println("Fallo al instanciar el token :"+e.getMessage());
				token ="Fallo token";
				return token;
			}

			// Store the session and the token in our collections
			this.mapSessions.put(sessionName, session);
			this.mapSessionNamesTokens.put(sessionName, new ConcurrentHashMap<>());
			this.mapSessionNamesTokens.get(sessionName).put(token, OpenViduRole.PUBLISHER);

				// TODO Auto-generated catch block
				

			return token;
	}

	 public Response removeUser( String sessionName, String token ) {
			// If the session exists
			if (this.mapSessions.get(sessionName) != null && this.mapSessionNamesTokens.get(sessionName) != null) {

				// If the token exists
				if (this.mapSessionNamesTokens.get(sessionName).remove(token) != null) {
					// User left the session
					if (this.mapSessionNamesTokens.get(sessionName).isEmpty()) {
						// Last user left: session must be removed
						this.mapSessions.remove(sessionName);

					}
					return Response.ok().build();
					//return new ResponseEntity<>(HttpStatus.OK);
				} else {
					// The TOKEN wasn't valid
					System.out.println("Problems in the app server: the TOKEN wasn't valid");
					return Response.serverError().build();
					//return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}

			} else {
				// The SESSION does not exist
				System.out.println("Problems in the app server: the SESSION does not exist");
				return Response.serverError().build();
				//return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
	 }
	 public void objetosViduPorSesion() {
		 //private Map<String, Session> mapSessions
		 Set salas = mapSessions.entrySet();
		 System.out.println("Sesiones:");
		 System.out.println(salas.toString());


	 }
}
