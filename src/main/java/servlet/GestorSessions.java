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
import java.util.Collection;
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
	    
	    //Referencias a hilos activos de salas
	    private Map<String,Thread> hiloSala ;
	    
	    //Mapa de peticiones comprobacion premios por usuarios
	             //username PeticionPremio<UserBean,"LInea o Bingo">
	    private Map<String,PeticionPremio> listaPeticionesPremios;
	    
	    //Mapa de premios comprobados de todas las salas (Ojo filtrar por sala)(Liquidacion de premios)
	    private Map<PeticionPremio,Carton>  pilaAnunciaPremios;
	    

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
                //Recuperacion contexto salas
			this.jugadasSalas=leeContext();
	        if(this.jugadasSalas==null){
	        	this.jugadasSalas = new ConcurrentHashMap<>();
	        	log.info("Gestor inicializado por jugadas salas =null");
	        }

                this.sessions = new ConcurrentHashMap<>();
                log.info("Gestor :mapas cargados");
                
                this.hiloSala = new ConcurrentHashMap<>();
                this.listaPeticionesPremios = new ConcurrentHashMap<>();
                this.pilaAnunciaPremios = new ConcurrentHashMap<>();
                
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
	    
	    public synchronized boolean comprobarLineaDeCarton(String sala,String nRef, UserBean user){
	    	//Comprobacion de carton en faceta "super"

	    	PocketBingo pb = getJugadasSalas(sala);
	    	List<Integer>numerosCalled = pb.getNumerosCalled();
	    	int resultControlLinea;
	    	boolean hayLinea=false;
	    				
	    				Carton carton = new Carton().consultaObjetoCarton(nRef);
	    			
	    				int numeros[][] = carton.getNumeros();
	    				resultControlLinea=0;
	    				//Thread.sleep(1000);
	    				for(int f=0;f < 3; f++){
	    					resultControlLinea=0;
	    					for(int c=0; c<9 ; c++){
	    						int numero = numeros[f][c];
	    						if(numerosCalled.contains(numero)){
	    							//	Enviar mensaje de encender numero a Carton por numero OK (En cliente marcar el numero cono OK)
	    							//De momento no lo enviamos/
	
	    								if(f==0)resultControlLinea+=5;
	    								if(f==1)resultControlLinea+=50;	
	    								if(f==2)resultControlLinea+=500;	  
	    						}
	    					}
	    					if (resultControlLinea == 25 || resultControlLinea == 250 || resultControlLinea == 2500 ) {
	    	    							try {
												user.getSesionSocket().getBasicRemote().sendText("Hay premio Linea, Enhorabuena ");

												String key = user.getUsername();	//PeticionPremio userBeanPeticiones = this.listaPeticionesPremios.get(key);
												PeticionPremio peticionLineaManual = new PeticionPremio();
												peticionLineaManual.setPremio("Linea");
												peticionLineaManual.setUserbean(user);
												//Un control para no repetir el registro de premio sobre el mismo carton
											    Collection<Carton> cartones = pilaAnunciaPremios.values();
											    Iterator itCartones = cartones.iterator();
											    while(itCartones.hasNext()){
											    	Carton esteCarton = (Carton)itCartones.next();
											    	String nRefDeEste =esteCarton.getnRef()+""; 
											
											    	if (nRef.equals(nRefDeEste))return false;
											    }
												pilaAnunciaPremios.put(peticionLineaManual, carton);
												
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
	    	    							hayLinea=true;
	    	    							f=3;
	    					}else{
	    						//if(!hayLinea){
	    							try {
	    								user.getSesionSocket().getBasicRemote().sendText("No tienes Linea... ");
	    								log.info("No Hay Linea ,result:"+resultControlLinea +" Carton:" + carton.getnRef());
	    							} catch (IOException e) {
	    								// 		TODO Auto-generated catch block
	    								e.printStackTrace();
	    							}
	    					}
	    				}

	    return hayLinea;
	    }
	    
	    public Map<PeticionPremio, Carton> getPilaAnunciaPremios() {
			return pilaAnunciaPremios;
		}

		public void setPilaAnunciaPremios(Map<PeticionPremio, Carton> pilaAnunciaPremios) {
			this.pilaAnunciaPremios = pilaAnunciaPremios;
		}

		public synchronized boolean comprobarLineas(String sala) throws InterruptedException{
	    	//Nos bajamos un juego de userbeans jugando al bingo en las la sala dada.
	    	//Despues recorremos los cartones de cada userbean y comprobamos la linea
	    	// a cada carton premiado lo registramos con su userbean propietario
	    	Set<UserBean> userbeans = this.dameUserBeans("jugador",sala);
	    	PocketBingo pb = getJugadasSalas(sala);
	    	List<Integer>numerosCalled = pb.getNumerosCalled();
	    	int resultControlLinea;
	    	boolean hayLinea=false;
	    	
	    		Iterator<UserBean> it =userbeans.iterator();
	    		//HashMap<UserBean,Carton>  pilaAnunciaPremios = new HashMap<UserBean,Carton> ();
	    		while(it.hasNext()){

	    			UserBean user = it.next();
	    			Vector<Carton>vCarton = user.getvCarton();	
	    			Iterator<Carton> itCarton = vCarton.iterator();
	    			while(itCarton.hasNext()){
	    				Carton carton = (Carton)itCarton.next();
	    				int numeros[][] = carton.getNumeros();
	    				resultControlLinea=0;
	    				Thread.sleep(1000);
	    				for(int f=0;f < 3; f++){
	    					resultControlLinea=0;
	    					for(int c=0; c<9 ; c++){
	    						int numero = numeros[f][c];
	    						if(numerosCalled.contains(numero)){
	    							//	Enviar mensaje de encender numero a Carton por numero OK (En cliente marcar el numero cono OK)
	    							//De momento no lo enviamos/
	    							
	    							   try {
	    									
	    									user.getSesionSocket().getBasicRemote().sendText("numeroOK_"+carton.getnOrden()+"F"+(f+1)+"C"+(c+1));
	    									//Thread.sleep(1000);
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
	    						String key = user.getUsername();
	    						PeticionPremio userBeanPeticiones = this.listaPeticionesPremios.get(key);
	    							if(!(userBeanPeticiones==null)){
	    								if(userBeanPeticiones.getUserbean().getSalonInUse().equals(sala)&&userBeanPeticiones.getPremio().equals("Linea")){
	    									pilaAnunciaPremios.put(userBeanPeticiones, carton);
	    									
	    	    							log.info("Hay Linea ,result:"+resultControlLinea+" Fila "+ (f+1) + " Carton:" + carton.getnRef());
	    	    							log.info("tamaño en Pila ahora("+pilaAnunciaPremios.size()+")");
	    	    							try {
												user.getSesionSocket().getBasicRemote().sendText("Hay premio Linea, Enhorabuena ");
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
	    	    							hayLinea=true;
	    	    							f=3;
	    								}

	    							}else{
    	    							try {
    										user.getSesionSocket().getBasicRemote().sendText("Habia Linea y no la has cantado ...");
    										f=3;
    									} catch (IOException e) {
    										// TODO Auto-generated catch block
    										e.printStackTrace();
    									}
    								
	    							}


	    							
	    					}else{
	    						//if(!hayLinea){
	    							try {
	    								user.getSesionSocket().getBasicRemote().sendText("No tienes Linea... ");
	    								log.info("No Hay Linea ,result:"+resultControlLinea +" Carton:" + carton.getnRef());
	    							} catch (IOException e) {
	    								// 		TODO Auto-generated catch block
	    								e.printStackTrace();
	    							}
	    						//}	
	    					}
	    				}
	    			}                                                                                                                                                                                                    
	    		}

	    		
	    		return hayLinea;
	    }
	    public boolean liquidacionPremios(String sala){
    		//ESto deberia ir en otra fase separada
    		//Tratamiento comprobacion peticiones premios
	    	boolean hayPremios=false;
	    	LiquidadorPremios lp = new LiquidadorPremios();
    		Set<PeticionPremio> userBeansPremiados = pilaAnunciaPremios.keySet();
    		Iterator<PeticionPremio> itPremiados = userBeansPremiados.iterator();
    		log.info("Liquidando premios ... tamaño en Pila("+userBeansPremiados.size()+")");
    		while(itPremiados.hasNext()){
    			log.info("En el iterador hay objetos premio");
    			PeticionPremio pp =  itPremiados.next();
    			UserBean ubPremiado =pp.getUserbean();
    			
    			if(ubPremiado.getSalonInUse().equals(sala)){
    				Carton carton  = pilaAnunciaPremios.get(pp);
    				try {
    					//Si la transaccion de liquidacion se completa. lo anunciamos
    					float premioCobrado = lp.saldarPremio(sala, ubPremiado, pp.getPremio());
    					if(premioCobrado>0){
    						ubPremiado.getSesionSocket().getBasicRemote().sendText("LiquidandoPremio");
    						ubPremiado.getSesionSocket().getBasicRemote().sendText("!Premio "+pp.getPremio()+"("+premioCobrado+" €)¡ Carton:"+carton.getnRef()+", enhorabuena");
    					}
     					
    					hayPremios=true;
    					
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
    			try {
					Thread.sleep(4500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		this.borrarListaPeticionPremios(sala);
    		this.borrarListaPremiosLiquidados(sala);
    		log.info("Hay premios :" + hayPremios);
    		return hayPremios;
	    }
	    
	    public synchronized boolean comprobarBingoDeCarton(String sala,String nRef,  UserBean user){
	    	//Comprobacion de carton en faceta "super"

	    	PocketBingo pb = getJugadasSalas(sala);
	    	List<Integer>numerosCalled = pb.getNumerosCalled();
	    	int resultControlBingo;
	    	boolean hayBingo=false;

	    				Carton carton = new Carton().consultaObjetoCarton(nRef);
	    			
	    				int numeros[][] = carton.getNumeros();
	    				resultControlBingo=0;
	    				//Thread.sleep(1000);
	    				for(int f=0;f < 3; f++){
	    					
	    					for(int c=0; c<9 ; c++){
	    						int numero = numeros[f][c];
	    						if(numerosCalled.contains(numero)){
	    							//	Enviar mensaje de encender numero a Carton por numero OK (En cliente marcar el numero cono OK)
	    							
	    							   try {
	    									
	    									user.getSesionSocket().getBasicRemote().sendText("numeroOK_"+carton.getnOrden()+"F"+(f+1)+"C"+(c+1));
	    									//Thread.sleep(1000);
	    								} catch (IOException e) {
										// 	TODO Auto-generated catch block
	    									e.printStackTrace();
	    								}
	    								if(f==0)resultControlBingo+=5;
	    								if(f==1)resultControlBingo+=50;	
	    								if(f==2)resultControlBingo+=500;	  
	    						}
	    					}
	    				}
	    				log.info("Result Control Bingo de "+ user.getUsername()+ " y carton " + carton.getnRef() + " = " + resultControlBingo  );
    					if (resultControlBingo == 2775 ) {
							try {
								user.getSesionSocket().getBasicRemote().sendText("Hay premio Bingo, Enhorabuena ");

								String key = user.getUsername();	//PeticionPremio userBeanPeticiones = this.listaPeticionesPremios.get(key);
								PeticionPremio peticionBingoManual = new PeticionPremio();
								peticionBingoManual.setPremio("Bingo");
								peticionBingoManual.setUserbean(user);
								//Un control para no repetir el registro de premio sobre el mismo carton
							    Collection<Carton> cartones = pilaAnunciaPremios.values();
							    Iterator itCartones = cartones.iterator();
							    while(itCartones.hasNext()){
							    	Carton esteCarton = (Carton)itCartones.next();
							    	String nRefDeEste =esteCarton.getnRef()+""; 
							
							    	if (nRef.equals(nRefDeEste))return false;
							    }
								pilaAnunciaPremios.put(peticionBingoManual, carton);
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							hayBingo=true;
							
    					}else{
    						//if(!hayLinea){
    						try {
    							user.getSesionSocket().getBasicRemote().sendText("No tienes Bingo... ");
    							log.info("No Hay Bingo ,result:"+resultControlBingo +" Carton:" + carton.getnRef());
    						} catch (IOException e) {
    							// 		TODO Auto-generated catch block
    							e.printStackTrace();
    						}
			}
	    				
    				return hayBingo;
	    }
	    
	    public synchronized boolean comprobarBingos(String sala) throws InterruptedException{
	    	//Nos bajamos un juego de userbeans jugando al bingo en las la sala dada.
	    	//Despues recorremos los cartones de cada userbean y comprobamos el Bingo
	    	// a cada carton premiado lo registramos con su userbean propietario
	    	Set<UserBean> userbeans = this.dameUserBeans("jugador",sala);
	    	PocketBingo pb = getJugadasSalas(sala);
	    	List<Integer>numerosCalled = pb.getNumerosCalled();
	    	int resultControlBingo;
	    	boolean hayBingo=false;
	    	
	    		Iterator<UserBean> it =userbeans.iterator();
	    		//HashMap<UserBean,Carton>  pilaAnunciaPremios = new HashMap<UserBean,Carton> ();
	    		while(it.hasNext()){

	    			UserBean user = it.next();
	    			Vector<Carton>vCarton = user.getvCarton();	
	    			Iterator<Carton> itCarton = vCarton.iterator();
	    			while(itCarton.hasNext()){
	    				Carton carton = (Carton)itCarton.next();
	    				int numeros[][] = carton.getNumeros();
	    				resultControlBingo=0;
	    				Thread.sleep(1000);
	    				for(int f=0;f < 3; f++){
	    					
	    					for(int c=0; c<9 ; c++){
	    						int numero = numeros[f][c];
	    						if(numerosCalled.contains(numero)){
	    							//	Enviar mensaje de encender numero a Carton por numero OK (En cliente marcar el numero cono OK)
	    							
	    							   try {
	    									
	    									user.getSesionSocket().getBasicRemote().sendText("numeroOK_"+carton.getnOrden()+"F"+(f+1)+"C"+(c+1));
	    									//Thread.sleep(1000);
	    								} catch (IOException e) {
										// 	TODO Auto-generated catch block
	    									e.printStackTrace();
	    								}
	    								if(f==0)resultControlBingo+=5;
	    								if(f==1)resultControlBingo+=50;	
	    								if(f==2)resultControlBingo+=500;	  
	    						}
	    					}
	    				}
	    				log.info("Result Control Bingo de "+ user.getUsername()+ " y carton " + carton.getnRef() + " = " + resultControlBingo  );
    					if (resultControlBingo == 2775 ) {
							//	user.getSesionSocket().getBasicRemote().sendText("Hay Linea ,result:"+resultControlLinea);
						String key = user.getUsername();
						PeticionPremio userBeanPeticiones = this.listaPeticionesPremios.get(key);
							if(!(userBeanPeticiones==null)){
								if(userBeanPeticiones.getUserbean().getSalonInUse().equals(sala)&&userBeanPeticiones.getPremio().equals("Bingo")){
									pilaAnunciaPremios.put(userBeanPeticiones, carton);
									
	    							log.info("Hay Bingo ,result:"+resultControlBingo + " Carton:" + carton.getnRef());
	    							log.info("tamaño en Pila ahora("+pilaAnunciaPremios.size()+")");
	    							try {
										user.getSesionSocket().getBasicRemote().sendText("Hay premio Bingo, Enhorabuena ");
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
	    							hayBingo=true;
								}

							}else{
    							try {
									user.getSesionSocket().getBasicRemote().sendText("Habia Bingo y no le has cantado ...");
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							
							}
					}else{
						
							try {
								user.getSesionSocket().getBasicRemote().sendText("No tienes Bingo... ");
								log.info("No Hay Bingo ,result:"+resultControlBingo +" Carton:" + carton.getnRef());
							} catch (IOException e) {
								// 		TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
	    				
	    			}                                                                                                                                                                                                    
	    		}
	    		return hayBingo;
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
		  	log.info("Host bingo:"+System.getenv("bingo_SERVICE_HOST"));
		  	log.info("Host mysql:"+System.getenv("svc/mysql_SERVICE_HOST"));
		  	log.info("MYSQL_USER:"+System.getenv("MYSQL_USER"));		  	
		  	log.info("MYSQL_PASSWORD:"+System.getenv("MYSQL_PASSWORD"));		  	
		  	log.info("MYSQL_ROOT_PASSWORD:"+System.getenv("MYSQL_USER"));		  	
		  	

		  	
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

