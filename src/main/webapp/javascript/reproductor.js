/**
 * Codigo manejador player 
 */
var numeroCantaores = 1; // De momento solo hay un cantaor
var rangosLola = ["BuenasNoches",0.8930,5.5210,"linea",6,8.6588,"bingo",8.6588,11.746,"lineaOk",11.746,14.798,"bingoOk",14.798,18.396,"1",18.407,22.029,"2",22.029,23.363,"3",23.363,25.917,"4",25.917,27.960,"5",27.960,30.770,"6",30.770,33.543,"7",33.543,35.945,"8",35.945,38.545,"9",38.545,41.202,"10",41.202,43.446,"11",43.446,47.028,"12",47.028,49.772,"13",49.772,52.54,"14",52.54,54.506,"15",54.506,57.594,"16",57.594,59.620,"17",59.620,62.022,"18",62.022,65.492,"19",65.492,67.976,"20",67.976,69.774,"21",69.774,72.548,"22",72.548,75.948,"23",75.948,78.187,"24",78.187,80.811,"25",80.811,83.140,"26",83.140,85.816,"27",85.816,87.823,"28",87.823,90.520,"29",90.520,93.000,"30",93.000,95.035,"31",95.035,98.296,"32",98.296,100.40,"33",100.40,102.89,"34",102.89,105.84,"35",105.84,108.69,"36",108.69,110.67,"37",110.67,113.97,"38",113.97,116.60,"39",116.60,118.48,"40",118.48,121.16,"41",121.16,123.77,"42",123.77,126.35,"43",126.35,128.65,"44",128.65,131.00,"45",131.00,134.46,"46",134.46,137.27,"47",137.27,139.83,"48",139.83,142.76,"49",142.76,145,"50",145,148,"51",148,150.93,"52",150.93,154,"53",154,155.68,"54",155.68,158.82,"55",158.82,161.43,"56",161.43,163.80,"57",163.80,166.68,"58",166.68,169.80,"59",169.80,172.10,"60",172.10,175.42,"61",175.42,178.65,"62",178.65,181.53,"63",181.53,184.79,"64",184.79,187.42,"65",187.42,190.26,"66",190.26,192.68,"67",192.68,195.30,"68",195.30,197.81,"69",197.81,200.42,"70",200.42,202.12,"71",202.12,204.87,"72",204.87,207.87,"73",207.87,209.73,"74",209.73,212.55,"75",212.55,216.15,"76",216.15,218.11,"77",218.11,221.44,"78",221.44,224.83,"79",224.83,227.62,"80",227.62,229.85,"81",229.85,232.39,"82",232.39,235.53,"83",235.53,237.42,"84",237.42,240.51,"85",240.51,243.21,"86",243.21,246.30,"87",246.30,249.58,"88",249.58,252.26,"89",252.26,254.05,"90",254.05,256.75];
var rangosInes = ["1",39.42,41.70,"2",41.70,44.11,"3",44.11,47.11,"4",47.11,49.70,"5",49.70,52,"6",53.20,55.20,"7",55.20,56.88,"8",56.88,59.74,"9",59.74,61.47,"10",61.47,63.43,"11",64.40,67.00,"12",67.00,69.68,"13",69.68,71.42,"14",71.42,74.41,"15",74.41,76.78,"16",76.78,78.36,"17",78.36,80.82,"18",80.82,83.19,"19",83.19,86.15,"20",86.15,88.45,"21",88.45,91.18,"22",91.18,93.98,"23",93.98,95.66,"24",95.66,98.76,"25",98.76,100.79,"26",101,103.5,"27",103.5,107.10,"28",107.10,109.04,"29",109.04,111.33,"30",111.33,112.65,"31",112.65,114.75,"32",114.75,116.61,"33",117.8,118.99,"34",120,122.39,"35",123,124.53,"36",125.00,127.00,"37",127.00,129.4,"38",129.4,131.6,"39",131.6,134.00,"40",134,136.60,"41",136.60,139.01,"42",139.01,141.30,"43",141.30,144.36,"44",144.36,145.97,"45",145.97,148.60,"46",148.60,151.11,"47",151.11,153.80,"48",153.80,155.58,"49",155.58,158.03,"50",158.60,160.66,"51",160.66,163.24,"52",163.24,165.60,"53",165.60,167.80,"54",167.80,170.21,"55",170.21,173,"56",173.00,174.48,"57",175,177.06,"58",177.06,179.16,"59",179.16,182.2,"60",182.2,184,"61",184,186.7,"62",186.7,189.03,"63",189.03,190.88,"64",190.88,193.66,"65",193.66,196.157,"66",196.157,198.57,"67",198.57,200.4,"68",200.6,203.85,"69",203.85,206.25,"70",206.25,208.70,"71",208.70,211.57,"72",211.57,213.70,"73",213.70,215.45,"74",215.45,218.004,"75",218.004,220.75,"76",220.75,223.3,"77",223.3,224.89,"78",224.89,226.69,"79",226.69,229.02,"80",229.02,230.69,"81",230.69,233.195,"82",233.195,235.425,"83",235.425,237.565,"84",237.565,239.1,"85",239.1,241.29,"86",241.29,243.6,"87",243.8,245.55,"88",245.55,247.64,"89",247.64,249.755,"90",250,252.23,"linea",253,255.6,"bingo",265,271.95,"lineaOk",260,263.85,"bingoOk",271.95,292,"BuenasNoches",18.00,37.3]

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
var valCodecs="WEBM";
var resultDialogo="Empezar";
var videoEnable="true";
var starsEnable="true";
var lineaCantada="false";
var bingoCantado="false";
var precioCarton=0;
var nCartonesManuales=0;
var nCartonesAutomaticos;
var porCientoLinea=0;
var porCientoBingo=0;
var porCientoCantaor=0;
var sumaTantos=0;
var sumaCaja=0;
var flagVideoReady="false";
var myV=1;
var bucle4;
var bucle7;
var posterImage;
var flagBucle4=0;
var contBackground=255;
var contBackground2=91;
//var nombreRangos="rangosLola";
var nombreRangos="rangosInes";
//var nombreFileVideo="http://boga.esy.es/video/BingoLola.mov";
var nombreFileVideo="http://boga.esy.es/video/BingoInes.mov";
var cajaFecha;
var arrayMessages=null;
var copYColor,my_color;
var copymyV;
var copynpat;
var MAX_DEPTH = 32;
var stars = new Array(512);
var puntoEstrella=0;
var ctx;
var numero,subNumero;
var ptrn;
var myVarX;
var myVarY;
var mi_Radio;
var c1,c2,c3,c4;
var varXTexto;
var MAX_DEPTH = 32;
var copy_my_pattern,my_pattern;
var factorX;
var sumador=1;
var bolaPreparada="false";
var salaInUse;
var largoCeldaMensajes;
var comboTexto;
var labelTexto;

function iniciar() {
	salaInUse = document.getElementById("sala");
	rangos=eval(nombreRangos);
	video = document.getElementById("medio");
	//video.src=nombreFileVideo;
	video.type="video/webm";
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
		//if(lineaCantada=="true" || lineaCantada=="comprobando")return;
		
		//lineaCantada="comprobando";
		socket_send("Linea");
		//triggerLinea="true";
	};
	
	boton_Bingo= document.getElementById("boton_Bingo");
	boton_Bingo.onclick = function(){
		//if(lineaCantada=="false" || lineaCantada=="comprobando" || bingoCantado=="comprobando")return;
		//bingoCantado="comprobando";
		socket_send("Bingo");
		//triggerBingo="true";
	};

	boton_LineaOk= document.getElementById("boton_LineaOk");
	boton_LineaOk.onclick = function(){
		//if(lineaCantada!="comprobando")return;

		socket_send("Linea_OK");
	};
	
	boton_BingoOk= document.getElementById("boton_BingoOk");
	boton_BingoOk.onclick = function(){
		//if(bingoCantado!="comprobando")return;
		//palabraBingo.style.visibility="hidden";

		socket_send("Bingo_OK");
	};
	
	boton_Continuar = document.getElementById("boton_Continuar");
	boton_Continuar.onclick = function(){
		socket_send("Continue");
	}
	
	boton_iniciar = document.getElementById("iniciar");
	boton_iniciar.onclick = function(){ arrancar()};
	
	boton_resume = document.getElementById("resume");
	boton_resume.onclick = function(){ resumir()};
	
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
	
	boton_comando = document.getElementById("finalizar");
	boton_comando.onclick = function(){finalizarPartida()};
	
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
	labelTexto= document.getElementById("labelTexto");
	
	cajaTamano = document.getElementById("maxTamano");
	
	boton_play_range= document.getElementById("c_Range");
	boton_play_range.onclick = function(){play_range2(document.getElementById("seek_ini").value,document.getElementById("seek_fin").value)};
	posterImage=document.getElementById("my_poster");
	window.onresize = function(e){
		//e.preventDefault();
		resizeBolas();
	}
	show_InMessage("Ancho Canvas="+canvas.width+",alto="+canvas.height);
	creaSocket(salaInUse.textContent);


	
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
			enciendeVideo();
			$( "input" ).on( "click", function() {
		$( "#log" ).html( $( "input:checked" ).val() + " is checked!" );
		});
	}else{
			videoEnable="false";
			apagaVideo();
	}
	});
	
	$("input[name=starsON]").change(function(){
		if($("input[name=starsON]").is(':checked')){
			starsEnable="true";
			iniciarFondoEstrellas();
		}else{
			starsEnable="false";
			//detenerFondoEstrellas();
		}
	});
	
	$( "#slider" ).slider();
	$( "#slider" ).slider({
		  max: maxTamano
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
		nuevoTamano=ui.value;
		resizeBolas(nuevoTamano);
	});		

	
	$( "#aplicarDelay" ).click(function() {
		nuevoDelay=$("#delay").val();
		socket_send("JSON#SET_DATOS_DELAY#"+nuevoDelay);
		$( "#opciones" ).dialog("close");
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
			  var array = obtenerDatosCartones();//Funcion Ajax de obtencion datos de contexto
			  nCartonesAutomaticos = array[3];
			  nCartonesManuales = array[4];
			  nCartonesJuego = nCartonesManuales + nCartonesAutomaticos;
			  $("#NCartonesJuego").val(nCartonesJuego);
			  $("#NCartonesManuales").val(nCartonesManuales);
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
		    	  precioCarton=parseFloat($("#PrecioCarton").val()).toFixed(2);
		    	  nCartonesManuales=parseInt($("#NCartonesManuales").val());
		    	  porCientoLinea=parseInt($("#porcientoLinea").val());
		    	  porCientoBingo=parseInt($("#porcientoBingo").val());
				  porCientoCantaor=parseInt($("#porcientoCantaor").val());
		    	  mensaje="JSON#SET_DATOS_CARTONES#"+precioCarton+"#"+nCartonesManuales+"#"+porCientoLinea+"#"+porCientoBingo+"#"+porCientoCantaor;
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
			
}
function fullscreen(e){
    if (e.webkitRequestFullScreen) {
      e.webkitRequestFullScreen();
    } else if(e.mozRequestFullScreen) {
      e.mozRequestFullScreen();
    }
}
function enciendeVideo(){
	if(videoEnable=="true")video.style.visibility="visible";
	posterImage.style.visibility="hidden";
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
		
		//video.play();
		//video.pause();
		bucle2 = setInterval(function(){ esperarReadyState() }, 1000);
}
function esperarReadyState(){
	if(flagVideoReady=="false")return;
	flagVideoReady=false;
	window.clearInterval(bucle2);
	
	play_range(myRango[0],myRango[1]);
	
}

function show_InMessage(contenido,activoMarquee){
	largoCeldaMensajes = comboTexto.clientWidth;
	textoLargo = largoCeldaMensajes+"px";
	if(activoMarquee!=null){
		longitudActualComboTexto = 400;
		comboTexto.innerHTML="<marquee id='marquesina' behavior='scroll' direction='left' scrollamount='4' width='"+textoLargo+"'>"+contenido+"</marquee>";
	}else{
		
		comboTexto.innerHTML= "<label width='"+textoLargo+"'  id='labelTexto' class='classMessage' >"+contenido+"</label>";
	
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
	

	bucle=setInterval(procesarCuadros, 100);
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
	iniciarFondoEstrellas();
	numin=Math.floor((Math.random()*numeroCantaores))+1;
	if(numin==1)cantaor="Ines";
	if(numin==2)cantaor="Lola";
	elegirCantaor(cantaor);
	//show_InMessage(" ");
	
}
function arrancar(){
	//El socket ya esta creado
	
	fullscreen(document.getElementById("content"));

	//video.play();
	//video.pause();
	//Se supone que aqui ya se conoce la sala y la partida sobre la que se juega
	//
	socket_send("newGame");
	initInterface();
}
function resumir(){
	//El socket ya esta creado
	fullscreen(document.getElementById("content"));
	//video.play();
	//video.pause();
	//Se supone que aqui ya se conoce la sala y la partida sobre la que se juega
	//
	socket_send("resume");
	initInterface();
	
}

function creaSocket(sala){
	var wsUri = getRootUri() + sala;
	
	socket=new WebSocket(wsUri);
	
	//socket=new WebSocket("ws://localhost:8080/wildfly/actions");
	
	socket.addEventListener('open', abierto, false);
	socket.addEventListener('message', recibido, false);
	socket.addEventListener('close', cerrado, false);
	socket.addEventListener('error', errores, false);	
	
}
function finalizarPartida(){
	socket_send("Finalize");
}

function getRootUri() {
	/*Web Sockets on OpenShift work over ports 8000 for ws and 8443 for wss,*/
	    
		if(document.location.hostname=="localhost"){
			nameEndPoint = "/wildfly-1.0/";//haber
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
	refreshDatosCartones();
	
	anchoPantalla=window.innerWidth;
	resizeBolas();
	alto=window.innerHeight;
	
	caja_spy.value=""+anchoPantalla+"x"+alto;
	show_InMessage("socket abierto");
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
	/*
	xLinea=Math.floor((sumaCaja*porCientoLinea)/sumaTantos);
	xBingo=Math.floor((sumaCaja*porCientoBingo)/sumaTantos);
	zCantaor=Math.floor((sumaCaja*porCientoCantaor)/sumaTantos);
	*/
	xLinea=parseFloat((sumaCaja*porCientoLinea)/sumaTantos).toFixed(2);
	xBingo=parseFloat((sumaCaja*porCientoBingo)/sumaTantos).toFixed(2);
	zCantaor=parseFloat((sumaCaja*porCientoCantaor)/sumaTantos).toFixed(2);
	//xLinea=(sumaCaja*porCientoLinea)/sumaTantos).toFixed(2);
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
	nuevoTamano=Math.floor(anchoPantalla/27);
	if(nuevoTamano>82)nuevoTamano=maxTamano;
	}else{
		nuevoTamano=tamanoMenu;
	}
	if(nuevoTamano>82)nuevoTamano=maxTamano;
	caja_spy.value=""+anchoPantalla+"x"+alto +" -->"+nuevoTamano;
	for(i=1;i<91;i++){
		elemento=document.getElementById(""+i);
		elemento.style.fontSize=nuevoTamano+"px";
	}
	largoCeldaMensajes = comboTexto.clientWidth;
	
	
	/*Haber
	anchoposterImage=posterImage.offsetHeight;
	altoposterImage=posterImage.offsetWidth;
	posterImage.style.height=altoposterImage;
	posterImage.style.width=anchoposterImage;
	*/
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
					subNumero=arrayMessages[3];
					numero=arrayMessages[1];
	    	    	//subDraw(arrayMessages[3],arrayMessages[1]);
	    	    }else{
	    	    	subNumero=0;
	    	    	numero=arrayMessages[1];
	    	    	//subDraw(0,arrayMessages[1]);
	    	    }
				subDraw(subNumero,numero);
				play_range(myRango[0],myRango[1]);	
	    	    
				break;
		case "EnciendeVideo":
				enciendeVideo();
				break;
		case "ApagaVideo":
				apagaVideo();
				break;
		case "EndBalls":
				
				numerin = Math.floor((Math.random()*10))+1;	
					poster="url('images/EndBalls"+numerin+".gif')";

					//posterImage.style.height=altoposterImage;
					//posterImage.style.width=anchoposterImage;
					
					apagaVideo();
					posterImage.style.visibility="visible";
					posterImage.style.backgroundImage=poster;
					show_InMessage("HAGAN SUS APUESTAS...HABRA UNA PARTIDA ESPECIAL CADA HORA Y MEDIA",true)
					
					detenerFondoEstrellas();
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
				numerin = Math.floor((Math.random()*10))+1;
				poster="url('images/gifLinea"+numerin+".gif')";
				posterImage.style.backgroundImage=poster;
				apagaVideo();
				show_InMessage("COMPROBANDO LINEA ....",true);
				posterImage.style.visibility="visible";
				anchoposterImage=posterImage.offsetHeight;
				altoposterImage=posterImage.offsetWidth;
				break;
		case "ComprobarBingo":
				numerin = Math.floor((Math.random()*10))+1;
				poster="url('images/gifBingo"+numerin+".gif')";
				posterImage.style.backgroundImage=poster;
				apagaVideo();
				show_InMessage("COMPROBANDO BINGO ....",true);
				posterImage.style.visibility="visible";
				//	Se deberia escribir en el canvas 	
				
				//	etqLinea=document.getElementById("labelLinea");
				//etqLinea.style.
				break;				
		case "EncenderNumero":
				if(arrayMessages[1] == null || (arrayMessages[1]+"").length >2)return;
				if(arrayMessages[2] == "simple"){
					encenderNumero(arrayMessages[1],"simple");
				}else{
					encenderNumero(arrayMessages[1]);
				}
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
		case "ApagaBingo":
			apagaBingo();
			break;
		case "WarningFinalizando":
			    show_InMessage("Atencion, finalizando partida; ¿Hay mas Bingos?","blink");
			    break;
		case "DATOSCARTONES":
				precioCarton=parseFloat(arrayMessages[1]);
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
function detectarFinVideo(){
	if(video.ended){
		apagarVideo();
		window.clearInterval(bucle8);
		bucle8=null;
		
	}
}
function apagarNumero(n){
	document.getElementById(n).style.color="#280000";
	document.getElementById(n).style.backgroundColor="#000000";
}
function encenderNumero(numero,modo){
	if(numero=="0")return;
	document.getElementById(numero).style.color="#FF3333";//255,91,91
	flagBucle4=0;
	contBackground=200;
	contBackground2=91;
	if(modo=="simple"){
	}else{
		bucle4 = setInterval(function(){ recrearFondo(numero) }, 50);
	}
	
}
function recrearFondo(num){
	if(contBackground==0 && contBackground2==0){
		window.clearInterval(bucle4);
		bucle4=null;
	}
	contBackground=contBackground-6;
	contBackground2=contBackground2-6;
	if(contBackground<0)contBackground=0;
	if(contBackground2<0)contBackground2=0;
	_color = 'rgba(' + contBackground+ ',' +contBackground2 + ','+contBackground2+',1)';
	document.getElementById(num).style.backgroundColor=_color;
	
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
function obtenerDatosCartones(){
	//servlet de servicio --->SourcerArraysCarton
	var params = new Object();
	var arrayDatosCartones ;
	params.perfil="supervisor";
	params.usuario="super";
	params.comando="DatosCartones";

	$.ajax({
		  type: 'POST',
		  url: "SourcerArraysCarton",
		  data: params,
		  dataType:"json",
		  async:false
		}).done(function( data ) {
			
			myArrayDatosCartones=data;
			precioCarton= myArrayDatosCartones[0];
			saldo=myArrayDatosCartones[1];
			nCartones=myArrayDatosCartones[2];
	});
	return myArrayDatosCartones;
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
			//socket_send("refresh_refresh_refresh2");
		}
	}
	
}
//numero,ejeX,ejeY,radio,gradiente(Si-no),modoPatttern,colorPattern,selectorPattern
function draw(numero,varX,varY,miRadio,booleanGrad,v,color,thisPattern) {
	if(bolaPreparada!="true")return;
	if(numero==0 || (numero+"".lenght >2))return;
	ctxCanvas.beginPath();
    ctxCanvas.fillStyle = thisPattern;
    ctxCanvas.arc(varX, varY,miRadio, 0,Math.PI * 2,false);
    ctxCanvas.fill();
    
    if(v==1 || v==3){
    	grd = ctxCanvas.createRadialGradient(varX, varY - miRadio, Math.floor(miRadio/2)+Math.floor(miRadio/4), varX + miRadio,varY, miRadio*3);
    	
    	grd.addColorStop(0, color);
    	grd.addColorStop(1, "white");
    	ctxCanvas.fillStyle = grd;
    	ctxCanvas.fill();
    }
    
	varXTexto = (varX + (factorX*sumador)-1);
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
    	grdColor = "rgba("+c1+","+c2+","+c3+",0)";
    	grd = ctxCanvas.createRadialGradient(varX,varY ,0,varX,varY,Math.floor(miRadio));
    	//grd.addColorStop(0,"rgba(255,255,255,0)");
    	grd.addColorStop(0,grdColor);
    	grd.addColorStop(1, "black");
    	ctxCanvas.fillStyle = grd;
    	ctxCanvas.fill();
  	}
}
function refreshBolas(){
	if((numero+"").length < 3){
		draw(numero,myVarX,myVarY,mi_Radio,"true",myV,my_color,my_pattern);
	}
	
	
	if(subNumero!=0){
		  draw(subNumero,myVarX-Math.floor((mi_Radio*2)),mi_Radio+1,mi_Radio/2,"true",copymyV,copYColor,copy_my_pattern);
	  	}
}	
function subDraw(subNumero,Numero) {
		//varX,varY,miRadio
	  //Preparar ejes bolas
	  
	  myVarX=Math.floor((elementCanvas.width)/2);
	  myVarY=Math.floor((elementCanvas.height)/2);
	  mi_Radio= Math.floor(myVarX/3);
	  	copYColor=my_color;
		copymyV=myV;
		
		copy_my_pattern=my_pattern;
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
		
		factorX=((Math.random()*2)+1);
		factorSigno = (Math.random()*10)+1;
		if (factorSigno>5){
				sumador=+1;
			}else{
				sumador=-1;		
		}
		npat++;
		if(npat>4) npat=1;
		if((numero+"").length > 2)return;  
		  var img = new Image();
		  mySrc="images/pattern"+npat+".jpg";
		  img.src = mySrc;
		  img.onload = function(){
			  my_pattern = ctxCanvas.createPattern(img,"repeat");
		  }
		 bolaPreparada="true";	

}
function iniciarFondoEstrellas(){
	
	 initStars();
	 ctx = canvas.getContext("2d");
     bucle6=setInterval(loop,66);
     
}
function detenerFondoEstrellas(){
	window.clearInterval(bucle6);	
}
 
     /* Returns a random number in the range [minVal,maxVal] */
    function randomRange(minVal,maxVal) {
      return Math.floor(Math.random() * (maxVal - minVal - 1)) + minVal;
    }
 
    function initStars() {
      for( var i = 0; i < stars.length; i++ ) {
        stars[i] = {
          x: randomRange(-25,25),
          y: randomRange(-25,25),
          z: randomRange(1,MAX_DEPTH)
         }
      }
    }
 
    function loop() {
      if(starsEnable=="true"){
    	  ctx.save();
    	  var halfWidth  = canvas.width / 2;
    	  var halfHeight = canvas.height / 2;
    	  
    	  ctx.fillStyle = "rgb(0,0,0)";
    	  ctx.fillRect(0,0,canvas.width,canvas.height);
 
    	  for( var i = 0; i < stars.length; i++ ) {
    		  stars[i].z -= 0.2;
 
    		  if( stars[i].z <= 0 ) {
    			  stars[i].x = randomRange(-25,25);
    			  stars[i].y = randomRange(-25,25);
    			  stars[i].z = MAX_DEPTH;
    		  }
 
    		  var k  = 128.0 / stars[i].z;
    		  var px = stars[i].x * k + halfWidth;
    		  var py = stars[i].y * k + halfHeight;
 
    		  if( px >= 0 && px <= 500 && py >= 0 && py <= 400 ) {
    			  var size = (1 - stars[i].z / 32.0) * 5;
    			  var shade = parseInt((1 - stars[i].z / 32.0) * 255);
    			  ctx.fillStyle = "rgb(" + shade + "," + shade + "," + shade + ")";
    			  ctx.fillRect(px,py,size,size);
    		  }
    	  }
       }
      ctx.restore();
      if(!isNaN(myVarX)){
    	  refreshBolas();
      }
    }
window.onload=iniciar;

