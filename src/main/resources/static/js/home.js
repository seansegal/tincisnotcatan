$(window).load(function() {
	var href = window.location.pathname;
	if (href == "/home") {
		deleteCookie("desiredGroupId");
	}


	// If on mobile, render mobile currently not supported:
	isMobile = function() {
		var check = false;
		(function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))) check = true;})(navigator.userAgent||navigator.vendor||window.opera);
		return check;
	};

	if(isMobile()){
		$("#use-desktop-modal").modal("show");
	}

	// If not in chrome, display useful modal
	var isChrome = !!window.chrome;
	if (!isChrome && !isMobile()) {
		$("#use-chrome-modal").modal("show");
	}
});

// ---------- Setup ---------- //

// Establish the WebSocket connection and set up event handlers
if (document.location.hostname == "localhost") {
	// use http
	webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port
			+ "/groups/");
} else {
	// we're on heroku - use https:
	webSocket = new WebSocket("wss://" + location.hostname + ":"
			+ location.port + "/groups/");
}

// Send a heartbeat on the websocket
function heartbeat() {
	var beat = "HEARTBEAT";
	webSocket.send(JSON.stringify(beat));
}

// Start heartbeats when the websocket opens
webSocket.onopen = function() {
	window.setInterval(heartbeat, 10 * 1000);
};

// Handle message from the websocket
webSocket.onmessage = function(msg) {
	var data = JSON.parse(msg.data);
	console.log(data);
	if (data == "HEARTBEAT") {
		return;
	}

	$("#startGameButton").prop("disabled", data.atLimit);
	if (data.atLimit) {
		$("#startGameButton").text("Sorry, Game Limit Reached");
	} else {
		$("#startGameButton").text("Start Game!");
	}

	if (data.hasOwnProperty("groups")) {
		createJoinableGameList(data.groups);
	}
}

/*
 * Displays all available groups to join.
 * @param groups - the current groups to join
 */
function createJoinableGameList(groups) {
	$("#games-list").empty();

	// Add message if no groups exist yet
	if (groups.length === 0) {
		$("#games-list")
				.append(
						"<li class='list-group-item'>No available games. Create your own!</li>");
		return;
	}

	// Add to list of joinable games
	for (var i = 0; i < groups.length; i++) {
		var group = groups[i].group;
		$("#games-list")
				.append(
						"<li class='list-group-item'><div class='row'>"
								+ "<div class='col-xs-4 text-left vertical-center'><span>"
								+ group.groupName
								+ ":</span></div>"
								+ "<div class='col-xs-4 text-center vertical-center'>"
								+ "<span><strong>"
								+ group.currentSize
								+ "/"
								+ group.maxSize
								+ "</strong> Players</span></div>"
								+ "<div class='col-xs-4 text-right'><input class='btn btn-default join-game-btn col-xs-4' "
								+ "type='submit' onClick='return existingGameSelected(this)' value='Join Game' gameid='"
								+ group.id + "' maxSize='" + group.maxSize
								+ "'></div></div>");

	}
}

// Move from username entry screen to game creation/join screen
$("#enter-name-begin-btn").click(openCreateJoinGame);
$("#nameEntry").keypress(function(event) {
	var keyPressed = (event.keyCode ? event.keyCode : event.which);
	if (keyPressed === 13) {
		openCreateJoinGame();
	}
});

// Only allow alphanumeric and whitespace characters as user input
$("#nameEntry, #game-name-entry").on("input", function(event) {
	var input = $(this);
	var currText = input.val();

	var cleanedText = currText.replace(/[^A-Za-z0-9\s]+/g, "");
	input.val(cleanedText);
});

// Make sure dynamic rates option only shown when decimal option selected
$("#decimal-option").click(function() {
	$("#dynamic-rates-container").removeClass("hidden");
});

$("#integer-option").click(function() {
	$("#dynamic-rates-container").addClass("hidden");
});

/*
 * Opens the create/join game screen.
 */
function openCreateJoinGame() {
	var name = $("#nameEntry").val();
	if (name !== undefined && name !== "") {
		$("#pre-name-container").addClass("hidden");
		$("#post-name-container").removeClass("hidden");

		// Vertically center text
		var btnHeight = $("#games-list .join-game-btn").outerHeight();
		$("#games-list .vertical-center").css("height", btnHeight);
		$("#games-list .vertical-center *")
				.css("line-height", btnHeight + "px");
	}
}

/*
 * Handle a request to join an existing game.
 * @param caller - the object that called this function
 */
function existingGameSelected(caller) {
	console.log(caller);
	var userName = id("nameEntry").value;
	var groupId = $(caller).attr("gameid");
	var groupSize = $(caller).attr("maxSize");
	var victoryPoints = id("victory-points-input").value;
	var isDecimal = $("#decimal-option").hasClass("active");
	var isDynamic = isDecimal && $("#dynamic-rates-option").hasClass("active");
	var isStandard = $("#default-board-option").hasClass("active");

	if (userName == undefined || userName == "") {
		alert("Please select a username");
		return false;
	}

	setCookie("desiredGroupId", groupId);
	setCookie("userName", userName);
	setCookie("numPlayersDesired", groupSize);
	setCookie("victoryPoints", victoryPoints);
	setCookie("isDecimal", isDecimal);
	setCookie("isDynamic", isDynamic);
	setCookie("isStandard", isStandard);
	deleteCookie("USER_ID");
	return true;
}

// Display all cookies
function displayCookies() {
	alert(document.cookie);
}

/*
 * Returns a cookie of the given name.
 * @param name - the name of the cookie
 */
function getCookie(name) {
	var nameEQ = name + "=";
	// alert(document.cookie);
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ')
			c = c.substring(1);
		if (c.indexOf(nameEQ) != -1) {
			return c.substring(nameEQ.length, c.length);
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
	var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement
			: null);
	if ((evt.keyCode == 13) && (evt.target.id != "startGameButton")) {
		return false;
	}
}

/*
 * Handle a request to start a new game.
 */
function startGamePressed() {
	var userName = id("nameEntry").value;
	var numPlayers = id("numPlayersDesired").value;
	var groupName = id("game-name-entry").value;
	var victoryPoints = id("victory-points-input").value;
	var isDecimal = $("#decimal-option").hasClass("active");
	var isDynamic = isDecimal && $("#dynamic-rates-option").hasClass("active");
	var isStandard = $("#default-board-option").hasClass("active");

	if (userName == undefined || userName == "") {
		alert("Please select a username");
		return false; // will not allow the get reqeust to process.
	}

	if (groupName === undefined || groupName === "") {
		alert("Please select a name for your game");
		return false;
	}

	setCookie("userName", userName);
	setCookie("numPlayersDesired", numPlayers);
	setCookie("victoryPoints", victoryPoints);
	setCookie("isDecimal", isDecimal);
	setCookie("groupName", groupName);
	setCookie("isDynamic", isDynamic);
	setCookie("isStandard", isStandard);

	deleteCookie("USER_ID");
	return true; // will allow the get request to process.
}

function deleteCookie(name) {
	document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
};

// Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
	id(targetId).insertAdjacentHTML("afterbegin", message);
}

// Helper function for selecting element by id
function id(id) {
	return document.getElementById(id);
}

document.onkeypress = stopReturnKey;
