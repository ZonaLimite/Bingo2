package servlet;

import java.util.Vector;

public class PremiosUsuario {
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
	int cantidadJugada;
	Vector<Carton> premios = new Vector<Carton>();

}
