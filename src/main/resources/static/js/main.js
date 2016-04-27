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

    $("#end-turn-btn").click(sendRollDiceAction);
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

	container.css("padding-top", "5px");
	container.css("padding-bottom", "5px");

	container.append("<h5>" + message + "</h5>");
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

	for (var i = 0; i < board.intersections.length; i++) {
		if (board.intersections[i].canBuildSettlement) {
			board.intersections[i].highlight();
		}
	}
}

function exitSettlementMode() {
	currentMode = BUILD_MODE.NONE;

	var btnElement = $("#settlement-build-btn");
	btnElement.off("click", exitSettlementMode);
	btnElement.click(enterSettlementMode);

	btnElement.removeClass("btn-danger");
	btnElement.addClass("btn-default");
	btnElement.val("Build Settlement");

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

function enterRoadMode() {
	exitBuildMode();
	currentMode = BUILD_MODE.ROAD;

	var btnElement = $("#road-build-btn");
	btnElement.off("click", enterRoadMode);
	btnElement.click(exitRoadMode);

	btnElement.removeClass("btn-default");
	btnElement.addClass("btn-danger");
	btnElement.val("Cancel Build");

	board.paths[5].highlight();
	board.paths[8].highlight();
	board.paths[16].highlight();
}

function exitRoadMode() {
	currentMode = BUILD_MODE.NONE;

	var btnElement = $("#road-build-btn");
	btnElement.off("click", exitRoadMode);
	btnElement.click(enterRoadMode);

	btnElement.removeClass("btn-danger");
	btnElement.addClass("btn-default");
	btnElement.val("Build Road");

	board.paths[5].unHighlight();
	board.paths[8].unHighlight();
	board.paths[16].unHighlight();
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
$("#dev-card-buy-btn").click(exitBuildMode);
$("#knight-btn").click(exitBuildMode);
$("#year-of-plenty-btn").click(exitBuildMode);
$("#monopoly-btn").click(exitBuildMode);
$("#road-building-btn").click(exitBuildMode);

$("#dev-card-buy-btn").click(sendBuyDevCardAction);

//////////////////////////////////////////
// Monopoly Modal 
//////////////////////////////////////////

var selectedMonopolyResource = null;
var selectedElement;

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
});

$("#play-monopoly-btn").click(function(event) {
	if (selectedMonopolyResource) {
		sendPlayMonopolyAction(selectedMonopolyResource);
		$("#monopoly-modal").modal("hide");
	}
});

$("#monopoly-modal").on("hide.bs.modal", function(event) {
 	// Unhighlight previously selected resource, if one was previously selected
	if (selectedElement) {
		selectedElement.removeClass("highlighted-monopoly-circle");
	}

	selectedMonopolyResource = null;
	selectedElement = null;
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
