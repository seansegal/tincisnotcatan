var board = undefined;
var players = [];

$(window).load(function() {
	board = new Board();
	// Center
	board.addTile({x: 0, y: 0, z: 0}, RESOURCE.WOOD);

	// First ring
	board.addTile({x: 1, y: 0, z: 0}, RESOURCE.SHEEP);
	board.addTile({x: 0, y: 1, z: 0}, RESOURCE.WHEAT);
	board.addTile({x: 0, y: 0, z: 1}, RESOURCE.BRICK);
	board.addTile({x: 1, y: 1, z: 0}, RESOURCE.SHEEP);
	board.addTile({x: 0, y: 1, z: 1}, RESOURCE.WOOD);
	board.addTile({x: 1, y: 0, z: 1}, RESOURCE.ORE);

	// Outer ring
	board.addTile({x: 2, y: 0, z: 0}, RESOURCE.SHEEP);
	board.addTile({x: 0, y: 2, z: 0}, RESOURCE.WHEAT);
	board.addTile({x: 0, y: 0, z: 2}, RESOURCE.ORE);
	board.addTile({x: 2, y: 2, z: 0}, RESOURCE.WHEAT);
	board.addTile({x: 2, y: 1, z: 0}, RESOURCE.BRICK);
	board.addTile({x: 1, y: 2, z: 0}, RESOURCE.WOOD);
	board.addTile({x: 0, y: 2, z: 2}, RESOURCE.DESERT);
	board.addTile({x: 0, y: 2, z: 1}, RESOURCE.BRICK);
	board.addTile({x: 0, y: 1, z: 2}, RESOURCE.ORE);
	board.addTile({x: 2, y: 0, z: 2}, RESOURCE.SHEEP);
	board.addTile({x: 2, y: 0, z: 1}, RESOURCE.WHEAT);
	board.addTile({x: 1, y: 0, z: 2}, RESOURCE.WOOD);
	
	// Add intersections: inner ring
	board.addIntersection({x: 0, y: 0, z: 0}, {x: 0, y: 1, z: 0}, {x: 0, y: 1, z: 1});
	board.addIntersection({x: 0, y: 0, z: 0}, {x: 0, y: 0, z: 1}, {x: 0, y: 1, z: 1});
	board.addIntersection({x: 0, y: 0, z: 0}, {x: 1, y: 0, z: 0}, {x: 1, y: 1, z: 0});
	board.addIntersection({x: 0, y: 0, z: 0}, {x: 0, y: 1, z: 0}, {x: 1, y: 1, z: 0});
	board.addIntersection({x: 0, y: 0, z: 0}, {x: 1, y: 0, z: 0}, {x: 1, y: 0, z: 1});
	board.addIntersection({x: 0, y: 0, z: 0}, {x: 0, y: 0, z: 1}, {x: 1, y: 0, z: 1});
	
	// Add intersections: middle ring
	board.addIntersection({x: 0, y: 2, z: 0}, {x: 0, y: 1, z: 0}, {x: 0, y: 2, z: 1});
	board.addIntersection({x: 0, y: 2, z: 0}, {x: 0, y: 1, z: 0}, {x: 1, y: 2, z: 0});
	board.addIntersection({x: 1, y: 2, z: 0}, {x: 0, y: 1, z: 0}, {x: 1, y: 1, z: 0});
	board.addIntersection({x: 1, y: 1, z: 0}, {x: 1, y: 2, z: 0}, {x: 2, y: 2, z: 0});
	board.addIntersection({x: 1, y: 1, z: 0}, {x: 2, y: 1, z: 0}, {x: 2, y: 2, z: 0});
	board.addIntersection({x: 2, y: 0, z: 0}, {x: 1, y: 0, z: 0}, {x: 2, y: 1, z: 0});
	board.addIntersection({x: 0, y: 2, z: 0}, {x: 0, y: 1, z: 0}, {x: 0, y: 2, z: 1});
	board.addIntersection({x: 1, y: 0, z: 0}, {x: 1, y: 0, z: 1}, {x: 2, y: 0, z: 1});
	board.addIntersection({x: 1, y: 0, z: 0}, {x: 2, y: 0, z: 0}, {x: 2, y: 0, z: 1});
	board.addIntersection({x: 1, y: 0, z: 1}, {x: 2, y: 0, z: 1}, {x: 2, y: 0, z: 2});
	board.addIntersection({x: 1, y: 0, z: 1}, {x: 1, y: 0, z: 2}, {x: 0, y: 0, z: 1});
	board.addIntersection({x: 1, y: 0, z: 1}, {x: 2, y: 0, z: 2}, {x: 1, y: 0, z: 2});
	board.addIntersection({x: 1, y: 0, z: 2}, {x: 0, y: 0, z: 1}, {x: 0, y: 0, z: 2});
	board.addIntersection({x: 0, y: 0, z: 1}, {x: 0, y: 0, z: 2}, {x: 0, y: 1, z: 2});	
	board.addIntersection({x: 1, y: 0, z: 0}, {x: 1, y: 1, z: 0}, {x: 2, y: 1, z: 0});
	board.addIntersection({x: 0, y: 1, z: 1}, {x: 0, y: 1, z: 2}, {x: 0, y: 0, z: 1});
	board.addIntersection({x: 0, y: 1, z: 1}, {x: 0, y: 1, z: 2}, {x: 0, y: 2, z: 2});
	board.addIntersection({x: 0, y: 1, z: 1}, {x: 0, y: 1, z: 0}, {x: 0, y: 2, z: 1});
	board.addIntersection({x: 0, y: 2, z: 1}, {x: 0, y: 1, z: 1}, {x: 0, y: 2, z: 2});

	// Add paths
	board.addRoad({x: 0, y: 0, z: 0}, {x: 0, y: 1, z: 0}, {x: 1, y: 1, z: 0},
			{x: 0, y: 0, z: 0}, {x: 1, y: 0, z: 0}, {x: 1, y: 1, z: 0});
	board.addRoad({x: 1, y: 1, z: 0}, {x: 1, y: 2, z: 0}, {x: 2, y: 2, z: 0},
			{x: 1, y: 2, z: 0}, {x: 2, y: 2, z: 0}, {x: 2, y: 3, z: 0});
	board.addRoad({x: 1, y: 0, z: 0}, {x: 1, y: 0, z: 1}, {x: 2, y: 0, z: 1},
			{x: 1, y: 0, z: 0}, {x: 2, y: 0, z: 0}, {x: 2, y: 0, z: 1});
	board.addRoad({x: 0, y: 0, z: 0}, {x: 1, y: 0, z: 0}, {x: 1, y: 1, z: 0},
			{x: 0, y: 0, z: 0}, {x: 1, y: 0, z: 0}, {x: 1, y: 0, z: 1});
	
	// Create players
	var p1 = new Player(1, "#FF4747");
	var p2 = new Player(2, "#1995D5");
	var p3 = new Player(3, "#F6F3EA");
	var p4 = new Player(4, "#FEB33C");

	players.push(p1, p2, p3, p4);

	// Add settlements
	board.intersections[0].addSettlement(p1);
//	board.intersections[2].addSettlement(p1);
	board.intersections[24].addSettlement(p2);
	board.intersections[8].addSettlement(p2);
	board.intersections[10].addSettlement(p3);
	board.intersections[18].addSettlement(p4);

	// Add roads
	board.roads[0].addRoad(p1);
	board.roads[1].addRoad(p1);
	board.roads[2].addRoad(p2);
	board.roads[3].addRoad(p1);

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
