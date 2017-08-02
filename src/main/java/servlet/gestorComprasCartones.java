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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSessionAComparar = req.getSession(false);
        UserBean user = null;
        //Identificacion de la sesion de usuario//
        if(httpSessionAComparar!=null){
            String usuario = req.getParameter("usuario");
            String sala = req.getParameter("sala");
            int nCartonesAComprar = new Integer(req.getParameter("nCartones"));
            
            Set userBeans = gestorSesions.dameUserBeansPorUser(usuario);
            Iterator it = userBeans.iterator();
            while(it.hasNext()){
                UserBean user_ = (UserBean)it.next();
                String idSesion = user_.getSesionHttp().getId();
                String usuarioreg = user_.getUsername();
                String perfil= user_.getPerfil();
                //if(perfil.equals("jugador")&&usuario.equals(usuarioreg)){
                    
                if(idSesion.equals(httpSessionAComparar.getId())&& perfil.equals("jugador")&&usuarioreg.equals(usuario)){
                    user = user_;
                    cargarVectordeCartonesdeUsuario(user,nCartonesAComprar);
                }
            }
            if(user==null){
                String mensaje="Error,Usuario no esta registrado o no esta On Line ";
                resp.getWriter().print(mensaje);
                return;
            }
            
            //Comprobacion estado Pocket Bingo en "Finalized"
            PocketBingo pocketBingoSala= gestorSesions.getJugadasSalas(user.getSalonInUse());
            if(!pocketBingoSala.getIdState().equals("Finalized")){
                String mensaje="Error,Compra de cartones aun no permitida. Espere a que acabe la partida";
                resp.getWriter().print(mensaje);
                return;
            }            
            //comprobarSaldoUsuario();
            //realizarCompraTransaccion();
            
            String mensaje="Inf,Compra de cartones efectuada";
            resp.getWriter().print(mensaje);
            System.out.println(mensaje);
        }
    }
    private int consultarSaldoUsuario(String usuario, String perfil) {
        int saldo=0;
        
        return saldo;
    }
    private boolean realizarCompraTransaccion(){
        boolean okeyCompra=false;
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
            //Preguntar cuantos cartones hay registrados
             ResultSet rs = st.executeQuery("Select count('idCarton')from cartonesbingo");
             if(rs.next()){
                 maxRegs=rs.getInt(1);
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
