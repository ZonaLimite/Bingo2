<%@ page language="java" 
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="servlet.UtilDatabase" %>
<%! HttpSession mySession = null;%>
<%! String vCaja = "0";%>
<%
		UtilDatabase udatabase = new UtilDatabase();
		vCaja = udatabase.consultaSQLUnica("Select SaldoCaja From caja");

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

<table id="resultLines" width="97%" border="1" align="center" class="Tabla" >
  <tr bgcolor="#003399">
    <td  width="50%" height="72" class="Cabecera" >Peticiones compra Bonus Pendientes</td>
    <td width="25%">
      <p align="center"><input type="button" width="30px" name="Mostrar Peticiones" value="Mostrar Peticiones" onclick="volcarPeticionesBonus()"></p>
    </td>
	<td >
	  <label class="soloCaja" >CAJA :</label >
	
    <input  class="Caja" type="text" value="<%=vCaja %>"></td>    
    </tr>
  <tr>
    <td class="Cabecera">Usuario</td>
    <td  class="Cabecera">Bonus</td>
    <td  class="Cabecera">Checking</td>
  </tr>
 
</table>

</body >

</html>
