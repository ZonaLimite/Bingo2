<%@ page language="java" 
    contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"
%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.Vector" %>
<%@ page import="servlet.Carton" %>
<%@ page import="servlet.UserBean" %>
<%! HttpSession mySession = null;%>
<%! UserBean myUser = null;%>
<%! Carton myCarton= null;%>
<%! String user = null;%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="vieport" content="width=800, height=480, initial-scale=1, orientation=landscape">
<title>Bingo 2016</title>
 
  <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
  
  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
  
<script src="javascript/carton.js"></script>

<link href="css/Carton.css" rel="stylesheet" type="text/css">
</head>
<body class="pagina" id="content">
<%
	mySession = request.getSession();
	if(mySession!=null){
		myUser = (UserBean)mySession.getAttribute("userBean");
		//De momento solo un carton por usuario
		int numCartones = myUser.getNumeroCartones();
		Vector<Carton> vCarton = (Vector<Carton>)myUser.getvCarton();
		myCarton= vCarton.elementAt(0)	;
	}
	
%>
<div id="agrupar">
<header id="cabecera">

<table width="100%" border="1" cellspacing="2" class="ano">
  <tr>
  <td width="12%" height="109" padding=0><img id="logo" src="images/IconoBola.jpg" width="56" height="45" longdesc="file:///C|/Users/boga/git/wildfly/src/main/webapp/images/IconoBola.jpg" ><label id="sala"><%=myUser.getSalonInUse()%></label><%= " "+ myUser.getUsername()+ " "%></td>
  
 <td width="60%" class="tablaInfo" ><table width="100%"  align="center" class="tablaInfo">
   <tr>
     <td width="90" class="celdaInfo"><span class="Comander">BOLA N:</span></td>
     <td width="35" id="datoOrdenBola" class="celdaInfo"  ><span class="Comander">
       <label id="labelOrden" class="valorInfo">0</label>
     </span></td>
     <td width="76" id="datoLinea" class="celdaInfo" ><label id="labelLinea">LINEA:</label></td>
     <td width="196" class="celdaInfo"><span class="Comander">
       <label id="valorLinea" class="valorInfo">0 €</label>
     </span></td>
   </tr>
   <tr>
     <td class="celdaInfo"><span class="Comander"> CARTONES</span></td>
     <td id="datoCartones" class="celdaInfo" ><span class="Comander">
       <label id="valorCartones" class="valorInfo">0</label>
     </span></td>
     <td class="celdaInfo"><label id="labelBingo">BINGO:</label></td>
     <td  id="datoBingo" class="celdaInfo" ><span class="Comander">
       <label id="valorBingo" class="valorInfo">0 €</label>
     </span></td>
   </tr>
   <tr>
     <td colspan="4" class="valorInfo" id="comboTexto"></td>
   </tr>
 </table>
 <!--
   CARTON n:
   <%= " "+ myCarton.getnCarton()+" "%> SERIE: <%= " " + myCarton.getnCarton()+" "%>PRECIO:<%= myCarton.getPrecioCarton()%>
     -->
  
	
    <td id="CajaDcha" width="38%">	          
    <canvas id="canvas_bola" class="canvasBola">
	           
    </canvas></td>
  </tr>
   
</table>
</header>

<table class="tablero">
    <tr>
         <td onClick="analizaTecla(this)" class="panel"> 
          <canvas class="canvasNumero" id="F1C1"></canvas>             
        </td>
    <td class="panel" >	          
    <canvas class="canvasNumero" id="F1C2" >
	           
    </canvas></td>
      <td class="panel"> 
     	<canvas class="canvasNumero" id="F1C3"></canvas>   
        </td>
      <td class="panel"> 
		<canvas class="canvasNumero" id="F1C4"></canvas> 
        </td>
      <td class="panel"> 
		<canvas class="canvasNumero" id="F1C5"></canvas>            
        </td>
      <td class="panel">
     	 <canvas class="canvasNumero" id="F1C6"></canvas> 
      </td >
      <td  class="panel">
		<canvas class="canvasNumero" id="F1C7"></canvas>               
     </td >
        <td  class="panel">
		<canvas class="canvasNumero" id="F1C8"></canvas>          
        </td >
        <td  class="panel">
		<canvas class="canvasNumero" id="F1C9"></canvas>          
        </td >

    </tr>
   <tr>
         <td onClick="analizaTecla(this)" class="panel"> 
          <canvas class="canvasNumero" id="F2C1"></canvas>             
        </td>
    <td class="panel">	          
    <canvas class="canvasNumero"  id="F2C2">
	           
    </canvas></td>
      <td class="panel"> 
     	<canvas class="canvasNumero" id="F2C3"></canvas>   
        </td>
      <td class="panel"> 
		<canvas class="canvasNumero" id="F2C4"></canvas> 
        </td>
      <td class="panel"> 
		<canvas class="canvasNumero" id="F2C5"></canvas>            
        </td>
      <td class="panel">
     	 <canvas class="canvasNumero" id="F2C6"></canvas> 
      </td >
      <td  class="panel">
		<canvas class="canvasNumero" id="F2C7"></canvas>               
     </td >
        <td  class="panel">
		<canvas class="canvasNumero" id="F2C8"></canvas>          
        </td >
        <td  class="panel">
		<canvas class="canvasNumero" id="F2C9"></canvas>          
        </td >

    </tr>
   <tr>
         <td onClick="analizaTecla(this)" class="panel"> 
          <canvas class="canvasNumero" id="F3C1"></canvas>             
        </td>
    <td class="panel" >	          
    <canvas class="canvasNumero" id="F3C2">
	           
    </canvas></td>
      <td class="panel"> 
     	<canvas class="canvasNumero" id="F3C3"></canvas>   
        </td>
      <td class="panel"> 
		<canvas class="canvasNumero" id="F3C4"></canvas> 
        </td>
      <td class="panel"> 
		<canvas class="canvasNumero" id="F3C5"></canvas>            
        </td>
      <td class="panel">
     	 <canvas class="canvasNumero" id="F3C6"></canvas> 
      </td >
      <td  class="panel">
		<canvas class="canvasNumero" id="F3C7"></canvas>               
     </td >
        <td  class="panel">
		<canvas class="canvasNumero" id="F3C8"></canvas>          
        </td >
        <td  class="panel">
		<canvas class="canvasNumero" id="F3C9"></canvas>          
        </td >

    </tr>
</table>

 


<article id="articulo">	
<div align="center">  
<label id="boton_Linea" CLASS="menu_li2" >LINEA</label>
<label id="boton_Bingo" CLASS="menu_li2" >BINGO</label>
<label id="boton_Jugar" CLASS="menu_li2" >JUGAR</label>
</div>  
</article>
</div>

 






</body>

</html>
