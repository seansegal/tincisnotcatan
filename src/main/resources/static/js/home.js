
// ---------- Setup ---------- //

//Establish the WebSocket connection and set up event handlers
if (document.location.hostname == "localhost") {
	// use http
	webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/groups/");
} else {
	// we're on heroku - use https:
	webSocket = new WebSocket("wss://" + location.hostname + ":" + location.port + "/groups/");
}

function heartbeat() {
	var beat = "HEARTBEAT";
	webSocket.send(JSON.stringify(beat));
}

webSocket.onopen = function () {
	window.setInterval(heartbeat, 60 * 1000);
};

webSocket.onmessage = function (msg) {
	console.log(msg.data);
}


function displayCookies() {
	alert (document.cookie);
}
function getCookie(name) {
	var nameEQ = name + "=";
	//alert(document.cookie);
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
	var c = ca[i];
	while (c.charAt(0)==' ') c = c.substring(1);
	if (c.indexOf(nameEQ) != -1){
		return c.substring(nameEQ.length,c.length);
		}
	}
	return null;
}
function setCookie(cookie, value) {
	var eqVal = cookie + "=" + value;
	document.cookie = eqVal;
}

function stopReturnKey(evt) { 
	  var evt = (evt) ? evt : ((event) ? event : null); 
	  var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
	  if ((evt.keyCode == 13) && (evt.target.id != "startGameButton"))  {return false;} 
}

function startGamePressed() {
	var userName = id("nameEntry").value;
	var numPlayers = id("numPlayersDesired").value;
	if(userName == undefined || userName == "") {
		alert("Please select a username");
		return false; // will not allow the get reqeust to process.
	}
	setCookie("userName", userName);
	setCookie("numPlayersDesired", numPlayers);
	deleteCookie("USER_ID");
	return true; // will allow the get request to process.
	
}

function deleteCookie(name) {
    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
};

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}

document.onkeypress = stopReturnKey;

