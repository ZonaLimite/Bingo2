/**
 * Codigo manejador player 
 */
function iniciar() {
	medio=document.getElementById('medio');
	medio.addEventListener('progress', refreshCount, false);
}
function refreshCount(){
	contador=document.getElementById("contador");
	contador