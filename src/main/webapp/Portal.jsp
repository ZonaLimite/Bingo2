<%@ page language="java" 
    contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"
%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%! HttpSession mySession = null;%>
<%! String user = null;%>
<%! String sala = null;%>
<%! String perfil =null;%>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="vieport" content="width=642, initial-scale=1, orientation=landscape">
<title>Bingo Boga Home</title>
  <link href="css/Carton.css" rel="stylesheet" type="text/css">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
  <link href="css/Portal.css" rel="stylesheet" type="text/css">
  <link href="css/estilosDialogos.css" rel="stylesheet" type="text/css">  
  <script src="//code.jquery.com/jquery-1.12.4.js"></script>
  <script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script src="javascript/utilDialogos.js"></script>
  <script src="javascript/portal.js"></script>



</head>
<%
	
	mySession = request.getSession(false);
	if(mySession!=null){
		user = (String)mySession.getAttribute("usuario");
		perfil = (String)mySession.getAttribute("perfil");
		sala = (String)mySession.getAttribute("sala");
		if(user==null)response.sendRedirect("Login.html");
	}else{
		System.out.print("la sesion es null");
		response.sendRedirect("Login.html");
	}
%>
<body class="PortalBody">

<div class="container">
  <header>
    <img  width="100%" height="100px" src="images/pattern1.jpg" >
  </header>
<div class="sidebar1">
	<%if(!(user==null)){%>
		<label class="Bienvenido" >Bienvenido <%out.print(user); %></label>
    <ul class="nav">
      <li class="botonEnlace" id="AccessBingo">Acceso Bingo</li>
	  <li class="botonEnlace" id="botonRanking">Ranking Bingo</li>
	  <li class="botonEnlace" id="compraBonos">Compra Bonos</li>
	  <li class="botonEnlace" id="liquidarBonos">Liquidar Bonos</li>
	<%if(user.equals("super")){%>
		<li class="botonEnlace" id="volcadoBonos">Checkeo Bonos
		       <select name="idSala" id="idSala" >
       				<option value="sala1" selected>Sala1</option>
      			</select></td>
       <li class="botonEnlace" id="volcadoLiquidacion">Checkeo Liquidacion</li>  	       
	   <li class="botonEnlace" id="botonJugadores">Jugadores</li> 
	   <li class="botonEnlace" id="botonCartones">Cartones</li>  	        			
	<% }%>  

	  <li class="botonEnlace" id="botonCerrarSesion">CerrarSesion</li>               
    </ul>
  <!-- end .sidebar1 -->
   <% }%>
</div>
  
<div class="content" id="content">
       
        



<!-- end .content -->
</div>

  <footer id="resultMonitor">
 		ready...
  </footer>
  <!-- end .container --></div>
<input type="hidden" id="usuario"  name="usuario" value="<%out.print(user); %>">  
<input type="hidden" id="perfil"  name="perfil" value="<%out.print(perfil); %>">
<input type="hidden" id="sala"  name="sala" value="<%out.print(sala); %>">    
</body>
</html>
