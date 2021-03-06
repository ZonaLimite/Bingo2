<%@ page language="java" 
    contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"
%>
<!doctype html>
<html>
<head>
  <link href="css/Carton.css" rel="stylesheet" type="text/css">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
  <script src="//code.jquery.com/jquery-1.12.4.js"></script>
  <script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script src="javascript/canvasNumeros.js"></script>
  <script src="javascript/carton.js"></script>
  <script src="javascript/chat.js"></script>
</head>
<body>
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
    <canvas class="canvasNumero"  id="<%=request.getParameter("cartonSeq") %>F2C2"></canvas>  
    </td>
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
          <video class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F3C1"></video>             
        </td>
    <td class="panel" >	          
    <video class="canvasNumero" id="<%=request.getParameter("cartonSeq") %>F3C2"></video>
    
	           
   </td>
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
</body>