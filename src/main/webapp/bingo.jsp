<%@ page language="java" 
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="servlet.UserBean" %>
<%! HttpSession mySession = null;%>
<%! UserBean myUser = null;%>
<%! String user,sala = null;%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="vieport" content="width=device-width, initial-scale=1.0">
<title>Bingo 2016</title>
 <link href="css/estilosDialogos.css" rel="stylesheet" type="text/css">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
  
  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
  
<script src="javascript/reproductor.js"></script>

<link href="css/Bingo.css" rel="stylesheet" type="text/css">
</head>

<body class="pagina" id="content">
<%
	mySession = request.getSession(false);
	if(mySession!=null){
		//myUser = (UserBean)mySession.getAttribute("userBean"); 
		user = (String) mySession.getAttribute("usuario"); 
		sala = (String) mySession.getAttribute("sala"); 
	}
	
%>
<div id="agrupar">
<header id="cabecera">

<table width="100%" border="1" cellspacing="2" class="ano">
  <tr>
    <td padding=0 width="12%"><img id="logo" src="images/IconoBola.jpg" width="56" height="45" longdesc="file:///C|/Users/boga/git/wildfly/src/main/webapp/images/IconoBola.jpg" ><label id="sala"><%=sala%></label><%=" "+user%></td>
    <td width="68%" class="ano" >! BIENVENIDOS BINGO 2017 ¡<nav id="menu">
    <label id="iniciar" CLASS="menu_li" >INICIAR</label>
    <label id="resume" CLASS="menu_li" >RESUMIR</label>
	<label id="lab_cartones" CLASS="menu_li" >CARTONES</label>
	<label id="finalizar" CLASS="menu_li" >FINALIZAR</label>
	<label id="preferencias" CLASS="menu_li" >OPCIONES</label>	
	</nav>
	</td>
    <td id="CajaDcha" width="20%">2016</td>
  </tr>
   
</table>
</header>

<table class="tablero">
    <tr>
         <td class="panel"> 
            <label class="numeros" ></label>             
        </td>
      <td class="panel"> 
            <label id="10" class="numeros" >10</label>             
        </td>
      <td class="panel"> 
            <label id="20" class="numeros" >20</label>             
        </td>
      <td class="panel"> 
            <label id="30" class="numeros" >30</label>             
        </td>
      <td class="panel"> 
            <label id="40" class="numeros" >40</label>             
        </td>
      <td rowspan="10" class="panel" > 
            <article class="Comander" >
             <span >
			 <div id="my_poster">
             

              <video id="medio" class="fotom" preload="auto" >
   		    <source src="http://boga.esy.es/video/BingoInes.mov" type="video/mp4">
   		    <source src="http://boga.esy.es/video/BingoInes.ogv" type="video/ogg">
			<source src="http://boga.esy.es/video/BingoInes.webm" type="video/webm" >
   	         HTML5 video is not supported by this browser 
   	         </video>
   	         </div>
            </span>
              <table   align="center" class="tablaInfo">
     		<tr>
	        <td width="26%" class="celdaInfo"><span class="Comander"><span class="Comander">BOLA N:</span></span></td>
	        <td width="4%" id="datoOrdenBola" class="celdaInfo"  ><span class="Comander"><span class="Comander">
	          <label id="labelOrden" class="valorInfo">0</label>
	        </span></span></td>
	        <td width="37%" id="datoLinea" class="celdaInfo" ><label id="labelLinea">LINEA:</label></td>
	        <td width="33%" class="celdaInfo"><span class="Comander"><span class="Comander">
	          <label id="valorLinea" class="valorInfo">0 €</label>
	        </span></span></td>
	        </tr>
	      <tr>
	        <td class="celdaInfo"><span class="Comander"><span class="Comander"> CARTONES</span></span></td>
	        <td id="datoCartones" class="celdaInfo" ><span class="Comander"><span class="Comander">
	          <label id="valorCartones" class="valorInfo">0</label>
	        </span></span></td>
	        <td class="celdaInfo"><label id="labelBingo">BINGO:</label></td>
	        <td  id="datoBingo" class="celdaInfo" ><span class="Comander"><span class="Comander">
	          <label id="valorBingo" class="valorInfo">0 €</label>
	        </span></span></td>
    </tr>
	      <tr>
	        <td colspan="4"  class="celdaInfo" id="comboTexto" >
	          
	          <label  id="labelTexto" class="classMessage" >combotexto</label>
	         
	        
    	</tr>
</table>
<div class="flotador">
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
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
    <td class="otro"><label class="AIzquierdas">lOLA aRRANZ</label></td>
    <td class="otro"><label class="AIzquierdas">
    120 €</label></td>
    <td class="otroCentro">10<span class="AIzquierdas">
      
    </span></td>
    <td class="otroCentro">Manual</td>
</tr>
<tr onclick="valorCombo('elnombre')" class="fondosLineas" id="fila">
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
	
	          <span class="Comander">
	          <canvas id="canvas_bola" class="canvasBola">
	         
              </canvas>
	
       

        </span>
</article>      
             
      </td>
      <td class="panel">
      	<label id="50" class="numeros" >50</label>
      </td >
      <td class="panel">
            <label id="60" class="numeros" >60</label>              
     </td >

        <td class="panel">
            <label id="70" class="numeros" >70</label>              
        </td >
        <td class="panel">
            <label id="80" class="numeros" >80</label>              
        </td >

        <td class="panel">
            <label id="90" class="numeros" >90</label>              
        </td >

  </tr>
    <tr>
       <td class="panel">
         <label id="1" class="numeros" >01</label>
       </td>
               <td class="panel"> 
            <label id="11" class="numeros" >11</label>             
        </td>
      <td class="panel"> 
            <label id="21" class="numeros" >21</label>             
        </td>
      <td class="panel"> 
            <label id="31" class="numeros" >31</label>             
        </td>
      <td class="panel"> 
            <label id="41" class="numeros" >41</label>             
        </td>
      <td class="panel"> 
            <label id="51" class="numeros" >51</label>             
        </td>
      <td class="panel"> 
            <label id="61" class="numeros" >61</label>             
        </td>
    
        <td class="panel">
            <label id="71" class="numeros" >71</label>              
        </td >
        <td class="panel">
            <label id="81" class="numeros" >81</label>              
        </td >
		<td class="panel">
            <label class="numeros" ></label>              
        </td >
  </tr>

  <tr>
         <td class="panel"><label id="2" class="numeros" >02</label></td>
      <td class="panel"> 
            <label id="12" class="numeros" >12</label>             
        </td>
      <td class="panel"> 
            <label id="22" class="numeros" >22</label>             
        </td>
      <td class="panel"> 
            <label id="32" class="numeros" >32</label>             
        </td>
      <td class="panel"> 
            <label id="42" class="numeros" >42</label>             
        </td>
      <td class="panel"> 
            <label id="52" class="numeros" >52</label>             
        </td>
      <td class="panel"> 
            <label id="62" class="numeros" >62</label>             
        </td>
    
        <td class="panel">
            <label id="72" class="numeros" >72</label>              
        </td >
        <td class="panel">
            <label id="82" class="numeros" >82</label>              
        </td >
<td class="panel">
            <label class="numeros" ></label>              
        </td >
  </tr>
  <tr>
         <td class="panel"> 
            <label id="3" class="numeros" >03</label>             
        </td>
      <td class="panel"> 
            <label id="13" class="numeros" >13</label>             
        </td>
      <td class="panel"> 
            <label id="23" class="numeros" >23</label>             
        </td>
      <td class="panel"> 
            <label id="33" class="numeros" >33</label>             
        </td>
      <td class="panel"> 
            <label id="43" class="numeros" >43</label>             
        </td>
      
      <td class="panel"> 
            <label id="53" class="numeros" >53</label>             
        </td>
      <td class="panel"> 
            <label id="63" class="numeros" >63</label>             
        </td>
    
        <td class="panel">
            <label id="73" class="numeros" >73</label>              
        </td >
        <td class="panel">
            <label id="83" class="numeros" >83</label>              
        </td >
<td class="panel">
            <label class="numeros" ></label>              
        </td >
  </tr>
  <tr>
         <td class="panel"> 
            <label id="4" class="numeros" >04</label>             
        </td>
      <td class="panel"> 
            <label id="14" class="numeros" >14</label>             
      </td>
      <td class="panel"> 
            <label id="24" class="numeros" >24</label>             
      </td>
      <td class="panel"> 
            <label id="34" class="numeros" >34</label>             
      </td>
      <td class="panel"> 
            <label id="44" class="numeros" >44</label>             
      </td>
      
      <td class="panel"> 
            <label id="54" class="numeros" >54</label>             
      </td>
    
        <td class="panel">
            <label id="64" class="numeros" >64</label>              
        </td >
        <td class="panel">
            <label id="74" class="numeros" >74</label>              
        </td >
	<td class="panel">
            <label id="84" class="numeros" >84</label>              
      </td >
        <td class="panel">
            <label class="numeros" ></label>              
        </td >
  </tr>
  <tr>
         <td class="panel"><label id="5" class="numeros" >05</label></td>
      <td class="panel"> 
            <label id="15" class="numeros" >15</label>             
        </td>
      <td class="panel"> 
            <label id="25" class="numeros" >25</label>             
        </td>
      <td class="panel"> 
            <label id="35" class="numeros" >35</label>             
        </td>
      <td class="panel"> 
            <label id="45" class="numeros" >45</label>             
        </td>
      
      <td class="panel"> 
            <label id="55" class="numeros" >55</label>             
        </td>
    
        <td class="panel">
            <label id="65" class="numeros" >65</label>              
        </td >
        <td class="panel">
            <label id="75" class="numeros" >75</label>              
        </td >
		<td class="panel">
            <label id="85" class="numeros" >85</label>              
        </td >
        <td class="panel">
            <label class="numeros" ></label>              
        </td >
  </tr>
  <tr>
         <td class="panel"> 
            <label id="6" class="numeros" >06</label>
            </td>
      <td class="panel"> 
            <label id="16" class="numeros" >16</label>             
        </td>
      <td class="panel"> 
            <label id="26" class="numeros" >26</label>             
        </td>
      <td class="panel"> 
            <label id="36" class="numeros" >36</label>             
        </td>
      <td class="panel"> 
            <label id="46" class="numeros" >46</label>             
        </td>
      
      <td class="panel"> 
            <label id="56" class="numeros" >56</label>             
        </td>
    
        <td class="panel">
            <label id="66" class="numeros" >66</label>              
        </td >
        <td class="panel">
            <label id="76" class="numeros" >76</label>              
        </td >
		<td class="panel">
            <label id="86" class="numeros" >86</label>              
        </td >
        <td class="panel">
            <label class="numeros" ></label>              
        </td >
  </tr>
  <tr>
         <td class="panel"> 
            <label id="7" class="numeros" >07</label>             
        </td>
      <td class="panel"> 
            <label id="17" class="numeros" >17</label>             
        </td>
      <td class="panel"> 
            <label id="27" class="numeros" >27</label>             
        </td>
      <td class="panel"> 
            <label id="37" class="numeros" >37</label>             
        </td>
      <td class="panel"> 
            <label id="47" class="numeros" >47</label>             
        </td>
      
      	<td class="panel"> 
            <label id="57" class="numeros" >57</label>             
        </td>
    
        <td class="panel">
            <label id="67" class="numeros" >67</label>              
        </td >
        <td class="panel">
            <label id="77" class="numeros" >77</label>              
        </td >
		<td class="panel">
            <label id="87" class="numeros" >87</label>              
        </td >
        <td class="panel">
            <label class="numeros" ></label>              
        </td >
  </tr>
  <tr>
         <td class="panel"> 
            <label id="8" class="numeros" >08</label>             
        </td>
      <td class="panel"> 
            <label id="18" class="numeros" >18</label>             
        </td>
      <td class="panel"> 
            <label id="28" class="numeros" >28</label>             
        </td>
      <td class="panel"> 
            <label id="38" class="numeros" >38</label>             
        </td>
      <td class="panel"> 
            <label id="48" class="numeros" >48</label>             
        </td>
      
      <td class="panel"> 
            <label id="58" class="numeros" >58</label>             
        </td>
    
        <td class="panel">
            <label id="68" class="numeros" >68</label>              
        </td >
        <td class="panel">
            <label id="78" class="numeros" >78</label>              
        </td >
		<td class="panel">
            <label id="88" class="numeros" >88</label>              
        </td >
        <td class="panel">
            <label class="numeros" ></label>              
        </td >
  </tr>
  <tr>
         <td class="panel"> 
            <label id="9" class="numeros" >09</label>             
        </td>
      <td class="panel"> 
            <label id="19" class="numeros" >19</label>             
        </td>
      <td class="panel"> 
            <label id="29" class="numeros" >29</label>             
        </td>
      <td class="panel"> 
            <label id="39" class="numeros" >39</label>             
        </td>
      <td class="panel"> 
            <label id="49" class="numeros" >49</label>             
        </td>
      
      <td class="panel">
          <label id="59" class="numeros" >59</label>              
        </td >
        <td class="panel">
            <label id="69" class="numeros" >69</label>              
        </td >
		<td class="panel">
            <label id="79" class="numeros" >79</label>              
        </td >
        <td class="panel">
            <label id="89" class="numeros" >89</label>              
        </td >
        <td class="panel">
            <label class="numeros" ></label>              
        </td >
  </tr>
</table>

 


<article id="articulo">	
<div align="center">  
 
<label id="play" CLASS="menu_li2" >PLAY</label>
<label id="pause" CLASS="menu_li2" >PAUSE</label>
<label id="boton_Linea" CLASS="menu_li2" >LINEA</label>
<label id="boton_Bingo" CLASS="menu_li2" >BINGO</label>
<label id="boton_LineaOk" CLASS="menu_li2" >LINEA OK</label>
<label id="boton_BingoOk" CLASS="menu_li2" >BINGO OK</label>
<label id="boton_Continuar" CLASS="menu_li2" >CONTINUAR</label>
</div>  
</article>
</div>
<article>

<p><label id="foot"> BINGO 2016</label>></p>    



<footer id="footer" class="footerClass">
  <div class="Comander2">
<input type="submit" name="comando" id="comando" value="Enviar">
    <input type="text" name="text_comando" id="text_comando">
    <input type="button" id="notify">
      
      
	  <input type="text" id="seek_ini" value="00:00" size="20">
	  
      
	  <input type="text" id="seek_fin" value="00:00">
	  
	  
	  <input type="text" id="contador" value="00:00">
	  
	  
	    <input id="c_Range" value="Play Range" type="button">
  </div>   
  




<div id="opciones" title="Preferencias">

ELEGIR CANTAOR
    <select name="cantaor">
      <option value="0">Seleccione un cantaor</option>
      <!--<option value="Lola">Lola (velocida Lenta)</option>-->
      <option value="Ines">Ines (velocida Rapida)
      <option>
    </select>

  <p>&nbsp;
</p>
<form > 
  <table width="200">
    <tr>
      <td><label>
        <input type="radio" name="Codecs" value="MP4" id="Codecs_0">
        MP4</label>      </td>
    </tr>
    <tr>
      <td><label>
        <input type="radio" name="Codecs" value="OGG" id="Codecs_1">
        OGG</label>      </td>
    </tr>
    <tr>
      <td><label>
        <input type="radio" name="Codecs" value="WEBM" id="Codecs_2"  checked>
        WebM</label>      </td>
    </tr>
  </table>
</form>
<p>
<input name="videoON" type="checkbox" value="true" checked>
  Activar Video  </p>
<input name="starsON" type="checkbox" value="true" checked>
Activar StartsField
</p>

<hr width="75%"/>
<label>Tamano Maximo numeros</label>

<div id="slider"></div>
<p>
<input type="text" id="maxTamano">
<input type="button" id="aplicarSize" value="Aplicar Tamaño">
<hr width="75%"/>
<input type="text" id="delay" value="0">
<input type="button" id="aplicarDelay" value="Aplicar Delay">
</div>

<div id="cartones" title="Configuracion cartones"> 
<form>
<label >Precio Carton(€):</label>
  <input type="text" id="PrecioCarton">
<br>
<label >N: Cartones en Juego:</label>
<input type="text"  id="NCartonesJuego">
<br>
<label >N: Cartones Manuales:</label>
<input type="text" id="NCartonesManuales">
<br>
<label >Asignacion % Premio Linea:</label>
<input type="text" id="porcientoLinea">
<br>
<label >Asignacion % Premio Bingo:</label>
<input type="text" id="porcientoBingo">
<br>
<label >Asignacion % Cantaor     :</label>
<input type="text" id="porcientoCantaor">
</form>
</div>

<div id="premiosForm" title="- Comprobacion cartones -">
<form id="requestPremios">
<label>Numero de carton premiado?</label>
<br>
<input id="spinner_milenas" type="text" value="0" name="nSpin0" style=" width : 27px;">
<input id="spinner_centenas" type="text" value="0" name="nSpin1" style=" width : 27px;">
<input id="spinner_decenas" type="text" value="0" name="nSpin2" style=" width : 27px;">
<input id="spinner_unidades" type="text"  value="1" name="nSpin3" style=" width : 27px;">
<input type="hidden" id="nRef" name="nRef" value="0">
<input type="hidden" id="sala" name="sala" value="<%out.print(sala); %>">
<input type="hidden" id="usuario"  name="usuario" value="<%out.print(user); %>">
<br>
</form>
</div>
<audio id="audioWeb">
  	<source src="http://boga.esy.es/audio/AudioLinea1.mp3" type="audio/mpeg">
	Your browser does not support the audio element.
</audio>
</footer>
</body>

</html>
