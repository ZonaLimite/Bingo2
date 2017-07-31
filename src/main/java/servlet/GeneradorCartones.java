/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import servlet.ConnectionManager;

/**
 * //Generador de cartones y registro en base de datos
 * @author hormigueras
 */
public class GeneradorCartones {
    private int numeroSeriesRequeridas=0;
    private int numeroSeriesConseguidas=0;
    private Map series;
    
    //key=String-->"nSerie,nCarton"
    //value= array[3][9]
    private HashMap cartones;
    private List numerosComputados;
    private int numberSpecial=90;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        GeneradorCartones gen = new GeneradorCartones();
        HashMap totalCartones;
        gen.seriesRequeridas(1);
        totalCartones = gen.generar();
        //gen.serializarCartones(totalCartones);
        int cuantos=gen.registraCartones(totalCartones);
                
               // ("mysql.hostinger.es","u854424593_paco","ntmanager");
        //con = getConexionMySQL("192.168.1.130","paco","ntmanager");
        System.out.println("cartones registrados:"+cuantos);
        //System.out.println(gen.dameKeys());
        
    }
    public void seriesRequeridas(int cantidad){
        this.numeroSeriesRequeridas = cantidad;
    }
    
    public int registraCartones(HashMap cartones){
        int nRegistros=0;
        Connection myCon=null;
        PreparedStatement ps=null;
  
        try {
            //se utiliza una conexion local
            //myCon=ConnectionManager.getConexionMySQL("localhost", "paco", "ntmanager","wildfly");
            myCon = ConnectionManager.getConnection();
            String sCarton,sSerie;
            ps = myCon.prepareStatement("insert into cartonesbingo values (null,?,?,?)");
            Set setKeys = cartones.keySet();
            Iterator itKeys = setKeys.iterator();
            
            while(itKeys.hasNext()){
                String key=(String)itKeys.next();
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                ObjectOutputStream oos;
                        try {
                            oos = new ObjectOutputStream(byteArray);
                            oos.writeObject(cartones.get(key));
                        } catch (IOException ex) {
                            Logger.getLogger(GeneradorCartones.class.getName()).log(Level.SEVERE, null, ex);
                        }
                StringTokenizer st= new StringTokenizer(key,",");

                sSerie=st.nextToken();
                sCarton=st.nextToken();
                ps.setString(1, sCarton);
                ps.setString(2, sSerie);
                ps.setBytes(3, byteArray.toByteArray());
                ps.execute();
                nRegistros++;
            }

            
        } catch (SQLException ex) {
            Logger.getLogger(GeneradorCartones.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(myCon!=null)try {
                myCon.close();
                
            } catch (SQLException ex) {
                Logger.getLogger(GeneradorCartones.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(ps!=null)try {
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(GeneradorCartones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        
        return nRegistros;
    }
    
    public void serializarCartones(HashMap mapa){
    
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("c:\\\\temp\\cartones.map");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mapa);
            oos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GeneradorCartones.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GeneradorCartones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
 
    }
    
    public GeneradorCartones(){
        

       
    }
    private Set dameKeys(){
        Set keys= cartones.keySet();
        System.out.println("Numero de claves="+keys.size());
        return keys;
    }
    public HashMap generar(){
            cartones= new HashMap();
            for(int i=0; i < numeroSeriesRequeridas; i++){
                series= new HashMap();
                if(generaSerie()==90){
                    String cifra ="";
                    for(int vuelta=1;vuelta < 7;vuelta++){
                        int matriz[][] =(int[][])series.get(vuelta);
                        for(int f=0;f<3;f++){
                            for(int c=0;c<9;c++){
                                int valor = matriz[f][c];
                                if (valor < 10){
                                    cifra = "0"+valor;
                                }else{
                                    cifra= ""+valor;
                                }
                            System.out.print(cifra+" ");
                     
                        }
                        System.out.println();
                    }
                    System.out.println();
                    }
                    System.out.println("Numeros computados:"+numerosComputados.size());
                    System.out.print(numerosComputados.toString());
                    System.out.println();
                    recopilarSerie(series);
                 //añadir el objeto carton a la coleccion cartones
                }else{
                    i--;
                }

            }
        return this.cartones;    
    }
    private int recopilarSerie(Map mapa){
        int result=0;
        Calendar cal = Calendar.getInstance();
        long hora =cal.getTimeInMillis();
        for(int nCarton=1;nCarton <7;nCarton++){
             String keyCarton= ""+hora+","+nCarton;
             cartones.put(keyCarton, mapa.get(nCarton));
             result++;
        }
       
        return result;
    }
    private int generaSerie(){
            this.numerosComputados = new LinkedList();
            //genera un mapa de seis cartones y un array[3][9] con ceros.
            this.initMapaSerie();
            Date medidor = new Date();
            long valorAlInicio = medidor.getTime();
            long valorEnProceso;
            while(true){
            
                for(int nCarton=1; nCarton<7;nCarton++){
                    if(this.numerosComputados.size()==90){
                        for(int nCarton2=1; nCarton2<7;nCarton2++){
                            ordenaCarton(nCarton2);
                        }
                        return this.numerosComputados.size();
                    }
                    int fila = (int)Math.floor((Math.random()*3)+1);
                    int numero = numeroAleatorioNoRepetido(90);
                    int minCuantosEnFila=5;
                    int valor;
                    for(int it=1;it<4;it++){
                        valor=hayCuantosenLinea(nCarton, it);
                        if(valor<minCuantosEnFila ){
                            minCuantosEnFila=valor;
                            fila=it;
                        }

                    }
                    
                    rellenaCartones(nCarton,fila,numero);
                }
                medidor= new Date();
                valorEnProceso = medidor.getTime();
                if((valorEnProceso-valorAlInicio) > 2000)return this.numerosComputados.size();
            }
            
    }
    private void ordenaCarton(int nCarton){
        int matriz [][] =(int[][]) series.get(nCarton); 
        int filaValMax=0;
        int filaValMin=0;
        int valMin=0;
        int valMax=valMin;
        for(int col=0;col<9;col++){
           
            filaValMax=0;
            filaValMin=0;
            valMax=0;
            valMin=0;
            if(comprobarNumerosEnColumna(col+1, nCarton)>1){
            for(int fila=0;fila<3;fila++){
                int valor = matriz[fila][col];
                    if (valor>valMax && valor!=0){
                        valMax=valor;
                        filaValMax=fila;
                    }else{
                        if(valor!=0){
                            valMin=valor;
                            filaValMin=fila;
   
                        }
                    }           
            }
            if(filaValMin>filaValMax){
                    
                    int copyValMax=matriz[filaValMax][col];
                    int copyValMin=matriz[filaValMin][col];
                    
                    int copyFila=filaValMin;
                    filaValMin=filaValMax;
                    filaValMax=copyFila;
                    matriz[filaValMax][col]=copyValMax;
                    matriz[filaValMin][col]=copyValMin;
            }
            }
        }
        
        series.put(nCarton,matriz );
    }
    private void rellenaCartones(int numCarton,int fila, int numero){
        //seleccionamos numero carton
            if(cartonNumerado(numCarton))return;
            int columna = this.leCorrespondeColumna(numero);
            int cuantosEnColumna = comprobarNumerosEnColumna(columna,numCarton);
            if(cuantosEnColumna==2 || hay5enLinea(numCarton,fila) ){
                return;
            }
            int[][]matriz = (int[][]) series.get(numCarton);
            if(matriz[fila-1][columna-1]!=0){
                
                
                return;
            }
            matriz[fila-1][columna-1]=numero;
            
            series.put(numCarton,matriz);
            numerosComputados.add(numero);
            //this.numberSpecial--;
            
            
        
        //numero aleatorio no repetido en la serie
        //numero d fila aleatorio(1 a 3) No 3 columnas consecutivas
        //Calculo columna segun numero
        //ordenar columnas en cada carton
       
    }
    private boolean cartonNumerado(int numCarton){
        int numerosContiene=0;
        boolean estaNumerado=false;
        int[][]matriz = (int[][]) series.get(numCarton );
                    for(int i=0;i<3;i++){
                        for(int j=0;j<9;j++){
                            if(matriz[i][j]>0)numerosContiene++;
                        }
                    }
        if(numerosContiene==15)estaNumerado=true;
        return estaNumerado;
        
    }
    private boolean hay5enLinea(int numCarton, int fila){
        int hayEstos=0;
        boolean resul=false;
        int[][]matriz = (int[][]) series.get(numCarton);
        for(int col=0;col<9;col++){
            if(matriz[fila-1][col]>0)hayEstos++;
        }
        if(hayEstos>4)resul=true;
        return resul;
        
    } 
        private int hayCuantosenLinea(int numCarton, int fila){
        int hayEstos=0;
       
        int[][]matriz = (int[][]) series.get(numCarton);
        for(int col=0;col<9;col++){
            if(matriz[fila-1][col]>0)hayEstos++;
        }
        
        return hayEstos;
        
    } 

    private int comprobarNumerosEnColumna(int column, int carton){
        int cuantos=0;
        for(int f=0;f<3;f++){
           int[][]matriz = (int[][]) series.get(carton);
           int valor=matriz[f][column-1];
           if(valor>0)cuantos++;
        }
        return cuantos;
        
    }

    private int leCorrespondeColumna(int n){
        int col=0;
        if(n>0 & n<10)col=1;
        if(n>9 & n<20)col=2;
        if(n>19 & n<30)col=3;
        if(n>29 & n<40)col=4;
        if(n>39 & n<50)col=5;
        if(n>49 & n<60)col=6;
        if(n>59 & n<70)col=7;
        if(n>69 & n<80)col=8;
        if(n>79 & n<91)col=9;
        
        return col;
        
        
        
    }
    
    private void initMapaSerie(){
        for(int carton=1;carton<7;carton++){
            int matrizNumeros[][]= new int [3][9];
            for(int i=0;i<3;i++){
                        for(int j=0;j<8;j++){
                            matrizNumeros[i][j]=0;
            }
        }
        series.put(carton,matrizNumeros );
        }
        
    }
   
    private int numeroAleatorioNoRepetido(int maxNumber){
        
        int number=0;
        number = (int) Math.floor((Math.random()*maxNumber)+1);
        while(estaRepetido(number)){
            number = (int) Math.floor((Math.random()*maxNumber)+1);
        }
        
        return number;
                
        //return this.numberSpecial;
    }        
    private boolean estaRepetido(int num ){
        boolean repetead = false;
        if(this.numerosComputados.contains(num))repetead=true;
        return repetead;
    }

}
