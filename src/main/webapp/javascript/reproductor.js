/**
 * Codigo manejador player 
 */
var rangosLola = ["BuenasNoches",0.8930,5.5210,"linea",6,8.6588,"bingo",8.6588,11.746,"lineaOk",11.746,14.798,"bingoOk",14.798,18.396,"1",18.407,22.029,"2",22.029,23.363,"3",23.363,25.917,"4",25.917,27.960,"5",27.960,30.770,"6",30.770,33.543,"7",33.543,35.945,"8",35.945,38.545,"9",38.545,41.202,"10",41.202,43.446,"11",43.446,47.028,"12",47.028,49.772,"13",49.772,52.54,"14",52.54,54.506,"15",54.506,57.594,"16",57.594,59.620,"17",59.620,62.022,"18",62.022,65.492,"19",65.492,67.976,"20",67.976,69.774,"21",69.774,72.548,"22",72.548,75.948,"23",75.948,78.187,"24",78.187,80.811,"25",80.811,83.140,"26",83.140,85.816,"27",85.816,87.823,"28",87.823,90.520,"29",90.520,93.000,"30",93.000,95.035,"31",95.035,98.296,"32",98.296,100.40,"33",100.40,102.89,"34",102.89,105.84,"35",105.84,108.69,"36",108.69,110.67,"37",110.67,113.97,"38",113.97,116.60,"39",116.60,118.48,"40",118.48,121.16,"41",121.16,123.77,"42",123.77,126.35,"43",126.35,128.65,"44",128.65,131.00,"45",131.00,134.46,"46",134.46,137.27,"47",137.27,139.83,"48",139.83,142.76,"49",142.76,145,"50",145,148,"51",148,150.93,"52",150.93,154,"53",154,155.68,"54",155.68,158.82,"55",158.82,161.43,"56",161.43,163.80,"57",163.80,166.68,"58",166.68,169.80,"59",169.80,172.10,"60",172.10,175.42,"61",175.42,178.65,"62",178.65,181.53,"63",181.53,184.79,"64",184.79,187.42,"65",187.42,190.26,"66",190.26,192.68,"67",192.68,195.30,"68",195.30,197.81,"69",197.81,200.42,"70",200.42,202.12,"71",202.12,204.87,"72",204.87,207.87,"73",207.87,209.73,"74",209.73,212.55,"75",212.55,216.15,"76",216.15,218.11,"77",218.11,221.44,"78",221.44,224.83,"79",224.83,227.62,"80",227.62,229.85,"81",229.85,232.39,"82",232.39,235.53,"83",235.53,237.42,"84",237.42,240.51,"85",240.51,243.21,"86",243.21,246.30,"87",246.30,249.58,"88",249.58,252.26,"89",252.26,254.05,"90",254.05,256.75];
var rangosInes = ["1",2.51,5.07,"2",5.07,7.49,"3",7.49,9.86,"4",9.86,12.65,"5",12.65,14.95,"6",14.95,17.25,"7",17.25,19.35,"8",19.35,21.21,"9",21.21,24.14,"10",24.14,26.68,"11",26.68,28.33,"12",28.33,30.89,"13",30.89,33.68,"14",33.68,35.94,"15",35.94,38.26,"16",38.26,40.36,"17",40.36,42.90,"18",42.90,44.92,"19",44.92,46.55,"20",46.55,49.04,"21",49.04,50.69,"22",50.69,52.34,"23",52.34,54.45,"24",54.45,56.82,"25",56.82,59.43,"26",59.43,61.53,"27",61.53,63.63,"28",63.63,65.70,"29",65.70,67.09,"30",67.09,69.14,"31",69.14,71.26,"32",71.21,73.31,"33",73.31,75.24,"34",75.24,77.45,"35",77.45,79.82,"36",79.82,81.96,"37",81.96,84.10,"38",84.10,86.06,"39",86.06,88.20,"40",88.20,90.16,"41",90.16,92.30,"42",92.30,94.86,"43",94.86,96.72,"44",96.72,99.14,"45",99.14,101.79,"46",101.70,103.89,"47",103.89,106.19,"48",106.19,108.42,"49",108.42,110.52,"50",110.52,112.62,"51",112.62,115.13,"52",115.13,117.16,"53",117.16,119.58,"54",119.58,121.86,"55",121.86,124.40,"56",124.40,126.60,"57",126.60,128.56,"58",128.56,130.52,"59",130.52,132.52,"60",132.52,136.38,"61",136.38,140.21,"62",140.21,144.07,"63",144.07,147.60,"64",147.60,152.44,"65",152.53,156.03,"66",156.03,159.98,"67",159.98,163.77,"68",163.77,167.61,"69",167.61,171.20,"70",228.14,229.96,"71",229.96,231.61,"72",174.21,177.81,"73",177.81,181.90,"74",181.90,185.69,"75",185.69,189.11,"76",189.11,192.39,"77",192.39,194,"78",195.70,197.61,"79",199.50,200.90,"80",202.23,204.14,"81",204.14,206.84,"82",206.84,208.68,"83",208.68,210.75,"84",210.75,213.01,"85",213.01,215.57,"86",215.57,217.80,"87",217.80,219.83,"88",219.83,221.79,"89",221.79,223.91,"90",223.91,225.31,"linea",235,237.72,"bingo",239.19,242.50,"lineaOk",242.58,246.49,"bingoOk",246.49,250.88,"BuenasNoches",250.88,253.60]

var rangos;
var video; /** el elemento video */
var boton_play; 
var boton_pause;
var boton_Linea;
var boton_Bingo
var boton_iniciar;
var boton_Continuar;
var boton_play_range;
var boton_comando;
var boton_cartones;
var boton_notify;
var caja_output;
var caja_spy;
var datoOrdenBola;
var contador;
var lienzo;
var seeking="false";
var fin_seek;
var esPrimeraVez;
var anchoPantalla;
var elementCanvas;
var ctxCanvas;
var bucle=null;
var bucle2=null;
var npat=1;
var nuevoTamano;
var maxTamano=75;
var cajaTamano;
var tamanoMenu=null;
var palabraLinea;
var palabraBingo;
var triggerBingo;
var triggerLinea="false";
var colorTriggerLinea=900;
var datoCartones,datoLinea,datoBingo,etiquetaOrden
var valCodecs="MP4";
var resultDialogo="Empezar";
var videoEnable="true";
var lineaCantada="false";
var bingoCantado="false";
var precioCarton=0;
var nCartones=0;
var porCientoLinea=0;
var porCientoBingo=0;
var porCientoCantaor=0;
var sumaTantos=0;
var sumaCaja=0;
var flagVideoReady="false";
var myV=1;
//var nombreRangos="rangosLola";
var nombreRangos="rangosInes";
//var nombreFileVideo="http://boga.esy.es/video/BingoLola.mov";
var nombreFileVideo="http://boga.esy.es/video/BingoInes.mov";
var cajaFecha;
var arrayMessages=null;
var copYColor;
var copymyV;
var copynpat;
function iniciar() {
	rangos=eval(nombreRangos);
	video = document.getElementById("medio");
	//video.src=nombreFileVideo;
	video.type="video/mp4";
	video.oncanplay = function() {
	    flagVideoReady="true";
	};
	video.load();
	elementCanvas = document.getElementById("canvas_bola");	
	ctxCanvas = elementCanvas.getContext("2d");
	contador=document.getElementById("contador");
	palabraLinea = document.getElementById("labelLinea");
	palabraBingo = document.getElementById("labelBingo");
	datoCartones=document.getElementById("valorCartones");
    datoLinea=document.getElementById("valorLinea");
    datoBingo=document.getElementById("valorBingo");
    cajaFecha = document.getElementById("CajaDcha");
    etiquetaOrden = document.getElementById("labelOrden");
	boton_play = document.getElementById("play");
	boton_play.onclick = function() {reanudar()};

	boton_pause = document.getElementById("pause");
	boton_pause.onclick = function() { pausar()};
	
	boton_Linea= document.getElementById("boton_Linea");
	boton_Linea.onclick = function(){ 
		if(lineaCantada=="true" || lineaCantada=="comprobando")return;
		lineaCantada="comprobando";
		socket_send("Linea");
		triggerLinea="true";
	};
	
	boton_Bingo= document.getElementById("boton_Bingo");
	boton_Bingo.onclick = function(){
		if(lineaCantada=="false" || lineaCantada=="comprobando" || bingoCantado=="comprobando")return;
		bingoCantado="comprobando";
		socket_send("Bingo");
		triggerBingo="true";
	};

	boton_LineaOk= document.getElementById("boton_LineaOk");
	boton_LineaOk.onclick = function(){
		if(lineaCantada!="comprobando")return;
		apagaLinea();
		enciendeVideo();
		socket_send("Linea_OK");
	};
	
	boton_BingoOk= document.getElementById("boton_BingoOk");
	boton_BingoOk.onclick = function(){
		if(bingoCantado!="comprobando")return;
		palabraBingo.style.visibility="hidden";
		apagaBingo();
		enciendeVideo();
		socket_send("Bingo_OK");
	};
	
	boton_Continuar = document.getElementById("boton_Continuar");
	boton_Continuar.onclick = function(){
		
		if(lineaCantada=="comprobando"){
			lineaCantada="false";
			triggerLinea="false";
			socket_send("Continue");
		}
		if(bingoCantado=="comprobando"){
			bingoCantado="false";
			triggerBingo="false";
			socket_send("Continue");
		}
		

		
	}
	boton_iniciar = document.getElementById("iniciar");
	boton_iniciar.onclick = function(){ arrancar()};
	
	boton_opciones=document.getElementById("preferencias");
	boton_opciones.onclick = function(){
		$( "#opciones" ).dialog( "open" );
		$( "#aplicarSize" ).focus();
	}
	$( "#aplicarSize" ).click(function() {
		nuevoTamano=cajaTamano.value;
		resizeBolas(nuevoTamano);
		});

    boton_cartones=document.getElementById("lab_cartones");
	boton_cartones.onclick = function(){$( "#cartones" ).dialog( "open" );}
	
	boton_comando = document.getElementById("comando");
	boton_comando.onclick = function(){send_command()};
	
	datoOrdenBola= document.getElementById("datoOrdenBola")
	boton_notify = document.getElementById("notify");
	boton_notify.onclick = function(){
		socket_send("secuenciaAcabada");
	}
	caja_spy=document.getElementById("text_comando");
	canvas=document.getElementById('canvas_bola');
	lienzo=canvas.getContext('2d');
	
	comboTexto = document.getElementById("comboTexto");
	
	cajaTamano = document.getElementById("maxTamano");
	
	boton_play_range= document.getElementById("c_Range");
	boton_play_range.onclick = function(){play_range2(document.getElementById("seek_ini").value,document.getElementById("seek_fin").value)};
	
	window.onresize = function(e){
		//e.preventDefault();
		resizeBolas();
		};
	show_InMessage("Ancho Canvas="+canvas.width+",alto="+canvas.height);
	creaSocket("paquito");	
	
	$( "#dialog" ).dialog({ autoOpen: false , modal: true });
	$( "#dialog" ).dialog({
		  buttons: [
		    {
		      text: "Continuar",
		      icons: {
		        primary: "ui-icon-heart"
		      },
		      click: function() {
		    	  socket_send("resume");
		    	  initInterface();
		    	  $( this ).dialog( "close" );
		      }
		    },
		      // Uncommenting the following line would hide the text,
		      // resulting in the label being used as a tooltip
		      //showText: false
		    
		    {
			      text: "Empezar",
			      icons: {
			        primary: "ui-icon-heart"
			      },
			      click: function() {
			    	  socket_send("newGame");
			    	  initInterface();
			    	  $( this ).dialog( "close" );
			      }
			 
			      // Uncommenting the following line would hide the text,
			      // resulting in the label being used as a tooltip
			      //showText: false
			 }
		  ]
		});
	//Plantilla JQuery para Opciones 
	selectCantaor= document.getElementsByName("cantaor");
	$( "#opciones" ).dialog({ autoOpen: false , modal: true });
	
	$( "input" ).on( "click", function() {
		  valCodecs = $( "input:checked" ).val();
	});
	
	$("select[name=cantaor]").change(function(){
		valor=$("select[name=cantaor]").val();
		
		elegirCantaor(valor);
	});
	$("input[name=videoON]").change(function(){
		if($("input[name=videoON]").is(':checked')){
			videoEnable="true";
			enciendeVideo();$( "input" ).on( "click", function() {
  $( "#log" ).html( $( "input:checked" ).val() + " is checked!" );
});
	}else{
			videoEnable="false";
			apagaVideo();
	}
	});
	
	$( "#slider" ).slider();
	$( "#slider" ).slider({
		  max: 75
		});
	$( "#slider" ).slider({
		  min: 5
		});
	$( "#slider" ).slider({
		range: true
	});
	
	$( "#slider" ).on( "slide", function( event, ui ) {
		cajaTamano.value=ui.value;
		nuevoTamano=ui.value;
		resizeBolas(nuevoTamano);
		
	} );
	$("#masTamano").change(function (){
		alert("entro");
		nuevoTamano=ui.value;
		resizeBolas(nuevoTamano);
	});		

	//Plantilla JQuery para Dialogo Cartones
	$( "#cartones" ).dialog({ autoOpen: false , modal: true });
	$( "#cartones" ).dialog({
		  create: function( event, ui ) {
			  mensaje="JSON#GET_DATOS_CARTONES#";
			  socket_send(mensaje);
		  }
		});
	$( "#cartones" ).dialog({
		  open: function( event, ui ) {
			  $("#PrecioCarton").val(precioCarton);
			  $("#NCartonesJuego").val(nCartones);
			  $("#porcientoLinea").val(porCientoLinea);
			  $("#porcientoBingo").val(porCientoBingo);
			  $("#porcientoCantaor").val(porCientoCantaor);
			  
		  }
		});

	$( "#cartones" ).dialog({
		  buttons: [
		    {
		      text: "UPDATE",
		      icons: {
		        primary: "ui-icon-heart"
		      },
		      click: function() {
		    	  precioCarton=parseInt($("#PrecioCarton").val());
		    	  nCartones=parseInt($("#NCartonesJuego").val());
		    	  porCientoLinea=parseInt($("#porcientoLinea").val());
		    	  porCientoBingo=parseInt($("#porcientoBingo").val());
				  porCientoCantaor=parseInt($("#porcientoCantaor").val());
		    	  mensaje="JSON#SET_DATOS_CARTONES#"+precioCarton+"#"+nCartones+"#"+porCientoLinea+"#"+porCientoBingo+"#"+porCientoCantaor;
		    	  visualizaDatosCartones();
		    	  socket_send(mensaje);
		    	  $( this ).dialog( "close" );
		      }
		    },
		      // Uncommenting the following line would hide the text,
		      // resulting in the label being used as a tooltip
		      //showText: false
		    
		    {
			      text: "CANCEL",
			      icons: {
			        primary: "ui-icon-heart"
			      },
			      click: function() {
			    	  $( this ).dialog( "close" );
			      }
			 
			      // Uncommenting the following line would hide the text,
			      // resulting in the label being used as a tooltip
			      //showText: false
			 }
		  ]
		});
		bucle3 = setInterval(function(){ mostrarFecha() }, 1000);
	//Podriamos hacer autenticaUsuario() aqui;
}
function enciendeVideo(){
	if(videoEnable=="true")video.style.visibility="visible";
}

function apagaVideo(){
	video.style.visibility="hidden";
}

function apagaLinea(){
	document.getElementById("labelLinea").style.visibility="hidden";
	triggerLinea="false";
	lineaCantada="true";
}
function mostrarFecha(){
	var f = new Date();
	HTML=f.getDate() + "/" + (f.getMonth() +1) + "/" + f.getFullYear();
	HTML+="<br>";
	if(f.getHours()<10){
		horas="0"+f.getHours();
	}else{
		horas=f.getHours();
	}
	if(f.getMinutes()<10){
		minutos="0"+f.getMinutes();
	}else{
		minutos=f.getMinutes();
	}
	if(f.getSeconds()<10){
		segundos="0"+f.getSeconds();
	}else{
		segundos=f.getSeconds();
	}
	HTML+= horas+":"+minutos+":"+ segundos;
	cajaFecha.innerHTML=HTML;
	
	
}
function apagaBingo(){
	document.getElementById("labelBingo").style.visibility="hidden";
	triggerBingo="false";
	bingoCantado="true";
}
function elegirCantaor(cantaor){
	
		    //var playMsg = video.canPlayType('video/mp4; codecs="avc1.42E01E"');
		    
		    if (valCodecs=="MP4") {
		    	//msg.innerHTML += "mp4/H.264 is " + playMsg + " supported <br/>";
		    	if(cantaor=="Lola"){
					nombreFileVideo="http://boga.esy.es/video/BingoLola.mov";
					nombreRangos="rangosLola";
					
		    	}
		    	if(cantaor=="Ines"){
					nombreFileVideo="http://boga.esy.es/video/BingoInes.mov";
					nombreRangos="rangosInes";
				}

		    }
		    
		    
		    if (valCodecs=="OGG") {
		      //msg.innerHTML += "ogg is " + playMsg + " supported<br/>";
				if(cantaor=="Lola"){
					nombreFileVideo="http://boga.esy.es/video/BingoLola.ogv";
					nombreRangos="rangosLola";
				}
		    	if(cantaor=="Ines"){
					nombreFileVideo="http://boga.esy.es/video/BingoInes.ogv";
							
					nombreRangos="rangosInes";
				}
		    	
		    	
		    }
		    if (valCodecs=="WEBM") {
		    	if(cantaor=="Lola"){
					nombreFileVideo="http://boga.esy.es/video/BingoLola.webm";
									
					nombreRangos="rangosLola";
				}
		    	if(cantaor=="Ines"){
					nombreFileVideo="http://boga.esy.es/video/BingoInes.webm";
										
					nombreRangos="rangosInes";
				}

		    }
		  
		video.pause();
		flagVideoReady="false";
		if(bucle!=null)clearInterval(bucle);
		if(bucle2!=null)clearInterval(bucle2);
		
		video.src=nombreFileVideo;
		
		rangos=eval(nombreRangos);
		if (arrayMessages==null)return;
		myRango=sacarRangos(arrayMessages[1]);
		if(arrayMessages[2]!=null)etiquetaOrden.textContent=(arrayMessages[2]);
		video.load();
		
		video.play();
		video.pause();
		bucle2 = setInterval(function(){ esperarReadyState() }, 1000);
}
function esperarReadyState(){
	if(flagVideoReady=="false")return;
	flagVideoReady=false;
	window.clearInterval(bucle2);
	
	play_range(myRango[0],myRango[1]);
	
}

function show_InMessage(contenido,activoMarquee){
	if(activoMarquee!=null){
		comboTexto.innerHTML="<marquee id='marquesina' behavior='scroll' direction='left' scrollamount='4'>"+contenido+"</marquee>";
	}else{
	comboTexto.innerHTML=contenido;
	}	
	
	
}
function send_command(){
		text_message = document.getElementById("text_comando").value;
		socket_send(text_message);
}


function play_range(ini,fin){
	//video.pause();
	if(ini==null)return;
	if(fin==null)return;
	video.currentTime=ini;
	document.getElementById("seek_ini").value=ini;
	document.getElementById("seek_fin").value=fin;
	fin_seek=fin;
	seeking="true";
	esPrimeraVez="true";
	//bucle=setInterval(procesarCuadros, 100);
	bucle = setInterval(function(){ procesarCuadros() }, 60);
	
	
}
function play_range2(ini,fin){
	//video.currentTime=ini;
	video.currentTime=ini;
	fin_seek=fin;
	seeking="true";
	esPrimeraVez="true";
	//video.play();
	//bucle = setInterval(function(){ procesarCuadros() }, 60);
	

	//bucle=setInterval(procesarCuadros, 100);
}
function initInterface(){
	triggerBingo="false";
	triggerLinea="false";
	lineaCantada="false";
	bingoCantado="false";
	palabraLinea.style.backgroundColor="#000000";
	palabraBingo.style.backgroundColor="#000000";
	palabraLinea.style.visibility="visible";
	palabraBingo.style.visibility="visible";
	for(i=1;i<91;i++){
		apagarNumero(""+i);
	}
	canvas=document.getElementById('canvas_bola');
	canvas.width=canvas.width;
	lienzo=canvas.getContext('2d');
	lienzo.clearRect(0,0,canvas.width,canvas.heigth);
	//refresca los valores de estos enteros desde el servidor y los visualiza 
	// en el manejador de vuelta, como si un callback se tratara.
	refreshDatosCartones();
	
}
function arrancar(){
	//El socket ya esta creado
	
	video.play();
	video.pause();
	//Se supone que aqui ya se conoce la sala y la partida sobre la que se juega
	//
	socket_send("startGame");
	
}
function creaSocket(usuario){
	var wsUri = getRootUri() + "actions";
	
	socket=new WebSocket(wsUri);
	
	//socket=new WebSocket("ws://localhost:8080/wildfly/actions");
	
	socket.addEventListener('open', abierto, false);
	socket.addEventListener('message', recibido, false);
	socket.addEventListener('close', cerrado, false);
	socket.addEventListener('error', errores, false);	
	
}
function getRootUri() {
	/*Web Sockets on OpenShift work over ports 8000 for ws and 8443 for wss,*/
	    
		if(document.location.hostname=="localhost"){
			nameEndPoint = "/wildfly/";
			port="8080";
		}
		else{
			nameEndPoint="/";
			port="8000";
		}
		
        return "ws://" + (document.location.hostname == "" ? "localhost" : document.location.hostname) + ":" +
                (document.location.port == "" ? "8000" : document.location.port) + nameEndPoint;
    
}

function abierto(){
	show_InMessage("socket abierto");
	anchoPantalla=window.innerWidth;
	resizeBolas();
	alto=window.innerHeight;
	
	caja_spy.value=""+anchoPantalla+"x"+alto;
	        /*
        imageObj.onload = function() {

					lienzo.scale(1,1);
					ancho=window.innerWidth;
					alto=windows.innerHeight;
					canvas.width=ancho;
					canvas.height=alto;
                    lienzo.drawImage(imageObj, 0, 0,ancho,alto);
        };
 		 // Calls the imageObj.onload function asynchronously
       imageObj.src="images/Bingo.png";
       */
	
	
	//alert("Ancho canvas=" + canvas.width + ", alto="+ canvas.height);
	//imagen.addEventListener("load", function(){lienzo.drawImage(imagen,0,0,canvas.width,canvas.height)}, false);
	
	
	//socket_send("startGame");
}
function refreshDatosCartones(){
	//Envia al servidor una peticion de refresco de datos
	// el servidor envia un mensaje de respuesta con los datos y en 
	// el manejador (Case DATOSCARTONES), ya con las variables actualizadas
	// se visualzia el dato en la tabla llamando a visualizaDatosCartones.
	socket_send("JSON#GET_DATOS_CARTONES");
	
}
function visualizaDatosCartones(){
	sumaCaja = precioCarton*nCartones;
	sumaTantos = porCientoLinea+porCientoBingo+porCientoCantaor;
	xLinea=Math.floor((sumaCaja*porCientoLinea)/sumaTantos);
	xBingo=Math.floor((sumaCaja*porCientoBingo)/sumaTantos);
	zCantaor=Math.floor((sumaCaja*porCientoCantaor)/sumaTantos);
	if(xLinea+xBingo+zCantaor<sumaCaja){
		dif=sumaCaja-(xLinea+xBingo+zCantaor);
		if(dif=2){
			xBingo++;
			zCantaor++;
		}else if(dif=1){
			xBingo++;
		}else{
			xBingo=xBingo +dif;
		}
	}
	datoCartones.textContent=""+nCartones;
	datoLinea.textContent=xLinea+" €";
	datoBingo.textContent=xBingo+" €";
}
function resizeBolas(tamanoMenu){
	//alert("olas");
	if(tamanoMenu==null){
	anchoPantalla=window.innerWidth;
	alto=window.innerHeight;
	nuevoTamano=Math.floor(anchoPantalla/25);
	if(nuevoTamano>82)nuevoTamano=82;
	}else{
		nuevoTamano=tamanoMenu;
	}
	if(nuevoTamano>82)nuevoTamano=82;
	caja_spy.value=""+anchoPantalla+"x"+alto +" -->"+nuevoTamano;
	for(i=1;i<91;i++){
		elemento=document.getElementById(""+i);
		elemento.style.fontSize=nuevoTamano+"px";
	}
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

	mensaje=""+e.data;
	
	arrayMessages=mensaje.split("_");
	
	if(arrayMessages.length > 0){
		 
	comando=arrayMessages[0];
		switch(comando) {
		    
		//Cantar numero y mostrar orden bola
		case "cantarNumero":
	    	    myRango=sacarRangos(arrayMessages[1]);
				if(arrayMessages[2]!=null)etiquetaOrden.textContent=(arrayMessages[2]);
	    	    if(arrayMessages[3]!=null){
	    	    	subDraw(arrayMessages[3],arrayMessages[1]);
	    	    }else{
	    	    	subDraw(0,arrayMessages[1]);
	    	    }
				play_range(myRango[0],myRango[1]);	
	    	    break;
		case "EnciendeVideo":
				enciendeVideo();
				break;
		case "ApagaVideo":
				apagaVideo();
				break;
		case "EndBalls":
				apagaVideo();
				show_InMessage("HAGAN SUS APUESTAS...FELIZ 2016",true)
				break;
		case "Linea":
				myRango=sacarRangos(arrayMessages[1]);
				play_range(myRango[0],myRango[1]);
				break;
		case "Bingo":
				myRango=sacarRangos(arrayMessages[1]);
				play_range(myRango[0],myRango[1]);
				break
		case "ComprobarLinea":
				apagaVideo();
				//Se deberia escribir en el canvas 	
				show_InMessage("COMPROBANDO LINEA ....",true);
				//etqLinea=document.getElementById("labelLinea");
				//etqLinea.style.
				break;
		case "ComprobarBingo":
				apagaVideo();
				//	Se deberia escribir en el canvas 	
				show_InMessage("COMPROBANDO BINGO ....",true);
				//	etqLinea=document.getElementById("labelLinea");
				//etqLinea.style.
				break;				
		case "EncenderNumero":
				if(arrayMessages[1] == null || (arrayMessages[1]+"").length >2)return;
				encenderNumero(arrayMessages[1]);
				break;
		case "ApagarNumero":
				apagarNumero(arrayMessages[1]);
				break;
		case "Info":
				if(arrayMessages[1]="PocketAbierto"){
					//result=confirm("Hay una partida empezada,desea continuar(Aceptar) o empezar(Cancelar)")
					
					$( "#dialog" ).dialog( "open" );
				}
				break;
		case "ApagaLinea":
				apagaLinea();
				break;
		case "DATOSCARTONES":
				precioCarton=parseInt(arrayMessages[1]);
				nCartones=parseInt(arrayMessages[2]);
				porCientoLinea=parseInt(arrayMessages[3]);
				porCientoBingo=parseInt(arrayMessages[4]);
				porCientoCantaor=parseInt(arrayMessages[5]);
				//callback
				visualizaDatosCartones();
		default:
        		
		} 
	}
}
function apagarNumero(n){
	document.getElementById(n).style.color="#280000";
}
function encenderNumero(numero){
	
	document.getElementById(numero).style.color="#FF5B5B";
}

function sacarRangos(numerobase){
	var rangoCanto= [];
	indexSearch= rangos.indexOf(numerobase);
	rangoCanto.push(rangos[indexSearch + 1]);
	rangoCanto.push(rangos[indexSearch + 2]);
	return rangoCanto;
	//windexNumber= rangos.indexOf(numerobase):
		
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
function procesarCuadros(){
	contador.value=video.currentTime;
	//lienzo.drawImage(video,(canvas.width/2)-22,(canvas.height/2)-26,58,51);
	if(triggerLinea=="true"){
		if(colorTriggerLinea >999)colorTriggerLinea=900;
		color="#"+colorTriggerLinea;
		palabraLinea.style.backgroundColor=color;
		colorTriggerLinea++;
	}
	if(triggerBingo=="true"){
		if(colorTriggerLinea >999)colorTriggerLinea=900;
		color="#"+colorTriggerLinea;
		palabraBingo.style.backgroundColor=color;
		colorTriggerLinea++;
	}
	if(seeking=="true"){
		if(esPrimeraVez=="true"){
			
			if(!video.seeking){
				video.play();
				esPrimeraVez = "false";
			}
			/*
			video.oncanplay = function() {
			    video.play();
			    esPrimeraVez = "false";
			};*/
			

		}
		if(video.currentTime >= fin_seek){
			window.clearInterval(bucle);
			seeking="false";
			video.pause();
			socket_send("refresh_refresh_refresh1");		
			socket_send("seekingFinished");
			socket_send("refresh_refresh_refresh2");
		}
	}
	
}
//numero,ejeX,ejeY,radio,gradiente(Si-no),modoPatttern,colorPattern,selectorPattern
function draw(numero,varX,varY,miRadio,booleanGrad,v,color,thisnpat) {
  if((numero+"").length > 2)return;  
  //ctxCanvas.clearRect(0,0,elementCanvas.Width,elementCanvas.Heigth); 
  //ctxCanvas.width=ctxCanvas.width;
  // create new image object to use as pattern
  var img = new Image();
  //varX=Math.floor((elementCanvas.width)/2);
  //varY=Math.floor((elementCanvas.height)/2);
  //miRadio= Math.floor(varX/3);
  
  //copyVary=varY;
  //copyRadio=miRadio;
  mySrc="images/pattern"+thisnpat+".jpg";
  //npat++;
  //if(npat>4) npat=1;
  img.src = mySrc;
  img.onload = function(){
	//borrar previo
	
    // create pattern
	//v= Math.floor((Math.random() * 3) + 1);
	
	var ptrn = ctxCanvas.createPattern(img,"repeat");
    ctxCanvas.beginPath();
    ctxCanvas.fillStyle = ptrn;
    ctxCanvas.arc(varX, varY,miRadio, 0,Math.PI * 2,false);
    ctxCanvas.fill();
    
    if(v==1 || v==3){
    	grd = ctxCanvas.createRadialGradient(varX, varY - miRadio, Math.floor(miRadio/2)+Math.floor(miRadio/4), varX + miRadio,varY, miRadio*3);
    	
    	grd.addColorStop(0, color);
    	grd.addColorStop(1, "white");
    	ctxCanvas.fillStyle = grd;
    	ctxCanvas.fill();
    }
    
    var sumador=1;
    factorX=(Math.random()*(Math.floor(miRadio/3)));
	factorSigno = (Math.random()*1)+1;
	if (factorSigno==1){
		sumador=+1;
	}else{
		sumador=-1;		
	}
	varXTexto = (varX + (factorX*sumador))+1;
    ctxCanvas.beginPath();
    ctxCanvas.fillStyle ="white";
    ctxCanvas.arc(varXTexto, varY,Math.floor(miRadio/2)+2 , 0,Math.PI * 2,false);
    ctxCanvas.fill();

    ctxCanvas.beginPath();
	//tamanoLetras = Math.flor((nuevoTamano/1.3));
    Fuente = Math.floor(miRadio/1.3)+"px bold Console";
    ctxCanvas.font = Fuente;
	var textMeter = ctxCanvas.measureText(""+numero);
    anchoTexto= textMeter.width;
    ctxCanvas.fillStyle="black";
    if(numero<10){
    	yText=varY + (Math.floor(anchoTexto/3))+ Math.floor(anchoTexto/3);
    }else{
    	yText=varY + (Math.floor(anchoTexto/3));
    }
    ctxCanvas.fillText(""+numero,varXTexto-(Math.floor(anchoTexto/2)),yText );
    ctxCanvas.fill();
    
    //ctxCanvas.beginPath();
    if(booleanGrad=="true"){
    	ctxCanvas.arc(varX, varY,miRadio+1, 0,Math.PI * 2,false);
    	//miRadio= Math.floor(varX/3);
    	color = "rgba("+c1+","+c2+","+c3+",0)";
    	grd = ctxCanvas.createRadialGradient(varX,varY ,0,varX,varY,miRadio+1);
    	//grd.addColorStop(0,"rgba(255,255,255,0)");
    	grd.addColorStop(0,color);
    	grd.addColorStop(1, "black");
    	ctxCanvas.fillStyle = grd;
    	ctxCanvas.fill();
  	}
  }
}
function subDraw(subNumero,Numero) {
		//varX,varY,miRadio
	
	  myVarX=Math.floor((elementCanvas.width)/2);
	  myVarY=Math.floor((elementCanvas.height)/2);
	  mi_Radio= Math.floor(myVarX/3);
	  ctxCanvas.clearRect(0,0,elementCanvas.Width,elementCanvas.Heigth); 
	  ctxCanvas.width=ctxCanvas.width;
	  //ctxCanvas.save();
	  
	  //varX=Math.floor((elementCanvas.width)/4);
	  //varY=Math.floor((elementCanvas.height)/2.5);
	  //ctxCanvas.translate(varX-miRadio, varY-(Math.floor(miRadio/2)));
	  
	  	//ctxCanvas.restore();
	  	if(subNumero!=0){
		  draw(subNumero,myVarX-Math.floor((mi_Radio*2)),myVarY-(Math.floor(mi_Radio/2)),mi_Radio/2,"true",copymyV,copYColor,copynpat);
	  	}
	  	//calculo color	
	  	c1=Math.floor((Math.random() * 255) + 1);
		c2=Math.floor((Math.random() * 255) + 1);
		c3=Math.floor((Math.random() * 255) + 1);
		c4 =Math.floor((Math.random() * 10) + 1);
		myV= Math.floor((Math.random() * 3) + 1);
		var alfa = "0."+c4;
    	if(c4==10)alfa=1;
    	if(myV==1){
    		my_color = 'rgba(' + c1+ ',' +c2 + ','+c3+","+alfa+')';
    	}else if(myV==3 || myV==2){
    		my_color = 'rgba(' + c1+ ',' +c2 + ','+c3+',1)';
    	}
		
		npat++;
		if(npat>4) npat=1;
		copYColor=my_color;
		copymyV=myV;
		copynpat=npat;
		
		
		draw(Numero,myVarX ,myVarY,mi_Radio,"true",myV,my_color,npat);
}
window.onload=iniciar;

