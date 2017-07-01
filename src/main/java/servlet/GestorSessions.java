package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.jboss.logging.Logger;

	@Startup
	@Singleton
	public class GestorSessions implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -124517853214941713L;
		Logger log = Logger.getLogger("GestorSessions");
	    private Map<String, Vector<UserBean>> sessions;
	    
	    //Mapa seguimiento jugadas Bingo por Sala (Sala,PocketBingo)
	    private Map<String, PocketBingo> jugadasSalas;

	    public PocketBingo getJugadasSalas(String sala) {
			PocketBingo pb = jugadasSalas.get(sala);
			return pb;
		}

		public void setJugadasSalas(String sala,PocketBingo pb) {
			jugadasSalas.put(sala, pb) ;
		}

		@PostConstruct
	    public void init() {
	        sessions = new ConcurrentHashMap<>();
	        
	      //se crea un mapa temporal al inicio
	      //cuando haya tiempo hay que leerlo del disco con LeeContext
	        jugadasSalas = new ConcurrentHashMap<>();
	        
	        
	        log.info("Gestor inicializado2");
	    }
		
		@PreDestroy
		public void finalize(){
			
			log.info("Guardando contexto pockets bingo");
		}
	    //Añade un nuevo elemento activo a la sesion dada, si no existe ya.(su Sesion)
	  
	    public void add(String user,UserBean userBean) {
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

	    
	    public Set<UserBean> dameUserBeans(String perfilAComparar){
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
	
	    public void remove(Session session) {
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
	            			//vectorUserBean.remove(ub);//borramos UserBean asociado a esta sesion websocket
	            			  itUsersBean.remove();
	            			
	            			log.info("Removida Sesion socket"+idSesionAComparar);
	            			
	            			if(vectorUserBean.size()==0){
	            				//sessions.remove(ub.getUsername());
	            				itClaves.remove();
	            			}
	            			log.info("Jugadores presentes3 :"+sessions.keySet().toString());
	            		  }
	             }
	         }
	        
	    }
	    
	    private PocketBingo leePocket(String sala, Session sesion){
		  	String ruta,fichero;
		  	PocketBingo aux=null;
		  	String uri=sesion.getRequestURI().toString();
		  	if(uri.equals("/wildfly-1.0/sala1")){
		  		ruta="C:\\\\put\\HTML5\\PocketBingo";
		  		fichero=ruta+"\\"+sala;
		  	}else{
	  				ruta = System.getenv("OPENSHIFT_DATA_DIR");
	  				fichero=ruta+sala;
		  	}
	        try
	        	{
		            // Se crea un ObjectInputStream
		            ObjectInputStream ois = new ObjectInputStream(
		                    new FileInputStream(fichero));
		            
		            // Se lee el primer objeto
		            aux = (PocketBingo)ois.readObject();
		            
		            ois.close();
		        }catch (Exception e1){
		           log.error("Problem serializacion File="+fichero);
		           e1.printStackTrace();
		        }
		        
		    
		    return aux; 
	  }
	    private void guardaContext(String sala, Session sesion, PocketBingo pb){
		  	String ruta,fichero;
		  	
		  	String uri=sesion.getRequestURI().toString();
		  	//log.info("la uri es:"+uri);
		  	if(uri.equals("/wildfly-1.0/sala1")){
		  		ruta="C:\\\\put\\HTML5\\PocketBingo";
		  		fichero=ruta+"\\"+sala;
		  	}else{
					ruta = System.getenv("OPENSHIFT_DATA_DIR");
					fichero=ruta+sala;
					log.info("ghuaradndo Pocket"+ fichero);
		  	}
		  try
	      {
	          ObjectOutputStream oos = new ObjectOutputStream(
	                  new FileOutputStream(fichero));
	          
	              oos.writeObject(pb);
	              
	          oos.close();
	      } catch (Exception e)
	      {
	          log.error("Excepcion Guarda Pocket "+ fichero);
	    	  e.printStackTrace();
	      }  
		  
	  }
	  private void borraPocket(String user, Session sesion){
		  String ruta,fichero;
		  	
		  	String uri=sesion.getRequestURI().toString();
		  	if(uri.equals("/wildfly-1.0/actions")){
		  		ruta="C:\\\\put\\HTML5\\PocketBingo";
		  		fichero=ruta+"\\"+user;
		  	}else{
					ruta = System.getenv("OPENSHIFT_DATA_DIR");
					fichero=ruta+user;
		  	}
		  	File fileUser= new File(fichero);
		  	fileUser.delete();
		  	
	  }

	}

