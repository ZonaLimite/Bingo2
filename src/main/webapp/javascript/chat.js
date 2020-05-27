/**
 * Video Chat
 */

const configuration = {
  iceServers: [{
    urls: 'stun:stun.l.google.com:19302'
  }]
};

// Necesidades media requeridas
var mediaConstraints = {
		  audio: false, 
		  video: false 
};
var myPeerConnection = null;    // RTCPeerConnection
var callerUsername = null;
var targetUsername = null;
var quienSoy = null;
//Actualizando contrains presentes en este dispostivo
function comprobarConstraits(){
	navigator.mediaDevices.enumerateDevices().then(devices => {
		if (devices.length > 0){
			for(i=0; i < devices.length;i++){
				console.warn("id="+devices[i].deviceId+" clase:"+devices[i].kind+" descripcion:"+devices[i].label);
				if(devices[i].kind=="audioinput")mediaConstraints.audio=true;
				if(devices[i].kind=="videoinput")mediaConstraints.video=true
			}
		}
	});
}
// Empezando una llamada a un user
function invite() {
	  listElem = document.getElementById("userlistbox");
	  n = listElem.selectedIndex;
	  if (n == -1)return;
	  user = listElem.options[n].value;	  
	  if(user=="Llamar a")return;
	  if (myPeerConnection) {
	    alert("You can't start a call because you already have one open!");
	  } else {
		  
		var clickedUsername = user;
	    if (clickedUsername === usuario) {//comprobar ambito de usuario aqui
	      alert("Estoy asustado, no deberias llamarte a ti mismo");
	      return;
	    }
	    quienSoy = "caller";
	    targetUsername = clickedUsername;
	    createPeerConnectionCaller();
		
	    navigator.mediaDevices.getUserMedia(mediaConstraints)
	    .then(function(localStream) {
	      			document.getElementById("local_video").srcObject = localStream;
	      			localStream.getTracks().forEach(track => 
		  			myPeerConnection.addTrack(track, localStream));
	    })
		.catch(handleGetUserMediaError);
	    };
}

// Y por ultimo el Caller tiene reconocimiento de haber sido aceptada
// la invitacion por el cally, al recibir su anwser
function handleVideoAnswerMsg(msg) {


	  // Configure the remote description, which is the SDP payload
	  // in our "video-answer" message.

	  desc = new RTCSessionDescription(msg.sdp);
		  myPeerConnection.setRemoteDescription(desc)
		  .catch(reportError);
	  console.info("*** Call "+msg.name+" has accepted our call");
}

// El manejador que detecta tracks añadidas al PeerConnection
function handleTrackEvent(event) {
	 //Pendiente revisar estos elementos en el html (video y boton colgar)
	  document.getElementById("received_video").srcObject = event.streams[0];
	  show_InMessage("Conexion establecida ...",null);
	  document.getElementById("hangup-button").disabled = false;
}

// Creacion del peer de conexion y manejadores de eventos para el handshake
// Utilizado por Caller y cally
function createPeerConnectionCaller() {
	  myPeerConnection = new RTCPeerConnection(configuration);
	  
	  myPeerConnection.onnegotiationneeded = handleNegotiationNeededEvent;
	  myPeerConnection.onicecandidate = handleICECandidateEventCaller;
	  myPeerConnection.ontrack = handleTrackEvent;
	  
	  myPeerConnection.onremovetrack = handleRemoveTrackEvent;
	  myPeerConnection.oniceconnectionstatechange = handleICEConnectionStateChangeEvent;
	  myPeerConnection.onicegatheringstatechange = handleICEGatheringStateChangeEvent;
	  myPeerConnection.onsignalingstatechange = handleSignalingStateChangeEvent;
	  
}
function createPeerConnectionCally() {
	  myPeerConnection = new RTCPeerConnection(configuration);
	  //myPeerConnection.onnegotiationneeded = handleNegotiationNeededEvent;
	  myPeerConnection.onicecandidate = handleICECandidateEventCally;
	  myPeerConnection.ontrack = handleTrackEvent;
	  myPeerConnection.onremovetrack = handleRemoveTrackEvent;
	  myPeerConnection.oniceconnectionstatechange = handleICEConnectionStateChangeEvent;
	  myPeerConnection.onicegatheringstatechange = handleICEGatheringStateChangeEvent;
	  myPeerConnection.onsignalingstatechange = handleSignalingStateChangeEvent;
	  
}
// Cuando hemos iniciado la llamada (creado peer, obtenido media y
// hecha descripcion local ...
function handleNegotiationNeededEvent() {
	  myPeerConnection.createOffer().then(function(offer) {
	    return myPeerConnection.setLocalDescription(offer);
	  })
	  .then(function() {
		  var msg ={
			      name: usuario,
			      target: targetUsername,
			      type: "video-offer",
			      sdp: myPeerConnection.localDescription
		  };
		  socket_send_JSON(msg);
	  })
	  .catch(reportError);
}

//En el cally se recibe la oferta a traves del socket (signalising) 
//y se llama a esta funcion  ...
function handleVideoOfferMsg(msg) {
	  var localStream = null;

	  callerUsername = msg.name;
	  quienSoy = "cally";
	  createPeerConnectionCally();
	  
	  var desc = new RTCSessionDescription(msg.sdp);

	  myPeerConnection.setRemoteDescription(desc).then(function () {
	    return navigator.mediaDevices.getUserMedia(mediaConstraints);
	  })
	  .then(function(stream) {
	    localStream = stream;
	    document.getElementById("local_video").srcObject = localStream;

	    localStream.getTracks().forEach(track => myPeerConnection.addTrack(track, localStream));
	  })
	  .then(function() {
	    return myPeerConnection.createAnswer();
	  })
	  .then(function(answer) {
	    return myPeerConnection.setLocalDescription(answer);
	  })
	  .then(function() {
	    var msg = {
	      name: usuario,
	      target: callerUsername,
	      type: "video-answer",
	      sdp: myPeerConnection.localDescription
	    };

	    socket_send_JSON(msg);
	  })
	  .catch(handleGetUserMediaError);
}

// La capa del navegador Caller, nos envia candidatos para enviar al cally
function handleICECandidateEventCaller(event) {
	  if (event.candidate) {
		  console.info("Caller "+usuario+" Enviando candidatos al Cally "+ targetUsername)
		  msg = {
			  	  name: usuario,
			      type: "new-ice-candidate",
			      target: targetUsername,
			      candidate: event.candidate
		  }
		  
		  socket_send_JSON(msg);
	  }
}
// La capa del navegador Cally, proporciona candiadtos para enviar al caller
function handleICECandidateEventCally(event) {
	  if (event.candidate) {
		  console.info("Cally "+usuario+ " Enviando candidatos al Caller "+ callerUsername)
		  msg = {
			  	  name: usuario,
			      type: "new-ice-candidate",
			      target: callerUsername,
			      candidate: event.candidate
		  }
		  
		  socket_send_JSON(msg);
	  }
}
// Los mensajes recibidos en el websocket tipo NewICE se tratan aqui
function handleNewICECandidateMsg(msg) {
	  var candidate = new RTCIceCandidate(msg.candidate);
	  console.info("Cally "+usuario+ " regsitrando candidatos del Caller "+ msg.name)
	  myPeerConnection.addIceCandidate(candidate)
	    .catch(error => {
	    	reportError(error);
	    	return;
	    });
}


// Detecta si se desactiva un canal o track
function handleRemoveTrackEvent(event) {
	  var stream = document.getElementById("received_video").srcObject;
	  var trackList = stream.getTracks();
	 
	  if (trackList.length == 0) {
	    closeVideoCall();
	  }
}

// Utilizado para solicitar el fin de la llamada y para avisar al otro peer
function hangUpCall() {
	  var target;
	  if(quienSoy=="caller")target=targetUsername;
	  if(quienSoy=="cally")target=callerUsername;
	  closeVideoCall();
	  msg = {
			    name: usuario,
			    target: target,
			    type: "hang-up"
	  }
	  socket_send_JSON(msg);
}

//Abort connection(liberacion de recursos)
function closeVideoCall() {
	    
	  var remoteVideo = document.getElementById("received_video");
	  var localVideo = document.getElementById("local_video");

	  if (myPeerConnection) {
	    myPeerConnection.ontrack = null;
	    myPeerConnection.onremovetrack = null;
	    myPeerConnection.onremovestream = null;
	    myPeerConnection.onicecandidate = null;
	    myPeerConnection.oniceconnectionstatechange = null;
	    myPeerConnection.onsignalingstatechange = null;
	    myPeerConnection.onicegatheringstatechange = null;
	    myPeerConnection.onnegotiationneeded = null;

	    if (remoteVideo.srcObject) {
	      remoteVideo.srcObject.getTracks().forEach(track => track.stop());
	    }

	    if (localVideo.srcObject) {
	      localVideo.srcObject.getTracks().forEach(track => track.stop());
	    }

	    myPeerConnection.close();
	    myPeerConnection = null;
	  }

	  remoteVideo.removeAttribute("src");
	  remoteVideo.removeAttribute("srcObject");
	  localVideo.removeAttribute("src");
	  remoteVideo.removeAttribute("srcObject");

	  //document.getElementById("hangup-button").disabled = true;
	  targetUsername = null;
	  callerUsername = null;
	  quienSoy = null;
	  console.info("conexion finalizada");
	  show_InMessage("conexion finalizada ...",null);
}

// Manejador de errores del RTCpeerConnection
function handleICEConnectionStateChangeEvent(event) {
	  switch(myPeerConnection.iceConnectionState) {
	    case "closed":
	    case "failed":
	    case "disconnected":
	      closeVideoCall();
	      break;
	  }
}
function handleICEGatheringStateChangeEvent(event) {
	  // Our sample just logs information to console here,
	  // but you can do whatever you need.
}
function handleSignalingStateChangeEvent(event) {
	  switch(myPeerConnection.signalingState) {
	    case "closed":
	      closeVideoCall();
	      break;
	  }
};
function handleGetUserMediaError(e) {
	  switch(e.name) {
	    case "NotFoundError":
	      alert("Unable to open your call because no camera and/or microphone" +
	            "were found.");
	      break;
	    case "SecurityError":
	    case "PermissionDeniedError":
	      // Do nothing; this is the same as the user canceling the call.
	      break;
	    default:
	      alert("Error opening your camera and/or microphone: " + e.message);
	      break;
	  }

	  closeVideoCall();
}
function reportError(errMessage) {
	  console.error(`Error ${errMessage.name}: ${errMessage.message}`);
}
