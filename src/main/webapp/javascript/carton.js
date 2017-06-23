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
	//boton_iniciar = document.getElementById("iniciar");
	//boton_iniciar.onclick = function(){ arrancar()};
	
	//boton_resume = document.getElementById("resume");
	//boton_resume.onclick = function(){ resumir()};
	
	datoOrdenBola= document.getElementById("datoOrdenBola")
	canvas=document.getElementById('canvas_bola');
	lienzo=canvas.getContext('2d');
	
	comboTexto = document.getElementById("comboTexto");
	
	/*
	window.onresize = function(e){
		//e.preventDefault();
		resizeBolas();
	}*/
	creaSocket(salaInUse.textContent);
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
	if(activoMarquee!=null){
		longitudActualComboTexto = 400;
		comboTexto.innerHTML="<marquee id='marquesina' behavior='scroll' direction='left' scrollamount='4'width="+longitudActualComboTexto+">"+contenido+"</marquee>";
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
	//resizeBolas();
	alto=window.innerHeight;
	refreshDatosCartones();
	//caja_spy.value=""+anchoPantalla+"x"+alto;
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
	/*
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
