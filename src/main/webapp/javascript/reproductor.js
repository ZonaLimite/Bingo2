/**
 * Codigo manejador player 
 */
var rangos = ["1",0,2,"2",2,4,"3",4,6,"4",6,8,"5",8,10,"6",10,12];
var video; /** el elemento video */
var boton_play; 
var boton_pause;
var boton_iniciar;
var boton_play_range;
var boton_comando;
var boton_notify;
var caja_output;
var datoOrdenBola;
var ultimaBola;
var contador;
var lienzo;
var seeking="false";
var fin_seek;
var canvas;
var espacioTotal;

function iniciar() {
	video = document.getElementById("medio");
		
	contador=document.getElementById("contador");
	video.ontimeupdate = function() {refreshCount()};
    
	boton_play = document.getElementById("play");
	boton_play.onclick = function() {reanudar()};

	boton_pause = document.getElementById("pause");
	boton_pause.onclick = function() { pausar()};
	
	boton_iniciar = document.getElementById("iniciar");
	boton_iniciar.onclick = function(){ arrancar()};
	
	boton_comando = document.getElementById("comando");
	boton_comando.onclick = function(){send_command()};
	
	datoOrdenBola= document.getElementById("datoOrdenBola")
	boton_notify = document.getElementById("notify");
	boton_notify.onclick = function(){
		socket_send("secuenciaAcabada");
	}
		
	comboTexto = document.getElementById("comboTexto");
	
	boton_play_range= document.getElementById("c_Range");
	boton_play_range.onclick = function(){play_range(document.getElementById("seek_ini").value,document.getElementById("seek_fin").value)};
	
	canvas=document.getElementById('lienzo');
	lienzo=canvas.getContext('2d');
	
	window.onresize= function(){
		espacioTotal=canvas.scrollWidth;
	}
	

	
}

function show_InMessage(contenido){
	
	inicial=comboTexto.innerHTML;
	comboTexto.innerHTML="<option value='"+ contenido+"'>"+contenido+"</option>"+inicial;
	comboTexto.value=contenido;
	
}
function send_command(){
		text_message = document.getElementById("text_comando").value;
		socket_send(text_message);
}
function refreshCount(){
	
	if(seeking=="true"){
			
		if(video.currentTime >= fin_seek){
			seeking="false";
			colocaBola(ultimaBola);	
			video.pause();
			window.clearInterval(bucle);
			seeking="false";
			socket_send("secuenciaAcabada");
		}
	}
	contador.value=video.currentTime;
}
function play_range(ini,fin){
	document.getElementById("seek_ini").value=ini;
	document.getElementById("seek_fin").value=fin;
	fin_seek=fin;
	video.currentTime=ini;
	seeking="true";
	bucle=setInterval(procesarCuadros, 33);
	video.play();
	
}
function arrancar(){
	//creacion de webSocket y autenticacion
	//autenticaUsuario();
	video.play();
	video.pause();
	creaSocket("paquito");
	
}
function creaSocket(usuario){
	var wsUri = getRootUri() + "actions";
	
	socket=new WebSocket(wsUri);
	
	//socket=new WebSocket("ws://localhost:8080/wildfly/actions");
	
	socket.addEventListener('open', abierto, false);
	socket.addEventListener('message', recibido, false);
	socket.addEventListener('close', cerrado, false);
	socket.addEventListener('error', errores, false);	
	
}
function getRootUri() {
	/*Web Sockets on OpenShift work over ports 8000 for ws and 8443 for wss,*/
	    
		if(document.location.hostname=="localhost"){
			nameEndPoint = "/wildfly/";
			port="8080";
		}
		else{
			nameEndPoint="/";
			port="8000";
		}
		
        return "ws://" + (document.location.hostname == "" ? "localhost" : document.location.hostname) + ":" +
                (document.location.port == "" ? "8000" : document.location.port) + nameEndPoint;
    
}

function abierto(){
	show_InMessage("socket abierto");
	
	var imageObj = new Image();
        
        imageObj.onload = function() {
                    lienzo.drawImage(imageObj, 0, 0);
					espacioTotal=canvas.scrollWidth;
			
        };
 		 // Calls the imageObj.onload function asynchronously
         imageObj.src ="images/Bingo.png";
	        
       
	
	
	//alert("Ancho canvas=" + canvas.width + ", alto="+ canvas.height);
	//imagen.addEventListener("load", function(){lienzo.drawImage(imagen,0,0,canvas.width,canvas.height)}, false);
	
	
	//socket_send("startGame");
}
function cerrado(){
	show_InMessage("El socket se ha cerrado");
}
function errores(e){
	show_InMessage("Error "+ e.ToString())
}
function recibido(e){
	//manejador mensajes
	show_InMessage(e.data);

	mensaje=""+e.data;
	
	arrayMessages=mensaje.split("_");
	
	if(arrayMessages.length > 0){
		 
	comando=arrayMessages[0];
		switch(comando) {
		    
		//Cantar numero y mostrar orden bola
		case "cantarNumero":
	    	    myRango=sacarRangos(arrayMessages[1]);
				datoOrdenBola.innerHTML=arrayMessages[2];
	    	    play_range(myRango[0],myRango[1]);	
	    	    break;
		case "Info":
				if(arrayMessages[1]="PocketAbierto"){
					result=confirm("Hay una partida empezada,desea continuar(Yes) o empezar(No)")
					if(result){
						socket_send("resume");
					}else{
						socket_send("startGame")
					}
				}
		    default:
        		
		} 
	}
}
function sacarRangos(numerobase){
	var rangoCanto= [];
	indexSearch= rangos.indexOf(numerobase);
	rangoCanto.push(rangos[indexSearch + 1]);
	ultimaBola = numerobase;
	rangoCanto.push(rangos[indexSearch + 2]);

	return rangoCanto;
	//indexNumber= rangos.indexOf(numerobase):
		
}
function socket_send(comanda){
	socket.send(comanda);
}
function pausar(){
	  boton_pause.style.borderBottomStyle = "inset";
	  boton_pause.style.borderLeftStyle = "inset";
  	  video.pause();
      boton_play.style.borderBottomStyle = "outset";
	  boton_play.style.borderLeftStyle = "outset";	
}

function reanudar(){
	boton_play.style.borderBottomStyle = "inset";
	boton_play.style.borderLeftStyle = "inset";
	video.play();
	   boton_pause.style.borderBottomStyle = "outset";
	   boton_pause.style.borderLeftStyle = "outset";
}
function procesarCuadros(){
	//lienzo.drawImage(video,0,0,video.width,video.height,(canvas.width/2)-30,(canvas.height/2)-30,30,30);
	lienzo.drawImage(video,(canvas.width/2)-22,(canvas.height/2)-26,58,51);

}

function colocaBola(nBola){
//CalculaPosicion
var StringNumero = ""+nBola;

espacioPrivate= (espacioTotal*15)/100;
espacioCante= Math.round(espacioPrivate);
espacioDoble= espacioTotal - espacioCante;
espacioTramas = Math.round(espacioDoble/2);
espacioColNumber= Math.round(espacioTramas/5);
ComienzoTrama1 = 0
ComienzoTrama2 = espacioTramas + espacioPrivate;
espacioOffset = Math.round(espacioColNumber/2);
if(StringNumero.length ==2){
	MultiplicadorFila = StringNumero.substr(1, 1);
	situacionCol = StringNumero.substr(0, 1);
}else{
	MultiplicadorFila=nBola;
	situacionCol=0;
}
if(nBola<50){
	distanciaEntreBolasX = espacioColNumber ;
}else{
	distanciaEntreBolasX = espacioCante + espacioColNumber;
}

xNumero= (situacionCol * espacioOffset) + distanciaEntreBolasX;
yNumero= (espacioOffset * MultiplicadorFila) + (espacioOffset/2)+2;
// DibujaLaBola
DibujaLaBola(xNumero,yNumero,espacioOffset/2,nBola);
}

function DibujaLaBola(x,y,radio,bola){
	lienzo.beginPath();
	lienzo.arc(x,y,radio,0,2*Math.PI);
	lienzo.stroke();
	lienzo.fillStyle="red";
	lienzo.fill()

	
	lienzo.font="20px Arial";
	texto=""+bola;
	anchoTexto=(lienzo.measureText(texto).width);
	if(bola<10){
		altoTexto=anchoTexto;
	}else{
		altoTexto=Math.round(anchoTexto/2 +2);
	}
	
	lienzo.beginPath();
	lienzo.arc(x,y,(anchoTexto/2)+2,0,2*Math.PI);
	lienzo.stroke();
	lienzo.fillStyle="white";
	lienzo.fill();

	lienzo.beginPath();
	lienzo.fillStyle="black";	
	lienzo.fillText(texto,x - (anchoTexto/2),(y + altoTexto)- (altoTexto/2) + 1);
	lienzo.fill();
	

}

window.onload=iniciar;
