package servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

public class ComprobadorPremios {
	static Logger log = Logger.getLogger("ComprobadorPremios");
	
	private GestorSessions gestorSesions;
	
	public ComprobadorPremios(GestorSessions gs){
		this.gestorSesions=gs;
	}	

	public synchronized boolean comprobarLineas(String sala) throws InterruptedException{
    	//Nos bajamos un juego de userbeans jugando al bingo en las la sala dada.
    	//Despues recorremos los cartones de cada userbean y comprobamos la linea
    	//a cada carton premiado lo registramos con su userbean propietario
    	Set<UserBean> userbeans = gestorSesions.dameUserBeans("jugador",sala);
    	PocketBingo pb = gestorSesions.getJugadasSalas(sala);
    	List<Integer>numerosCalled = pb.getNumerosCalled();
    	PeticionPremio userBeanPeticiones=null;
    	int resultControlLinea;
    	boolean hayLinea=false;
    	
    		Iterator<UserBean> it = userbeans.iterator();
    		//HashMap<UserBean,Carton>  pilaAnunciaPremios = new HashMap<UserBean,Carton> ();
    		while(it.hasNext()){

    			UserBean user = it.next();
    			Vector<Carton>vCarton = user.getvCarton();	
    			Iterator<Carton> itCarton = vCarton.iterator();
    			while(itCarton.hasNext()){
    				Carton carton = (Carton)itCarton.next();
    				int numeros[][] = carton.getNumeros();
    				resultControlLinea=0;
    				//Thread.sleep(3000);
    				for(int f=0;f < 3; f++){
    					resultControlLinea=0;
    					for(int c=0; c<9 ; c++){
    						int numero = numeros[f][c];
    						if(numerosCalled.contains(numero)){
    							//Enviar mensaje de encender numero a Carton por numero OK (En cliente marcar el numero como OK)
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
    						userBeanPeticiones = gestorSesions.getListaPeticionesPremios().get(key);
    							if(!(userBeanPeticiones==null)){
    								if(userBeanPeticiones.getUserbean().getSalonInUse().equals(sala)&&userBeanPeticiones.getPremio().equals("Linea")){
    									// Debido a la naturaleza del Objeto Mapa, cada insercion de instancia userBeanPeticiones (put)machaca
    									// a cualquiera existente previamente, lo cual garantiza que solo un premio por usuario (el ultimo regitrado)
    									//se registra
    									
    									gestorSesions.getPilaAnunciaPremios(sala).put(userBeanPeticiones, carton);
    									
    	    							log.info("Hay Linea ,result:"+resultControlLinea+" Fila "+ (f+1) + " Carton:" + carton.getnRef());
    	    							log.info("tamaño en Pila ahora("+gestorSesions.getPilaAnunciaPremios(sala).size()+")");
    	    							try {
											user.getSesionSocket().getBasicRemote().sendText("Hay premio Linea en Carton:"+carton.getnRef()+", Enhorabuena ");
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
    								//Para no borrar un premio anunciado antes, solo se procesa esta parte si no ha habido
    								//premio antes.
    								if(esteUsuarioYaTienePremio(userBeanPeticiones,gestorSesions.getPilaAnunciaPremios(sala))){
    									
    								}else{
    									user.getSesionSocket().getBasicRemote().sendText("No tienes Linea... ");
    								}
    								
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
	private boolean esteUsuarioYaTienePremio(PeticionPremio pp, Map<PeticionPremio,Carton> anunciaPremios){
		boolean result = false;
		if(pp==null)return result;
			Set<PeticionPremio> setAnunciaPremios = anunciaPremios.keySet();
			Iterator<PeticionPremio> it = setAnunciaPremios.iterator();
			while(it.hasNext()){
				PeticionPremio ppPremiado = (PeticionPremio)it.next();
				if(ppPremiado==pp)result=true;
			}
		return result;
	}
	
    public synchronized boolean comprobarLineaDeCarton(String sala,String nRef, UserBean user){
    	//Comprobacion de carton en faceta "super"

    	PocketBingo pb = gestorSesions.getJugadasSalas(sala);
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
										    Collection<Carton> cartones = gestorSesions.getPilaAnunciaPremios(sala).values();
										    Iterator itCartones = cartones.iterator();
										    while(itCartones.hasNext()){
										    	Carton esteCarton = (Carton)itCartones.next();
										    	String nRefDeEste =esteCarton.getnRef()+""; 
										
										    	if (nRef.equals(nRefDeEste))return false;
										    }
											gestorSesions.getPilaAnunciaPremios(sala).put(peticionLineaManual, carton);
											
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
    public synchronized boolean comprobarBingos(String sala) throws InterruptedException{
    	//Nos bajamos un juego de userbeans jugando al bingo en las la sala dada.
    	//Despues recorremos los cartones de cada userbean y comprobamos el Bingo
    	// a cada carton premiado lo registramos con su userbean propietario
    	Set<UserBean> userbeans = gestorSesions.dameUserBeans("jugador",sala);
    	PocketBingo pb = gestorSesions.getJugadasSalas(sala);
    	List<Integer>numerosCalled = pb.getNumerosCalled();
    	int resultControlBingo;
    	boolean hayBingo=false;
    	PeticionPremio userBeanPeticiones=null;;   	
    	
    	Iterator<UserBean> it =userbeans.iterator();
    	//Iterador Usuarios
    	while(it.hasNext()){
    				
    			UserBean user = it.next();
    			Vector<Carton>vCarton = user.getvCarton();	
    			Iterator<Carton> itCarton = vCarton.iterator();
    			//Iterador Cartones de Usuario
    			
    			while(itCarton.hasNext()){
    				Carton carton = (Carton)itCarton.next();
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
    				//Si es un carton con Premio
					String key = user.getUsername();
    				userBeanPeticiones = gestorSesions.getListaPeticionesPremios().get(key);
    				if (resultControlBingo == 2775 ) {
						//	user.getSesionSocket().getBasicRemote().sendText("Hay Linea ,result:"+resultControlLinea);
    
    					
    					//Si se ha solicitado el Bingo 
    					if(!(userBeanPeticiones==null)){
							if(userBeanPeticiones.getUserbean().getSalonInUse().equals(sala)&&userBeanPeticiones.getPremio().equals("Bingo")){
								gestorSesions.getPilaAnunciaPremios(sala).put(userBeanPeticiones, carton);
								
    							log.info("Hay Bingo ,result:"+resultControlBingo + " Carton:" + carton.getnRef());
    							log.info("tamaño en Pila ahora("+gestorSesions.getPilaAnunciaPremios(sala).size()+")");
    							try {
									user.getSesionSocket().getBasicRemote().sendText("Hay premio Bingo en Carton:"+carton.getnRef()+", Enhorabuena ");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
    							hayBingo=true;
							}
						//Si tiene Bingo y no le ha solicitado, le damos una oportunidad
    					}else{
							try {
								user.getSesionSocket().getBasicRemote().sendText("! Habia Bingo en Carton "+ carton.getnRef()+"  y no le cantas? ... ¡");
								Thread.sleep(5000);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						}
    				//Si no es un carton con Bingo y se ha solicitado la peticion de premio ...(sino no hago nada)
					}else{
					
						try {
							if(!(userBeanPeticiones==null)){
								if(userBeanPeticiones.getUserbean().getSalonInUse().equals(sala)&&userBeanPeticiones.getPremio().equals("Bingo")){
									//Si este carton no tiene Premio, pero si hay ya algun otro carton premiado de este usuario
									if(esteUsuarioYaTienePremio(userBeanPeticiones,gestorSesions.getPilaAnunciaPremios(sala))){

									}else{
											log.info("No Hay Bingo ,result:"+resultControlBingo +" Carton:" + carton.getnRef());
											user.getSesionSocket().getBasicRemote().sendText("No tienes Bingo... ");
									}		
								}						
							}
						} catch (IOException e) {
							// 		TODO Auto-generated catch block
							e.printStackTrace();
						}
				   }
    		}	                                                                                                                                                                                                    
    	}
    	return hayBingo;
    } 
    public synchronized boolean comprobarBingoDeCarton(String sala,String nRef,  UserBean user){
    	//Comprobacion de carton en faceta "super"

    	PocketBingo pb = gestorSesions.getJugadasSalas(sala);
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
						    Collection<Carton> cartones = gestorSesions.getPilaAnunciaPremios(sala).values();
						    Iterator itCartones = cartones.iterator();
						    while(itCartones.hasNext()){
						    	Carton esteCarton = (Carton)itCartones.next();
						    	String nRefDeEste =esteCarton.getnRef()+""; 
						
						    	if (nRef.equals(nRefDeEste))return false;
						    }
							gestorSesions.getPilaAnunciaPremios(sala).put(peticionBingoManual, carton);
							
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
}
