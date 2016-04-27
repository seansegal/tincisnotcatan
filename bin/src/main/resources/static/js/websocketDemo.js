//Establish the WebSocket connection and set up event handlers

if (document.location.hostname == "localhost") {
	// use http
	webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/action/");
} else {
	// we're on heroku - use https:
	webSocket = new WebSocket("wss://" + location.hostname + ":" + location.port + "/action/");
}


webSocket.onopen = function () {
    sendGetGameStateAction();
};

webSocket.onmessage = function (msg) {
    var data = JSON.parse(msg.data);
    console.log(data);

    if(data.hasOwnProperty("requestType")) {
    	switch(data.requestType) {
    	case "chat":
    		updateChat(data);
    		return;
    	case "getGameState":
    		handleGetGameState(data);
    		return;
<<<<<<< HEAD
        case "buildSettlement":
            handleBuildSettlement(data.content);
=======
        case "action":
            handleActionResponse(data);
>>>>>>> landingPage
            return;
    	default:
    		console.log("unsupported request type");
    		return;
    	}
    } else {
    	console.log("No request type indicated for response");
    }
};

<<<<<<< HEAD
function sendGetGameStateAction() {
    var playersReq = {requestType: "getGameState", content: {}};
=======
function handleActionResponse(data) {
	switch(data.action) {
	// add action handlers here!
	case  "buildSettlement":
		return handleBuildSettlement(data);
	default:
		console.log("action object with no action identifier");
	}
}

function sendGetGameStateAction() {
    var playersReq = {requestType: "getGameState"};
>>>>>>> landingPage
    webSocket.send(JSON.stringify(playersReq));
}

function sendRollDiceAction() {
<<<<<<< HEAD
    var rollDiceReq  = {requestType: "action", content: {action: "rollDice", player: playerId}};
=======
    var rollDiceReq  = {requestType: "action", "action": "rollDice", "player": playerId};
>>>>>>> landingPage
    webSocket.send(JSON.stringify(rollDiceReq));
}

function sendBuildSettlementAction(intersectCoordinates) {
<<<<<<< HEAD
    var buildReq  = {requestType: "action", content: {action: "buildSettlement", coordinate: intersectCoordinates, player: 0}};
=======
    var buildReq  = {requestType: "action", "action": "buildSettlement", "coordinate": intersectCoordinates, "player": 0};
>>>>>>> landingPage
    webSocket.send(JSON.stringify(buildReq));
}

function sendBuildCityAction(intersectCoordinates) {
<<<<<<< HEAD
    var buildReq  = {requestType: "action", content: {action: "buildCity"}};
    webSocket.send(JSON.stringify(buildReq));
}

function sendBuildRoadAction(start, end) {
    var buildReq  = {requestType: "action", content: {action: "buildRoad"}};
    webSocket.send(JSON.stringify(buildReq));
}

=======
    var buildReq  = {requestType: "action", "action" : "buildCity"};
    webSocket.send(JSON.stringify(buildReq));
}

function sendBuildRoadAction(start, end) {
    var buildReq  = {requestType: "action", "action": "buildRoad"};
    webSocket.send(JSON.stringify(buildReq));
}

>>>>>>> landingPage
function handleGetGameState(gameStateData) {
    // Set global data
    playerId = gameStateData.playerID;
    currentTurn = gameStateData.currentTurn;

    // Create players
    playersById = {};
    players = parsePlayers(gameStateData.players);
    for (var i = 0; i < players.length; i++) {
        playersById[players[i].id] = players[i];
    }

    for (var i = 0; i < 4; i++) {
        players[i].fillPlayerTab();
    }

    // Draw hand
    fillPlayerHand(gameStateData.hand);

    // Draw trade rates
    fillPlayerTradeRates(gameStateData.players[playerId].rates); // TODO: change to reflect current player

    // Create board
    board = new Board();
    board.createBoard(gameStateData.board);
    board.draw();
}

function handleBuildSettlement(response) {

}

//Send message if "Send" is clicked
id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});

//Send message if enter is pressed in the input field
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) { sendMessage(e.target.value); }
});


//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    if (message !== "") {
<<<<<<< HEAD
    	var pack = {"requestType" : "chat", "content" : {"message" : message}}
=======
    	var pack = {"requestType" : "chat", "message" : message};
>>>>>>> landingPage
        webSocket.send(JSON.stringify(pack));
        id("message").value = "";
    }
}

//Update the chat-panel, and the list of connected users
function updateChat(msg) {
    console.log(msg);
    if(msg.hasOwnProperty('ERROR')) {
    	alert(msg.ERROR);
    } else {
        insert("chat", msg.userMessage);
    }

}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}
