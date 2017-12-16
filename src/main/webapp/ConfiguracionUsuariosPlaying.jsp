<%@ page language="java" 
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="servlet.UtilDatabase" %>
<%@ page import="java.util.Vector" %>
<%! HttpSession mySession = null;%>
<%! Vector<String[]> vectorResultsSQL = null;%>
<%
		UtilDatabase udatabase = new UtilDatabase();
        //arrayCampos[1] =1.User
		vectorResultsSQL = udatabase.dameVectorResultsSQL("Select User From usuarios Order by idUsuarios", 1);

%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Documento sin título</title>
<link href="css/estilosDialogos.css" rel="stylesheet" type="text/css">
<script src="javascript/utilDialogos.js"></script>
</head>

<body>

<table id="resultLines" width="97%" border="1" align="center" class="Tabla">
  <tr bgcolor="#003399">
    <td  width="50%" height="72" class="Cabecera" ><p>Usuarios Registrados</p>
      <form name="form1" method="post" action="">
        <select class="Caja" name="registeredPlayers" id="registeredPlayers">
          <%
        	for(int i=0; i < vectorResultsSQL.size();i++){
				String array[] = vectorResultsSQL.elementAt(i);
        		String user = array[0];	
        		%>
        		<option value="<%=user %>"><%=user %></option>
      	  <%} %>
      </select>
      </form>
 
    <td width="25%">
      <p align="center"><input type="button" width="50px" name="botonJugar" id="botonJugar" value="Añadir Jugador" onclick="registraJugador()">
      </p>
   
    </td>
	<td >
	  <label class="soloCaja" >SALA :</label >
	
    <input  class="Caja" type="text" Id="sala" value="sala1"/p></td>    
    </tr>
  <tr>
    <td class="Cabecera">Usuarios en Juego</td>
    <td  class="Cabecera">Saldo</td>
    <td  class="Cabecera">Opciones</td>
  </tr>

<tr class="fondosLineas" >
    <td class="otro"><label class="AIzquierdas">Diego Perez</label></td>
    <td class="otro"><label class="AIzquierdas">120 €</label></td>
    <td class="otro"><label class="DatosCentrados"><input type="button" name="Quitar" id="Quitar" value="Quitar"></label></td>
         
</tr>
<article id="htmlJugadores">

</article>

 
</table>

</body>
</html>
