/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlet;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author hormigueras
 */
public class PocketBingo implements Serializable {
    
	
	private int numeroOrden = 0;
    private String idPlayer = "";
    private Vector numerosCalled = new Vector() ;
    private int lastNumber;
    private int newBola;
    private String reasonInterrupt;
    private boolean secuenciaAcabada=true;
    private boolean lineaCantada=false;
    private boolean bingoCantado=false;
    private String IdState = "NewGame";
    private String precioCarton="0";
    private String nCartones="0";
    private String porcientoLinea="0";
    private String porcientoBingo="0";
    private String porcientoCantaor="0";
    private int delay = 1500;
    
    
    /* 
     * IdState :
     *  NewGame,Linea,Bingo,Started,Finalized.
     */
    public void initPocket(){
    	numeroOrden=0;
    	idPlayer="";
    	numerosCalled= new Vector();
    	lastNumber=0;
    	newBola=0;
    	reasonInterrupt="";
    	secuenciaAcabada=true;
    	lineaCantada=false;
    	bingoCantado=false;
    	IdState="NewGame";

    }
    public boolean isLineaCantada() {
		return lineaCantada;
	}
	public void setLineaCantada(boolean lineaCantada) {
		this.lineaCantada = lineaCantada;
	}
	public boolean isBingoCantado() {
		return bingoCantado;
	}
	public void setBingoCantado(boolean bingoCantado) {
		this.bingoCantado = bingoCantado;
	}
    public boolean getSecuenciaAcabada(){
        return secuenciaAcabada;
    }
    public String getIdState() {
		
    	return IdState;
	}
	public void setIdState(String idState) {
		IdState = idState;
	}
	public void setSecuenciaAcabada(boolean secAca){
        this.secuenciaAcabada = secAca;
    }
    public int getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(int numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }

    public List<Integer> getNumerosCalled() {
        return numerosCalled;
    }

    public void addNumerosCalled(int addNumero) {
        numerosCalled.add(addNumero);
    }

    public int getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(int lastNumber) {
        this.lastNumber = lastNumber;
    }

    public int getNewBola() {
        return newBola;
    }

    public void setNewBola(int newBola) {
        this.newBola = newBola;
    }
	public String getReasonInterrupt() {
		return reasonInterrupt;
	}
	public void setReasonInterrupt(String reasonInterrupt) {
		this.reasonInterrupt = reasonInterrupt;
	}
	public String getPrecioCarton() {
		return precioCarton;
	}
	public void setPrecioCarton(String precioCarton) {
		this.precioCarton = precioCarton;
	}
	public String getnCartones() {
		return nCartones;
	}
	public void setnCartones(String nCartones) {
		this.nCartones = nCartones;
	}
	public String getPorcientoLinea() {
		return porcientoLinea;
	}
	public void setPorcientoLinea(String porcientoLinea) {
		this.porcientoLinea = porcientoLinea;
	}
	public String getPorcientoBingo() {
		return porcientoBingo;
	}
	public void setPorcientoBingo(String porcientoBingo) {
		this.porcientoBingo = porcientoBingo;
	}
	public String getPorcientoCantaor() {
		return porcientoCantaor;
	}
	public void setPorcientoCantaor(String porcientoCantaor) {
		this.porcientoCantaor = porcientoCantaor;
	}
	public int getDelay() {
		return delay;
	}
	public void setDelay(int delay) {
		this.delay = delay;
	}
}
