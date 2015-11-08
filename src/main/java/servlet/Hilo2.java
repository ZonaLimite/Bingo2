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
    public Hilo2(Session sesion){
       this.session = sesion;
       
    }
    @Override
    public void run(){
        this.pb=(PocketBingo)session.getUserProperties().get("user");
    	for(int i=1; i <91 ;i++)   {
          log.log(Level.INFO, "Enviando mensaje de Hilo2 :{0}", i);
   
          synchronized(this){
            try{
                int number = calculaBolaNueva();
                pb.setNewBola(number);
                pb.setSecuenciaAcabada(false);
                session.getBasicRemote().sendText("comando_cantar"+number);
                wait(); 

            } catch (InterruptedException ex) {
        	   log.info("He sido interrumpido");
        	   String reasonInterrupt=pb.getReasonInterrupt();
               switch(reasonInterrupt){
               		case "secuenciaAcabada":
               			enviarMensaje("comando_EncenderNumero"+pb.getNewBola());
               			pb.setLastNumber(pb.getNewBola());
                        pb.addNumerosCalled(pb.getNewBola());
               		
               		break;
               		case "offLine":
               			enviarMensaje("No mas bolas para cantar");
               			return;
               }
               
           } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
           }
          
          }   
        }
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
        number =  (new Random().nextInt(90))+1;
        while(numeroValido==false){
            Iterator itNumeros = pb.getNumerosCalled().iterator();
            numeroValido=true;            
            while(itNumeros.hasNext()){
               int compNumber=(int) itNumeros.next();
               if(compNumber==number){
                   log.info("Se produce coincidencia");
                   numeroValido=false;
                   number =  (new Random().nextInt(90))+1;
                   break;
               }
               
            }    
            
        }
        
        return number;
        
    }
}
