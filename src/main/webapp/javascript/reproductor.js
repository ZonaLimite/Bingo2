/**
 * Codigo manejador player 
 */
var video; /** el elemento video */
var boton_play; 
var boton_pause;
var boton_iniciar;
var boton_play_range;
var boton_comando;
var boton_notify;
var caja_output;
var contador;
var seeking=false;
var fin_seek=5;

function iniciar() {
	video = document.getElementById("medio");

	contador=document.getElementById("contador");
	video.ontimeupdate = function() {refreshCount()};
    
	boton_play = document.getElementById("play");
	//boton_play.onclick = function() { reanudar()};
	boton_play.onclick = function() {reanudar()};

	boton_pause = document.getElementById("pause");
	boton_pause.onclick = function() { pausar()};
	
	boton_iniciar = document.getElementById("iniciar");
	boton_iniciar.onclick = function(){ arrancar()};
	
	boton_comando = document.getElementById("comando");
	boton_comando.onclick = function(){send_command()};
	
	boton_notify = document.getElementById("notify");
	boton_notify.onclick = function(){
		socket_send("secuenciaAcabada");
	}
		
	comboTexto = document.getElementById("comboTexto");
	
	boton_play_range= document.getElementById("c_Range");
	boton_play_range.onclick = function(){play_range(document.getElementById("seek_ini").value,document.getElementById("seek_fin").value)};
	
}

function show_InMessage(contenido){
	
	inicial=comboTexto.innerHTML;
	comboTexto.innerHTML="<option value='"+ contenido+"'>"+contenido+"</option>"+inicial;
	
}
function send_command(){
		text_message = document.getElementById("text_comando").value;
		socket_send(text_message);
}
function refreshCount(){
	value_contador=video.currentTime;
	if(seeking==true){
		if(value_contador >= fin_seek){
			seeking=false;
			video.pause();
			socket_send("secuenciaAcabada");
		}
	}
	contador.value=value_contador;
}
function play_range(ini,fin){
	seeking=true;
	fin_seek=fin;
	video.currentTime=ini;
	video.play();
}
function arrancar(){
	//creacion de webSocket y autenticacion
	//autenticaUsuario();
	creaSocket("paquito");
}
function creaSocket(usuario){
	show_InMessage("entrando en socket");
	socket=new WebSocket("ws://localhost:8080/wildfly/actions");

	socket.addEventListener('open', abierto, false);
	socket.addEventListener('message', recibido, false);
	socket.addEventListener('close', cerrado, false);
	socket.addEventListener('error', errores, false);	
}

function abierto(){
	show_InMessage("socket abierto");
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

window.onload=iniciar;
