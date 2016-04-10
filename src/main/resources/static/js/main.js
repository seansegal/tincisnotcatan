var board = undefined;
var players = [];

$(window).load(function() {
	board = new Board();
	// Center
	board.addTile({x: 0, y: 0, z: 0});

	// First ring
	board.addTile({x: 1, y: 0, z: 0});
	board.addTile({x: 0, y: 1, z: 0});
	board.addTile({x: 0, y: 0, z: 1});
	board.addTile({x: 1, y: 1, z: 0});
	board.addTile({x: 0, y: 1, z: 1});
	board.addTile({x: 1, y: 0, z: 1});

	// Outer ring
	board.addTile({x: 2, y: 0, z: 0});
	board.addTile({x: 0, y: 2, z: 0});
	board.addTile({x: 0, y: 0, z: 2});
	board.addTile({x: 2, y: 2, z: 0});
	board.addTile({x: 2, y: 1, z: 0});
	board.addTile({x: 1, y: 2, z: 0});
	board.addTile({x: 0, y: 2, z: 2});
	board.addTile({x: 0, y: 2, z: 1});
	board.addTile({x: 0, y: 1, z: 2});
	board.addTile({x: 2, y: 0, z: 2});
	board.addTile({x: 2, y: 0, z: 1});
	board.addTile({x: 1, y: 0, z: 2});
	
	// Add settlements
	board.addIntersection({x: 1, y: 0, z: 0}, {x: 1, y: 1, z: 0}, {x: 2, y: 1, z: 0});
	board.addIntersection({x: 0, y: 0, z: 0}, {x: 0, y: 1, z: 0}, {x: 0, y: 1, z: 1});
	board.addIntersection({x: 0, y: 2, z: 0}, {x: 0, y: 1, z: 0}, {x: 0, y: 2, z: 1});
	board.addIntersection({x: 0, y: 1, z: 1}, {x: 0, y: 1, z: 2}, {x: 0, y: 0, z: 2});

	// Create players
	players.push(new Player(1, "#FF0000"));
	players.push(new Player(2, "#FFFF00"));
	players.push(new Player(3, "#00FF00"));
	players.push(new Player(4, "#0000FF"));

	redrawCatan();
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
