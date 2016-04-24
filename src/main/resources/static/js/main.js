var board = undefined;
var players = [];
var playersById = {};

$(window).load(function() {
	board = new Board();

    $(function () {
  		$('[data-toggle="popover"]').popover()
	});
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

	board.intersections[5].highlight();
	board.intersections[11].highlight();
	board.intersections[21].highlight();
	board.intersections[15].highlight();
}

function onSettlementBuildCancel(event) {
	var btnElement = $(event.target);
	btnElement.off("click", onSettlementBuildCancel);
	btnElement.click(onSettlementBuild);

	btnElement.removeClass("btn-danger");
	btnElement.addClass("btn-default");
	btnElement.val("Build Settlement");

	board.intersections[5].unHighlight();
	board.intersections[11].unHighlight();
	board.intersections[21].unHighlight();
	board.intersections[15].unHighlight();
}

$("#settlement-build-btn").click(onSettlementBuild);

$("#city-build-btn").click(function() {
	console.log("city clicked");
});

$("#road-build-btn").click(function() {
	console.log("road clicked");
});

$("#dev-card-build-btn").click(function() {
	console.log("dev card clicked");
});
