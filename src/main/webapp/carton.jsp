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
<%
	Vector<Carton> vCarton=null;
	mySession = request.getSession();
	if(mySession!=null){
		myUser = (UserBean)mySession.getAttribute("userBean");
		//De momento solo un carton por usuario
		//int numCartones = myUser.getNumeroCartones();
		vCarton = (Vector<Carton>)myUser.getvCarton();
	}
%>
<body class="pagina" id="content">
<input id="numeroCartonesComprados" type="hidden" name="cuantosCartones" value="<%=vCarton.size()%>">
<div id="agrupar">
<header id="cabecera">

<table width="100%" border="1" cellspacing="2" class="ano">
  <tr>
  <td width="12%" height="109" padding=0><img id="logo" src="images/IconoBola.jpg" width="56" height="45" longdesc="file:///C|/Users/boga/git/wildfly/src/main/webapp/images/IconoBola.jpg" ><label id="sala"><%=myUser.getSalonInUse()%></label>
    <span class="labelUser">
    <label class="labelUser"><%=myUser.getUsername()%></label>
    </span></td>
  
 <td width="60%" class="tablaInfo" ><table width="100%"  align="center" class="tablaInfo">
   <tr>
     <td width="97" class="celdaInfo"><span class="Comander">BOLA N:</span></td>
     <td width="37" id="datoOrdenBola" class="celdaInfo"  ><span class="Comander">
       <label id="labelOrden" class="valorInfo">0</label>
     </span></td>
     <td width="118" id="datoLinea" class="celdaInfo" ><label id="labelLinea">LINEA:</label></td>
     <td width="176" class="celdaInfo"><span class="Comander">
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
     <td colspan="4" class="valorInfo" >
     <article id="articulo">	
		<div align="center">  
		<label id="boton_Linea" CLASS="menu_li2" >LINEA</label>
		<label id="boton_Bingo" CLASS="menu_li2" >BINGO</label>
		<label id="boton_Jugar" CLASS="menu_li2" >JUGAR</label>
		<label id="boton_Carton" CLASS="menu_li2" >CARD</label>
		</div>  
	</article>
	</td>
   </tr>
	   <tr>
     <td colspan="4" class="valorInfo" id="comboTexto">
     <label  id="labelTexto" >combotexto</label>
	 </td>
   </tr>      
 </table>
 
  
	
    <td id="CajaDcha" width="38%">	          
    <canvas id="canvas_bola" class="canvasBola">
	           
    </canvas></td>
  </tr>
   
</table>
</header>

<%	
    for(int i=0; i < vCarton.size();i++){
    	myCarton = vCarton.elementAt(i);
%>
        <jsp:include page="tablaCarton.jsp" flush="true">
        <jsp:param name="cartonSeq" value="<%= i+1 %>" />
        <jsp:param name="nRef" value="<%= myCarton.getnRef()+\"\" %>" /> 
        </jsp:include>    
<%
    }
%>



</div>

<div id="cartones" title="Compra de cartones"> 
<form id="requestForm">

<label >Numero de cartones a jugar?:</label><input type="text" value="1" name="nCartones">
<input type="hidden" id="sala" name="sala" value="<%=myUser.getSalonInUse() %>">
<input type="hidden" id="usuario"  name="usuario" value="<%=myUser.getUsername() %>">
<input type="text" id="feedback" >

</form>
</div>


</body>

</html>
