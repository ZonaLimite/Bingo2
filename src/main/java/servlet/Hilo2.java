/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.websocket.Session;

/**
 *
 * @author Paco Boga
 */
//public class Hilo2 extends Thread{
public class Hilo2 extends Thread{
    Logger log = Logger.getLogger("HiloSala1");
    JTextArea area2;
    PocketBingo pb;
    Session session;
    String status;
    int maxNumbers = 90;
    int orden;
    int delay= 0;
    public Hilo2(Session sesion, int ndelay){
       this.session = sesion;
       this.delay = ndelay;
       
    }
    @Override
    public void run(){
        this.pb=(PocketBingo)session.getUserProperties().get("user");
        status=pb.getIdState();
        
        log.info("IdState en 'run' bucle:" +status);
        synchronized(this){
        if(status.equals("NewGame")){
        	orden =1;
        	pb.setIdState("Started");
        	pb.setNumeroOrden(1);
    		this.enviarMensaje("cantarNumero_BuenasNoches_0");
    		try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				
			}

        }else {//else if(status.equals("Started"))
        	Vector<?> listaNumeros=(Vector<?>) pb.getNumerosCalled();
    		int i;
        	for(i=0;i<listaNumeros.size();i++){
    			this.enviarMensaje("EncenderNumero_"+listaNumeros.elementAt(i)+"_simple");
    			//this.enviarMensaje("bolaJuego_"+(i+1));
    			try {
    				Thread.sleep(400);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
        	orden=pb.getNumeroOrden();;
        	int number= pb.getNewBola();
        	//this.enviarMensaje("bolaJuego_"+orden);
        	enviarMensaje("cantarNumero_"+number+"_"+orden);
        	enviarMensaje("EncenderNumero_"+number);
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
                	enviarMensaje("Linea_linea");
                	//enviarMensaje("ComprobarLinea");
                	pb.setIdState("ComprobandoLinea");
                	i--;
                }else if(pb.getIdState().equals("ComprobandoLinea")){
                	enviarMensaje("ComprobarLinea");
                	i--;
                }else if(pb.getIdState().equals("LineaOk")){
                	pb.setIdState("Started");
                	enviarMensaje("cantarNumero_lineaOk_"+pb.getNumeroOrden());
                	i--;
                }else if(pb.getIdState().equals("Continue")){
                	pb.setIdState("Started");
                	enviarMensaje("cantarNumero_"+pb.getNewBola()+"_"+pb.getNumeroOrden());
                	i--;
                
                }else if(pb.getIdState().equals("Bingo")){
                	enviarMensaje("Bingo_bingo");
                	//enviarMensaje("ComprobarLinea");
                	pb.setIdState("ComprobandoBingo");
                	i--;
                }else if(pb.getIdState().equals("ComprobandoBingo")){
                	enviarMensaje("ComprobarBingo");
                	i--;
                }else if(pb.getIdState().equals("BingoOk")){
                	pb.setIdState("Finalized");
                	enviarMensaje("cantarNumero_bingoOk_"+pb.getNumeroOrden());
                	i--;
                }
                
                else{
            	
                	int number = calculaBolaNueva();
                
                	
                	//pb.setSecuenciaAcabada(false);
                	enviarMensaje("cantarNumero_"+number+"_"+i+"_"+pb.getLastNumber());
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
               				enviarMensaje("EndBalls");
               				return;
               			}

               			pb.addNumerosCalled(pb.getNewBola());
               			enviarMensaje("EncenderNumero_"+pb.getNewBola());
               			
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
               			enviarMensaje("EndBalls");
               			pb.setIdState("Finalized");
               			return;
               }
           }
          
          }   
        }
    	enviarMensaje("EndBalls");
    	pb.setIdState("Finalized");
    	return;
    }
    private void enviarMensaje(String textMessage){
    	try {
    		log.info("Enviando mensaje desde Hilo:" + textMessage);
    		session.getBasicRemote().sendText(textMessage);
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
