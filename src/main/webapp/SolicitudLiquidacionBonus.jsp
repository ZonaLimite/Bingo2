<%@ page language="java" 
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="servlet.UtilDatabase" %>
<%@ page import="java.util.Vector" %>
<%! HttpSession mySession = null;%>
<%! String user = null;%>
<%! String vCaja = null;%>
<%! Vector<String[]> vectorResultsSQL = null;%>
<%
		UtilDatabase udatabase = new UtilDatabase();
		mySession = request.getSession(false);
		if(mySession!=null){
			user = (String)mySession.getAttribute("usuario");
		}else{
			return;
		}
        //arrayCampos[1] =1.User
		vectorResultsSQL = udatabase.dameVectorResultsSQL("Select User From usuarios Order by idUsuarios", 1);
		
		String vSaldoUser = udatabase.consultaSQLUnica("Select Saldo From usuarios Where User ='"+user+"'");

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>

<link href="css/estilosDialogos.css" rel="stylesheet" type="text/css">
<script src="//code.jquery.com/jquery-1.12.4.js"></script>
  <script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script src="javascript/utilDialogos.js"></script>

</head>
<body class="cuerpo">
	
		<form>
		<table class="TablaLogin" width="90%" border="1" align="center">
		  <tr>
			<td colspan="2" class="rotulo">Solicitud Liquidacion Bonus Bingo Home</td>
          </tr>
		  <tr>
		    <td width="50%" class="TextLogin">
       
                    <select onchange="consultaSaldo()"class="Caja" name="registeredPlayers" id="registeredPlayers">
                      <%if(user.equals("super")){
			         
        				for(int i=0; i < vectorResultsSQL.size();i++){
						String array[] = vectorResultsSQL.elementAt(i);
        				String userReg = array[0];	
        				%>
        				<option value="<%=userReg %>"><%=userReg %></option>
      	 			 	<%}
					  }else{%>
                      	<option value="<%=user %>" selected><%=user %></option>
                      <%}%>
      </select>
	        <td width="50%"><input name="nBonus"  type="text" class="Caja" id="nBonus" size="20" maxlength="5" value="<%=vSaldoUser %>">	        	&euro;
            <br>
	       	<label > Saldo Actual:</label>
	        <label id="sSaldo"> <%=vSaldoUser %> &euro;</label></td>
            
	      </tr>
          <tr>
          <td colspan="2" class="botonSubmit"><input type="button" value="Solicitar Liquidacion" onClick="solicitarLiquidacionBonus()"></td>
          </tr>
          <tr>
            <td colspan="2" class="botonSubmit" id="result">&nbsp;</td>
          </tr>
          <tr>
            <td height="92" colspan="2" class="TextoArea" id="infoArea" ><p>Puede recuperar su Saldo a traves de este Formulario. El gestor de sala atendera sus peticiones y proporcionara su saldo solicitado. En ese momento recibira un Email de confirmacion.</p>
            <p>! Suerte !</p></td>
          </tr>
        </table>
		<p>
		 
		</p>
</form>	
	
</body>
</html>
