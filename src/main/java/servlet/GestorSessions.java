package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
                //Recuperacion contexto salas
			this.jugadasSalas=leeContext();
	        if(this.jugadasSalas==null){
	        	this.jugadasSalas = new ConcurrentHashMap<>();
	        	log.info("Gestor inicializado por jugadas salas =null");
	        }
                //Inicializacion sesiones
                //Aunque el saldo de usuario es un campo del Hashmap volatil
                //mantendremos el segumiento de saldo a traves de base de datos
                //manteniendo las transaccione del saldo directamente sobre la
                //base de datos de usuarios
                // Y sera a cada Login de usuario cuando se vuelque el dato de
                //la base a el bean UserBean
                this.sessions = new ConcurrentHashMap<>();
                log.info("Gestor :mapas cargados");
	        
	    }
		
		@Override
		@PreDestroy
		final public void finalize(){
			log.info("Guardando contexto pockets bingo");
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
		
	    //Añade un nuevo elemento activo a la sesion dada, si no existe ya.(su Sesion)
	  
	    public synchronized void add(String user,UserBean userBean) {
	    		String usuarioAComparar = userBean.getUsername();
	    		String perfilAComparar = userBean.getPerfil();
	    		Vector<UserBean> myVector = sessions.get(user);//

	    			if (myVector==null)myVector= new Vector<UserBean>(); 
		    		UserBean UserBeanUtilizado= sesionUtilizada(usuarioAComparar,perfilAComparar,userBean);
	    			myVector.add(UserBeanUtilizado);
	    			sessions.put(user, myVector);
	    			log.info("UserBean de user:"+UserBeanUtilizado.getUsername()+",perfil:"+UserBeanUtilizado.getPerfil()+" añadido a mapa para user:"+user);
	    		
	    			log.info("Jugadores presente:"+ sessions.keySet().toString() );
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
                Set<UserBean> usuarios = dameUserBeansEnPortal("jugador");
                Iterator it = usuarios.iterator();
                while(it.hasNext()){
                    UserBean ub = (UserBean) it.next();
                    if(ub.getSalonInUse().equals(sala)){
                        ub.setvCarton(new Vector<Carton>());
                        log.info("vector cartones inicializado (EndBalls) para sala"+ sala);
                    }
                }
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
	    

	    
	    public void comprobarLineas(String sala){
	    	//Nos bajamos un juego de userbeans jugando al bingo en las la sala dada.
	    	//Despues recorremos los cartones de cada userbean y comprobamos la linea
	    	// a cada carton premiado lo registramos con su userbean propietario
	    	Set<UserBean> userbeans = this.dameUserBeans("jugador",sala);
	    	PocketBingo pb = getJugadasSalas(sala);
	    	List<Integer>numerosCalled = pb.getNumerosCalled();
	    	int resultControlLinea;
	    	
	    		Iterator<UserBean> it =userbeans.iterator();
	    		HashMap<UserBean,Carton>  pilaAnunciaPremios = new HashMap<UserBean,Carton> ();
	    		while(it.hasNext()){

	    			UserBean user = it.next();
	    			Vector<Carton>vCarton = user.getvCarton();	
	    			Iterator<Carton> itCarton = vCarton.iterator();
	    			while(itCarton.hasNext()){
	    				Carton carton = (Carton)itCarton.next();
	    				int numeros[][] = carton.getNumeros();
	    				resultControlLinea=0;
	    				for(int f=0;f < 3; f++){
	    					resultControlLinea=0;
	    					for(int c=0; c<9 ; c++){
	    						int numero = numeros[f][c];
	    						if(numerosCalled.contains(numero)){
	    							//	Enviar mensaje de encender numero a Carton por numero OK (En cliente marcar el numero cono OK)
	    								try {
	    									
	    									user.getSesionSocket().getBasicRemote().sendText("numeroOK_"+carton.getnOrden()+"F"+(f+1)+"C"+(c+1));
	    								} catch (IOException e) {
										// 	TODO Auto-generated catch block
	    									e.printStackTrace();
	    								}
	    								if(f==0)resultControlLinea+=5;
	    								if(f==1)resultControlLinea+=50;	
	    								if(f==2)resultControlLinea+=500;	  
	    						}
	    					}
	    					if (resultControlLinea == 25 || resultControlLinea == 250 || resultControlLinea == 2500 ) {
	    							//	user.getSesionSocket().getBasicRemote().sendText("Hay Linea ,result:"+resultControlLinea);
	    							pilaAnunciaPremios.put(user, carton);
	    							log.info("Hay Linea ,result:"+resultControlLinea+" Fila "+ (f+1) + " Carton:" + carton.getnRef());
	    							//	user.getSesionSocket().getBasicRemote().sendText("Linea OK, Enhorabuena recoja su premio");
	    							
	    							//	lineaOK de user user en carton con ref nRef en fila f
	    					}else{
	    						try {
	    							user.getSesionSocket().getBasicRemote().sendText("No hay Linea, Continuamos ,result:"+resultControlLinea);
	    							log.info("No Hay Linea ,result:"+resultControlLinea +" Fila "+ (f+1) +" Carton:" + carton.getnRef());
	    						} catch (IOException e) {
	    							// 	TODO Auto-generated catch block
	    							e.printStackTrace();
	    						}
	    					}
	    				}
	    			}                                                                                                                                                                                                    
	    					
	    			
	    			
	    		}
	    		Set<UserBean> userBeansPremiados = pilaAnunciaPremios.keySet();
	    		Iterator<UserBean> itPremiados = userBeansPremiados.iterator();
	    		while(itPremiados.hasNext()){
	    			UserBean ubPremiado = itPremiados.next();
	    			Carton carton  = pilaAnunciaPremios.get(ubPremiado);
	    			try {
						ubPremiado.getSesionSocket().getBasicRemote().sendText("Premio Carton:"+carton.getnRef()+", enhorabuena");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    }

	    public void comprobarBingos(String sala){
	    	//Nos bajamos un juego de userbeans jugando al bingo en las la sala dada.
	    	//Despues recorremos los cartones de cada userbean y comprobamos la linea
	    	// a cada carton premiado lo registramos con su userbean propietario
	    	Set<UserBean> userbeans = this.dameUserBeans("jugador",sala);
	    	PocketBingo pb = getJugadasSalas(sala);
	    	List<Integer>numerosCalled = pb.getNumerosCalled();
	    	int resultControlLinea;
	    	
	    		Iterator<UserBean> it =userbeans.iterator();
	    		HashMap<UserBean,Carton>  pilaAnunciaPremios = new HashMap<UserBean,Carton> ();
	    		while(it.hasNext()){

	    			UserBean user = it.next();
	    			Vector<Carton>vCarton = user.getvCarton();	
	    			Iterator<Carton> itCarton = vCarton.iterator();
	    			while(itCarton.hasNext()){
	    				Carton carton = (Carton)itCarton.next();
	    				int numeros[][] = carton.getNumeros();
	    				resultControlLinea=0;
	    				for(int f=0;f < 3; f++){
	    					
	    					for(int c=0; c<9 ; c++){
	    						int numero = numeros[f][c];
	    						if(numerosCalled.contains(numero)){
	    							//	Enviar mensaje de encender numero a Carton por numero OK (En cliente marcar el numero cono OK)
	    								try {
	    									
	    									user.getSesionSocket().getBasicRemote().sendText("numeroOK_"+carton.getnOrden()+"F"+(f+1)+"C"+(c+1));
	    								} catch (IOException e) {
										// 	TODO Auto-generated catch block
	    									e.printStackTrace();
	    								}
	    								if(f==0)resultControlLinea+=5;
	    								if(f==1)resultControlLinea+=50;	
	    								if(f==2)resultControlLinea+=500;	  
	    						}
	    					}

	    				}
    					if (resultControlLinea == 2775) {
							//	user.getSesionSocket().getBasicRemote().sendText("Hay Linea ,result:"+resultControlLinea);
							pilaAnunciaPremios.put(user, carton);
							log.info("Hay Bingo ,result:"+resultControlLinea+" Carton:" + carton.getnRef());
							//	user.getSesionSocket().getBasicRemote().sendText("Linea OK, Enhorabuena recoja su premio");
							
							//	lineaOK de user user en carton con ref nRef en fila f
					}else{
						try {
							user.getSesionSocket().getBasicRemote().sendText("No hay Bingo, Continuamos ,result:"+resultControlLinea);
							log.info("No Hay Bingo ,result:"+resultControlLinea +" Carton:" + carton.getnRef());
						} catch (IOException e) {
							// 	TODO Auto-generated catch block
							e.printStackTrace();
						}
					}	    				
	    			}                                                                                                                                                                                                    
	    					
	    			
	    			
	    		}
	    		Set<UserBean> userBeansPremiados = pilaAnunciaPremios.keySet();
	    		Iterator<UserBean> itPremiados = userBeansPremiados.iterator();
	    		while(itPremiados.hasNext()){
	    			UserBean ubPremiado = itPremiados.next();
	    			Carton carton  = pilaAnunciaPremios.get(ubPremiado);
	    			try {
						ubPremiado.getSesionSocket().getBasicRemote().sendText("Premio Carton:"+carton.getnRef()+", enhorabuena");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
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
	            			log.info("Coleccionado UserBean de usuario:"+ub.getUsername()+" perfil:"+ perfil + "socketSesion:"+ub.getSesionSocket().getId());
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
	            			  log.info("Coleccionado UserBean por usuario:"+ub.getUsername() +" y perfil "+perfil+" y iDHttpSession ");  
	            			return ub;
	            			
	            		  }
	             }
	         }
	    	
	    	return ub;
	    }            	    
	    public synchronized void remove(Session session) {
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
	    public synchronized void removeUserBean(HttpSession session) {
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
	            		  if(idSession.equals(idSesionAComparar)){
	            			//vectorUserBean.remove(ub);//b
                                         
	            			  itUsersBean.remove();
	            			
	            			log.info("Removido userbean por atributo HttpSession invalidado");
	            			
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


    

	}

