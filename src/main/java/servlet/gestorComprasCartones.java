/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author javie
 */
@WebServlet("/gestorComprasCartones")
public class gestorComprasCartones extends HttpServlet {
    @Inject
    private GestorSessions gestorSesions;
    
    //private UserBean user = null;

    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSessionAComparar = req.getSession(false);
        UserBean user_ = null;
        PocketBingo pocketBingoSala= null;
        //Identificacion de la sesion de usuario//
        if(httpSessionAComparar!=null){
            String usuario = req.getParameter("usuario");
            String sala = req.getParameter("sala");
            int nCartonesAComprar = new Integer(req.getParameter("nCartones"));

            user_= gestorSesions.dameUserBeansPorUser(usuario, "jugador", httpSessionAComparar.getId());
            if(user_==null){
                String mensaje="Error,Usuario no esta registrado o \nno esta On Line ";
                resp.getWriter().print(mensaje);
                return;
            }

            pocketBingoSala= gestorSesions.getJugadasSalas(user_.getSalonInUse());
 
            //Comprobacion estado Pocket Bingo en "Finalized"
            if(!pocketBingoSala.getIdState().equals("Finalized")){
                    String mensaje="Error,Compra de cartones aun no permitida.\nPartida no finalizada";
                    resp.getWriter().print(mensaje);
                    return;
             } 
          
           //comprobarSaldoUsuario();
            String mensaje2="";
            float precioCarton = new Float (pocketBingoSala.getPrecioCarton());
            float precioCompraActual = nCartonesAComprar * precioCarton;
            float saldoUsuario = user_.getSaldo();
            if(precioCompraActual > saldoUsuario){
                String mensaje="Error,No hay saldo suficiente";
                resp.getWriter().print(mensaje);
                return;
            } else{           
            
            	//realizarCompraTransaccion();
            	boolean comoHaIdo = realizarCompraTransaccion(precioCompraActual,user_);
            	if(comoHaIdo){
            		cargarVectordeCartonesdeUsuario(user_,nCartonesAComprar);
            		mensaje2="Inf,Compra de cartones efectuada";
            	}else{
            		mensaje2="Err,Fallo transaccion compra";
            	}
            		resp.getWriter().print(mensaje2);
            		System.out.println(mensaje2);
            }
        }
    }

    private boolean realizarCompraTransaccion(float precioCompra, UserBean myUser){
        boolean okeyCompra=false;
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        //Formateador de datos decimales. Limitado a dos digitos.
        simbolos.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#######.##",simbolos);
        float saldoRestante = myUser.getSaldo() - precioCompra;
        String Consulta = "UPDATE usuarios SET Saldo = "+formateador.format(saldoRestante)+" WHERE User = '"+myUser.getUsername()+"'";
        System.out.println(Consulta);
        int result=UtilDatabase.updateQuery(Consulta);
        if(result>0){
        	myUser.setSaldo(new  Float(formateador.format(saldoRestante)));
        	okeyCompra=true;
        }
        
        return okeyCompra;
    }
    private void cargarVectordeCartonesdeUsuario(UserBean user_, int nCartones){
                if(user_.getPerfil().equals("jugador")){
                	Vector<Carton> myVectorCarton = user_.getvCarton();
                    for(int i = 0; i< nCartones;i++){
                    	
                        Carton carton = this.cargaObjetoCarton(user_.getSalonInUse());
                        myVectorCarton.add(carton);
                        user_.setvCarton(myVectorCarton);
                    }
                    
                }
        
    }
    public Carton cargaObjetoCarton(String sala){
        Carton carton = new Carton();
        //Connection con = ConnectionManager.getConexionMySQL("localhost", "paco", "ntmanager","wildfly");
        Connection con = ConnectionManager.getConnection();
        Statement st=null;
        try {
            int maxRegs=0;
            int idCartonElegido=0;
            int matrizNumeros[][]=null;
            int idCarton;
            String nCarton;
            String nSerie;
            Blob blob;
            st = con.createStatement();
            //Preguntar cuantos cartones hay registrados/Reservamos los 100 ultimos para juego en papel.
             ResultSet rs = st.executeQuery("Select count('idCarton')from cartonesbingo");
             if(rs.next()){
                 maxRegs=rs.getInt(1);
                 if(maxRegs>200)maxRegs = maxRegs -100;
             }
             Set<Carton> sCartones = gestorSesions.dameSetCartonesEnJuego(sala);
             //Elegir uno, que no este ya en juego//
             boolean seguirBuscandoUnCartonLibre = true;
             boolean estaRepetido = false;
             
             while(seguirBuscandoUnCartonLibre){
                 idCartonElegido = (int) Math.floor((Math.random()*maxRegs)+1);
                 seguirBuscandoUnCartonLibre=false;
                 Iterator it = sCartones.iterator();
                 while(it.hasNext()){
                     Carton c = (Carton)it.next();
                     if(c.getnRef()==idCartonElegido)estaRepetido=true;
                 }
                 if(estaRepetido){
                     seguirBuscandoUnCartonLibre=true;
                     estaRepetido = false;
                 }else{
                     seguirBuscandoUnCartonLibre=false;
                 }
             }
             rs = st.executeQuery("Select idCarton, nCarton, nSerie, arrayCarton from cartonesbingo where idCarton="+idCartonElegido);
             if(rs.next()){
                 idCarton= rs.getInt(1);
                 nCarton = rs.getString(2);
                 nSerie = rs.getString(3);
                   // Se obtiene el campo blob
                 blob = rs.getBlob(4);

                // Se reconstruye el objeto con un ObjectInputStream
                
                try {
                    ObjectInputStream ois = new ObjectInputStream(blob.getBinaryStream());
                     try {
                         matrizNumeros = (int[][]) ois.readObject();
                     } catch (ClassNotFoundException ex) {
                         Logger.getLogger(gestorComprasCartones.class.getName()).log(Level.SEVERE, null, ex);
                     }
                    carton.setnRef(idCarton);
                    carton.setnCarton(nCarton);
                    carton.setnSerie(nSerie);
                    carton.setNumeros(matrizNumeros);
                    carton.setBingoCantado(false);
                    carton.setLineaCantado(false);
                   
                } catch (IOException ex) {
                    Logger.getLogger(gestorComprasCartones.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //matrizArrayCarton int [][] 
             }
        } catch (SQLException ex) {
            Logger.getLogger(gestorComprasCartones.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(con!=null)con.close();
                if(st!=null)st.close();
            } catch (SQLException ex) {
                Logger.getLogger(gestorComprasCartones.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
        return carton;
    }


}
