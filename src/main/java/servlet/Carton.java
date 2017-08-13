package servlet;

public class Carton {
	
	private int nRef;
	private String nCarton;
	private String nSerie;
	private String PrecioCarton;
	//3 lineas de 5 numeros cada una y 9 Columnas nunca 3 a la vez
	//6 cartones cada serie.
	//Fuente Letra Arial condensada tamaño 28
	
	private int[][] numeros = new int[3][9] ;
	private String numeroPantalla;
	private boolean lineaCantado=false;
	private boolean bingoCantado=false;
	private int nOrden=0;
	
	public int getnOrden() {
		return nOrden;
	}
	public void setnOrden(int nOrden) {
		this.nOrden = nOrden;
	}
	public int getnRef() {
        return nRef;
    }
    public void setnRef(int nRef) {
        this.nRef = nRef;
    }
	public String getnCarton() {
		return nCarton;
	}
	public void setnCarton(String nCarton) {
		this.nCarton = nCarton;
	}
	public String getnSerie() {
		return nSerie;
	}
	public void setnSerie(String nSerie) {
		this.nSerie = nSerie;
	}
	public String getPrecioCarton() {
		return PrecioCarton;
	}
	public void setPrecioCarton(String precioCarton) {
		PrecioCarton = precioCarton;
	}
	public String getNumeroPantalla() {
		return numeroPantalla;
	}
	public void setNumeroPantalla(String numeroPantalla) {
		this.numeroPantalla = numeroPantalla;
	}
	public boolean isLineaCantado() {
		return lineaCantado;
	}
	public void setLineaCantado(boolean lineaCantado) {
		this.lineaCantado = lineaCantado;
	}
	public boolean isBingoCantado() {
		return bingoCantado;
	}
	public void setBingoCantado(boolean bingoCantado) {
		this.bingoCantado = bingoCantado;
	}
	public int[][] getNumeros() {
		return numeros;
	}
	public void setNumeros(int[][] numeros) {
		this.numeros = numeros;
	}
	
}
