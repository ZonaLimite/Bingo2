package servlet;

import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

public class UserBean {
	
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String perfil;
    private String statusPlayer;
    private String salonInUse;
    private Integer numeroCartones=0;
    private HttpSession sesionHttp;
    private Session sesionSocket;
    private Vector<Carton> vCarton = new Vector();
    private Integer saldo=0;
    
    public Integer getNumeroCartones() {
		return numeroCartones;
	}

	public Integer getSaldo() {
		return saldo;
	}

	public void setSaldo(Integer saldo) {
		this.saldo = saldo;
	}

	public void setNumeroCartones(Integer numeroCartones) {
		this.numeroCartones = numeroCartones;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean valid;
	
	
    public String getFirstName() {
       return firstName;
	}

    public void setFirstName(String newFirstName) {
       firstName = newFirstName;
	}

	
    public String getLastName() {
       return lastName;
			}

    public void setLastName(String newLastName) {
       lastName = newLastName;
			}
			

    public String getPassword() {
       return password;
	}

    public void setPassword(String newPassword) {
       password = newPassword;
	}
	
			
    public String getUsername() {
       return username;
			}

    public void setUserName(String newUsername) {
       username = newUsername;
			}

				
    public boolean isValid() {
       return valid;
	}

    public void setValid(boolean newValid) {
       valid = newValid;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getStatusPlayer() {
		return statusPlayer;
	}

	public void setStatusPlayer(String statusPlayer) {
		this.statusPlayer = statusPlayer;
	}

	public String getSalonInUse() {
		return salonInUse;
	}

	public void setSalonInUse(String salonInUse) {
		this.salonInUse = salonInUse;
	}

	public HttpSession getSesionHttp() {
		return sesionHttp;
	}

	public void setSesionHttp(HttpSession sesionHttp) {
		this.sesionHttp = sesionHttp;
	}

	public Session getSesionSocket() {
		return sesionSocket;
	}

	public void setSesionSocket(Session sesionSocket) {
		this.sesionSocket = sesionSocket;
	}

	public Vector<Carton> getvCarton() {
		return vCarton;
	}

	public void setvCarton(Vector<Carton> vCarton) {
		this.vCarton = vCarton;
	}	
}

