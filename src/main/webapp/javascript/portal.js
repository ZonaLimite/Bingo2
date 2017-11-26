var botonLogin;


function iniciar(){
	botonLogin = document.getElementById("botonLogin");
	botonLogin.onclick = function(){ mostrarHTML("MostrarLogin");};

	botonUsuarios = document.getElementById("botonRanking");
	botonUsuarios.onclick = function(){ mostrarHTML("MostrarUsuarios");};	
	

}	


function mostrarHTML(comando){
			//servlet de servicio --->HtmlDinamicoServLet

			var params = new Object();
			
			
			
			params.comando=comando;


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

window.onload=iniciar;