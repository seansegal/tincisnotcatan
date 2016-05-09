$(window).load(function() {
	var href = window.location.pathname;
	if (href == "/home") {
		deleteCookie("desiredGroupId");
	}

	// If not in chrome, display useful modal
	var isChrome = !!window.chrome && !!window.chrome.webstore;
	if (!isChrome) {
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

function heartbeat() {
	var beat = "HEARTBEAT";
	webSocket.send(JSON.stringify(beat));
}

webSocket.onopen = function() {
	window.setInterval(heartbeat, 60 * 1000);
};

webSocket.onmessage = function(msg) {
	var data = JSON.parse(msg.data);
	console.log(data);

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

$("#decimal-option").click(function() {
	$("#dynamic-rates-container").removeClass("hidden");
});

$("#integer-option").click(function() {
	$("#dynamic-rates-container").addClass("hidden");
});

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

function displayCookies() {
	alert(document.cookie);
}

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
