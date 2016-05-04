var board = undefined;
var players = [];
var playersById = {};
var playerId = -1;
var currentPlayerTurn = -2;

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
		num = num + ((text === "") ? 0 : parseInt(text));
	});

	return num;
}

$(".yop-number").change(function(event) {
	var oldVal = $(this).data("oldVal");
	var newVal = parseInt($(this).val());

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
		var res1 = null;
		var res2 = null;

		inputs.each(function(idx) {
			var num = parseInt($(this).val());
			if (num === 1) {
				if (foundFirst) {
					res2 = $(this).attr("res");
				} else {
					res1 = $(this).attr("res");
					foundFirst = true;
				}
			} else if (num === 2) {
				res1 = $(this).attr("res")
				res2 = $(this).attr("res");
			}
		});

		sendPlayYearOfPlentyAction(res1, res2);
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
		num = num + ((text === "") ? 0 : parseInt(text));
	});

	return -num;
}

function enterDiscardModal(numToDiscard) {
	$("#discard-modal").modal("show");
	$("#num-resources-to-discard").text(numToDiscard);
	$("#discard-btn").prop("disabled", true);

	var playerHand = playersById[playerId].hand;
	var maxHand = {brick: playerHand.brick, 
					wood: playerHand.wood, 
					ore: playerHand.ore, 
					wheat: playerHand.wheat, 
					sheep: playerHand.sheep};
	var currHand = {brick: playerHand.brick, 
					wood: playerHand.wood, 
					ore: playerHand.ore, 
					wheat: playerHand.wheat, 
					sheep: playerHand.sheep};
	redrawHand();

	function redrawHand() {
		$("#discard-hand-number-brick").text(currHand.brick);
		$("#discard-hand-number-wood").text(currHand.wood);
		$("#discard-hand-number-ore").text(currHand.ore);
		$("#discard-hand-number-wheat").text(currHand.wheat);
		$("#discard-hand-number-sheep").text(currHand.sheep);
	}

	$(".discard-number").change(function(event) {
		var oldVal = $(this).data("oldVal");
		var newVal = parseInt($(this).val());
		var res = $(this).attr("res");

		// Handle cases where you select too many resources or a positive resource amount
		if (oldVal === undefined && calcNumDiscards() > numToDiscard) {
			$(this).val("0");
			$(this).data("oldVal", 0);
		} else if (isNaN(newVal) || newVal > 0 || calcNumDiscards() > numToDiscard) {
			$(this).val(oldVal);
		} else {
			$(this).data("oldVal", newVal);
			if (oldVal === undefined) {
				currHand[res] = currHand[res] + newVal;
			} else {
				currHand[res] = currHand[res] + (newVal - oldVal);
			}
		}

		// Handle case where you selected more of a resource than you hold
		if (currHand[res] < 0) {
			if (oldVal === undefined) {
				$(this).val("0");
				$(this).data("oldVal", 0);
				currHand[res] = currHand[res] - newVal;
			} else {
				$(this).val(oldVal);
				$(this).data("oldVal", oldVal);
				currHand[res] = currHand[res] - (newVal - oldVal);
			}
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
				var num = ((text === "") ? 0 : parseInt(text));
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

var moveRobberMode = false;

function highlightRobbableTiles() {
	moveRobberMode = true;
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
var selectedToGetResource  = "hey there";

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

//////////////////////////////////////////
// Interplayer Trade Panel
//////////////////////////////////////////

var currentTrade = {brick: 0, wood: 0, ore: 0, wheat: 0, sheep: 0};

function clearInterplayerTrades() {
	// Clear currently displayed trade
	$(".interplayer-trade-input").val("");

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
		currentTrade[resource] = parseInt(newVal);
		updateToGiveGetPanels(resource, parseInt(newVal), parseInt(oldVal));
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
	console.log(tradeData);
	
	// Add resources in trade to review modal
	for (var res in tradeData) {
		var number = tradeData[res];
		
		if (number !== 0) {
			var container = $(number > 0 ? "#review-to-give-container" : "#review-to-get-container");
			var element = container.children("[res=" + res + "]");
			
			element.removeClass("hidden");
			element.children(".review-trade-number").text(Math.abs(number));
		}
	}
	
	$("#review-trade-modal").modal("show");
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

	$("#trade-responses-modal").modal("show");
}

$("#trade-responses-cancel-trade-btn").click(function(event) {
	sendTradeResponseAction(false, playerId, 0);
});

