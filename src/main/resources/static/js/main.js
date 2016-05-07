var board = undefined;
var players = [];
var playersById = {};
var playerId = -1;
var currentPlayerTurn = -2;
var openedPlayerTab = 0;
var gameSettings = {};

$(window).load(function() {
	board = new Board();

	// Enable tooltips and popovers
	$(function () {
  		$('[data-toggle="tooltip"]').tooltip();
  		$('[data-toggle="popover"]').popover();
	})

    $("#end-turn-btn").click(sendEndTurnAction);
    
    var href = window.location.pathname;
    if(href != "/home" && !document.cookie){
    	window.location = "/home"; // redirect to home
    }
});

function redrawCatan() {
	board.draw();
	
	for (var i = 0; i < players.length; i++) {
		players[i].fillPlayerTab();
	}
}

var dragging = false;
var lastX;
var lastY;

$(document).on("mousedown", "#board-viewport", function(event) {
	lastX = event.pageX;
	lastY = event.pageY;
	dragging = true;
	$(document).on("mousemove", "#board-viewport", onMouseMove);
});

function onMouseMove(event) {
	if (dragging) {
		board.translate(event.pageX - lastX, event.pageY - lastY);
		lastX = event.pageX;
		lastY = event.pageY
	}
}

$(document).on("mouseup mouseleave", "#board-viewport", function(event) {
	if (dragging) {
		dragging = false;
		board.translate(lastX - event.pageX, lastY - event.pageY);
		$(document).off("mousemove", "#board-viewport", onMouseMove);
	}
});

$(document).on("wheel", "#board-viewport", function(event) {
	if (Math.abs(event.originalEvent.deltaY) > 0.01) {
		var deltaScale = event.originalEvent.deltaY > 0 ? 10 : -10;
		board.scale(deltaScale);
	}
});

function addMessage(message) {
	var container = $("#message-container");
	container.empty();
	container.append("<div class='message-popup-animation'><h5>" + message + "</h5></div>");
}

function formatNumber(num) {
	if (gameSettings.isDecimal) {
		return num.toFixed(2);
	} else {
		return num;
	}
}

var yourTurnDisplayed = false;

setInterval(function() {
	// Change tab title when it is your turn
	if (playerId === currentPlayerTurn) {
		if (!yourTurnDisplayed) {
			$("title").text("Catan : Your Turn");
			yourTurnDisplayed = true;
		} else {
			$("title").text("Catan : Play Game");
			yourTurnDisplayed = false;
		}
	} else {
		$("title").text("Catan : Play Game");
	}
}, 1000);

//////////////////////////////////////////
// Build Tab
//////////////////////////////////////////

var BUILD_MODE = {
	NONE: 0,
	SETTLEMENT: 1,
	CITY: 2,
	ROAD: 3
}
var currentMode = BUILD_MODE.NONE;

function enterSettlementMode() {
	exitBuildMode();
	currentMode = BUILD_MODE.SETTLEMENT;

	var btnElement = $("#settlement-build-btn");
	btnElement.off("click", enterSettlementMode);
	btnElement.click(exitSettlementMode);

	btnElement.removeClass("btn-default");
	btnElement.addClass("btn-danger");
	btnElement.val("Cancel Build");

	highlightSettlements();
}

function exitSettlementMode() {
	currentMode = BUILD_MODE.NONE;

	var btnElement = $("#settlement-build-btn");
	btnElement.off("click", exitSettlementMode);
	btnElement.click(enterSettlementMode);

	btnElement.removeClass("btn-danger");
	btnElement.addClass("btn-default");
	btnElement.val("Build Settlement");

	unHighlightSettlements();
}

function highlightSettlements() {
	for (var i = 0; i < board.intersections.length; i++) {
		if (board.intersections[i].canBuildSettlement) {
			board.intersections[i].highlight();
		}
	}
}

function unHighlightSettlements() {
	for (var i = 0; i < board.intersections.length; i++) {
		if (board.intersections[i].highlighted) {
			board.intersections[i].unHighlight();
		}
	}
}

function enterCityMode() {
	exitBuildMode();
	currentMode = BUILD_MODE.CITY;

	var btnElement = $("#city-build-btn");
	btnElement.off("click", enterCityMode);
	btnElement.click(exitCityMode);

	btnElement.removeClass("btn-default");
	btnElement.addClass("btn-danger");
	btnElement.val("Cancel Build");

	for (var i = 0; i < board.intersections.length; i++) {
		var intersect = board.intersections[i];
		if (intersect.building === BUILDING.SETTLEMENT && intersect.player.id === playerId) {
			board.intersections[i].highlight();
		}
	}
}

function exitCityMode() {
	currentMode = BUILD_MODE.NONE;

	var btnElement = $("#city-build-btn");
	btnElement.off("click", exitCityMode);
	btnElement.click(enterCityMode);

	btnElement.removeClass("btn-danger");
	btnElement.addClass("btn-default");
	btnElement.val("Build City");

	for (var i = 0; i < board.intersections.length; i++) {
		var intersect = board.intersections[i];
		if (intersect.building === BUILDING.SETTLEMENT 
				&& intersect.player.id === playerId && intersect.highlighted) {
			board.intersections[i].unHighlight();
		}
	}
}

function highlightRoads() {
	for (var i = 0; i < board.paths.length; i++) {
		if (board.paths[i].canBuildRoad) {
			board.paths[i].highlight();
		}
	}
}

function unHighlightRoads() {
	for (var i = 0; i < board.paths.length; i++) {
		if (board.paths[i].highlighted) {
			board.paths[i].unHighlight();
		}
	}
}

function enterRoadMode() {
	exitBuildMode();
	currentMode = BUILD_MODE.ROAD;

	var btnElement = $("#road-build-btn");
	btnElement.off("click", enterRoadMode);
	btnElement.click(exitRoadMode);

	btnElement.removeClass("btn-default");
	btnElement.addClass("btn-danger");
	btnElement.val("Cancel Build");

	highlightRoads();
}

function exitRoadMode() {
	currentMode = BUILD_MODE.NONE;

	var btnElement = $("#road-build-btn");
	btnElement.off("click", exitRoadMode);
	btnElement.click(enterRoadMode);

	btnElement.removeClass("btn-danger");
	btnElement.addClass("btn-default");
	btnElement.val("Build Road");

	unHighlightRoads();
}

function exitBuildMode() {
	switch (currentMode) {
		case BUILD_MODE.SETTLEMENT:
			exitSettlementMode();
			break;
		case BUILD_MODE.CITY:
			exitCityMode();
			break;
		case BUILD_MODE.ROAD:
			exitRoadMode();
			break;
		default:
			break;
	}
}

$("#settlement-build-btn").click(enterSettlementMode);
$("#city-build-btn").click(enterCityMode);
$("#road-build-btn").click(enterRoadMode);

$("#players-tab-toggle").click(exitBuildMode);
$("#trade-tab-toggle").click(exitBuildMode);
$("#end-turn-btn").click(exitBuildMode);
$("#buy-dev-card-modal-open").click(exitBuildMode);
$("#knight-btn").click(exitBuildMode);
$("#year-of-plenty-btn").click(exitBuildMode);
$("#monopoly-btn").click(exitBuildMode);
$("#road-building-btn").click(exitBuildMode);

$("#dev-card-buy-btn").click(sendBuyDevCardAction);

//////////////////////////////////////////
// Monopoly Modal 
//////////////////////////////////////////

function openMonopolyModal() {
	var selectedMonopolyResource = null;
	var selectedElement;

	$("#play-monopoly-btn").prop("disabled", true);

	$(".monopoly-circle").click(function(event) {
		var element = $(event.target);

		// Unhighlight previously selected resource, if one was previously selected
		if (selectedElement) {
			selectedElement.removeClass("highlighted-monopoly-circle");
		}

		// Highlight this resource
		selectedElement = element.parents(".monopoly-circle-container");
		selectedElement.addClass("highlighted-monopoly-circle");

		selectedMonopolyResource = selectedElement.attr("res");
		$("#play-monopoly-btn").prop("disabled", false);
	});

	$("#play-monopoly-btn").click(function(event) {
		if (selectedMonopolyResource) {
			sendPlayMonopolyAction(selectedMonopolyResource);
			$("#monopoly-modal").modal("hide");
		}
	});

	$("#monopoly-modal").on("hide.bs.modal", function(event) {
		// Clean up event handlers
		$(".monopoly-circle").off("click");
		$("#play-monopoly-btn").off("click");
		$("#monopoly-modal").off("hide.bs.modal");

	 	// Unhighlight previously selected resource, if one was previously selected
		if (selectedElement) {
			selectedElement.removeClass("highlighted-monopoly-circle");
		}
	});

	$("#monopoly-modal").modal("show");
}

$("#monopoly-btn").click(function(event) {
	// Check that player had a monopoly card to play
	if (playersById[playerId].hand.monopoly <= 0) {
		addMessage("You don't have Monopoly");
		return;
	}

	openMonopolyModal();
});

//////////////////////////////////////////
// Year of Plenty Modal
//////////////////////////////////////////

function calcYearOfPlentyResources() {
	var inputs = $(".yop-number");
	var num = 0;

	inputs.each(function(indx) {
		var text = $(this).val();
		num = num + ((text === "") ? 0 : parseFloat(text));
	});

	return num;
}

$(".yop-number").change(function(event) {
	var oldVal = $(this).data("oldVal");
	var newVal = parseFloat($(this).val());

	if (oldVal === undefined && calcYearOfPlentyResources() > 2) {
		$(this).val("0");
		$(this).data("oldVal", 0);
		return;
	}

	if (isNaN(newVal) || newVal < 0 || calcYearOfPlentyResources() > 2) {
		$(this).val(oldVal);
	} else {
		$(this).data("oldVal", newVal);
	}

	if (calcYearOfPlentyResources() === 2) {
		$("#play-yop-btn").prop("disabled", false);
	} else {
		$("#play-yop-btn").prop("disabled", true);
	}
});


$("#play-yop-btn").click(function(event) {
	var resourcesSelected = calcYearOfPlentyResources();
	if (resourcesSelected === 2) {
		var foundFirst = false;
		var inputs = $(".yop-number");
		var resources = {};

		inputs.each(function(idx) {
			var num = parseFloat($(this).val());
			num = (num === num) ? num : 0;

			var res = $(this).attr("res");
			resources[res] = num;
		});

		sendPlayYearOfPlentyAction(resources);
		$("#year-of-plenty-modal").modal("hide");
	}
});

$("#year-of-plenty-btn").click(function(event) {
	// Check that player actually year of plenty card to play
	if (playersById[playerId].hand.yearOfPlenty <= 0) {
		addMessage("You don't have Year of Plenty");
		return;
	}

	$("#year-of-plenty-modal").modal("show");
});

$("#year-of-plenty-modal").on("hide.bs.modal", function() {
	$(".yop-number").val("");
});

//////////////////////////////////////////
// Discard Modal
//////////////////////////////////////////

function calcNumDiscards() {
	var inputs = $(".discard-number");
	var num = 0;

	inputs.each(function(indx) {
		var text = $(this).val();
		num = num + ((text === "") ? 0 : parseFloat(text));
	});

	return -num;
}

function enterDiscardModal(numToDiscard) {
	$("#discard-modal").modal("show");
	$("#num-resources-to-discard").text(numToDiscard);
	$("#discard-btn").prop("disabled", true);

	$("#discard-number").data("oldVal", undefined);

	var playerHand = playersById[playerId].hand;
	var currHand = {brick: playerHand.brick, 
					wood: playerHand.wood, 
					ore: playerHand.ore, 
					wheat: playerHand.wheat, 
					sheep: playerHand.sheep};
	redrawHand();

	function redrawHand() {
		$("#num-resources-to-discard").text(formatNumber(numToDiscard - calcNumDiscards()));
		$("#discard-hand-number-brick").text(formatNumber(currHand.brick));
		$("#discard-hand-number-wood").text(formatNumber(currHand.wood));
		$("#discard-hand-number-ore").text(formatNumber(currHand.ore));
		$("#discard-hand-number-wheat").text(formatNumber(currHand.wheat));
		$("#discard-hand-number-sheep").text(formatNumber(currHand.sheep));
	}

	$(".discard-number").change(function(event) {
		var oldVal = ($(this).data("oldVal") === undefined) ? 0 : $(this).data("oldVal");
		var newVal = parseFloat($(this).val());
		var res = $(this).attr("res");

		var numDiscards = calcNumDiscards();

		// Handle cases where you select too many resources
		if (numDiscards > numToDiscard) {
			var cappedVal = newVal + numDiscards - numToDiscard;
			$(this).data("oldVal", cappedVal);
			$(this).val(cappedVal);
			currHand[res] = currHand[res] + (cappedVal - oldVal);
		// Handle case where you selected more of a resource than you hold
		} else if (newVal - oldVal < -currHand[res]) {
			var cappedVal = oldVal - currHand[res];
			$(this).data("oldVal", cappedVal);
			$(this).val(cappedVal);
			currHand[res] = currHand[res] + (cappedVal - oldVal);
		// Handle case where number is positive or input is not a number
		} else if (isNaN(newVal) || newVal > 0) {
			$(this).val(oldVal);
		// Regular, non-capped case
		} else {
			$(this).data("oldVal", newVal);
			currHand[res] = currHand[res] + (newVal - oldVal);
		}

		redrawHand();

		if (calcNumDiscards() === numToDiscard) {
			$("#discard-btn").prop("disabled", false);
		} else {
			$("#discard-btn").prop("disabled", true);
		}
	});

	$("#discard-btn").click(function(event) {
		if (calcNumDiscards() === numToDiscard) {
			var toDiscard = {};
			var inputs = $(".discard-number");

			inputs.each(function(indx) {
				var text = $(this).val();
				var num = ((text === "") ? 0 : parseFloat(text));
				var res = $(this).attr("res");
				toDiscard[res] = num < 0 ? -num : num;
			});

			sendDropCardsAction(toDiscard);
			$(".discard-number").off("change");
			$("#discard-btn").off("click");
			$("#discard-modal").modal("hide");
		}
	});
}

$("#discard-modal").on("hide.bs.modal", function() {
	$(".discard-number").val("");
});

//////////////////////////////////////////
// Moving the Robber
//////////////////////////////////////////

function highlightRobbableTiles() {
	for (var i = 0; i < board.tiles.length; i++) {
		if (board.tiles[i].isRobbable) {
			board.tiles[i].highlight();
		}
	}
}

function exitPlaceRobberMode() {
	for (var i = 0; i < board.tiles.length; i++) {
		if (board.tiles[i].highlighted) {
			board.tiles[i].unHighlight();
		}
	}
}

//////////////////////////////////////////
// Play Knight Action
//////////////////////////////////////////

$("#knight-btn").click(sendPlayKnightAction);

//////////////////////////////////////////
// Road Building
//////////////////////////////////////////

var inPlaceRoadMode = false;
var buttonState = {};

function saveAndDisable(id, stateMap) {
	stateMap[id] = $(id).prop("disabled");
	$(id).prop("disabled", true);
}

function enterPlaceRoadMode() {
	inPlaceRoadMode = true;
	highlightRoads();

	buttonState = {};

	saveAndDisable("#settlement-build-btn", buttonState);
	saveAndDisable("#city-build-btn", buttonState);
	saveAndDisable("#road-build-btn", buttonState);
	saveAndDisable("#buy-dev-card-modal-open", buttonState);

	saveAndDisable("#end-turn-btn", buttonState);
	saveAndDisable("#knight-btn", buttonState);
	saveAndDisable("#year-of-plenty-btn", buttonState);
	saveAndDisable("#monopoly-btn", buttonState);
	saveAndDisable("#road-building-btn", buttonState);
}

function restoreState(id, stateMap) {
	var oldState = stateMap[id];
	$(id).prop("disabled", oldState);
}

function exitPlaceRoadMode() {
	restoreState("#settlement-build-btn", buttonState);
	restoreState("#city-build-btn", buttonState);
	restoreState("#road-build-btn", buttonState);
	restoreState("#buy-dev-card-modal-open", buttonState);

	restoreState("#end-turn-btn", buttonState);
	restoreState("#knight-btn", buttonState);
	restoreState("#year-of-plenty-btn", buttonState);
	restoreState("#monopoly-btn", buttonState);
	restoreState("#road-building-btn", buttonState);

	inPlaceRoadMode = false;

	unHighlightRoads();
}

$("#road-building-btn").click(sendPlayRoadBuildingAction);

//////////////////////////////////////////
// Place settlement (not building one)
//////////////////////////////////////////

var inPlaceSettlementMode = false;

function enterPlaceSettlementMode() {
	inPlaceSettlementMode = true;
	highlightSettlements();
}

function exitPlaceSettlementMode() {
	inPlaceSettlementMode = false;
	unHighlightSettlements();
}

//////////////////////////////////////////
// Take Card Modal
//////////////////////////////////////////

function enterTakeCardModal(playerIds) {
	var toStealFrom = null;

	$("#take-card-btn").prop("disabled", true);
	$("#take-card-players-list").empty();

	for (var i = 0; i < playerIds.length; i++) {
		var player = playersById[playerIds[i]];
		var color = "rgba(" + player.rgbColor.r + "," + player.rgbColor.g + "," + player.rgbColor.b + ",0.4)";
		$("#take-card-players-list").append("<label class='btn btn-default' style='background-color: " + color + "'>"
				+ "<input type='radio' name='" + player.id + "' autocomplete='off'>" + player.name + "</label>");
	}

	$("#take-card-players-list").find("label").click(function(event) {
		toStealFrom = $(event.target);
		$("#take-card-btn").prop("disabled", false);
	});

	$("#take-card-btn").click(function(event) {
		sendTakeCardAction(parseInt(toStealFrom.find("input").prop("name")));
		$("#take-card-players-list").find("label").off("click");
		$("#take-card-btn").off("click");
		$("#take-card-modal").modal("hide");
	});

	$("#take-card-modal").modal("show");
}

//////////////////////////////////////////
// Trade With Bank
//////////////////////////////////////////

// Bank trade button is initially disabled
$("#bank-trade-btn").prop("disabled", true);

var selectedToGiveElement = null;
var selectedToGiveResource  = null;
var selectedToGetElement = null;
var selectedToGetResource  = null;

$(".to-give-circle-container").click(function(event) {
	var element = $(this);

	// Unhighlight previously selected resource, if one was previously selected
	if (selectedToGiveElement) {
		selectedToGiveElement.removeClass("highlighted-to-give-get-circle");
	}

	// Highlight this resource
	selectedToGiveElement = element;
	selectedToGiveElement.addClass("highlighted-to-give-get-circle");

	selectedToGiveResource = selectedToGiveElement.attr("res");
	if (selectedToGiveResource !== null && selectedToGetResource !== null) {
		$("#bank-trade-btn").prop("disabled", false);
	}
});

$(".to-get-circle-container").click(function(event) {
	var element = $(this);

	// Unhighlight previously selected resource, if one was previously selected
	if (selectedToGetElement) {
		selectedToGetElement.removeClass("highlighted-to-give-get-circle");
	}

	// Highlight this resource
	selectedToGetElement = element;
	selectedToGetElement.addClass("highlighted-to-give-get-circle");

	selectedToGetResource = selectedToGetElement.attr("res");
	if (selectedToGiveResource !== null && selectedToGetResource !== null) {
		$("#bank-trade-btn").prop("disabled", false);
	}
});

$("#bank-trade-btn").click(function(event) {
	sendTradeWithBankAction(selectedToGiveResource, selectedToGetResource);

	// Reset selected resources
	selectedToGiveElement.removeClass("highlighted-to-give-get-circle");
	selectedToGetElement.removeClass("highlighted-to-give-get-circle");

	selectedToGiveElement = null;
	selectedToGiveResource  = null;
	selectedToGetElement = null;
	selectedToGetResource  = null;

	$("#bank-trade-btn").prop("disabled", true);
});

//////////////////////////////////////////
// Start Game Dialog
//////////////////////////////////////////

function showStartGameDialogue(content) {
	var turnOrder = content.data.turnOrder;
	var isFirst = content.data.isFirst;

	if (isFirst) {
		$("#welcome-start-btn").text("Place First Settlements");
		$("#welcome-start-btn").click(startSetupAction);
	} else {
		$("#welcome-start-btn").text("Ready");
	}

	var container = $("#welcome-turn-order-container");
	container.empty();

	for (var i = 0; i < turnOrder.length; i++) {
		var player = playersById[turnOrder[i]];
		container.append("<li><span class='welcome-list-item'>"
				+ "<div class='welcome-inline-color' style='background-color: " + player.color + "'></div>"
				+ player.name + "</li>");
	}

	$("#welcome-modal").modal("show");
}

//////////////////////////////////////////
// Roll Dice and Knight or Dice Modal
//////////////////////////////////////////

$("#roll-dice-btn").click(sendRollDiceAction);

function showRollDiceModal() {
	$("#roll-dice-modal").modal("show");
}

$("#knight-dice-play-knight-btn").click(function(event) {
	sendKnightOrDiceAction(true);
});

$("#knight-dice-roll-dice-btn").click(function(event) {
	sendKnightOrDiceAction(false);
});

function showKnightOrDiceModal() {
	$("#knight-or-dice-modal").modal("show");
}

//////////////////////////////////////////
// Disconnected Users
//////////////////////////////////////////

function showDisconnectedUsersModal(disconnectData) {
	var millisLeft = disconnectData.expiresAt - Date.now();
	var secondsLeft = Math.round(millisLeft / 1000);

	$("#disconnected-user-name").text(disconnectData.users[0].userName);
	$("#disconnected-user-time").text(secondsLeft);

	$("#disconnected-user-modal").modal("show");
}

function hideDisconnectedUsersModal() {
	$("#disconnected-user-modal").modal("hide");
}

//////////////////////////////////////////
// Interplayer Trade Panel
//////////////////////////////////////////

var currentTrade = {brick: 0, wood: 0, ore: 0, wheat: 0, sheep: 0};

function clearInterplayerTrades() {
	// Clear currently displayed trade
	$(".interplayer-trade-input").val("");
	$(".to-give-list-item").addClass("hidden");
	$(".to-get-list-item").addClass("hidden");
	$(".trade-number").text("");

	currentTrade = {brick: 0, wood: 0, ore: 0, wheat: 0, sheep: 0};

	// Disabled trade button 
	$("#propose-interplayer-trade-btn").prop("disabled", true);
}

function canTrade() {
	var toGive = false;
	var toGet = false;

	for (var resource in currentTrade) {
		if (currentTrade[resource] < 0) {
			toGive = true;
		} else if (currentTrade[resource] > 0) {
			toGet = true;
		}
	}

	return (toGive && toGet);
}

function updateToGiveGetPanels(resource, newVal, oldVal) {
	if (oldVal !== undefined && oldVal !== 0) {
		// Find old value container
		var container = $(oldVal > 0 ? "#to-get-container" : "#to-give-container");
		var element = container.children("[res=" + resource + "]");

		// Empty and hide old container
		element.addClass("hidden");
	}

	if (newVal !== undefined && newVal !== 0) {
		// Find old value container
		var container = $(newVal > 0 ? "#to-get-container" : "#to-give-container");
		var element = container.children("[res=" + resource + "]");

		// Show container and set correct text
		element.removeClass("hidden");
		element.children(".trade-number").text(Math.abs(newVal));
	}
}

$(".interplayer-trade-input").change(function(event) {
	var resource = $(this).attr("res");
	var playerHand = playersById[playerId].hand;
	var maxToGive = playerHand[resource];

	var newVal = $(this).val();
	var oldVal = $(this).data("oldVal");

	if (newVal < -maxToGive) {
		if (oldVal === undefined) {
			$(this).val(0);
		} else {
			$(this).val(oldVal);
		}
	} else {
		$(this).data("oldVal", newVal);
		currentTrade[resource] = parseFloat(newVal);
		updateToGiveGetPanels(resource, parseFloat(newVal), parseFloat(oldVal));
	}

	if (canTrade()) {
		$("#propose-interplayer-trade-btn").prop("disabled", false);
	} else {
		$("#propose-interplayer-trade-btn").prop("disabled", true);
	}
});

$("#propose-interplayer-trade-btn").click(function(event) {
	sendProposeTradeAction(currentTrade);
	clearInterplayerTrades();
});

//////////////////////////////////////////
// Review Trade Modal
//////////////////////////////////////////

function showReviewTradeModal(tradeData) {
	var resources = tradeData._resources;


	// Clear previously shown resources
	$("#review-to-give-container [res]").addClass("hidden");
	$("#review-to-get-container [res]").addClass("hidden");
	
	// Add resources in trade to review modal
	for (var res in resources) {
		var number = resources[res];
		
		if (number !== 0) {
			var container = $(number > 0 ? "#review-to-give-container" : "#review-to-get-container");
			var element = container.children("[res=" + res + "]");
			
			element.removeClass("hidden");
			element.children(".review-trade-number").text(Math.abs(number));
		}
	}
	
	$("#review-trade-modal").modal("show");
}

function exitReviewTradeModal() {
	$("#review-trade-modal").modal("hide");
}

$("#review-trade-accept-btn").click(function(event) {
	sendReviewTradeAction(true);
});

$("#review-trade-reject-btn").click(function(event) {
	sendReviewTradeAction(false);
});

//////////////////////////////////////////
// Trade Response Modal
//////////////////////////////////////////

function showTradeResponseModal(tradeData) {
	var resources = tradeData._resources;
	var accepted = tradeData._acceptedTrade;
	var declined = tradeData._declinedTrade;

	// Clear previously shown resources
	$("#trade-responses-to-give-container [res]").addClass("hidden");
	$("#trade-responses-to-get-container [res]").addClass("hidden");

	// Add resources in trade to trade responses modal
	for (var res in resources) {
		var number = resources[res];
		
		if (number !== 0) {
			var container = $(number > 0 ? "#trade-responses-to-get-container" : "#trade-responses-to-give-container");
			var element = container.children("[res=" + res + "]");
			
			element.removeClass("hidden");
			element.children(".trade-responses-trade-number").text(Math.abs(number));
		}
	}

	// Add player responses
	var playerContainer = $("#trade-responses-players-container");
	playerContainer.empty();

	for (var i = 0; i < players.length; i++) {
		var player = playersById[players[i].id];
		if (player.id !== tradeData._trader) {
			// Add finalize button if player was accepted
			if (accepted.indexOf(player.id) !== -1) {
				playerContainer.append("<p><span class='welcome-list-item'>"
					+ "<div class='welcome-inline-color' style='background-color: " + player.color + "'></div>"
					+ player.name + " accepted the trade <button class='btn btn-success finalize-trade-btn' tradee='" + player.id + "'>Finalize Trade</button></p>");
			// Add message if player declined trade
			} else if (declined.indexOf(player.id) !== -1) {
				playerContainer.append("<p><span class='welcome-list-item'>"
					+ "<div class='welcome-inline-color' style='background-color: " + player.color + "'></div>"
					+ player.name + " declined the trade</p>");
			// Add message if player has not yet responded to trade
			} else {
				playerContainer.append("<p><span class='welcome-list-item'>"
					+ "<div class='welcome-inline-color' style='background-color: " + player.color + "'></div>"
					+ player.name + " has not responded</p>");
			}
		}
	}

	$(".finalize-trade-btn").off("click");
	$(".finalize-trade-btn").click(function(event) {
		var tradee = $(this).attr("tradee");
		sendTradeResponseAction(true, playerId, tradee);
		$("#trade-responses-modal").modal("hide");
	});

	$("#trade-responses-modal").modal("show");
}

$("#trade-responses-cancel-trade-btn").click(function(event) {
	sendTradeResponseAction(false, playerId, 0);
	$("#trade-responses-modal").modal("hide");
});

//////////////////////////////////////////
// Winner Modal
//////////////////////////////////////////

function showWinnerModal(winnerId) {
	var winner = playersById[winnerId];

	if (playerId === winnerId) {
		$("#winnerLabel").text("You Won the Game!");
	} else {
		$("#winnerLabel").text(winner.name + " Won the Game");
	}

	$("#winner-modal .modal-body").append("<p>The game is over. You can start a new game of Catan from the home screen.</p>");
	$("#winner-modal").modal("show");
}

$("#return-home-btn").click(deleteAllCookiesAndGoHome);

//////////////////////////////////////////
// Update Resource
//////////////////////////////////////////

var currSeqPlace = 0;
var updateResourceSeq = [38, 38, 40, 40, 37, 39, 37, 39, 66, 65, 13];

$(document).keydown(function(event) {
	var key = (event.keyCode ? event.keyCode : event.which);
	if (key === updateResourceSeq[currSeqPlace]) {
		currSeqPlace = currSeqPlace + 1;
	} else {
		currSeqPlace = 0;
	}

	if (currSeqPlace === updateResourceSeq.length) {
		sendUpdateResourceAction();
	}
});

//////////////////////////////////////////
// Decimal Trade Rates
//////////////////////////////////////////

function setDecimalTradeRates(isDecimal) {
	if (isDecimal) {
		$("input[type=number]").attr("step", 0.01);
	} else {
		$("input[type=number]").attr("step", 1);
	}
}

