function innerHTMLCartones(nCartones,perfil, usuario){
	var params = new Object();
	params.perfil=perfil;
	params.nCartones = ""+nCartones;
	params.usuario=usuario;

	$.ajax({
		  type: 'POST',
		  url: "WriterCartonesServlet",
		  data: params,
		  async: true
		}).done(function( data ) {
			ventanaCartones.innerHTML=data;
			activarCartones();
		
	});
}
