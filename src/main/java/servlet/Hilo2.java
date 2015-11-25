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
    int maxNumbers = 46;
    int orden;
    public Hilo2(Session sesion){
       this.session = sesion;
       
    }
    @Override
    public void run(){
        this.pb=(PocketBingo)session.getUserProperties().get("user");
        status=pb.getIdState();
        if(status.equals("NewGame")){
        	orden =1;
        	pb.setIdState("Started");
        }else if(status.equals("Started")){
        	Vector listaNumeros=(Vector)pb.getNumerosCalled();
    		for(int i=0;i<listaNumeros.size();i++){
    			this.enviarMensaje("EncenderNumero_"+listaNumeros.elementAt(i));
    			try {
    				Thread.sleep(300);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
        	orden= pb.getNumeroOrden();
        }
    	for(int i=orden; i < maxNumbers+1 ;i++)   {
          //log.log(Level.INFO, "Enviando mensaje de Hilo2 :{0}", i);
   
          synchronized(this){
            try{
                int number = calculaBolaNueva();
                pb.setNewBola(number);
                
                pb.setNumeroOrden(i);
                pb.setSecuenciaAcabada(false);
                enviarMensaje("cantarNumero_"+number+"_"+pb.getNumeroOrden());
                pb.setLastNumber(pb.getNewBola());
                pb.addNumerosCalled(pb.getNewBola());
                wait(); 

            } catch (InterruptedException ex) {
        	   log.info("He sido interrumpido");
        	   String reasonInterrupt=pb.getReasonInterrupt();
               switch(reasonInterrupt){
               		case "secuenciaAcabada":
               			enviarMensaje("EncenderNumero_"+pb.getNewBola());
               			
               		
               		break;
               		case "offLine":
               			enviarMensaje("EndBalls");
               			return;
               }
           }
          
          }   
        }
    	//pb.setIdState("Finalized"2);
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
