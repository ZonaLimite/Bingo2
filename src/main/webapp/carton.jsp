<%@ page language="java" 
    contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"
%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%! HttpSession mySession = null;%>
<%! String user = null;%>
<%! String sala = null;%>
<%! String perfil = null;%>
<%! String ipwebserver = null;%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="vieport" content="width=800, initial-scale=1, orientation=landscape">
<title>Bingo 2016</title>
  <link href="css/Carton.css" rel="stylesheet" type="text/css">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
  <script src="//code.jquery.com/jquery-1.12.4.js"></script>
  <script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script src="javascript/canvasNumeros.js"></script>
  <script src="javascript/carton.js"></script>
  <script src="javascript/chat.js"></script>
  <script src="javascript/app.js"></script>
  <script src="javascript/openvidu-browser-2.13.0.js"></script>
</head>
<%
	
	mySession = request.getSession(false);
	String url="Login.jsp";
	if(mySession!=null){
		System.out.println("la mySesion "+ mySession.getId()+ " de "+mySession.getAttribute("usuario"));
 
		if(mySession.getAttribute("usuario")==null){
	        response.sendRedirect(url); //logged-in page // 
	        return;//Si entramos por jsp es un checkeo de sesion
		}
		
		user = request.getParameter("usuario");
		sala = request.getParameter("sala");
		perfil = request.getParameter("perfil");
		
	}else{
		System.out.print("la sesion es null");

        response.sendRedirect(url); //logged-in page // 
        return;
	}
	String protocol = request.getScheme();
	String host = request.getServerName();
	int port= request.getServerPort();
	String ipWebServer = protocol+"://"+host+":"+port+"/wildfly";				
%>
<body class="pagina" id="content">
<div id="agrupar">
<header id="cabecera">

<table width="100%" border="1" cellspacing="2" class="ano">
  <tr>
  	<td width="12%"  padding=0>
  	 <div id="contenedorIZq" >
  	   <div id="separador"  >
  	     <div class="anuladorHigh" id="divNormal"  >
  	 	 	<label class="labelUser">Saldo : </label>
  	  		<label class="saldo" id="saldo">100 €</label><br>
  	  		<a href=Portal.jsp><img id="logo" src="images/IconoBola.jpg" width="56" height="45"></a><br>
  	  		
      		<span class="labelUser">
      		<label class="labelUser"><%out.print(user);%></label>
      		</span><br>
            <button  class="jq2" id="video-button" role="button">
        			VideoConf.
      	 	</button>
      	</div>
	  	<div id="main-video" class="anuladorHigh2">
	  	    <button  class="jq2" id="device-button" role="button">
        			¿Dispositivo?
      	 	</button>
	  		<video width="100%"  autoplay playsinline="true">	</video>
            <input class="btn btn-large btn-danger" type="button" id="buttonLeaveSession" onmouseup="removeUser(); leaveSession()" value="Leave session">
	  	</div>
    </div>
  	     
     </div>

    </td>
 
    
    <td width="58%" class="tablaInfo" >
    <table width="100%"  align="center" class="tablaInfo">
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
     <article id="articulo" name="articulo">	
		<div align="center">  
		<label id="boton_Linea" CLASS="menu_li2" >LINEA</label>
		<label id="boton_Bingo" CLASS="menu_li2" >BINGO</label>
		<label id="boton_Jugar" CLASS="menu_li2" >FULL</label>
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
  </td>
  
  <td width="30%" id="CajaDcha">
	         
		<select name="Usuarios"class="userConect" id="userlistbox" >
		</select>
        <button class="jq" id="hangup-button" role="button">
        			Colgar
      	 </button>
		<canvas id="canvas_bola" class="canvasBola"></canvas>

	</td>
  </tr>
   
</table>
</header>

<article id="innerHTMLCartones">
	
    	<!-----------------------------Espacio para cartones via Servlet (WriterCartonesServlet)  -->
	
</article>
</div>


<footer id="footer" class="footerClass">

<div id="cartones" title="Compra de cartones"> 
	<form id="requestForm">
		<label >Numero de cartones a jugar?:</label>
		<input id="spinner" type="text" width="20" value="1" name="nCartones" style=" width : 27px;">
		<input type="hidden" id="sala" name="sala" value="<%out.print(sala); %>">
		<input type="hidden" id="usuario"  name="usuario" value="<%out.print(user); %>">
		<input type="hidden" id="perfil"  name="perfil" value="<%out.print(perfil); %>">
<br>
		<label id="feedback" style=" width : 100%;"></label>
		<img id="Loto2" class="hiddenImage" src="./images/Loto2.png">
	</form>
</div>

<div id="welcome" title="¡BIENVENIDOS!"> 
	<form >
		<label >Pulsa "START" para comenzar</label>
	</form>	
</div>
<!-------------------Formulario para Jquery Eleccion devices -->
<div id="divDevices" title="Cambio Dispositivo audio-video"> 
	<form >
		<label >Dispositivos disponibles</label>
		<select name="devices"class="userConect" id="idDevices" >
	  		
			</select>
	</form>	
</div>

<audio id="audioWeb" class="audioClass" controls >
  	<source src="<%out.print(ipWebServer); %>/audio/AudioLinea1.mp3" type="audio/mpeg">
	Your browser does not support the audio element....
</audio>

			<div class="chat">

    				<ul class="userlistbox"></ul>
    				<div class="chatbox"></div>
    				<div class="camerabox">
      					<video id="received_video" autoplay></video>
      					<video id="local_video" autoplay muted></video>
    				</div>
			
			</div>
			
<!-----------Espacio definicion contenedores videocnferencia  -->
			<div id="session" hidden=true>
				<div id="session-header">
					<h1 id="session-title"></h1>
					
				</div>
        <div id="second-video" class="secondVid">
		<video width="100%" autoplay playsinline="true"></video>
		</div>	 
				<div id="video-container" class="col-md-6"></div>
			</div>	    		
</footer>

</body>

</html>
