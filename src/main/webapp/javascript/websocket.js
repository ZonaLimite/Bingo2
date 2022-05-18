function iniciar(){
	alert("inicio");
	cajadatos=document.getElementById('cajadatos');
	var boton_enviar=document.getElementById('boton');
	var boton_open = document.getElementById('Open');
	boton_enviar.addEventListener('click', enviar, false);
	boton_open.addEventListener('click', open, false);
	
} 
function open(){
	alert("Open");
	socket=new WebSocket("ws://localhost:8080/wildfly/example");
	socket.addEventListener('message', recibido, false);
}

function recibido(e){
	var lista=cajadatos.innerHTML;
	cajadatos.innerHTML='Recibido: '+e.data+'<br>'+lista;
} 

function enviar(){
	alert("enviar");
	var comando=document.getElementById('comando').value;
	socket.send(comando);
} 
