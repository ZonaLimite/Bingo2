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


<link href="css/Portal.css" rel="stylesheet" type="text/css">
<script src="javascript/utilDialogos.js"></script>
</head>
<body>
<div float="left">
<div class="content2">

<table id="resultLines" width="97%" border="1" align="center" class="Tabla">
  <tr bgcolor="#003399" id="fila">
    <td ><span class="soloCaja">CAJA BINGO</span>
      <form name="form1" method="post" action="">
        <span class="soloCaja">
        <input  class="Caja" type="text" Id="precioCarton2" value="120 €"/p>
        </span>
      </form>
 
    <td colspan="1">
      <label class="soloCaja" >CARTON<br>
      </label ><input  class="Caja" type="text" Id="precioCarton" value="12 €"/p>
    <br></td>
   <td colspan="2">
      <label class="soloCaja" >TOT.CARTONES<br>
      </label ><input  class="Caja" type="text" Id="precioCarton" value="20 €"/p>
    <br></td> 
  </tr>
  <tr>
    <td width="40%"class="Cabecera">USUARIOS EN JUEGO</td>
    <td width="30%"  class="Cabecera">SALDO</td>
    <td width="15%" class="Cabecera">CARTONES</td>
    <td width="15%"  class="Cabecera">PERFIL</td>
  </tr>
  </table>
</div> 
<div class="content2">

<table id="resultLines2" width="97%" border="1" align="center" class="Tabla">

<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td width="40%" class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td width="30%" class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td  width="15%" class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td width="15%"class="otroCentro">Manual</td>
</tr><tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr><tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr><tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr><tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr><tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr><tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr><tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr><tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr><tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr><tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr><tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>

 
</table>
</div>
</div>
</body>
</html>
