package servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

//import org.jboss.logging.Logger;

	@Startup
	@Singleton
	public class GestorSessions implements Serializable{
            /**
              * 
            */
            private static final long serialVersionUID = -124517853214941713L;
            Logger log = Logger.getLogger("GestorSessions");
		
        //Mapa seguimiento sesiones de usuario
	    private Map<String, Vector<UserBean>> sessions;
	    
	    //Mapa seguimiento jugadas Bingo por Sala (Sala,PocketBingo)
	    private ConcurrentHashMap<String, PocketBingo> jugadasSalas;
	    
	    //Referencias a hilos activos de salas
	    private Map<String,Thread> hiloSala ;
	    
	    //Mapa de peticiones comprobacion premios por usuarios
	           //username PeticionPremio<UserBean,"LInea o Bingo">
	    private Map<String,PeticionPremio> listaPeticionesPremios;
	    
	    //Mapa de premios comprobados de todas las salas (Ojo filtrar por sala)(Liquidacion de premios)
	    private Map<PeticionPremio,Carton>  pilaAnunciaPremios;
	    
	    //Pila de peticiones de compra Bonus
	    private Map<Long ,PeticionBonus> peticionesBonus;


		//Pila de peticiones de Liquidacion Bonus
	    private Map<Long ,PeticionLiquidacionBonus> peticionesLiquidacionBonus;	    

	    
	    public PeticionLiquidacionBonus getPeticionesLiquidacionBonus(Long idReg) {
			return this.peticionesLiquidacionBonus.get(idReg);
		}

	    public void registraPeticionBonus(Long idReg,PeticionBonus pb){
	    	this.peticionesBonus.put(idReg, pb);
	    }
	    public void registraPeticionLiquidacionBonus(Long idReg,PeticionLiquidacionBonus pb){
	    	this.peticionesLiquidacionBonus.put(idReg, pb);
	    }
	    public Set<Long> getPeticionLiquidacionBonus(){
	    	return this.peticionesLiquidacionBonus.keySet();
	    }	    
	    public PeticionLiquidacionBonus getPeticionLiquidacionBonus(Long idReg){
	    	return this.peticionesLiquidacionBonus.get(idReg);
	    }	    
	    public Set<Long> getPeticionBonus(){
	    	return this.peticionesBonus.keySet();
	    }	    
	    public PeticionBonus getPeticionBonus(Long idReg){
	    	return this.peticionesBonus.get(idReg);
	    }
	    public void borraPeticionLiquidacionBonus(Long idReg){
	    	this.peticionesLiquidacionBonus.remove(idReg);
	    }
	    public void borraPeticionBonus(Long idReg){
	    	this.peticionesBonus.remove(idReg);
	    }
	    
	    public Thread getHiloSala(String sala) {
			return this.hiloSala.get(sala);
		}

		public void addHiloSala(String sala, Thread hilo) {
			this.hiloSala.put(sala, hilo);
			log.info("Hilo de sala " +sala+" ,registrado");
		}

		public PocketBingo getJugadasSalas(String sala) {
            PocketBingo pb = jugadasSalas.get(sala);
            return pb;
        }

        public void setJugadasSalas(String sala,PocketBingo pb) {
        	jugadasSalas.put(sala, pb) ;
        }
        
		public Map<String,PeticionPremio> getListaPeticionesPremios() {
			return listaPeticionesPremios;
		}
		
		public void borrarListaPeticionPremios(String sala){
			Collection<PeticionPremio> setPeticionesSala = this.listaPeticionesPremios.values();
			Iterator<PeticionPremio> it = setPeticionesSala.iterator();
			while(it.hasNext()){
				PeticionPremio pp = (PeticionPremio)it.next();
				UserBean ub = pp.getUserbean();
				if(ub.getSalonInUse().equals(sala)){
					it.remove();
				}
			}
			
		}
		public void borrarListaPremiosLiquidados(String sala){
			Collection<PeticionPremio> setPremios = this.pilaAnunciaPremios.keySet();
			Iterator<PeticionPremio> it = setPremios.iterator();
			while(it.hasNext()){
				PeticionPremio pp = (PeticionPremio)it.next();
				UserBean ub = pp.getUserbean();
				if(ub.getSalonInUse().equals(sala)){
					it.remove();
				}
			}
			
		}

		public synchronized boolean addPeticionPremios(UserBean userbean,String premio) {
			boolean registrado = false;
			String key = userbean.getUsername();
			if(!(this.listaPeticionesPremios.containsKey(key))){
				PeticionPremio pp= new PeticionPremio();
				pp.setPremio(premio);
				pp.setUserbean(userbean);
				this.listaPeticionesPremios.put(key, pp);
				registrado=true;
			}
			return registrado;
		}       

		@PostConstruct
	    public void init() {
                //Recuperacion contexto salas Bingo
			this.jugadasSalas=readContext("bingo");
			//this.jugadasSalas=null;
	        if(this.jugadasSalas==null){
	        	this.jugadasSalas = new ConcurrentHashMap<>();
	        	log.info("Gestor inicializado por jugadas salas =null");
	        }else{
	        	log.info("Contexto recuperado de base de datos correctamente");
	        }

                this.sessions = new ConcurrentHashMap<>();
                this.hiloSala = new ConcurrentHashMap<>();
                this.listaPeticionesPremios = new ConcurrentHashMap<>();
                this.pilaAnunciaPremios = new ConcurrentHashMap<>();
                this.peticionesBonus = new ConcurrentHashMap<>();
                this.peticionesLiquidacionBonus = new ConcurrentHashMap<>();
	    }
		


		@Override
		@PreDestroy
		final public void finalize(){
			log.info("El resultado de registrarContexto ha sido : "+this.registraContexto("bingo",this.jugadasSalas) );			
			
		}
	    //Añade un nuevo elemento activo a la sesion dada, si no existe ya.(su Sesion)
	    public synchronized boolean add(String user,UserBean userBean) {
	    	boolean insertado = false;
	    	if( !(userBean.getStatusPlayer().equals("playingBingo"))){
	    	String usuarioAComparar = userBean.getUsername();
	    		String perfilAComparar = userBean.getPerfil();
	    		Vector<UserBean> myVector = sessions.get(user);//

	    			if (myVector==null)myVector= new Vector<UserBean>();
	    			UserBean ubam= sesionUtilizada(usuarioAComparar,perfilAComparar,userBean);
		    		//Vaya tela
		    			myVector.add(ubam);
		    			sessions.put(user, myVector);
		    			log.info("UserBean de user:"+userBean.getUsername()+",perfil:"+userBean.getPerfil()+" añadido a mapa para user:"+user);
		    			insertado=true;
		    			log.info("Jugadores presente:"+ sessions.keySet().toString() );
	    	}else{	
	    		insertado=false;
	    	}
	    	return insertado;
	    }
	    //Comprueba si la sesion Http esta utilizada y el perfil
	
	    public synchronized UserBean sesionUtilizada(String usuarioAComparar,String perfilAComparar,UserBean userbean){
	    	UserBean myUserBean=userbean;;

	    	
	    	String  idSesionHttpAComparar = userbean.getSesionHttp().getId();
	    	//String perfilAComparar = userBean.getPerfil();
	    	//String usuarioAComparar = userBean.getUsername();
	    	
	    	Set<String> juegoClaves= sessions.keySet();	    	
	    	Iterator<String> itClaves = juegoClaves.iterator();
	    	
	         while (itClaves.hasNext()){
	             String usuarios = itClaves.next();
	             Vector<UserBean> vectorUserBean =sessions.get(usuarios);
	             Iterator<UserBean> itUsersBean = vectorUserBean.iterator();
	             while(itUsersBean.hasNext()){

	            		  UserBean ub = itUsersBean.next();
	            		  String perfil = ub.getPerfil();
	            		  String usuario = ub.getUsername();
	            		  if(perfil.equals(perfilAComparar) && usuario.equals(usuarioAComparar)){
	            			 
	            			 //Conservamos la compra de cartones anterior del usuario
	            			 Vector<Carton> cartones= ub.getvCarton();
	            			 myUserBean.setvCarton(cartones);
	            			 if(idSesionHttpAComparar.equals(ub.getSesionHttp().getId())){
	            				 //Vamos a probar a no borrar la sesion duplicada, haber que pasa
	            				 itUsersBean.remove();
	            			 }
	            			 log.info("Si estaba este usuario y perfil iniciados(recuperando cartones)");
	            			 return myUserBean;
	            			 
	            		  }else{
	            			  log.info("No hay esta sesion y perfil iniciados");
	            		  }
	             }
	         }
	         return myUserBean; 

	    }

	    public synchronized void resetCartones(String sala){
            //Borrado de cartones automaticos    
	    	Set<UserBean> usuarios = dameUserBeansEnPortal("jugador");
                Iterator it = usuarios.iterator();
                while(it.hasNext()){
                    UserBean ub = (UserBean) it.next();
                    if(ub.getSalonInUse().equals(sala)){
                        ub.setvCarton(new Vector<Carton>());
                        log.info("vector cartones inicializado (EndBalls) para sala"+ sala);
                    }
                }
	    	//Borrado de cartones OffLine
	        this.getJugadasSalas(sala).resetCartonesUsuariosOffLine();
	    }
	    public synchronized Set<Carton> dameSetCartonesEnJuego(String sala){
                Set<Carton> setCartones = new LinkedHashSet<>();
                  Set<UserBean> usuarios = dameUserBeans("jugador");
                  Iterator<UserBean> it = usuarios.iterator();
                  while(it.hasNext()){
                    UserBean ub = (UserBean) it.next();
                    if(ub.getSalonInUse().equals(sala)){
                        Vector<Carton> vCartonesUser = ub.getvCarton();
                        for(int vC=0;vC<vCartonesUser.size();vC++){
                            Carton c = (Carton)vCartonesUser.get(vC);
                            setCartones.add(c);
                        }
                  }
                }
                return setCartones;
            }
            
	    public synchronized Map<String,Carton> dameMapUsuariosYCartonesEnJuego(String perfil,String sala){
                HashMap<String,Carton> relacion = new HashMap<>();
                  Set<UserBean> usuarios = dameUserBeans("jugador");
                  Iterator<UserBean> it = usuarios.iterator();
                  while(it.hasNext()){
                    UserBean ub = (UserBean) it.next();
                    if(ub.getPerfil().equals(perfil)&ub.getSalonInUse().equals(sala)){
                        Vector<Carton> vCartonesUser = ub.getvCarton();
                        	
                        	for(int vC=0;vC<vCartonesUser.size();vC++){
                        		Carton c = (Carton)vCartonesUser.get(vC);
                        		relacion.put(ub.getUsername(), c);
                        	}
                     
                  }
                }
                return relacion;
            }
	    

	    
	    public Map<PeticionPremio, Carton> getPilaAnunciaPremios(String sala) {
	    	Map<PeticionPremio,Carton> pilaAnunciaPremiosFiltered = this.pilaAnunciaPremios;
	    	Set<Map.Entry<PeticionPremio, Carton>> anunciaPremiosSala = pilaAnunciaPremiosFiltered.entrySet();
	    	Iterator<Map.Entry<PeticionPremio, Carton>> it = anunciaPremiosSala.iterator();
	    	while(it.hasNext()){
	    		Map.Entry<PeticionPremio,Carton> entryPP = (Map.Entry<PeticionPremio, Carton>)it.next();
	    		PeticionPremio pp = entryPP.getKey();
	    		UserBean ub = pp.getUserbean();
	    		if(!(ub.getSalonInUse().equals(sala)))it.remove();
	    		
	    	}
			return pilaAnunciaPremiosFiltered;
		}

		public void setPilaAnunciaPremios(Map<PeticionPremio, Carton> pilaAnunciaPremios) {
			this.pilaAnunciaPremios = pilaAnunciaPremios;
		}



	    

	    
            
	    public synchronized Set<UserBean> dameUserBeans(String perfilAComparar){
	    	Set<UserBean> juegoUserBeans = new LinkedHashSet<UserBean>();
	    	Set<String> juegoClaves= sessions.keySet();	    	
	    	Iterator<String> itClaves = juegoClaves.iterator();
	         while (itClaves.hasNext()){
	             String usuarios = itClaves.next();
	             Vector<UserBean> vectorUserBean =sessions.get(usuarios);
	             Iterator<UserBean> itUsersBean = vectorUserBean.iterator();
	             while(itUsersBean.hasNext()){

	            		  UserBean ub = itUsersBean.next();
	            		  
	            		  String perfil = ub.getPerfil();
	            		  if(perfil.equals(perfilAComparar)&&(ub.getStatusPlayer().equals("playingBingo"))){
	            			//
	            			juegoUserBeans.add(ub);
	            			log.info("Coleccionado UserBean de usuario:"+ub.getUsername()+" perfil:"+ perfil) ;
	            		  }
	             }
	             
	         }
	         return juegoUserBeans;
	    }
	    public synchronized Set<UserBean> dameUserBeans(String perfilAComparar, String sala){
	    	Set<UserBean> juegoUserBeans = new LinkedHashSet<UserBean>();
	    	Set<String> juegoClaves= sessions.keySet();	    	
	    	Iterator<String> itClaves = juegoClaves.iterator();
	         while (itClaves.hasNext()){
	             String usuarios = itClaves.next();
	             Vector<UserBean> vectorUserBean =sessions.get(usuarios);
	             Iterator<UserBean> itUsersBean = vectorUserBean.iterator();
	             while(itUsersBean.hasNext()){

	            		  UserBean ub = itUsersBean.next();
	            		  String salaInUse = ub.getSalonInUse();
	            		  String perfil = ub.getPerfil();
	            		  if(perfil.equals(perfilAComparar)&&(salaInUse.equals(sala))&&(ub.getStatusPlayer().equals("playingBingo"))){
	            			//
	            			juegoUserBeans.add(ub);
	            			log.info("Coleccionado UserBean de usuario:"+ub.getUsername()+" perfil:"+ perfil + "socketSesion:"+ub.getSesionSocket().getId());
	            		  }
	             }
	             
	         }
	         return juegoUserBeans;
	    }	    
	    public synchronized Set<UserBean> dameUserBeansEnPortal(String perfilAComparar){
		    	Set<UserBean> juegoUserBeans = new LinkedHashSet<UserBean>();
		    	Set<String> juegoClaves= sessions.keySet();	    	
		    	Iterator<String> itClaves = juegoClaves.iterator();
		         while (itClaves.hasNext()){
		             String usuarios = itClaves.next();
		             Vector<UserBean> vectorUserBean =sessions.get(usuarios);
		             Iterator<UserBean> itUsersBean = vectorUserBean.iterator();
		             while(itUsersBean.hasNext()){

		            		  UserBean ub = itUsersBean.next();
		            		  
		            		  String perfil = ub.getPerfil();
		            		  if(perfil.equals(perfilAComparar)){
		            			//
		            			juegoUserBeans.add(ub);
		            			log.info("Coleccionado UserBeans de usuario:Con sesion en Portal");
		            		  }
		             }
		         }	         
	    	
	    	return juegoUserBeans;
	    }
	    
	    public synchronized Set<UserBean> dameUserBeansPorUser(String userAComparar){
	    	Set<UserBean> juegoUserBeans = new LinkedHashSet<UserBean>();
	    	Set<String> juegoClaves= sessions.keySet();	    	
	    	Iterator<String> itClaves = juegoClaves.iterator();
	         while (itClaves.hasNext()){
	             String usuarios = itClaves.next();
	             Vector<UserBean> vectorUserBean =sessions.get(usuarios);
	             Iterator<UserBean> itUsersBean = vectorUserBean.iterator();
	             while(itUsersBean.hasNext()){

	            		  UserBean ub = itUsersBean.next();
	            		  String user = ub.getUsername();
	            		  if(user.equals(userAComparar)){
	            			juegoUserBeans.add(ub);
	            			log.info("Coleccionado UserBean por usuario:"+ub.getUsername() );
	            		  }
	             }
	         }
	    	
	    	return juegoUserBeans;
	    } 
	    public synchronized Vector<UserBean> dameVectorUserBeansUsuario(String user){
	    	return this.sessions.get(user);
	    }
	    public synchronized void setVectorUserBeansUsuario(String user, Vector<UserBean> vectorUsersBean){
	    	this.sessions.put(user, vectorUsersBean);
	    }
	    
	    public synchronized UserBean dameUserBeansPorUser(String userAComparar,String perfilAComparar,String idSession){
	    	UserBean ub = null;
	    	Set<String> juegoClaves= sessions.keySet();	    	
	    	Iterator<String> itClaves = juegoClaves.iterator();
	         while (itClaves.hasNext()){
	             String usuarios = itClaves.next();
	             Vector<UserBean> vectorUserBean =sessions.get(usuarios);
	             Iterator<UserBean> itUsersBean = vectorUserBean.iterator();
	             while(itUsersBean.hasNext()){

	            		  ub = itUsersBean.next();
	            		  String user = ub.getUsername();
	            		  String perfil = ub.getPerfil();
	            		  String idHttpSession=ub.getSesionHttp().getId();
	            		  if(user.equals(userAComparar)&&perfil.equals(perfilAComparar)&&idHttpSession.equals(idSession)){
	            			  log.info("Coleccionado UserBean por usuario:"+ub.getUsername() +" y perfil "+perfil+" y iDHttpSession : "+ idHttpSession);  
	            			return ub;
	            			
	            		  }else{
	            			 ub = null;
	            		  }
	             }
	         }
	    	
	    	return ub;
	    }
	    
	    public synchronized void remove(Session session) {
	    	//Utilizado para controlar la salida del ambito de juego Bingo ("notPlayinBingo"
	    	//El userbean permanece registrado ya que depende de la sesion Http.
	    	String idSesionAComparar = session.getId();
	    	Set<String> juegoClaves= sessions.keySet();	    	
	    	Iterator<String> itClaves = juegoClaves.iterator();
	    	
	    	
	         while (itClaves.hasNext()){
	             String usuarios = itClaves.next();
	             Vector<UserBean> vectorUserBean =sessions.get(usuarios);
	             Iterator<UserBean> itUsersBean = vectorUserBean.iterator();
	             log.info("Sesiones abiertas :");
	             while(itUsersBean.hasNext()){	

	            		  UserBean ub = itUsersBean.next();
	            		  Session itSesion=ub.getSesionSocket();
                          String idSession="";
                          if(itSesion==null){
                        	 
                          }else{
                        	  idSession=itSesion.getId();
                          }
	            		  if(idSession.equals(idSesionAComparar)){
	            			//vectorUserBean.remove(ub);//b
                                         
	            			  //itUsersBean.remove();
	            			  //Ahora, solo ajustamos status del userBean
	            			  ub.setStatusPlayer("notPlayingBingo");
	            		  }
	            			log.info(ub.getUsername() + ":" +ub.getStatusPlayer());
	             }
     			if(vectorUserBean.size()==0){
    				//sessions.remove(ub.getUsername());
    				itClaves.remove();
    			}

	         }
	        
	    }
	    public synchronized void removeUserBean(HttpSession session,String usuario) {
	    	//Utilizado para eliminar userbeans del mapa sessions.El usuario queda off_line(Sesion caducada)
	    	String idSesionAComparar = session.getId();
	    	Set<String> juegoClaves= sessions.keySet();	    	
	    	Iterator<String> itClaves = juegoClaves.iterator();
	    	
	    	
	         while (itClaves.hasNext()){
	             String usuarios = itClaves.next();
	             Vector<UserBean> vectorUserBean =sessions.get(usuarios);
	             Iterator<UserBean> itUsersBean = vectorUserBean.iterator();
	             while(itUsersBean.hasNext()){	

	            		  UserBean ub = itUsersBean.next();
	            		  //String idSession = ub.getSesionSocket().getId();
                                  String idSession = ub.getSesionHttp().getId();
                                  String userb = ub.getUsername();
	            		  if(idSession.equals(idSesionAComparar)&& userb.equals(usuario)){
	            			//vectorUserBean.remove(ub);//b
	            			  String usuarioInvalidado = ub.getUsername();
	            			  Session mySession = ub.getSesionSocket();
	            			  try {
	            				  if (!(mySession==null)){
	            					  mySession.getBasicRemote().sendText("SesionHttpCaducada");
	            				  }
							} catch (IOException e) {
								log.info(e.getMessage());
								//e.printStackTrace();
							}
	            			  itUsersBean.remove();
	            			  
	            			log.info("Removido userbean por atributo HttpSession invalidado :"+usuarioInvalidado);
	            			
	            			if(vectorUserBean.size()==0){
	            				//sessions.remove(ub.getUsername());
	            				itClaves.remove();
	            			}
	            			log.info("Sesiones abiertas :"+sessions.keySet().toString());
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
		           log.severe("Problem serializacion File="+fichero);
		           e1.printStackTrace();
		        }
		        
		    
		    return aux; 
	  }
	  private Map<String,PocketBingo> leeContext(){
		  	String ruta,fichero;
		  	Map<String,PocketBingo> aux=null;
		  	ruta = System.getenv("OPENSHIFT_DATA_DIR");

		  	
		  	if(ruta==null){
		  		ruta="C:\\\\put\\HTML5\\PocketBingo";
		  		fichero=ruta+"\\"+"MapaBingos";
		  	}else{
	  				
	  				fichero=ruta+"MapaBingos";
		  	}
	        try
	        	{
		            // Se crea un ObjectInputStream
		            ObjectInputStream ois = new ObjectInputStream(
		                    new FileInputStream(fichero));
		            
		            // Se lee el primer objeto
		            aux = (Map<String,PocketBingo>)ois.readObject();
		            
		            ois.close();
		            log.info("MapaBingos recuperado contexto");
		        }catch (Exception e1){
		           log.severe("Problem serializacion File="+fichero);
		           e1.printStackTrace();
		        }
		        
		    
		    return aux; 
	  }	    
	    public void guardaContext(){
		  	String ruta,fichero;
	
					ruta = System.getenv("OPENSHIFT_DATA_DIR");
					if(ruta==null){
						ruta="C:\\\\put\\HTML5\\PocketBingo\\";
					}
					fichero=ruta+"MapaBingos";//
					
		  
		  try
	      {
	          ObjectOutputStream oos = new ObjectOutputStream(
	                  new FileOutputStream(fichero));
	          
	              oos.writeObject(this.jugadasSalas);
	              log.info("guardando Pocket"+ fichero);
	              
	          oos.close();
	      } catch (Exception e)
	      {
	          log.severe("Excepcion Guarda Pocket "+ fichero);
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
	    public int registraContexto(String juego,ConcurrentHashMap<String, PocketBingo> contexto){
	        Connection myCon=null;
	        PreparedStatement ps=null;
	        int result=0;
	  
	        try {
	            myCon = ConnectionManager.getConnection();
	            Statement st = myCon.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                ObjectOutputStream oos;
                        try {
                            oos = new ObjectOutputStream(byteArray);
                            oos.writeObject(contexto);
                        } catch (IOException ex) {
                            Logger.getLogger("RegistroContexto").info("Ha habido problema al escribir sobre Output Stream:"+ ex);
                        }
        	    ResultSet rs = st.executeQuery("Select * From contexto Where juego = 'bingo'");                        
	            if(rs.next()){
	            	String update = "update contexto set contexto = ? where juego = ?";
	            	ps = myCon.prepareStatement(update);
	                ps.setBytes(1, byteArray.toByteArray());
	                ps.setString(2, "bingo");

	                result = ps.executeUpdate();
	            }else{
	            	ps = myCon.prepareStatement("insert into contexto values (?,?)");
	                ps.setString(1, "bingo");
	                ps.setBytes(2, byteArray.toByteArray());	
	                if(ps.execute()){
	                	ResultSet rs2 = ps.getResultSet();
		                if(rs2.next())result=1;
	                }
	            }

	        } catch (SQLException ex) {
                Logger.getLogger("RegistroContexto").info("Ha habido problema SQl a l registrar contexto:"+ ex);

	        }finally{
	            if(myCon!=null)try {
	                myCon.close();
	                
	            } catch (SQLException ex) {
                    Logger.getLogger("RegistroContexto").info("Ha habido problema finalmente al escribir sobre Output Stream:"+ ex);
	            }
	            if(ps!=null)try {
	                ps.close();
	            } catch (SQLException ex) {
                    Logger.getLogger("RegistroContexto").info("Ha habido problema finalmente 2 al cerrar conexiones:"+ ex);
	            }
	        }
	        return result;

	        

	    }
	    public ConcurrentHashMap<String, PocketBingo> readContext(String juego){
	        Connection con = ConnectionManager.getConnection();
	        Statement st=null;
	        ResultSet rs=null;
            ConcurrentHashMap<String, PocketBingo> myJuegosSalas=null;

	        try {
	            int idCarton;
	            String nCarton;
	            String nSerie;
	            Blob blob;
	             st = con.createStatement();
	             rs = st.executeQuery("Select contexto from contexto where juego='"+juego+"'");
	             if(rs.next()){
	                   // Se obtiene el campo blob
	                 blob = rs.getBlob(1);
	                // Se reconstruye el objeto con un ObjectInputStream
	                try {
	                    ObjectInputStream ois = new ObjectInputStream(blob.getBinaryStream());
	                     try {
	                         myJuegosSalas = (ConcurrentHashMap<String, PocketBingo>) ois.readObject();
	                     } catch (ClassNotFoundException ex) {
	                         Logger.getLogger("readContext(GestosSessions)").info("Ha habido problema en leerContexto:\n"+ex);
	                     }
	                   
	                } catch (IOException ex) {
                        Logger.getLogger("readContext(GestosSessions)").info("Ha habido problema(2) en leerContexto:\n"+ex);
	                }
	                
	             }
	        } catch (SQLException ex) {
                Logger.getLogger("readContext(GestosSessions)").info("Ha habido problemaSQL en leerContexto:\n"+ex);
	        }finally{
	            try {
	                if(con!=null)con.close();
	                if(st!=null)st.close();
	            } catch (SQLException ex) {
                    Logger.getLogger("readContext(GestosSessions)").info("Ha habido problema finalmente");
	            }
	           
	        }
	        return myJuegosSalas;//	    
	    }

}