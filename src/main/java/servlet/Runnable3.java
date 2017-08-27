package servlet;

import java.io.IOException;
import java.util.Iterator;

import java.util.Random;
import java.util.Set;
import java.util.Vector;

import java.util.logging.Logger;

import javax.swing.JTextArea;
import javax.websocket.Session;

/**
 *
 * @author hormigueras
 */
//public class Hilo2 extends Thread{
public class Runnable3 implements Runnable{
    Logger log = Logger.getLogger("HiloSala1");
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
    	
        this.pb=gestorSesions.getJugadasSalas(estaSalaEs);
        status=pb.getIdState();
        
        
        synchronized(this){
        if(status.equals("NewGame")){
        	orden =1;
        	pb.setIdState("Started");
        	pb.setNumeroOrden(1);
    		this.enviarMensajeAPerfil("cantarNumero_BuenasNoches_0","supervisor");
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
        	enviarMensajeAPerfil("cantarNumero_"+number+"_"+orden,"jugador");
        	enviarMensajeAPerfil("EncenderNumero_"+number,"supervisor");
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
                	//enviarMensajeAPerfil("ComprobarLinea");
                	pb.setIdState("ComprobandoLinea");
                	i--;
                }else if(pb.getIdState().equals("ComprobandoLinea")){
                	enviarMensajeAPerfil("ComprobarLinea","supervisor");
                	enviarMensajeAPerfil("ComprobarLinea","jugador");
                	gestorSesions.comprobarLineas(estaSalaEs);
                	Thread.sleep(5000);
                	//Si alguna no esta cantada se avisa y se da una opcion mas
                	enviarMensajeAPerfil("Hay alguna linea mas?","supervisor");
                	enviarMensajeAPerfil("Hay alguna linea mas?","jugador");
                	Thread.sleep(3000);
                	//Al llegar aqui, preguntamos a super por premios de cartones manuales y que los meta en mapa Premios Manuales
        	    	// Se puede aprovechar en esta fase, para integrar los premios de cartones manuales que puedan existir
        	    	// Sera necesario hacer esto, si existen cartones manuales.
        	    		int nCartonesManuales = new Integer(gestorSesions.getJugadasSalas(this.estaSalaEs).getnCartonesManuales());
        	    	// Si los hay, habra que avisar a super, que nos informe de ello
        	    	
        	    		if(nCartonesManuales>0){
        	    				//Emviar un mensaje atraves del webSocket a super, para ser tratado en la interface reproductor
        	    				this.enviarMensajeAPerfil("PreguntarPremios", "supervisor");
        	    				pb.setIdState("WaitingResultSuper");
        	    				i--;
        	    				n=0;
        	    		}else{
        	    			pb.setIdState("PremiosRecopilados");//
        	    			n=1;
        	    			i--;
        	    		}
        	    		
                }else if(pb.getIdState().equals("PremiosRecopilados")){
                		if(gestorSesions.liquidacionPremios(estaSalaEs)){
                			//if(gestorSesions.comprobarLineas(estaSalaEs)){
                			Thread.sleep(3000);
                			pb.setIdState("LineaOk");
                			pb.setLineaCantada(true);
                			enviarMensajeAPerfil("ApagaLinea","supervisor");
                			enviarMensajeAPerfil("ApagaLinea","jugador");
                		}else{
                			pb.setIdState("Continue");
                		}
                		Thread.sleep(5000);
                		enviarMensajeAPerfil("EnciendeVideo","supervisor");
                		n=1;
                		i--;
                
                }else if(pb.getIdState().equals("LineaOk")){
                	pb.setIdState("Started");
                	enviarMensajeAPerfil("cantarNumero_lineaOk_"+pb.getNumeroOrden(),"supervisor");
                
                	i--;
                }else if(pb.getIdState().equals("Continue")){
                	pb.setIdState("Started");
                	enviarMensajeAPerfil("cantarNumero_"+pb.getNewBola()+"_"+pb.getNumeroOrden(),"supervisor");
                	i--;
                	enviarMensajeAPerfil("cantarNumero_"+pb.getNewBola()+"_"+pb.getNumeroOrden(),"jugador");
               //---------------------------------------------------------------------------------------------- 
                }else if(pb.getIdState().equals("Bingo")){
                    	enviarMensajeAPerfil("Bingo_bingo","supervisor");
                    	pb.setIdState("ComprobandoBingo");
                    	i--;
               }else if(pb.getIdState().equals("ComprobandoBingo")){
                    	enviarMensajeAPerfil("ComprobarBingo","supervisor");
                    	enviarMensajeAPerfil("ComprobarBingo","jugador");
                    	gestorSesions.comprobarBingos(estaSalaEs);
                    	Thread.sleep(5000);
                    	//Si alguna no esta cantada se avisa y se da una opcion mas
                    	enviarMensajeAPerfil("Hay algun Bingo mas?","supervisor");
                    	enviarMensajeAPerfil("Hay algun Bingo mas?","jugador");
                    	Thread.sleep(2000);
                    	if(gestorSesions.liquidacionPremios(estaSalaEs)){
                    	//if(gestorSesions.comprobarLineas(estaSalaEs)){
                    		Thread.sleep(4000);
                    		pb.setIdState("BingoOk");
                    		pb.setBingoCantado(true);
                			enviarMensajeAPerfil("ApagaBingo","supervisor");
                			enviarMensajeAPerfil("ApagaBingo","jugador");
                    	}else{
                    		pb.setIdState("Continue");
                    	}
                    	Thread.sleep(5000);
            			enviarMensajeAPerfil("EnciendeVideo","supervisor");
                    	n=1;
                    	i--;
                }else if(pb.getIdState().equals("BingoOk")){
                    	
                    	enviarMensajeAPerfil("cantarNumero_bingoOk_"+pb.getNumeroOrden(),"supervisor");
                    	pb.setIdState("EndBalls");
                    	i--;
                }else if(pb.getIdState().equals("Continue")){
                    	pb.setIdState("Started");
                    	enviarMensajeAPerfil("cantarNumero_"+pb.getNewBola()+"_"+pb.getNumeroOrden(),"supervisor");
                    	i--;
                    	enviarMensajeAPerfil("cantarNumero_"+pb.getNewBola()+"_"+pb.getNumeroOrden(),"jugador");                	
                //---------------------------------------------------------------------------------------------                	
              /*  }else if(pb.getIdState().equals("Bingo")){
                	enviarMensajeAPerfil("Bingo_bingo","supervisor");
                	
                	//enviarMensajeAPerfil("ComprobarLinea");
                	pb.setIdState("ComprobandoBingo");
                	i--;
                }else if(pb.getIdState().equals("ComprobandoBingo")){
                	enviarMensajeAPerfil("ComprobarBingo","supervisor");
                	enviarMensajeAPerfil("ComprobarBingo","jugador");
                	gestorSesions.comprobarBingos(estaSalaEs);
                	i--;
                }else if(pb.getIdState().equals("BingoOk")){
                	pb.setIdState("EndBalls");
                	enviarMensajeAPerfil("cantarNumero_bingoOk_"+pb.getNumeroOrden(),"supervisor");
                	i--;*/
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
                	enviarMensajeAPerfil("cantarNumero_"+number+"_"+i+"_"+pb.getLastNumber(),"jugador");
                	
                	
                	pb.setNewBola(number);//bola en pantalla
                	pb.setNumeroOrden(i);
                }
                log.info("Orden antes del wait "+ pb.getNumeroOrden()+ "n= "+n);
 
   	            wait(n); 
   				


            } catch (InterruptedException ex) {
        	   //log.info("He sido interrumpido");
               n=0;
        	   String reasonInterrupt=pb.getReasonInterrupt();
        	   log.info("Interrupt recibido (reason):"+ reasonInterrupt + " IdState:" + pb.getIdState());
               switch(reasonInterrupt){
               		case "secuenciaAcabada":
               			
               			pb.setLastNumber(pb.getNewBola());
               			
               			if(!(pb.getNewBola()==0))pb.addNumerosCalled(pb.getNewBola());
               			enviarMensajeAPerfil("EncenderNumero_"+pb.getNewBola(),"supervisor");

               			if(pb.getIdState().equals("Started") && pb.getNumeroOrden()==90){
           					i--;
           					pb.setIdState("WarningFinalizando");
               	        	enviarMensajeAPerfil("WarningFinalizando","jugador");
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
               				enviarMensajeAPerfil("EndBalls","supervisor");
               				enviarMensajeAPerfil("EndBalls","jugador"); 
               				gestorSesions.resetCartones(estaSalaEs);
               				pb.resetNumerosCalled();
               				pb.setIdState("Finalized");
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
               				
               		case "Finalize":
                        gestorSesions.resetCartones(estaSalaEs);
                        pb.resetNumerosCalled();
               			enviarMensajeAPerfil("EndBalls","supervisor");
               			enviarMensajeAPerfil("EndBalls","jugador");               			
               			pb.setIdState("Finalized");
               			return;
               }
           }
          
          }   
        }

    	//Esto nose si se se ejecuta alguna vez; quizas al llegar al 90 y no haber ningn cambio de estado
    	log.info("Se ha cabado la secuencia y no hay cantados bingos");
        gestorSesions.resetCartones(estaSalaEs);
        pb.resetNumerosCalled();
    	enviarMensajeAPerfil("EndBalls","supervisor");
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
    		while (itBeans.hasNext()){
    			Session sesionActiva = itBeans.next().getSesionSocket();
    			sesionActiva.getBasicRemote().sendText(textMessage);
    			log.info("Enviando mensaje desde Hilo3(modified):" + textMessage);
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
