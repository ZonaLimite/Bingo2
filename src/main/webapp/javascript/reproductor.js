/**
 * Codigo manejador player 
 */
var video; /** el elemento video */
var boton_play; 
var boton_pause;
var boton_iniciar;
var boton_comando;
function iniciar() {
	video = document.getElementById("medio");
	video.ontimeupdate = function() {refreshCount()};
    
	boton_play = document.getElementById("play");
	//boton_play.onclick = function() { reanudar()};
	boton_play.addEventListener('click', reanudar, false);

	boton_pause = document.getElementById("pause");
	boton_pause.onclick = function() { pausar()};
	
	boton_iniciar = document.getElementById("iniciar");
	boton_iniciar.onclick = function(){ arrancar()};
	
	boton_comando = document.getElementById("comando");
	boton_comando.onclick = function(){send_command()};
	
}

function send_command(){
		text_message = document.getElementById("text_comando").value;
		socket_send(text_message);
}
function refreshCount(){
	contador=document.getElementById("contador");
	contador.value=video.currentTime;
}

function arrancar(){
	//creacion de webSocket y autenticacion
	//autenticaUsuario();
	creaSocket("paquito");
}
function creaSocket(usuario){
	alert("entrando en socket");
	socket=new WebSocket("ws://localhost:8080/wildfly/actions");

	socket.addEventListener('open', abierto, false);
	socket.addEventListener('message', recibido, false);
	socket.addEventListener('close', cerrado, false);
	socket.addEventListener('error', errores, false);	
}

function abierto(){
	alert("socket abierto");
}
function cerrado(){
	alert("El socket se ha cerrado");
}
function errores(e){
	alert("Error "+ e.ToString())
}
function recibido(e){
	//manejador mensajes
	cajadatos=document.getElementById('output');
	cajadatos.innerHTML='Recibido: '+e.data;
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
	alert("reanudar");
	boton_play.style.borderBottomStyle = "inset";
	boton_play.style.borderLeftStyle = "inset";
	video.play();
	   boton_pause.style.borderBottomStyle = "outset";
	   boton_pause.style.borderLeftStyle = "outset";
}

window.onload=iniciar;
