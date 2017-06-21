package servlet;

import static java.lang.Thread.sleep;

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
    JTextArea area2;
    PocketBingo pb;
    Session session;
    String status;
    int maxNumbers = 90;
    int orden;
    int delay= 0;
	
   //
    //private GestorSessions gestorSesions;
    
    public Runnable3(Session sesion, int ndelay){
       this.session = sesion;
       this.delay = ndelay;
       
    }
    @Override
    public void run(){
    	log.info("IdState en 'run' putobucle3:" );
        this.pb=(PocketBingo)session.getUserProperties().get("sala1");
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
        	orden=pb.getNumeroOrden();;
        	int number= pb.getNewBola();
        	//this.enviarMensajeAPerfil("bolaJuego_"+orden);
        	enviarMensajeAPerfil("cantarNumero_"+number+"_"+orden,"supervisor");
        	enviarMensajeAPerfil("EncenderNumero_"+number,"supervisor");
        	enviarMensajeAPerfil("EncenderNumero_"+number,"jugador");
        	
        	pb.addNumerosCalled(pb.getNewBola());
        	try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	orden=pb.getNumeroOrden()+1;;
        }
        }
    	for(int i=orden; i < maxNumbers+1 ;i++)   {
          log.info("IdState en inicio bucle"+pb.getIdState()); 
   
          synchronized(this){
            try{
                if(pb.getIdState().equals("Linea")){
                	enviarMensajeAPerfil("Linea_linea","supervisor");
                	//enviarMensajeAPerfil("ComprobarLinea");
                	pb.setIdState("ComprobandoLinea");
                	i--;
                }else if(pb.getIdState().equals("ComprobandoLinea")){
                	enviarMensajeAPerfil("ComprobarLinea","supervisor");
                	i--;
                }else if(pb.getIdState().equals("LineaOk")){
                	pb.setIdState("Started");
                	enviarMensajeAPerfil("cantarNumero_lineaOk_"+pb.getNumeroOrden(),"supervisor");
                	i--;
                }else if(pb.getIdState().equals("Continue")){
                	pb.setIdState("Started");
                	enviarMensajeAPerfil("cantarNumero_"+pb.getNewBola()+"_"+pb.getNumeroOrden(),"supervisor");
                	i--;
                
                }else if(pb.getIdState().equals("Bingo")){
                	enviarMensajeAPerfil("Bingo_bingo","supervisor");
                	//enviarMensajeAPerfil("ComprobarLinea");
                	pb.setIdState("ComprobandoBingo");
                	i--;
                }else if(pb.getIdState().equals("ComprobandoBingo")){
                	enviarMensajeAPerfil("ComprobarBingo","supervisor");
                	i--;
                }else if(pb.getIdState().equals("BingoOk")){
                	pb.setIdState("Finalized");
                	enviarMensajeAPerfil("cantarNumero_bingoOk_"+pb.getNumeroOrden(),"supervisor");
                	i--;
                }
                
                else{
            	
                	int number = calculaBolaNueva();
                
                	
                	//pb.setSecuenciaAcabada(false);
                	enviarMensajeAPerfil("cantarNumero_"+number+"_"+i+"_"+pb.getLastNumber(),"supervisor");
                	
                	pb.setNewBola(number);//bola en pantalla
                	pb.setNumeroOrden(i);
                }
                log.info("Orden antes del wait "+ pb.getNumeroOrden());
                
                wait(); 

            } catch (InterruptedException ex) {
        	   //log.info("He sido interrumpido");
        	   String reasonInterrupt=pb.getReasonInterrupt();
        	   log.info("Interrupt recibido (reason):"+ reasonInterrupt + " IdState:" + pb.getIdState());
               switch(reasonInterrupt){
               		case "secuenciaAcabada":
               			
               			pb.setLastNumber(pb.getNewBola());
               			
               			if( pb.getIdState().equals("ComprobandoLinea")|| pb.getIdState().equals("ComprobandoBingo")|| pb.getIdState().equals("LineaOk")|| pb.getIdState().equals("BingoOk")){
               				break;
               			}
               			if(pb.getIdState().equals("Bingo") && pb.getNumeroOrden()==90){
           					i--;
           					break;
           				}
               			if(pb.getIdState().equals("Finalized")){
               				enviarMensajeAPerfil("EndBalls","supervisor");
               				return;
               			}

               			pb.addNumerosCalled(pb.getNewBola());
               			enviarMensajeAPerfil("EncenderNumero_"+pb.getNewBola(),"supervisor");
               			enviarMensajeAPerfil("EncenderNumero_"+pb.getNewBola(),"jugador");
               			
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
               				
               		case "offLine":
               			enviarMensajeAPerfil("EndBalls","supervisor");
               			pb.setIdState("Finalized");
               			return;
               }
           }
          
          }   
        }
    	enviarMensajeAPerfil("EndBalls","supervisor");
    	pb.setIdState("Finalized");
    	return;
    }
    
    private void enviarMensajeAPerfil(String textMessage,String perfil){
    	try {
    		log.info("Enviando mensaje desde Hilo3(modified):" + textMessage);
    		// Vamos a obtener las sesiones a las que vamos a redirigir los mensajes de momento
    		//son de superdf
    		//Set<UserBean> myUsersbean = gestorSesions.dameUserBeans(perfil);
    		GestorSessions gestorSesions = (GestorSessions)this.session.getUserProperties().get("gestorSesiones");
    		//Set<UserBean> myUsersbean = (Set<UserBean>)this.session.getUserProperties().get("sesiones");
    		Set<UserBean> myUsersbean = gestorSesions.dameUserBeans(perfil);
    		Iterator<UserBean> itBeans= myUsersbean.iterator();
    		while (itBeans.hasNext()){
    			Session sesionActiva = itBeans.next().getSesionSocket();
    			sesionActiva.getBasicRemote().sendText(textMessage);
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
               int compNumber=(int) itNumeros.next();
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
