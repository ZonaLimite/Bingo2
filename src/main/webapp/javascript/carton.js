/**
 * Codigo manejador player 
 */

var video; /** el elemento video */

var boton_Linea;
var boton_Bingo
var boton_Continuar;
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
var nCartones=0;
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

var numeroCartonesComprados;
var myArrayCartonesJuego= new Array();

function iniciar() {
	salaInUse = document.getElementById("sala");
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
    numeroCartonesJugador= document.getElementById("numeroCartonesComprados");
	
	boton_Linea= document.getElementById("boton_Linea");
	boton_Linea.onclick = function(){ 
		//if(lineaCantada=="true" || lineaCantada=="comprobando")return;
		//lineaCantada="comprobando";
		socket_send("Linea");
		triggerLinea="true";
	};
	
	boton_Bingo= document.getElementById("boton_Bingo");
	boton_Bingo.onclick = function(){
		//if(lineaCantada=="false" || lineaCantada=="comprobando" || bingoCantado=="comprobando")return;
		//bingoCantado="comprobando";
		socket_send("Bingo");
		triggerBingo="true";
	};

	
	
	boton_iniciar = document.getElementById("boton_Jugar");
	boton_iniciar.onclick = function(){ arrancar()};
	
	boton_Carton = document.getElementById("boton_Carton");
	boton_Carton.onclick = function(){ 
		$( "#cartones" ).dialog( "open" );
	};
	
	datoOrdenBola= document.getElementById("datoOrdenBola")
	canvas=document.getElementById('canvas_bola');
	lienzo=canvas.getContext('2d');
	
	comboTexto = document.getElementById("comboTexto");
	
	/*
	window.onresize = function(e){
		//e.preventDefault();
		resizeBolas();
	}*/
	activarCartones();
	creaSocket(salaInUse.textContent);
	
	
	//Manejo JQuery
	
	//Plantilla JQuery para Dialogo Cartones//
	
	$( "#spinner" ).spinner();
	$("#spinner").attr("readonly","true");
	$( "#cartones" ).dialog({ autoOpen: false ,
								 modal: true,
								 height: 'auto',
								 width: 360});
	$( "#cartones" ).dialog({
		  create: function( event, ui ) {
			  event.preventDefault();
			 // mensaje="JSON#GET_DATOS_CARTONES#";
			  //socket_send(mensaje);
		  }
		});
	$( "#cartones" ).dialog({
		open: function( event, ui ) {
			  event.preventDefault();
			  $("#feedback").text("Elija nº cartones y Pulse 'COMPRA'");
		  }
		});

	$( "#cartones" ).dialog({
		  buttons: [
		    {
		      text: "COMPRA",
		      icons: {
		        primary: "ui-icon-heart"
		      },
		      click: function() {
		    	  $.post("gestorComprasCartones",$( "#requestForm" ).serialize(), function(responseText){
		    		  //responsetext devuelve text/plain
		    		  $("#feedback").text( responseText);
		    		  
		    	  });
		    	  //visualizaDatosCartones();
		    	  //socket_send(mensaje);
		    	  //$( this ).dialog( "close" );
		      }
		    },
		      // Uncommenting the following line would hide the text,
		      // resulting in the label being used as a tooltip
		      //showText: false
		    
		    {
			      text: "CERRAR",
			      icons: {
			        primary: "ui-icon-heart"
			      },
			      click: function() {
			    	  $( this ).dialog( "close" );
			    	  window.location.reload(true);
			      }
			 
			      // Uncommenting the following line would hide the text,
			      // resulting in the label being used as a tooltip
			      //showText: false
			 }
		  ]
		});
	
	
}
function DrawNumberAt(number,id){
  element= document.getElementById(id);
  xWidth= element.width;
  yHeight = element.height;
  var ctx = element.getContext('2d');
  
  ctx.font = '120px Hotel Coral Essex';
  
  var textMeter = ctx.measureText(""+number);
  anchoTexto= textMeter.width;
  altoTexto = yHeight;
  x= Math.floor((xWidth/2))- Math.floor((anchoTexto/2)); 
  y= Math.floor((altoTexto/2))+Math.floor((altoTexto/3));
  ctx.fillStyle="#0099FF";
  //ctx.scale(2,2);
  ctx.fillText(number,x, y);
}

function comprarCartones(){
	
}

function borraNumeroCartonAt(id){
	  
	  elementCanvas= document.getElementById(id);	
	  elementCanvas.width=elementCanvas.width;
	  var ctx = elementCanvas.getContext('2d');
	  ctx.clearRect(0,0,elementCanvas.width,elementCanvas.heigth);

}
function fullscreen(e){

	if (e.requestFullscreen) {
		e.requestFullscreen();
	} else if (e.webkitRequestFullscreen) {
		e.webkitRequestFullscreen();
	} else if (e.mozRequestFullScreen) {
		e.mozRequestFullScreen();
	} else if (e.msRequestFullscreen) {
		e.msRequestFullscreen();
	}
}
function analizaTecla(e){
	refCanvas = e.firstElementChild.id;

	numOrdenCarton= refCanvas.charAt(0);
	numFilaCarton=refCanvas.charAt(2);
	numColCarton=refCanvas.charAt(4);
	
	numeroRefCarton=document.getElementById("refCarton"+numOrdenCarton).textContent;
	
	arrayCard = myArrayCartonesJuego[numOrdenCarton];
	arrayFila= arrayCard[numFilaCarton-1];
	numRepresentado=arrayFila[numColCarton-1];
	
	alert("numero de canvas:"+refCanvas+"\n"+"numero de nRefCarton="+numeroRefCarton+"\n"+"Numero representado="+numRepresentado);
	
}


function apagaLinea(){
	document.getElementById("labelLinea").style.visibility="hidden";
	triggerLinea="false";
	lineaCantada="true";
	show_InMessage("LINEA CORRECTA","blink")
}

function apagaBingo(){
	document.getElementById("labelBingo").style.visibility="hidden";
	//triggerBingo="false";
	//bingoCantado="true";
	show_InMessage("BINGO CORRECTO","blink")
}

function show_InMessage(contenido,activoMarquee){
	largoCeldaMensajes = comboTexto.clientWidth;
	textoLargo = largoCeldaMensajes+"px";//
	
	if(activoMarquee!=null){
		if(activoMarquee=="blink"){
			comboTexto.innerHTML="<label class='blink' width='"+textoLargo+"'  id='labelTexto' class='classMessage' >"+contenido+"</label>";
		}else {
			comboTexto.innerHTML="<marquee id='marquesina' behavior='scroll' direction='left' scrollamount='4' width='"+textoLargo+"'>"+contenido+"</marquee>";
		}

	}else{
		//comboTexto.innerHTML= "<label width='"+textoLargo+"'  id='labelTexto' class='classMessage' >"+contenido+"</label>";

		comboTexto.innerHTML= "<label width='"+textoLargo+"'  id='labelTexto' class='classMessage' >"+contenido+"</label>";	
	}
	
}
function send_command(){
		text_message = document.getElementById("text_comando").value;
		socket_send(text_message);
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
	etiquetaOrden.textContent="0";

	canvas=document.getElementById('canvas_bola');
	canvas.width=canvas.width;
	lienzo=canvas.getContext('2d');
	lienzo.clearRect(0,0,canvas.width,canvas.heigth);
	//borrarNumerosCarton();
	
	refreshDatosCartones();
	

	
}
function arrancar(){
	//El socket ya esta creado
	
	fullscreen(document.getElementById("content"));
	initInterface();
	startup();
}
function activarCartones(){
	numeroCartonesComprados=document.getElementById("numeroCartonesComprados").value;
	var array;
	for(nC=1;nC <= numeroCartonesComprados;nC++){
		numeroRefCarton=document.getElementById("refCarton"+nC).textContent;
		array=mostrarNumerosCarton(numeroRefCarton,nC);
		myArrayCartonesJuego[nC]=array;
	}
	
}

function mostrarNumerosCarton(nRefCarton,nOrdCarton){
			//servlet de servicio --->SourcerArraysCarton
			var OrdinalCarton = nOrdCarton;
			var params = new Object();
			var array2;
			//params.nCarton=document.getElementById("refCarton"+nC).textContent;
			params.nCarton=nRefCarton
			params.perfil="jugador";
			params.usuario=document.getElementById("usuario").value;
			params.comando="ArrayCartonBaseDatosPorNRef";


			$.ajax({
				  type: 'POST',
				  url: "SourcerArraysCarton",
				  data: params,
				  dataType:"json",
				  async:false
				}).done(function( data ) {
					encenderNumerosDeArrayCarton(data,OrdinalCarton);
					array2=data;
			});
			return array2;
}
function encenderNumerosDeArrayCarton(myData,numberCarton){
	var myArrayCartones=myData;
	
	for(f=0;f<3;f++){
		for(c=0;c<9;c++){
			arrayLinea= myArrayCartones[f];
			number = arrayLinea[c];
			DrawNumberAt(number,numberCarton+"F"+(f+1)+"C"+(c+1));
		}
	}
	
	
}
function borrarNumerosCarton(){
	/*
	for(f=1;f<4;f++){
		for(c=1;c<10;c++){
			borraNumeroCartonAt("F"+f+"C"+c);
		}
	}*/
	window.location.reload(true);
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
	show_InMessage("socket abierto");
	anchoPantalla=window.innerWidth;

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

	xLinea=parseFloat((sumaCaja*porCientoLinea)/sumaTantos).toFixed(2);
	xBingo=parseFloat((sumaCaja*porCientoBingo)/sumaTantos).toFixed(2);
	zCantaor=parseFloat((sumaCaja*porCientoCantaor)/sumaTantos).toFixed(2);

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
	    	    //myRango=sacarRangos(arrayMessages[1]);
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
				refreshBolas();
				//play_range(myRango[0],myRango[1]);	
	    	    
				break;
		case "EnciendeVideo":
				enciendeVideo();
				break;
		case "ApagaVideo":
				apagaVideo();
				break;
		case "InitInterface":
				initInterface();//
				show_InMessage("!! COMIENZA PARTIDA ¡¡","blink");	
				break;

		case "EndBalls":
				show_InMessage("PARTIDA FINALIZADA ....HAGAN SUS APUESTAS",true);
                                borrarNumerosCarton();
				break;
		case "Linea":
				show_InMessage("!! LINEA ¡¡","blink");	
				break;
		case "Bingo":
			show_InMessage("!! BINGO ¡¡","blink");	
			break;
				break
		case "ComprobarLinea":
				show_InMessage("COMPROBANDO LINEA ....",true);
				break;
		case "ComprobarBingo":
				show_InMessage("COMPROBANDO BINGO ....",true);
				break;				
		case "EncenderNumero":
				if(arrayMessages[1] == null || (arrayMessages[1]+"").length >2)return;
				if(arrayMessages[2] == "simple"){
					//encenderNumero(arrayMessages[1],"simple");
				}else{
					//encenderNumero(arrayMessages[1]);
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
	  
	  myVarX=Math.floor((elementCanvas.width)/2 + ((elementCanvas.width)/2)/2 );
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

