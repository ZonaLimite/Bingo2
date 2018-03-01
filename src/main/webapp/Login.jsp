<%@ page language="java" 
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%! HttpSession mySession = null;%>
<%! String resp = null;%>
<%
		resp = request.getParameter("resp");

%>
<!DOCTYPE html>
<html>
<head>
  <script src="//code.jquery.com/jquery-1.12.4.js"></script>
  <script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="javascript/utilDialogos.js"></script>

<meta charset="ISO-8859-1">
<title>Bingo Paco Home</title>
<style type="text/css">
.botonSubmit {
	text-align: center;
}
.rotulo {
	text-align: center;
	font-weight: bold;
}
.TextLogin {
	text-align: right;
}
.TablaLogin {
	background-color: #03F;
	color: #FFF;
	border-top-style: groove;
	border-right-style: groove;
	border-bottom-style: groove;
	border-left-style: groove;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 18px;
	font-weight: bolder;
}
.cuerpo {
	background-color: #000;
}
</style>


</head>
<body class="cuerpo">
	
	
		<form >
		<table class="TablaLogin" width="90%" border="1" align="center">
		  <tr>
			<td colspan="2" class="rotulo">
           Abrir Sesion Bingo Home
            </td>
          </tr>
		  <tr>
		    <td width="167" class="TextLogin">Usuario :
	        <td width="167"><input type="text" name="un" id="un"></td>
	      </tr>
		  <tr>
		    <td class="TextLogin">Password:</td>
		    <td><input type="password" name="pw" id="pw"></td>
	      </tr>
		  <tr>
		    <td class="TextLogin">Sala:</td>
		    <td><input name="sala" type="text" class="TablaLogin" id="sala" value="sala1"></td>
	      </tr>
          <tr>
          <td colspan="2" class="botonSubmit">
           <input type="button" value="Abrir Sesion" onClick="hacerLogin()">
		</td>
          </tr>
          <tr>
            <td colspan="2" class="botonSubmit" id="resp"><%if(!(resp==null)){%><label class="Bienvenido" ><%out.print(resp); %></label></td><%} %>
          </tr>
          <tr>
            <td class="botonSubmit">
            <input type="button" name="nuevoUsuario" id="nuevoUsuario" value="NuevoUsuario" onClick="mostrarHTML('MostrarNuevoUsuario')"></td>
            <td class="botonSubmit"><input type="button" name="rememberPassword" id="rememberPassword" value="Recordar Password"></td>
          </tr>
        </table>
		<p>
		 
		</p>
</form>	
	
</body>
</html>
