var boton_Imprimir;
var numeroCartones;
var ventanaCartones;
var img;

function iniciar(){
numeroCartones = document.getElementById("nCartones");
ventanaCartones = document.getElementById("contenedorHTML");	
boton_Imprimir = document.getElementById("botonImprimir");
boton_Imprimir.onclick = function(){ImprimirCartones(numeroCartones.value,"ImprimirCartones", "super");};
}

function ImprimirCartones(nCartones,comando, usuario){
	var params = new Object();
	params.comando=comando;
	params.nCartones = nCartones;
	params.usuario=usuario;

	$.ajax({
		  type: 'POST',
		  url: "WriterCartonesServlet",
		  data: params,
		  async: false
		}).done(function( data ) {
			ventanaCartones.innerHTML=data;
			activarCartones();
		
	});
}
function activarCartones(){
	numeroCartonesComprados=document.getElementById("numeroCartonesComprados").value;
	var array;
	img = document.getElementById("Loto2");
	for(nC=1;nC <= numeroCartonesComprados;nC++){
		numeroRefCarton=document.getElementById("refCarton"+nC).textContent;
		array=mostrarNumerosCarton(numeroRefCarton,nC);
		//myArrayCartonesJuego[nC]=array;
	}
	
}

function mostrarNumerosCarton(nRefCarton,nOrdCarton){
	//servlet de servicio --->SourcerArraysCarton
	var OrdinalCarton = nOrdCarton;
	var params = new Object();
	var array2;
	//params.nCarton=document.getElementById("refCarton"+nC).textContent;
	params.nCarton=nRefCarton
	params.perfil="supervisor";
	params.usuario="super";
	params.comando="ArrayCartonBaseDatos";


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
	//myArrayNumerosCantados = obtenerNumerosCantados();
	var siEstaCantado=false;
	for(f=0;f<3;f++){
		for(c=0;c<9;c++){
			arrayLinea= myArrayCartones[f];
			number = arrayLinea[c];
			id=numberCarton+"F"+(f+1)+"C"+(c+1);
			DrawNumberAt(number,id);
/*			
			for(x=0;x < myArrayNumerosCantados.length; x++){
				if(myArrayNumerosCantados[x]==number){
					siEstaCantado=true;
					x=myArrayNumerosCantados.length;
				}
			}
			if(siEstaCantado==true){
				element= document.getElementById(id);
				element.style.backgroundColor = "#FFC";//Marca numeros cantados en carton
				siEstaCantado=false;
			}
*/			
		}
	}
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
	  //Numero de color
	  //ctx.fillStyle="#0099FF";
	  ctx.fillStyle="#0099FF";

	  //ctx.scale(2,2);
	  if(number==0){

		  ctx.drawImage(img, 0, 0,xWidth,yHeight);//
	  }else{
		  ctx.fillText(number,x, y);
	  }
}
window.onload=iniciar;