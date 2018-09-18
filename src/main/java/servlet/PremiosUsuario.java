package servlet;

import java.io.Serializable;
import java.util.Vector;

public class PremiosUsuario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int cantidadJugada;
	Vector<Carton> premios = new Vector<Carton>();
	
	public int getCantidadJugada() {
		return cantidadJugada;
	}
	public void setCantidadJugada(int cantidadJugada) {
		this.cantidadJugada = cantidadJugada;
	}
	public Vector<Carton> getPremios() {
		return premios;
	}
	public void setPremios(Vector<Carton> premios) {
		this.premios = premios;
	}


}
