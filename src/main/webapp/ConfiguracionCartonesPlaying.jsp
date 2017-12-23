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
  <tr bgcolor="#003399" id="fila">
    <td  width="50%" height="72" class="Cabecera" ><p>Asignacion Cartones</p>
      <form name="form1" method="post" action="">
        <select  onChange="valorCombo(this.value)" class="Caja" name="OnLinePlayers" id="OnLinePlayers">
          <%
        	for(int i=0; i < vectorResultsSQL.size();i++){
				String array[] = vectorResultsSQL.elementAt(i);
        		String user = array[0];	
        		%>
        		<option value="<%=user %>"><%=user %></option>
      	  <%} %>
      </select>
      </form>
 
    <td colspan="2"   width="30%"><table width="100%" border="1">
  <tr>
    <td><input onClick="valorCarton('1')" type="button" class="cantidadCartones" value="1"></td>
    <td><input onClick="valorCarton('2')" type="button" class="cantidadCartones" value="2"></td>
    <td><input onClick="valorCarton('3')" type="button" class="cantidadCartones" value="3"></td>
  </tr>
  <tr>
    <td><input onClick="valorCarton('4')" type="button" class="cantidadCartones" value="4" ></td>
    <td><input onClick="valorCarton('5')" type="button" class="cantidadCartones" value="5"></td>
    <td><input onClick="valorCarton('6')" id="valorCarton6" type="button" class="cantidadCartones" value="6"></td>

  </tr>
</table>
</td>
    	
	<td  width="30%">
	  <label class="soloCaja" >Precio Carton<br>
      </label ><input  class="Caja" type="text" Id="precioCarton" value="0 €"/p>
	  <br>
	  <input name="Botón" type="button" class="soloRojo" value="Comprar Todos">
	 </td>    
    </tr>
  <tr>
    <td class="Cabecera">Usuarios en Juego</td>
    <td  class="Cabecera">Saldo</td>
    <td  class="Cabecera">Cartones</td>
    <td  class="Cabecera">Comprar</td>
  </tr>
<----------------------------------------------------------------------->
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">Diego Perez </label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">0<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">
      <input type="button" name="Quitar" id="Quitar" value="Comprar 1">
   </td>
</tr>
<----------------------------------------------------------------------->
 
</table>

</body>
</html>
