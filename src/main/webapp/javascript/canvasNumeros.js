var timer;
var canvasOffset;
var istrue = false;

var elementDrawing;
var delay = 1000; // how much long u have to hold click in MS

/*
document.addEventListener('touchstart', function(event) {
    alert(event.target);	
    istrue = true;
    timer = setTimeout(function(){ makeChange();},delay);
}, false);

document.addEventListener('touchmove', function(event) {
    //event.preventDefault();	
    istrue = true;
    timer = setTimeout(function(){ makeChange();},delay);
}, false);

*/
function startup() {
	
	
	var collectionCanvas = document.getElementsByTagName("canvas");
	  for(i=0;i<collectionCanvas.length;i++){
		  el=collectionCanvas[i];
		  el.addEventListener("mousedown",func,false);
		  el.addEventListener("mousemove",drawNow,false);
		  //el.addEventListener("mouseover",controlOver,false);		 
		  //el.addEventListener("mouseout",controlOut,false);				  
		  el.addEventListener("mouseup",revert,false);
		  el.addEventListener("touchstart", func, false);
		  el.addEventListener("touchend", revert, false);
		  //el.addEventListener("touchcancel", handleCancel, false);
		  el.addEventListener("touchmove", drawNow, false);
	  }
}

function controlOut(){
	
}
function func(event)
{
	
   event.preventDefault();
   elementDrawing = event.target.id;
   istrue = true;
   timer = setTimeout(function(){ makeChange(event);},delay);
   
}

function makeChange(event)
{
      if(timer)
      clearTimeout(timer);
      
      if(istrue)
      {
            /// rest of your code
          
          event.target.style.backgroundColor = "#FFC";
          istrue=false;
      

      }
}
function revert(evt)
{
   evt.preventDefault();	
   istrue =false;
 
}

function getMousePos(canvas, evt) {
    var rect = canvas.getBoundingClientRect();
    return {
        x: (evt.clientX - rect.left) / (rect.right - rect.left) * canvas.width,
        y: (evt.clientY - rect.top) / (rect.bottom - rect.top) * canvas.height
    };
}
function drawNow(event){
	//event.preventDefault();
	if(istrue){
		element = event.target;
		idElement = element.id;
		if(!(idElement==elementDrawing))return;//
		context = element.getContext("2d");
	    var pos = getMousePos(element, event);
	    posMouseX = pos.x;
	    posMouseY = pos.y;
		context.beginPath();

		context.fillStyle="rgba(0,255,0,0.3)";
		context.fillRect(posMouseX,posMouseY,20,20);
		//console.log(texto);
		//context.fillText(texto,2,Math.floor(element.height-10));
	}
}

function whateverFunc()
{
    alert('dblclick');
}