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
	setCookie("username", userName);
	setCookie("numPlayersDesired", numPlayers);
	alert(document.cookie);
	return true; // will allow the get request to process.
	
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}

document.onkeypress = stopReturnKey;

