function registraUsuario(){
	//servlet de servicio --->GestionUsuariosServlet
	var params = new Object();
	var textresult=document.getElementById("result");
	var infoArea=document.getElementById("infoArea");
	//params.nCarton=document.getElementById("refCarton"+nC).textContent;
	
	params.usuario=document.getElementById("usuario").value ;
	params.password=document.getElementById("password").value;
	params.email=document.getElementById("email").value;
	params.comando="AltaUsuario";
	var pass = document.getElementById("password").value;
	var pass2 = document.getElementById("passwordConfirm").value
	if(!(pass==pass2)){
		textresult.innerHTML="No coinciden la dos password escritas";
		return;
	}
	if(params.usuario=="" | params.password==""){
		textresult.innerHTML="Deben de introducirse todos los datos";
		return;		
	}
	if(params.email==""){
		textresult.innerHTML="Debe de introducir un Email valido";
		return;				
	}
	$.ajax({
		  type: 'POST',
		  url: "GestionUsuarios",
		  data: params,
		  dataType:"json",
		  async:true
		}).done(function( data ) {
			textresult.innerHTML=data;
			infoArea.innerHTML="";
			
	});
	return
}
function solicitarCompraBonus(){
	//servlet de servicio --->GestionComprasBonos
	var params = new Object();
	var textresult=document.getElementById("result");
	var infoArea=document.getElementById("infoArea");
	
	params.nBonus=document.getElementById("nBonus").value;
	params.usuario=document.getElementById("usuario").value ;
	params.sala=document.getElementById("sala").value;
	params.comando="PeticionCompraBonus"

	$.ajax({
		  type: 'POST',
		  url: "GestorComprasBonus",
		  data: params,
		  dataType:"json",
		  async:true
		}).done(function( data ) {
			textresult.innerHTML=data;
			infoArea.innerHTML="";
	});
	return
}

function volcarPeticionesBonus(){
	//servlet de servicio --->GestionComprasBonos
	var params = new Object();
	var resultLines=document.getElementById("resultLines");
	params.comando="VolcarListadoCompraBonus";

	params.sala=document.getElementById("idSala").value;

	$.ajax({
		  type: 'POST',
		  url: "GestorComprasBonus",
		  data: params,
		  dataType:"json",
		  async:true
		}).done(function( data ) {
			resultLines.innerHTML=data;

	});
	return
}

function realizarPagoBonus(idBonus){
	//servlet de servicio --->GestionComprasBonos
	var params = new Object();
	var resultLines=document.getElementById("resultLines");
	params.comando="RealizarPagoCompraBonus";
	params.sala=document.getElementById("idSala").value;
	params.idBonus=idBonus;

	$.ajax({
		  type: 'POST',
		  url: "GestorComprasBonus",
		  data: params,
		  dataType:"json",
		  async:true
		}).done(function( data ) {
			resultMonitor = document.getElementById("resultMonitor");			
			volcarPeticionesBonus();
			resultMonitor.innerHTML=data;
	});
	return;	
}
function borrarRegistroPeticionBonus(idBonus){
	//servlet de servicio --->GestionComprasBonos
	var params = new Object();
	var resultLines=document.getElementById("resultLines");
	params.comando="BorrarRegistroPeticionBonus";
	params.sala=document.getElementById("idSala").value;
	params.idBonus=idBonus;

	$.ajax({
		  type: 'POST',
		  url: "GestorComprasBonus",
		  data: params,
		  dataType:"json",
		  async:true
		}).done(function( data ) {
			resultMonitor = document.getElementById("resultMonitor");
			volcarPeticionesBonus();
			resultMonitor.innerHTML=data;

	});
	return;		
}

function registraJugador(){
	//servlet de servicio --->GestionUsuariosServlet
	var params = new Object();
	params.sala=document.getElementById("sala").value;
	selectPlayers = document.getElementById("registeredPlayers");

	params.usuario = selectPlayers.value;
	params.comando= "RegistraJugador";
	
		$.ajax({
		  type: 'POST',
		  url: "GestionUsuarios",
		  data: params,
		  dataType:"json",
		  async:true
		}).done(function( data ) {
			var resultLines=document.getElementById("resultLines");
			resultLines.innerHTML=data;
	});
	return;		
}
function removeJugador(jugador){
	//servlet de servicio --->GestionUsuariosServlet
	var params = new Object();
	params.sala=document.getElementById("sala").value;
	params.usuario = jugador;
	params.comando= "RemoveJugador";
	
		$.ajax({
		  type: 'POST',
		  url: "GestionUsuarios",
		  data: params,
		  dataType:"json",
		  async:true
		}).done(function( data ) {
			var resultLines=document.getElementById("resultLines");
			resultLines.innerHTML=data;
	});
}

function mostrarHTML(comando){
	//servlet de servicio --->HtmlDinamicoServLet
	
	var params = new Object();

	params.comando=comando;
	params.sala=document.getElementById("sala").value;	

	$.ajax({
		  type: 'POST',
		  url: "HtmlPortal",
		  data: params,
		  dataType:"html",
		  async:true
		}).done(function( data ) {
			content =document.getElementById("content");
			content.innerHTML = data;

	});

}


function hacerLogin(){
	//servlet de servicio --->HtmlDinamicoServLet
	
	var params = new Object();
	params.un=document.getElementById("un").value ;
	params.pw=document.getElementById("pw").value;	
	params.sala=document.getElementById("sala").value;		

	$.ajax({
		  type: 'POST',
		  url: "LoginServlet",
		  data: params,
		  async:true
		}).done(function( data ) {
			if(data=="Si"){
				window.location.replace("Portal.jsp");	
			}else{
			
				myResp =document.getElementById("resp");
				myResp.innerHTML="Usuario no registrado o password incorrecta";
			}
	});

}



