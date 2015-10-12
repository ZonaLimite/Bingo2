/**
 * Codigo manejador player 
 */
var video; /** el elemento video */
var boton_play; 
var boton_pause;

function iniciar() {
	alert("inicio");
	video = document.getElementById("medio");
	video.ontimeupdate = function() {refreshCount()};
    
	boton_play = document.getElementById("play");
	boton_play.onclick = function() { reanudar()};
	
	boton_pause = document.getElementById("pause");
	boton_pause.onclick = function() { pausar()};	
}
function refreshCount(){
	contador=document.getElementById("contador");
	contador.value=video.currentTime;
}

function arrancar(){
	
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
