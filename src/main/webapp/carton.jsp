<%@ page language="java" 
    contentType="text/html; charset=UTF-8"
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
<meta name="vieport" content="width=device-height, initial-scale=1.0">
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
  <td padding=0 width="12%"><img id="logo" src="images/IconoBola.jpg" width="56" height="45" longdesc="file:///C|/Users/boga/git/wildfly/src/main/webapp/images/IconoBola.jpg" ><label id="sala"><%=myUser.getSalonInUse()%></label><%= " "+ myUser.getUsername()+ " "%></td>
  
 <td width="68%" class="tablaInfo" ><table width="90%"  align="center" class="tablaInfo">
   <tr>
     <td width="26%" class="celdaInfo"><span class="Comander">BOLA N:</span></td>
     <td width="4%" id="datoOrdenBola" class="celdaInfo"  ><span class="Comander">
       <label id="labelOrden" class="valorInfo">0</label>
     </span></td>
     <td width="37%" id="datoLinea" class="celdaInfo" ><label id="labelLinea">LINEA:</label></td>
     <td width="33%" class="celdaInfo"><span class="Comander">
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
     <td colspan="4" class="valorInfo" id="comboTexto">comboTexto</td>
   </tr>
 </table>
 <!--
   CARTON n:
   <%= " "+ myCarton.getnCarton()+" "%> SERIE: <%= " " + myCarton.getnCarton()+" "%>PRECIO:<%= myCarton.getPrecioCarton()%>
     -->
  
	
    <td id="CajaDcha" width="20%">	          
    <canvas id="canvas_bola" class="canvasBola">
	           
    </canvas></td>
  </tr>
   
</table>
</header>

<table class="tablero">
    <tr>
         <td height="100" class="panel"> 
            <label ID="F1C1" class="numeros" ></label>             
        </td>
      <td class="panel"> 
        <label id="F1C2" class="numeros" >10</label>             
        </td>
      <td height="100" class="panel"> 
        <label id="F1C3" class="numeros" >20</label>             
        </td>
      <td height="100" class="panel"> 
        <label id="F1C4" class="numeros" >30</label>             
        </td>
      <td height="100" class="panel"> 
        <label id="F1C5" class="numeros" >40</label>             
        </td>
      
      <td height="100" class="panel">
      	<label id="F1C6" class="numeros" >50</label>
      </td >
      <td height="100" class="panel">
        <label id="F1C7" class="numeros" >60</label>              
     </td >

        <td height="100" class="panel">
          <label id="F1C8" class="numeros" >70</label>              
        </td >
        <td height="100" class="panel">
          <label id="F1C9" class="numeros" >80</label>              
        </td >

    </tr>
    <tr>
       <td class="panel">
         <label id="F2C1" class="numeros" >1</label>
       </td>
               <td class="panel"> 
            <label id="F2C2" class="numeros" >11</label>             
        </td>
      <td class="panel"> 
            <label id="F2C3" class="numeros" >21</label>             
        </td>
      <td class="panel"> 
            <label id="F2C4" class="numeros" >31</label>             
        </td>
      <td class="panel"> 
            <label id="F2C5" class="numeros" >41</label>             
        </td>
      <td class="panel"> 
            <label id="F2C6" class="numeros" >51</label>             
        </td>
      <td class="panel"> 
            <label id="F2C7" class="numeros" >61</label>             
        </td>
    
        <td class="panel">
            <label id="F2C8" class="numeros" >71</label>              
        </td >
        <td class="panel">
            <label id="F2C9" class="numeros" >81</label>              
        </td >
	</tr>

  <tr>
         <td class="panel"><label id="F3C1" class="numeros" >2</label></td>
      <td class="panel"> 
            <label id="F3C2" class="numeros" >12</label>             
        </td>
      <td class="panel"> 
            <label id="F3C3" class="numeros" >22</label>             
        </td>
      <td class="panel"> 
            <label id="F3C4" class="numeros" >32</label>             
        </td>
      <td class="panel"> 
            <label id="F3C5" class="numeros" >42</label>             
        </td>
      <td class="panel"> 
            <label id="F3C6" class="numeros" >52</label>             
        </td>
      <td class="panel"> 
            <label id="F3C7" class="numeros" >62</label>             
        </td>
    
        <td class="panel">
            <label id="F3C8" class="numeros" >72</label>              
        </td >
        <td class="panel">
            <label id="F3C9" class="numeros" >82</label>              
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
<article>
 






</body>

</html>
