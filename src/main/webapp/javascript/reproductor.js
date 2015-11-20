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
var contador;
var lienzo;
var seeking="false";
var fin_seek;


function iniciar() {
	video = document.getElementById("medio");
		
	contador=document.getElementById("contador");
	//video.ontimeupdate = function() {refreshCount()};
    
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
	
	
	
}

function show_InMessage(contenido){

	comboTexto.innerHTML=contenido;
	
	
}
function send_command(){
		text_message = document.getElementById("text_comando").value;
		socket_send(text_message);
}
function refreshCount(){
	
	if(seeking=="true"){
			
		if(video.currentTime >= fin_seek){
			seeking="false";
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

					lienzo.scale(1,1);
					ancho=(window.innerWidth*90)/100;
					alto=(ancho*50)/100;
					canvas.width=ancho;
					canvas.height=alto;
                    lienzo.drawImage(imageObj, 0, 0,ancho,alto);
        };
 		 // Calls the imageObj.onload function asynchronously
       imageObj.src="images/Bingo.png";
       
	
	
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
	rangoCanto.push(rangos[indexSearch + 2]);
	return rangoCanto;
	//windexNumber= rangos.indexOf(numerobase):
		
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
	if(seeking=="true"){
		
		if(video.currentTime >= fin_seek){
			seeking="false";
			video.pause();
			window.clearInterval(bucle);
			seeking="false";
			
			socket_send("secuenciaAcabada");
		}
	}
	contador.value=video.currentTime;
}
window.onload=iniciar;
