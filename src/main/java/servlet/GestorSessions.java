package servlet;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.jboss.logging.Logger;

@ApplicationScoped
	public class GestorSessions {
		Logger log = Logger.getLogger("GestorSessions");
	    private Map<String, Vector<UserBean>> sessions;

	    @PostConstruct
	    public void init() {
	        sessions = new ConcurrentHashMap<>();
	        
	        log.info("Gestor inicializado2");
	    }
	    //Añade un nuevo elemento activo a la sesion dada, si no existe ya.(su Sesion)
	  
	    void add(String user,UserBean userBean) {
	    		String idSesionAComparar = userBean.getSesionHttp().getId();
	    		String perfilAComparar = userBean.getPerfil();
	    		Boolean sesionUtilizada = sesionUtilizada(idSesionAComparar,perfilAComparar);
	    		if(!sesionUtilizada){	
	    			Vector<UserBean> myVector = sessions.get(user);
	    			if (myVector==null)myVector= new Vector<UserBean>(); 
	    			myVector.add(userBean);
	    			sessions.put(user, myVector);
	    			log.info("UserBean de user:"+userBean.getUsername()+",perfil:"+userBean.getPerfil()+" añadido a mapa para user:"+user);
	    			
	    		}else{
	    			log.info("Pila  UserBean inalterada");
	    		}
	    		log.info("Jugadores presente:"+ sessions.keySet().toString() );
	    }
	    //Comprueba si la sesion Http esta utilizada y el perfil
	
	    public boolean sesionUtilizada(String  idSesionHttpAComparar,String perfilAComparar){
	    	
	    	boolean siHaySesionUtilizada=false;

	    	
	    	//String  idSesionHttpAComparar = userBean.getSesionHttp().getId();
	    	//String perfilAComparar = userBean.getPerfil();
	    	//String usuarioAComparar = userBean.getUsername();
	    	
	    	Set<String> juegoClaves= sessions.keySet();	    	
	    	Iterator<String> itClaves = juegoClaves.iterator();
	    	
	         while (itClaves.hasNext()){
	             String usuarios = itClaves.next();
	             Vector<UserBean> vectorUserBean =sessions.get(usuarios);
	             Iterator<UserBean> itUsersBean = vectorUserBean.iterator();
	             while(itUsersBean.hasNext()){

	            		  UserBean ub = (UserBean)itUsersBean.next();
	            		  String perfil = ub.getPerfil();
	            		  String idSessionHttp = ub.getSesionHttp().getId();
	            		  if(perfil.equals(perfilAComparar) && idSessionHttp.equals(idSesionHttpAComparar)){
	            			 siHaySesionUtilizada=true;
	            			 log.info("Si hay esta sesion y perfil iniciados");
	            		  }else{
	            			  log.info("No hay esta sesion y perfil iniciados");
	            		  }
	             }
	         }
	         return siHaySesionUtilizada; 
	    }

	    
	    Set<UserBean> dameUserBeans(String perfilAComparar){
	    	Set<UserBean> juegoUserBeans = new LinkedHashSet<UserBean>();
	    	Set<String> juegoClaves= sessions.keySet();	    	
	    	Iterator<String> itClaves = juegoClaves.iterator();
	         while (itClaves.hasNext()){
	             String usuarios = itClaves.next();
	             Vector<UserBean> vectorUserBean =sessions.get(usuarios);
	             Iterator<UserBean> itUsersBean = vectorUserBean.iterator();
	             while(itUsersBean.hasNext()){

	            		  UserBean ub = (UserBean)itUsersBean.next();
	            		  String perfil = ub.getPerfil();
	            		  if(perfil.equals(perfilAComparar)){
	            			juegoUserBeans.add(ub);
	            			log.info("Coleccionado UserBean de usuario:"+ub.getUsername()+" perfil:"+ perfil + "socketSesion:"+ub.getSesionSocket().getId());
	            		  }
	             }
	         }
	    	
	    	return juegoUserBeans;
	    }
	
	    void remove(Session session) {
	    	String idSesionAComparar = session.getId();
	    	Set<String> juegoClaves= sessions.keySet();	    	
	    	Iterator<String> itClaves = juegoClaves.iterator();
	    	
	    	
	         while (itClaves.hasNext()){
	             String usuarios = itClaves.next();
	             Vector<UserBean> vectorUserBean =sessions.get(usuarios);
	             Iterator<UserBean> itUsersBean = vectorUserBean.iterator();
	             while(itUsersBean.hasNext()){

	            		  UserBean ub = (UserBean)itUsersBean.next();
	            		  String idSession = ub.getSesionSocket().getId();
	            		  if(idSession.equals(idSesionAComparar)){
	            			vectorUserBean.remove(ub);//borramos UserBean asociado a esta sesion websocket
	            			
	            			log.info("Removida Sesion socket"+idSesionAComparar);
	            			
	            			if(vectorUserBean.size()==0){
	            				sessions.remove(ub.getUsername());
	            			}
	            			log.info("Jugadores presentes3 :"+sessions.keySet().toString());
	            		  }
	             }
	         }
	        
	    }

	}

