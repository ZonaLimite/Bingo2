
var usuario;
var sala;
var perfil;
var url;

function iniciar2(){
	//alert("iniciar2");
	usuario = document.getElementById("usuario");
	perfil = document.getElementById("perfil");
	sala = document.getElementById("sala");
	if(perfil.value=="supervisor")url="WriterHeaderBingo?usuario="+usuario.value+"&sala="+sala.value+"&perfil="+perfil.value;
	if(perfil.value=="tablero")url="WriterHeaderTablero?usuario="+usuario.value+"&sala="+sala.value+"&perfil="+perfil.value;
	if(perfil.value=="jugador")url="WriterHeaderCarton?usuario="+usuario.value+"&sala="+sala.value+"&perfil="+perfil.value;
	if(usuario.value=="null")mostrarHTML("MostrarLogin")
	else{
		botonAccessBingo = document.getElementById("AccessBingo");
		botonAccessBingo.onclick = function(){ window.location.replace(url);};
		botonUsuarios = document.getElementById("botonRanking");
		botonUsuarios.onclick = function(){ mostrarHTML("MostrarGamerStatus");};	
		botonCerrarSesion = document.getElementById("botonCerrarSesion");
		botonCerrarSesion.onclick = function(){window.location.replace("HtmlPortal?comando=CerrarSesion");};
		botonCompraBonos = document.getElementById("compraBonos");
		botonCompraBonos.onclick = function(){mostrarHTML("Solicitud Bonus")};
		botonSolicitudLiquidarBonos = document.getElementById("liquidarBonos");
		botonSolicitudLiquidarBonos.onclick = function(){mostrarHTML("Solicitud Liquidacion")};
		botonVolcadoLiquidacionBonos = document.getElementById("volcadoLiquidacion");
		if(botonVolcadoLiquidacionBonos!=null){
			botonVolcadoLiquidacionBonos.onclick = function(){
			mostrarHTML("VolcadoPeticionesLiquidacionBonus");
			};
		}		
		botonVolcadoBonos = document.getElementById("volcadoBonos");
		if(botonVolcadoBonos!=null){
			botonVolcadoBonos.onclick = function(){
			mostrarHTML("VolcadoPeticionesBonus");
			};
		}
		botonJugadores = document.getElementById("botonJugadores");
		if(botonJugadores!=null){
			botonJugadores.onclick = function(){
			mostrarHTML("ConfigurarJugadores");
			};
		}
		botonCartones = document.getElementById("botonCartones");
		if(botonCartones!=null){
			botonCartones.onclick = function(){
			mostrarHTML("ConfigurarCartones");
			};
		}		
		
	}
	
}	



window.onload=iniciar2;