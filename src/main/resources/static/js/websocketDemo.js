//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/action/"); 

webSocket.onopen = function () {
    sendGetGameStateAction();
};

webSocket.onmessage = function (msg) {
    var data = JSON.parse(msg.data);
    console.log(data);
    
    if(data.hasOwnProperty("responseType")) {
    	switch(data.responseType) {
    	case "chat":
    		updateChat(data);
    		return;
    	case "getBoard":
    		handleGetBoard(data.content);
    		return;
    	case "getPlayers":
    		handleGetPlayers(data.content);
    		return;
    	case "getGameState":
    		handleGetGameState(data.content);
    		return;
    	default:
    		console.log("unsupported response type");
    		return;
    	}
    } else {
    	console.log("No response type indicated");
    }
};

function sendGetBoardAction() {
	var playersReq = {"requestType": "action", "content" : {"action" : "getBoard", "params" : []}};
    webSocket.send(JSON.stringify(playersReq));
}

function sendGetPlayersAction() {
	var playersReq = {"requestType": "action", "content" : {"action" : "getPlayers", "params" : []}};
    webSocket.send(JSON.stringify(playersReq));
}

function sendGetGameStateAction() {
    var playersReq = {"requestType": "getGameState", "content": {}};
    webSocket.send(JSON.stringify(playersReq));
}

function sendRollDiceAction() {
    var rollDiceReq  = {"requestType": "action", "content": {"action": "rollDice", "params": []}};
    webSocket.send(JSON.stringify(rollDiceReq));
}

function sendBuildSettlementAction() {
    var buildReq  = {"requestType": "action", "content": {"action": "buildSettlement", "params": []}};
    webSocket.send(JSON.stringify(buildReq));
}

function handleGetBoard(boardData) {
    board = new Board();
    board.createBoard(boardData);
    board.draw();
}

function handleGetPlayers(playersData) {
    playersById = {};
    players = parsePlayers(playersData.players);
    for (var i = 0; i < players.length; i++) {
        playersById[players[i].id] = players[i];
        players[i].fillPlayerTab();
    }
}

function handleGetGameState(gameStateData) {

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
    	var pack = {"requestType" : "chat", "content" : message}
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
