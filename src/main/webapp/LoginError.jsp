<%@ page language="java" 
    contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"
%>
<%@ page import="javax.servlet.http.HttpSession" %>

<%! HttpSession mySession = null;%>

<%! String user = null;%>
<%! String reasonError = "";%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="vieport" content="width=800, initial-scale=1, orientation=landscape">
<title>Login No permitido</title>
</head>
<%
	mySession = request.getSession(false);
	if(mySession!=null){
		reasonError = (String)mySession.getAttribute("Reason");
	}
%>
<body class="pagina" id="content">
<div >
	<h1><%=this.reasonError %>
	</h1>
</div>
</body>