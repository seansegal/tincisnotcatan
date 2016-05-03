
// ---------- Setup ---------- //

//Establish the WebSocket connection and set up event handlers
if (document.location.hostname == "localhost") {
	// use http
	webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/action/");
} else {
	// we're on heroku - use https:
	webSocket = new WebSocket("wss://" + location.hostname + ":" + location.port + "/action/");
}

function heartbeat() {
	var beat = "HEARTBEAT";
	webSocket.send(JSON.stringify(beat));
}

webSocket.onopen = function() {
	if(document.cookie.indexOf("USER_ID") > -1) {
		sendGetGameStateAction();
	}
	window.setInterval(heartbeat, 60 * 1000);
};

webSocket.onclose = function() {
    window.location.reload(true);
}

//////////////////////////////////////////
// Action Senders
//////////////////////////////////////////

function sendGetGameStateAction() {
    var playersReq = {requestType: "getGameState"};
    webSocket.send(JSON.stringify(playersReq));
}

function sendRollDiceAction() {
    var rollDiceReq  = {requestType: "action", action: "rollDice", player: playerId};
    webSocket.send(JSON.stringify(rollDiceReq));
}

function sendBuildSettlementAction(intersectCoordinates) {
    var buildReq  = {requestType: "action", action: "buildSettlement", coordinate: intersectCoordinates};
    webSocket.send(JSON.stringify(buildReq));
}

function sendPlaceSettlementAction(intersectCoordinates) {
    var placeReq  = {requestType: "action", action: "placeSettlement", coordinate: intersectCoordinates};
    webSocket.send(JSON.stringify(placeReq));
}

function sendBuildCityAction(intersectCoordinates) {
    var buildReq  = {requestType: "action", action : "buildCity", coordinate: intersectCoordinates};
    webSocket.send(JSON.stringify(buildReq));
}

function sendBuildRoadAction(start, end) {
    var buildReq  = {requestType: "action", action: "buildRoad", start: start, end: end};
    webSocket.send(JSON.stringify(buildReq));
}

function sendPlaceRoadAction(start, end) {
    var placeReq  = {requestType: "action", action: "placeRoad", start: start, end: end};
    webSocket.send(JSON.stringify(placeReq));
}

function sendBuyDevCardAction() {
    var buyReq = {requestType: "action", action: "buyDevCard"};
    webSocket.send(JSON.stringify(buyReq));
}

function sendPlayKnightAction() {
    var playReq = {requestType: "action", action: "playKnight"};
    webSocket.send(JSON.stringify(playReq));
}

function sendPlayMonopolyAction(resource) {
    var playReq = {requestType: "action", action: "playMonopoly", resource: resource};
    webSocket.send(JSON.stringify(playReq));
}

function sendPlayYearOfPlentyAction(res1, res2) {
    var playReq = {requestType: "action", action: "playYearOfPlenty", firstRes: res1, secondRes: res2};
    webSocket.send(JSON.stringify(playReq));
}

function sendPlayRoadBuildingAction() {
    var playReq = {requestType: "action", action: "playRoadBuilding"};
    webSocket.send(JSON.stringify(playReq));
}

function sendDropCardsAction(toDrop) {
    var dropReq = {requestType: "action", action: "dropCards", toDrop: toDrop};
    webSocket.send(JSON.stringify(dropReq));
}

function sendMoveRobberAction(coord) {
    var dropReq = {requestType: "action", action: "moveRobber", newLocation: coord};
    webSocket.send(JSON.stringify(dropReq));
}

function sendTakeCardAction(playerId) {
    var takeReq = {requestType: "action", action: "takeCard", takeFrom: playerId};
    webSocket.send(JSON.stringify(takeReq));
}

function sendTradeWithBankAction(toGive, toGet) {
    var tradeReq = {requestType: "action", action: "tradeWithBank", toGive: toGive, toGet: toGet};
    webSocket.send(JSON.stringify(tradeReq));
}

function startSetupAction() {
    var startReq = {requestType: "action", action: "startSetup"};
    webSocket.send(JSON.stringify(startReq));
}

function sendEndTurnAction() {
    var endReq = {requestType: "action", action: "endTurn"};
    webSocket.send(JSON.stringify(endReq));
}

// ---------- RESPONSES ---------- //

webSocket.onmessage = function (msg) {
    var data = JSON.parse(msg.data);
    console.log(data);

    if(data.hasOwnProperty("requestType")) {
        switch(data.requestType) {
        case "chat":
            updateChat(data);
            break;
        case "getGameState":
            handleGetGameState(data);
            break;
        case "action":
            handleActionResponse(data);
            break;
        case "setCookie":
            handleSetCookie(data);
            break;
        case "ERROR" :
            handleErrorFromSocket(data);
            break;
        case "disconnectedUsers":
        	console.log(data);
        	break;
        default:
            console.log("unsupported request type");
            break;
        }
    } else {
        console.log("No request type indicated for response");
    }
};

//////////////////////////////////////////
// Chat Responses
//////////////////////////////////////////

//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    if (message !== "") {
        var pack = {"requestType" : "chat", "message" : message};
        webSocket.send(JSON.stringify(pack));
        id("message").value = "";
    }
}

//Update the chat-panel, and the list of connected users
function updateChat(msg) {
    if(msg.hasOwnProperty('ERROR')) {
        alert(msg.ERROR);
    } else {
        var fromPlayer = playersById[msg.fromUser];
        insert("chat", "<div style='border-left-color: " + fromPlayer.color + "'>" + msg.userMessage + "</div>");
    }
}

//////////////////////////////////////////
// Action Handlers
//////////////////////////////////////////

function handleActionResponse(data) {
    if (data.content.hasOwnProperty("message")) {
        if (data.content.hasOwnProperty("followUpAction") 
                && data.content.followUpAction.actionData.hasOwnProperty("message")) {
            addMessage(data.content.followUpAction.actionData.message);
        } else {
            addMessage(data.content.message);
        }
    }

    switch (data.action) {
        case "startGame":
            showStartGameDialogue(data.content);
            break
        default:
            break;
    }
    
    if (data.content.hasOwnProperty("followUpAction")) {
        var action = data.content.followUpAction;
        switch (action.actionName) {
            case "moveRobber":
                highlightRobbableTiles();
                break
            case "dropCards":
                enterDiscardModal(action.actionData.numToDrop);
                break;
            case "takeCard":
                enterTakeCardModal(action.actionData.toTake);
                break;
            case "placeRoad":
                inPlaceRoadMode = true;
                break;
            case "placeSettlement":
                inPlaceSettlementMode = true;
                break;
            case "rollDice":
                showRollDiceModal();
                break;
            case "knightOrDice":
                showKnightOrDiceModal();
                break;
            default:
                break;
        }
    }
}

function handleGetGameState(gameStateData) {
    // Set global data
    playerId = gameStateData.playerID;
    currentPlayerTurn = gameStateData.currentTurn;

    // Create players
    playersById = {};
    players = parsePlayers(gameStateData.players);
    for (var i = 0; i < players.length; i++) {
        playersById[players[i].id] = players[i];
    }

    // Parse and draw hand
    fillPlayerHand(gameStateData.hand);

    // Create player tabs and turn counter
    $("#player-tabs").empty();
    $("#player-tabs-content").empty();
    $("#turn-display-container").empty();

    for (var i = 0; i < players.length; i++) {
        players[i].addPlayerTab();
        players[i].fillTurnDisplay();
    }

    // Select first players tab
    if (players.length > 0) {
        $("#player-tabs").children().first().addClass("active");
        $("#player-tabs-content").children().first().addClass("active");
    }

    // Show what buildings player can currently buy
    fillPlayerBuyOptions(gameStateData.hand);

    // Draw trade rates
    fillPlayerTradeRates(gameStateData.players[playerId].rates); // TODO: change to reflect current player

    // Create board
    board = new Board();
    board.createBoard(gameStateData.board);
    board.draw();

    // If in place road mode, enter build rode mode
    if (inPlaceRoadMode) {
        enterPlaceRoadMode();
    }

    // If in place settlment mode, enter place settlement mode
    if (inPlaceSettlementMode) {
        enterPlaceSettlementMode();
    }
}

//Send message if enter is pressed in the input field
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) { sendMessage(e.target.value); }
});


// ---------- SET COOKIE FROM SERVER ---------- //

function handleSetCookie(data) {
	console.log(data);
	for(i=0; i < data.cookies.length; i++) {
		if(data.cookies[i].name == "USER_ID") {
			var cook = data.cookies[i];
			setCookie(cook.name, cook.value);
			console.log("Cookies set to :" + document.cookie);
			sendGetGameStateAction();
		}
	}
}

// ---------- ERRORS ---------- //

function handleErrorFromSocket(data) {
	if(data.hasOwnProperty("description")){
		switch(data.description) {
		case "RESET":
			deleteCookie("USER_ID");
			window.location = "/home"; // redirect to home
			break;
		case "NOT_REGISTERED":
			alert("Internal error : user not registered");
			break;
		default:
			console.log(data.description);
		}
	}
}

// ---------- COOKIE MANAGEMENT ---------- //

function getCookie(name) {
    var nameEQ = name + "=";
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

function deleteCookie(name) {
    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
};

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("beforeend", message);
    $("#chat").scrollTop($("#chat")[0].scrollHeight);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}
