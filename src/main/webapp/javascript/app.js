var OV;
var session;

var sessionName;	// Name of the video session the user will connect to
var token;			// Token retrieved from OpenVidu Server
var nickName       // nombre de usuario client

/* OPENVIDU METHODS */

function joinSession(usuario,roomName) {
	//nickName esla variable de usuario del script original
	// aqui vamos a entrar con un usuario a traves de gestorsessions.
	nickName = usuario;
	sessionName=roomName;
	//var nickName = usuario;
	var userName = "user:"+nickName;
	//var userName = $("#user").val();
	getToken((token) => {

		// --- 1) Get an OpenVidu object ---

		OV = new OpenVidu();

		// --- 2) Init a session ---

		session = OV.initSession();

		// --- 3) Specify the actions when events take place in the session ---

		// On every new Stream received...
		session.on('streamCreated', (event) => {

			// Subscribe to the Stream to receive it
			// HTML video will be appended to element with 'video-container' id
			//var subscriber = session.subscribe(event.stream, 'video-container');
			//var subscriber = session.subscribe(event.stream, targetElement, {insertMode: 'APPEND'});

			//rutina de busqueda elemento video libre disponible para subscripcion
			elementTarget = buscarElementoVideoLibre();
		    //var subscriber = session.subscribe(event.stream,"1F1C1" ,{insertMode: 'REPLACE'});
			if(elementTarget==undefined){
				return;
			}
		    var subscriber = session.subscribe(event.stream, undefined);
		    subscriber.addVideoElement(elementTarget);
		    
		   // ES POSBLE QUE ESTO NO SE EJECUTE;POR UTILIZAR LA DEFINICION DIreCTA
				// When the HTML video has been appended to DOM...
				subscriber.on('videoElementCreated', (event) => {
				console.warn("el video elemento creado es :"+event.element);
				// Add a new HTML element for the user's name and nickname over its video
				//appendUserData(event.element, subscriber.stream.connection);
			});
		});

		// On every Stream destroyed...
		session.on('streamDestroyed', (event) => {
			//en el event  tenemos el stream. y en el Stream  ya traves de su streamManager 
			//,los videos que estan displayando el Stream con lo que ya podemos hacer lo que 
			// sea con lso videos dependientes.
			idVideoAfectado = event.stream.streamManager.videos[0].id;
			indexFinded = myArrayCeldasOcupadas.lastIndexOf(idVideoAfectado);
			myArrayCeldasOcupadas.splice(indexFinded,1);
			// tambien podemos utilizar la conexion para extraer la informacion del
			// usuario que ha dejado ede emitir
			datosDeConnection = event.stream.connection.data;
			
			//removeUserData(event.stream.connection);
		});

		// --- 4) Connect  to the session passing the retrieved token and some more data from
		//        the client (in this case a JSON with the nickname chosen by the user) ---

		
		session.connect(token, { clientData: nickName })
			.then(() => {

				// --- 5) Set page layout for active call ---

				
				$('#session-title').text(sessionName);
				var divNormal = document.getElementById('divNormal');
				divNormal.style.display = 'none';
				
				var divMain = document.getElementById('main-video');
				divMain.style.display = 'inherit';
				$('#session').show();
				show_InMessage("Videoconferencia activa ...",null)


					// --- 6) Test available devices ---
				    // Solo conectamos si hay dispositivos
					var audiosource=false;
					var videosource=false;
					OV.getDevices().then(devices => {
						if (devices.length > 0){
							for(i=0; i < devices.length;i++){
								console.warn("id="+devices[i].deviceId+" clase:"+devices[i].kind+" descripcion:"+devices[i].label);
								if(devices[i].kind=="audioinput")audiosource=undefined;
								if(devices[i].kind=="videoinput")videosource=undefined;
							}
						}
					    
						var publisher = OV.initPublisher('video-container', {
							audioSource: audiosource, // The source of audio. If undefined default microphone
							videoSource: videosource, // The source of video. If undefined default webcam
							publishAudio: true,  	// Whether you want to start publishing with your audio unmuted or not
							publishVideo: true,  	// Whether you want to start publishing with your video enabled or not
							resolution: '80x60',  // The resolution of your video
							frameRate: 30,			// The frame rate of your video
							insertMode: 'APPEND',	// How the video is inserted in the target element 'video-container'
							mirror: false       	// Whether to mirror your local video or not
						});

						// --- 7) Specify the actions when events take place in our publisher ---

						// When our HTML video has been added to DOM...
						publisher.on('videoElementCreated', (event) => {
						// Init the main video with ours and append our data
						var userData = {
							nickName: nickName,
							userName: userName
						};
						initLocalVideo(event.element, userData);
						//appendUserData(event.element, userData);
						$(event.element).prop('muted', true); // Mute local video
					});


					// --- 8) Publish your stream ---

					session.publish(publisher);
				});

			})
			.catch(error => {
				console.warn('There was an error connecting to the session:', error.code, error.message);
			});
	});

	return false;
}

function leaveSession() {

	// --- 9) Leave the session by calling 'disconnect' method over the Session object ---
	if(session==undefined)return;
	session.disconnect();
	session = null;

	// Removing all HTML elements with the user's nicknames
	cleanSessionView();
	
	//$('#join').show();
	var divNormal = document.getElementById('divNormal');
	divNormal.style.display = 'inherit';
	
	var divMain = document.getElementById('main-video');
	divMain.style.display = 'none';	
	//$('#session').hide();
	show_InMessage("Videoconferencia Off ...",null)
}

/* OPENVIDU METHODS */



/* APPLICATION REST METHODS */

function logIn() {
	var user = $("#user").val(); // Username
	var pass = $("#pass").val(); // Password

			$("#name-user").text(user);
			$("#not-logged").hide();
			$("#logged").show();
			// Random nickName and session
			$("#sessionName").val("Session " + Math.floor(Math.random() * 10));
			$("#nickName").val("Participant " + Math.floor(Math.random() * 100));
		

}

function logOut() {
	httpPostRequest(
		'api-login/logout',
		{},
		'Logout WRONG',
		(response) => {
			$("#not-logged").show();
			$("#logged").hide();
		}
	);
}

function getToken(callback) {
	//sessionName = $("#sessionName").val(); // Video-call chosen by the user
    //user= $("#nickName").val();
	httpPostRequest(
		'./rest-api/openvidu/get-token',
		{sessionName: sessionName, usuario: nickName},
		'Request of TOKEN gone WRONG:',
		(response) => {
			token = response; // Get token from response
			console.warn('Request of TOKEN gone WELL (TOKEN:' + token + ')');
			callback(token); // Continue the join operation
		}
	);
}

function removeUser() {
	if(token==undefined | session==null )return;
	httpPostRequest(
		'./rest-api/openvidu/remove-user',
		{sessionName: sessionName, token: token},
		'User couldn\'t be removed from session', 
		(response) => {
			console.warn("You have been removed from session " + sessionName);
		}
	);
}

function httpPostRequest(url, body, errorMsg, callback) {
	var http = new XMLHttpRequest();
	http.open('POST', url, true);
	http.setRequestHeader('Content-type', 'application/json');
	http.addEventListener('readystatechange', processRequest, false);
	http.send(JSON.stringify(body));

	function processRequest() {
		if (http.readyState == 4) {
			if (http.status == 200) {
				try {
					callback(http.responseText);
				} catch (e) {
					callback();
				}
			} else {
				console.warn(errorMsg);
				console.warn(http.responseText);
			}
		}
	}
}
function httpPostRequestPromise(url, body, errorMsg, callback){

		$.ajax({
			type: "POST",
			url: url,
			data: JSON.stringify(body),
			headers: {
				"Content-Type": "application/json"
			},
			success: response => callback(response),
			error: error => callback(errorMsg)
		});

}
/* APPLICATION REST METHODS */



/* APPLICATION BROWSER METHODS */

window.onbeforeunload = () => { // Gracefully leave session
	if (session) {
		removeUser();
		leaveSession();
	}
}

function appendUserData(videoElement, connection) {
	var clientData;
	var serverData;
	var nodeId;
	if (connection.nickName) { // Appending local video data
		clientData = connection.nickName;
		serverData = connection.userName;
		nodeId = 'main-videodata';
	} else {
		clientData = JSON.parse(connection.data.split('%/%')[0]).clientData;
		serverData = JSON.parse(connection.data.split('%/%')[1]).serverData;
		nodeId = connection.connectionId;
	}
	var dataNode = document.createElement('div');
	dataNode.className = "data-node";
	dataNode.id = "data-" + nodeId;
	dataNode.innerHTML = "<p class='nickName'>" + clientData + "</p>";
	videoElement.parentNode.insertBefore(dataNode, videoElement.nextSibling);
	addClickListener(videoElement, clientData, serverData);
}

function removeUserData(connection) {
	var userNameRemoved = $("#data-" + connection.connectionId);
	if ($(userNameRemoved).find('p.userName').html() === $('#main-video p.userName').html()) {
		cleanMainVideo(); // The participant focused in the main video has left
	}
	$("#data-" + connection.connectionId).remove();
}

function removeAllUserData() {
	$(".data-node").remove();
}

function cleanMainVideo() {
	$('#main-video video').get(0).srcObject = null;
	$('#main-video p').each(function () {
		$(this).html('');
	});
}

function addClickListener(videoElement, clientData, serverData) {
	videoElement.addEventListener('click', function () {
		var secondvideo = $('#second-video video').get(0);
		if (secondvideo.srcObject !== videoElement.srcObject) {
			$('#second-video').fadeOut("fast", () => {
				$('#second-video p.nickName').html(clientData);
				
				secondvideo.srcObject = videoElement.srcObject;
				$('#second-video').fadeIn("fast");
			});
		}
	});
}

function initMainVideo(videoElement, userData) {
	$('#main-video video').get(0).srcObject = videoElement.srcObject;
	$('#main-video p.nickName').html(userData.nickName);

	$('#main-video video').prop('muted', true);
}

function initLocalVideo(videoElement, userData) {
	$('#main-video video').get(0).srcObject = videoElement.srcObject;
	$('#main-video p.nickName').html(userData.nickName);

	$('#main-video video').prop('muted', true);
}

function initMainVideoThumbnail() {
	$('#main-video video').css("background", "url('images/subscriber-msg.jpg') round");
}

function isPublisher(userName) {
	return userName.includes('publisher');
}

function cleanSessionView() {
	removeAllUserData();
	cleanMainVideo();
	$('#main-video video').css("background", "");
	myArrayCeldasOcupadas = new Array();
}

/* APPLICATION BROWSER METHODS */