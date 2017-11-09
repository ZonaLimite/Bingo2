function innerHTMLCartones(nCartones,comando, usuario){
	var params = new Object();
	params.comando=comando;
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
