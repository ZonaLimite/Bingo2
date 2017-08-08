<table class="tablero">
    <tr>
         <td  class="panel"> 
          <canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F1C1" ></canvas>             
        </td>
    <td class="panel" >	          
  		  <canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F1C2" ></canvas>
    </td>
      <td class="panel"> 
     	<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F1C3"></canvas>   
        </td>
      <td class="panel"> 
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F1C4"></canvas> 
        </td>
      <td class="panel"> 
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F1C5"></canvas>            
        </td>
      <td class="panel">
     	 <canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F1C6"></canvas> 
      </td >
      <td  class="panel">
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F1C7"></canvas>               
     </td >
        <td  class="panel">
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F1C8"></canvas>          
        </td >
        <td  class="panel">
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F1C9"></canvas>          
        </td >

    </tr>
   <tr>
         <td class="panel"> 
          <canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F2C1"></canvas>             
        </td>
    <td class="panel">	          
    <canvas class="canvasNumero"  id="<%=request.getParameter("cartonSeq") %>F2C2">
	           
    </canvas></td>
      <td class="panel"> 
     	<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F2C3"></canvas>   
        </td>
      <td class="panel"> 
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F2C4"></canvas> 
        </td>
      <td class="panel"> 
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F2C5"></canvas>            
        </td>
      <td class="panel">
     	 <canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F2C6"></canvas> 
      </td >
      <td  class="panel">
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F2C7"></canvas>               
     </td >
        <td  class="panel">
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F2C8"></canvas>          
        </td >
        <td  class="panel">
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F2C9"></canvas>          
        </td >

    </tr>
   <tr>
         <td onClick="analizaTecla(this)" class="panel"> 
          <canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F3C1"></canvas>             
        </td>
    <td class="panel" >	          
    <canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F3C2">
	           
    </canvas></td>
      <td class="panel"> 
     	<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F3C3"></canvas>   
        </td>
      <td class="panel"> 
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F3C4"></canvas> 
        </td>
      <td class="panel"> 
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F3C5"></canvas>            
        </td>
      <td class="panel">
     	 <canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F3C6"></canvas> 
      </td >
      <td  class="panel">
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F3C7"></canvas>               
     </td >
        <td  class="panel">
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F3C8"></canvas>          
        </td >
        <td  class="panel">
		<canvas class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F3C9"></canvas>          
        </td >

    </tr>
    <tr>
    	<td colspan="9" class="letraCarton">CARTON n:<Label id="refCarton<%=request.getParameter("cartonSeq") %>"><%=request.getParameter("nRef")%></label></td>
    </tr>
</table>
