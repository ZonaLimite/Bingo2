function registraUsuario(){
	//servlet de servicio --->GestionUsuariosServlet
	var params = new Object();
	var textresult=document.getElementById("result");
	var infoArea=document.getElementById("infoArea");
	//params.nCarton=document.getElementById("refCarton"+nC).textContent;
	
	params.usuario=document.getElementById("usuario").value ;
	params.password=document.getElementById("password").value;
	params.email=document.getElementById("password").value;
	params.comando="AltaUsuario";
	var pass = document.getElementById("password").value;
	var pass2 = document.getElementById("passwordConfirm").value
	if(!(pass==pass2)){
		textresult.innerHTML="No coinciden la dos password escritas";
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