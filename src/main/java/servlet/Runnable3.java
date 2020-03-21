package servlet;

import java.io.IOException;
import java.util.Iterator;

import java.util.Random;
import java.util.Set;
import java.util.Vector;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.swing.JTextArea;
import javax.websocket.Session;

/**
 *
 * @author hormigueras
 */
//public class Hilo2 extends Thread{
public class Runnable3 implements Runnable{
    Logger log = Logger.getLogger("HiloSala1");
    

	private LiquidadorPremio lp; 
	private ComprobadorPremios cp;
    
    JTextArea area2;
    PocketBingo pb;
    Session session;
    String status;
    int maxNumbers = 90;
    int orden;
    int delay= 0;
    int n=0;
    private String estaSalaEs="sala1";
	
    private GestorSessions gestorSesions;
    
    public Runnable3(Session sesion, int ndelay){
       this.session = sesion;
       this.delay = ndelay;
       
    }
    @Override
    public void run(){
    	log.info("IdState en 'run' putobucle5:" );
    	gestorSesions = (GestorSessions) session.getUserProperties().get("gestorSesiones");
    	lp = new LiquidadorPremio(this.gestorSesions);
    	cp = new ComprobadorPremios(this.gestorSesions);
        this.pb=gestorSesions.getJugadasSalas(estaSalaEs);
        status=pb.getIdState();
        
        
        synchronized(this){
        if(status.equals("NewGame")){
        	orden =1;
        	pb.setIdState("Started");
        	pb.setNumeroOrden(1);
    		this.enviarMensajeAPerfil("cantarNumero_BuenasNoches_0","supervisor");
    		this.enviarMensajeAPerfil("cantarNumero_BuenasNoches_0","tablero");    		
    		try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				
			}

        }else {//else if(status.equals("Started"))
        	Vector<?> listaNumeros=(Vector<?>) pb.getNumerosCalled();
    		int i;
        	for(i=0;i<listaNumeros.size();i++){
    			this.enviarMensajeAPerfil("EncenderNumero_"+listaNumeros.elementAt(i)+"_simple","supervisor");
    			this.enviarMensajeAPerfil("EncenderNumero_"+listaNumeros.elementAt(i)+"_simple","tablero");    			
    			this.enviarMensajeAPerfil("EncenderNumero_"+listaNumeros.elementAt(i)+"_simple","jugador");
    			//this.enviarMensajeAPerfil("bolaJuego_"+(i+1));
    			try {
    				Thread.sleep(400);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
        	orden=pb.getNumeroOrden();
        	int number= pb.getNewBola();
        	//this.enviarMensajeAPerfil("bolaJuego_"+orden);
        	enviarMensajeAPerfil("cantarNumero_"+number+"_"+orden,"supervisor");
        	enviarMensajeAPerfil("cantarNumero_"+number+"_"+orden,"tablero");
        	enviarMensajeAPerfil("cantarNumero_"+number+"_"+orden,"jugador");
        	enviarMensajeAPerfil("EncenderNumero_"+number,"supervisor");
        	enviarMensajeAPerfil("EncenderNumero_"+number,"tablero");        	
        	enviarMensajeAPerfil("EncenderNumero_"+number,"jugador");
        	
        	if(!(pb.getNewBola()==0))pb.addNumerosCalled(pb.getNewBola());
        	try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
        	
        	orden=pb.getNumeroOrden()+1;;
        }
        }
    	for(int i=orden; i < maxNumbers+1 ;i++)   {
          log.info("IdState en inicio bucle"+pb.getIdState()); 
          
          n=0;// Garantizando que hay bloqueo de hilo,sino se ajusta lo contrario
          synchronized(this){
            try{
                if(pb.getIdState().equals("Linea")){
                	enviarMensajeAPerfil("Linea_linea","supervisor");
                	enviarMensajeAPerfil("Linea_linea","tablero");
                	
                	//enviarMensajeAPerfil("ComprobarLinea");
                	pb.setIdState("ComprobandoLinea");
                	i--;
                }else if(pb.getIdState().equals("ComprobandoLinea")){
                	enviarMensajeAPerfil("ComprobarLinea","supervisor");
                	enviarMensajeAPerfil("ComprobarLinea","tablero");                	
                	enviarMensajeAPerfil("ComprobarLinea","jugador");
                	//Comprobamos las lineas de todos los cartones y avisamos de quien tiene linea y alos despitados que la tienen y no la cantan
                	cp.comprobarLineas(estaSalaEs);
                	Thread.sleep(4000);
                	//Si alguna no esta cantada se avisa y se da una opcion mas
                	enviarMensajeAPerfil("Hay alguna linea mas?","supervisor");
                	enviarMensajeAPerfil("Hay alguna linea mas?","tablero");                	
                	enviarMensajeAPerfil("Hay alguna linea mas?","jugador");
                	Thread.sleep(3000);
                	//Al llegar aqui, preguntamos a super por premios de cartones manuales y que los meta en mapa Premios Manuales
        	    	// Se puede aprovechar en esta fase, para integrar los premios de cartones manuales que puedan existir
        	    	// Sera necesario hacer esto, si existen cartones manuales.
        	    		int nCartonesManuales = new Integer(gestorSesions.getJugadasSalas(this.estaSalaEs).getnCartonesManuales());
        	    	// Si los hay, habra que avisar a super, que nos informe de ello
        	    	
        	    		if(nCartonesManuales>0){
        	    				//Emviar un mensaje atraves del webSocket a super, para ser tratado en la interface reproductor
        	    				this.enviarMensajeAPerfil("PreguntarPremiosLinea", "supervisor");
        	    				this.enviarMensajeAPerfil("PreguntarPremiosLinea", "tablero");
        	    				pb.setIdState("WaitingResultSuper");
        	    				i--;
        	    				n=0;
        	    		}else{
        	    			pb.setIdState("PremiosRecopiladosLinea");//
        	    			n=1;
        	    			i--;
        	    		}
        	    		
                }else if(pb.getIdState().equals("PremiosRecopiladosLinea")){
                	
                		//if(gestorSesions.liquidacionPremios(estaSalaEs)){
            		
            		if(lp.liquidacionPremios(estaSalaEs)){                			
                			//if(gestorSesions.comprobarLineas(estaSalaEs)){
                			Thread.sleep(4000);
                			pb.setIdState("LineaOk");
                			pb.setLineaCantada(true);
                			enviarMensajeAPerfil("ApagaLinea","supervisor");
                			enviarMensajeAPerfil("ApagaLinea","tablero");                			
                			enviarMensajeAPerfil("ApagaLinea","jugador");
                   		}else{
                   			pb.setIdState("Continue");
                   			enviarMensajeAPerfil("Continuamos ...","supervisor");
                   			enviarMensajeAPerfil("Continuamos ...","tablero");                   			
                   			enviarMensajeAPerfil("Continuando partida ...","jugador");
                   		}
                   		n=1;
                   		i--;        
                   		Thread.sleep(2000);
                   		enviarMensajeAPerfil("EnciendeVideo","supervisor");
                   		enviarMensajeAPerfil("EnciendeVideo","tablero");                   		
                
                }else if(pb.getIdState().equals("LineaOk")){
                	n=0;
                	i--;
                	pb.setIdState("Started");
                	enviarMensajeAPerfil("cantarNumero_lineaOk_"+pb.getNumeroOrden(),"supervisor");
                	enviarMensajeAPerfil("cantarNumero_lineaOk_"+pb.getNumeroOrden(),"tablero");

                }else if(pb.getIdState().equals("Continue")){
                	n=0;
                	i--;
                	pb.setIdState("Started");
                	enviarMensajeAPerfil("cantarNumero_"+pb.getNewBola()+"_"+pb.getNumeroOrden(),"supervisor");
                	enviarMensajeAPerfil("cantarNumero_"+pb.getNewBola()+"_"+pb.getNumeroOrden(),"tablero");                	
                	enviarMensajeAPerfil("cantarNumero_"+pb.getNewBola()+"_"+pb.getNumeroOrden(),"jugador");
               //---------------------------------------------------------------------------------------------- 
                	
                	
                	
                	
                }else if(pb.getIdState().equals("Bingo")){
                    	enviarMensajeAPerfil("Bingo_bingo","supervisor");
                    	enviarMensajeAPerfil("Bingo_bingo","tablero");                    	
                    	pb.setIdState("ComprobandoBingo");
                    	i--;
                    	
               }else if(pb.getIdState().equals("ComprobandoBingo")){
                    	enviarMensajeAPerfil("ComprobarBingo","supervisor");
                    	enviarMensajeAPerfil("ComprobarBingo","tablero");
                    	enviarMensajeAPerfil("ComprobarBingo","jugador");
                    	cp.comprobarBingos(estaSalaEs);
                    	Thread.sleep(4000);
                    	//Si alguna no esta cantada se avisa y se da una opcion mas
                    	enviarMensajeAPerfil("Hay algun Bingo mas?","supervisor");
                    	enviarMensajeAPerfil("Hay algun Bingo mas?","tablero");
                    	enviarMensajeAPerfil("Hay algun Bingo mas?","jugador");
                    	Thread.sleep(3000);
                       	//Al llegar aqui, preguntamos a super por premios de cartones manuales y que los meta en mapa Premios Manuales
            	    	// Se puede aprovechar en esta fase, para integrar los premios de cartones manuales que puedan existir
            	    	// Sera necesario hacer esto, si existen cartones manuales.
            	    		int nCartonesManuales = new Integer(gestorSesions.getJugadasSalas(this.estaSalaEs).getnCartonesManuales());
            	    	// Si los hay, habra que avisar a super, que nos informe de ello
            	    	
            	    		if(nCartonesManuales>0){
            	    				//Emviar un mensaje atraves del webSocket a super, para ser tratado en la interface reproductor
            	    				this.enviarMensajeAPerfil("PreguntarPremiosBingo", "supervisor");
            	    				pb.setIdState("WaitingResultSuper");
            	    				i--;
            	    				n=0;
            	    		}else{
            	    			pb.setIdState("PremiosRecopiladosBingo");//
            	    			n=1;
            	    			i--;
            	    		} 
               	}else if(pb.getIdState().equals("PremiosRecopiladosBingo")){
               		//if(gestorSesions.liquidacionPremios(estaSalaEs)){
            		
            		if(lp.liquidacionPremios(estaSalaEs)){               		
               			Thread.sleep(4000);

               			pb.setIdState("BingoOk");
               			pb.setLineaCantada(true);
               			enviarMensajeAPerfil("ApagaBingo","supervisor");
               			enviarMensajeAPerfil("ApagaBingo","tablero");               			
               			enviarMensajeAPerfil("ApagaBingo","jugador");
               			
               		}else{
               			pb.setIdState("Continue");
               			enviarMensajeAPerfil("Continuamos ...","supervisor");
               			enviarMensajeAPerfil("Continuamos ...","tablero");               			
               			enviarMensajeAPerfil("Continuando partida ...","jugador");
               		}
               		n=1;
               		i--;        
               		Thread.sleep(2000);
               		enviarMensajeAPerfil("EnciendeVideo","supervisor");
               		enviarMensajeAPerfil("EnciendeVideo","tablero");               		
  	    		

                }else if(pb.getIdState().equals("BingoOk")){
                		n=0;
                		i--;
                    	enviarMensajeAPerfil("cantarNumero_bingoOk_"+pb.getNumeroOrden(),"supervisor");
                    	enviarMensajeAPerfil("cantarNumero_bingoOk_"+pb.getNumeroOrden(),"tablero");
                    	
                    	pb.setIdState("EndBalls");
                
                }else if(pb.getIdState().equals("Continue")){
                		n=0;
                		i--;
                    	pb.setIdState("Started");
                    	enviarMensajeAPerfil("cantarNumero_"+pb.getNewBola()+"_"+pb.getNumeroOrden(),"supervisor");
                    	enviarMensajeAPerfil("cantarNumero_"+pb.getNewBola()+"_"+pb.getNumeroOrden(),"tablero");
                    	enviarMensajeAPerfil("cantarNumero_"+pb.getNewBola()+"_"+pb.getNumeroOrden(),"jugador");                	
                //---------------------------------------------------------------------------------------------                	
 
                //----------------------------------------------------------------------------------------------
                }else if(pb.getIdState().equals("WarningFinalizando")){
                	n=20000;
                   	log.info("Haciendo tiempo que hemos llegado al 90 y no canta nadie");
                }	               
                
                else{
            	
                	int number = calculaBolaNueva();
                	// Ojo borramos peticiones comprobacion premios a cada bola cantada
                	this.gestorSesions.borrarListaPeticionPremios(this.estaSalaEs);
                	
                	//pb.setSecuenciaAcabada(false);
                	enviarMensajeAPerfil("cantarNumero_"+number+"_"+i+"_"+pb.getLastNumber(),"supervisor");
                	enviarMensajeAPerfil("cantarNumero_"+number+"_"+i+"_"+pb.getLastNumber(),"tablero");
                	enviarMensajeAPerfil("cantarNumero_"+number+"_"+i+"_"+pb.getLastNumber(),"jugador");
                	
                	
                	pb.setNewBola(number);//bola en pantalla
                	pb.setNumeroOrden(i);
                }
                log.info("Orden antes del wait "+ pb.getNumeroOrden()+ "n= "+n);
                //Establecimiento control de tiempo de juego//Time out too wait
   	            wait(n); 
   				


            } catch (InterruptedException ex) {
        	   log.info("He sido interrumpido y numnero ordem es:"+i);
        	    //Podemos ajustar un retardo alto aqui por defecto
               n=0;
        	   String reasonInterrupt=pb.getReasonInterrupt();
        	   log.info("Interrupt recibido (reason):"+ reasonInterrupt + " IdState:" + pb.getIdState());
               switch(reasonInterrupt){
               		case "secuenciaAcabada":
               			
               			pb.setLastNumber(pb.getNewBola());
               			
               			if(!(pb.getNewBola()==0))pb.addNumerosCalled(pb.getNewBola());
               			enviarMensajeAPerfil("EncenderNumero_"+pb.getNewBola(),"supervisor");
               			enviarMensajeAPerfil("EncenderNumero_"+pb.getNewBola(),"tablero");

               			if(pb.getIdState().equals("Started") && pb.getNumeroOrden()==90){
           					i--;
           					pb.setIdState("WarningFinalizando");
               	        	enviarMensajeAPerfil("WarningFinalizando","jugador");
               	        	enviarMensajeAPerfil("WarningFinalizando","tablero");
               	        	enviarMensajeAPerfil("WarningFinalizando","supervisor");
               	        	break;
               			}	
               			
               			if( pb.getIdState().equals("ComprobandoLinea")|| pb.getIdState().equals("ComprobandoBingo")|| pb.getIdState().equals("LineaOk")|| pb.getIdState().equals("BingoOk")){
               				break;
               			}
               			if(pb.getIdState().equals("Bingo") && pb.getNumeroOrden()==90){
           					i--;
           					break;
           				}
               			if(pb.getIdState().equals("EndBalls")){
               				gestorSesions.resetCartones(estaSalaEs);
               				pb.resetNumerosCalled();
               				enviarMensajeAPerfil("EndBalls","supervisor");
               				enviarMensajeAPerfil("EndBalls","tablero");               				
               				enviarMensajeAPerfil("EndBalls","jugador"); 
               				pb.setIdState("Finalized");
               				return;
               			}
               			if(pb.getIdState().equals("Finalized")){
               				//Se ha alcanzado el estado finalizado, no hay nada mas que hacer
               				return;
               			}
               			try {
               				delay=pb.getDelay();
               				Thread.sleep(delay);
               			} catch (InterruptedException e) {
               				// 	TODO Auto-generated catch block
               				e.printStackTrace();
               			}

               				break;
               		
               		case "continuar":
               				break;
               		case "Bingo":
               				if(pb.getIdState().equals("Bingo") && pb.getNumeroOrden()==90){
               					i--;
               				}
               				break;
               				
               		
               }
           }
          
          }   
        }

    	//Esto nose si se se ejecuta alguna vez; quizas al llegar al 90 y no haber ningn cambio de estado
    	log.info("Se ha cabado la secuencia y no hay cantados bingos");
        gestorSesions.resetCartones(estaSalaEs);
        pb.resetNumerosCalled();
    	enviarMensajeAPerfil("EndBalls","supervisor");
    	enviarMensajeAPerfil("EndBalls","tablero");
    	enviarMensajeAPerfil("EndBalls","jugador");                	
    	pb.setIdState("Finalized");
        log.info("Acabado bucle");//
    	return;
    	//Con return finalizamos el hilo
    }
    
    private void enviarMensajeAPerfil(String textMessage,String perfil){
    	try {
    		
    		// Vamos a obtener las sesiones a las que vamos a redirigir los mensajes de momento
    		//son de superdf
    		//Set<UserBean> myUsersbean = gestorSesions.dameUserBeans(perfil);
    		//GestorSessions gestorSesions = (GestorSessions)this.session.getUserProperties().get("gestorSesiones");
    		//Set<UserBean> myUsersbean = (Set<UserBean>)this.session.getUserProperties().get("sesiones");
    		Set<UserBean> myUsersbean = gestorSesions.dameUserBeans(perfil);
    		Iterator<UserBean> itBeans= myUsersbean.iterator();
    		UserBean usb ;
    		while (itBeans.hasNext()){
    			usb=itBeans.next();
    			Session sesionActiva = usb.getSesionSocket();
  

    				sesionActiva.getBasicRemote().sendText(textMessage);
    			
    			
    			log.info("Enviando mensaje desde Hilo3("+usb.getUsername()+"):"+ textMessage);
    		}
  		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("Fallo al mandar mesaje desde hilo");
			e.printStackTrace();
		}
    }
    
    public int calculaBolaNueva(){
        int number;
        
        boolean numeroValido=false;
        number =  (new Random().nextInt(maxNumbers))+1;
        while(numeroValido==false){
            Iterator<Integer> itNumeros = pb.getNumerosCalled().iterator();
            numeroValido=true;            
            while(itNumeros.hasNext()){
               int compNumber=itNumeros.next();
               if(compNumber==number){
                   //log.info("Se produce coincidencia");
                   numeroValido=false;
                   number =  (new Random().nextInt(maxNumbers))+1;
                   break;
               }
               
            }    
            
        }
        
        return number;
        
    }
}
