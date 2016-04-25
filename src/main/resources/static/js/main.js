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
	var deltaScale = event.originalEvent.deltaY > 0 ? 10 : -10;
	board.scale(deltaScale);
});

function onSettlementBuild(event) {
	var btnElement = $(event.target);
	btnElement.off("click", onSettlementBuild);
	btnElement.click(onSettlementBuildCancel);

	btnElement.removeClass("btn-default");
	btnElement.addClass("btn-danger");
	btnElement.val("Cancel Build");

	for (var i = 0; i < board.intersections.length; i++) {
		if (board.intersections[i].canBuildSettlement) {
			board.intersections[i].highlight();
		}
	}
}

function onSettlementBuildCancel(event) {
	var btnElement = $(event.target);
	btnElement.off("click", onSettlementBuildCancel);
	btnElement.click(onSettlementBuild);

	btnElement.removeClass("btn-danger");
	btnElement.addClass("btn-default");
	btnElement.val("Build Settlement");

	for (var i = 0; i < board.intersections.length; i++) {
		if (board.intersections[i].highlighted) {
			board.intersections[i].unHighlight();
		}
	}
}

$("#settlement-build-btn").click(onSettlementBuild);

function onCityBuild(event) {
	var btnElement = $(event.target);
	btnElement.off("click", onCityBuild);
	btnElement.click(onCityBuildCancel);

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

function onCityBuildCancel(event) {
	var btnElement = $(event.target);
	btnElement.off("click", onCityBuildCancel);
	btnElement.click(onCityBuild);

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

$("#city-build-btn").click(onCityBuild);

function onRoadBuild(event) {
	var btnElement = $(event.target);
	btnElement.off("click", onRoadBuild);
	btnElement.click(onRoadBuildCancel);

	btnElement.removeClass("btn-default");
	btnElement.addClass("btn-danger");
	btnElement.val("Cancel Build");

	board.paths[5].highlight();
	board.paths[8].highlight();
	board.paths[16].highlight();
}

function onRoadBuildCancel(event) {
	var btnElement = $(event.target);
	btnElement.off("click", onRoadBuildCancel);
	btnElement.click(onRoadBuild);

	btnElement.removeClass("btn-danger");
	btnElement.addClass("btn-default");
	btnElement.val("Build Road");

	board.paths[5].unHighlight();
	board.paths[8].unHighlight();
	board.paths[16].unHighlight();
}

$("#road-build-btn").click(onRoadBuild);

$("#dev-card-build-btn").click(function() {
	console.log("dev card clicked");
});

function addMessage(message) {
	var container = $("#message-container");
	container.empty();

	container.css("padding-top", "5px");
	container.css("padding-bottom", "5px");

	container.append("<h5>" + message + "</h5>");

}
