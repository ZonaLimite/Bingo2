/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

import static java.lang.Thread.sleep;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.websocket.Session;

/**
 *
 * @author hormigueras
 */
public class Hilo2 extends Thread{
    Logger log = Logger.getLogger("test");
    JTextArea area2;
    PocketBingo pb;
    Session session;
    String status;
    int maxNumbers = 90;
    int orden;
    public Hilo2(Session sesion){
       this.session = sesion;
       
    }
    @Override
    public void run(){
        this.pb=(PocketBingo)session.getUserProperties().get("user");
        status=pb.getIdState();
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
        	Vector listaNumeros=(Vector)pb.getNumerosCalled();
    		int i;
        	for(i=0;i<listaNumeros.size();i++){
    			this.enviarMensaje("EncenderNumero_"+listaNumeros.elementAt(i));
    			//this.enviarMensaje("bolaJuego_"+(i+1));
    			try {
    				Thread.sleep(300);
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
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	orden=pb.getNumeroOrden()+1;;
        }
        }
    	for(int i=orden; i < maxNumbers+1 ;i++)   {
          //log.log(Level.INFO, "Enviando mensaje de Hilo2 :{0}", i);
   
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
                	enviarMensaje("cantarNumero_"+number+"_"+i);
                	pb.setNewBola(number);//bola en pantalla
                	pb.setNumeroOrden(i);
                }
                //log.info("orden antes del wait "+ i);
                
                wait(); 

            } catch (InterruptedException ex) {
        	   //log.info("He sido interrumpido");
        	   String reasonInterrupt=pb.getReasonInterrupt();
               switch(reasonInterrupt){
               		case "secuenciaAcabada":
               			enviarMensaje("EncenderNumero_"+pb.getNewBola());
               			pb.setLastNumber(pb.getNewBola());
               			
               			if(pb.getIdState().equals("ComprobandoLinea")|| pb.getIdState().equals("ComprobandoBingo")|| pb.getIdState().equals("LineaOk")|| pb.getIdState().equals("BingoOk")){
               				break;
               			}
               			if(pb.getIdState().equals("Bingo") && pb.getNumeroOrden()==90){
               				i--;
               				break;
               			}
               			pb.addNumerosCalled(pb.getNewBola());

               			if(pb.getIdState().equals("Finalized")){
               				enviarMensaje("EndBalls");
               				return;
               			}
               		break;
               		
               		case "continuar":
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
			session.getBasicRemote().sendText(textMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public int calculaBolaNueva(){
        int number;
        
        boolean numeroValido=false;
        number =  (new Random().nextInt(maxNumbers))+1;
        while(numeroValido==false){
            Iterator itNumeros = pb.getNumerosCalled().iterator();
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
