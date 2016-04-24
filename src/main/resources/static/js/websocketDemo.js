//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat/");
var actionSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/action/");
// when we get a message from the server, we execute the following.
webSocket.onmessage = function (msg) { updateChat(msg); };
// when we get a closed connection from the server, we execute the following:
// webSocket.onclose = function () { alert("WebSocket connection closed") };
actionSocket.onopen = function () {
    sendGetPlayersAction();
    sendGetBoardAction();
};

actionSocket.onmessage = function (msg) {
    var data = JSON.parse(msg.data);
    console.log(data);

    if (data.hasOwnProperty("getBoard")) {
        handleGetBoard(data.getBoard);
    } else if (data.hasOwnProperty("getPlayers")) {
        handleGetPlayers(data.getPlayers);
    } else if (data.hasOwnProperty("getGameState")) {
        handleGetGameState();
    }
};

function sendGetBoardAction() {
    var playersReq = {"action": "getBoard"};
    actionSocket.send(JSON.stringify(playersReq));
}

function sendGetPlayersAction() {
    var playersReq = {"action": "getPlayers"};
    actionSocket.send(JSON.stringify(playersReq));
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
        webSocket.send(message);
        id("message").value = "";
    }
}

//Update the chat-panel, and the list of connected users
function updateChat(msg) {
    var data = JSON.parse(msg.data);
    console.log(msg);
    console.log(data.ERROR);
    if(data.hasOwnProperty('ERROR')) {
    	alert(data.ERROR);
    } else {
        insert("chat", data.userMessage);
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
