var ROAD_WIDTH_SCALE = 0.055;
var ROAD_LENGTH_SCALE = 0.95;

function Path(start1, start2, start3, end1, end2, end3) {
	this.originalStart = {coord1: start1, coord2: start2, coord3: start3};
	this.originalEnd = {coord1: end1, coord2: end2, coord3: end3};

	// Force start to be leftmost intersection and end to be rightmost intersection
	var first = findCenter(start1, start2, start3);
	var second = findCenter(end1, end2, end3);
	if (hexToCartesian(first).x < hexToCartesian(second).x) {
		this.start = first;
		this.end = second;
	} else {
		this.start = second;
		this.end = first;
	}

	this.id = ("path-x-" + this.start.x + "y-" + this.start.y + "z-" + this.start.z
				+ "-to-x-" + this.end.x + "y-" + this.end.y + "z-" + this.end.z).replace(/[.]/g, "_");

	this.containsRoad = false;
	this.player = null;

	this.highlighted = false;
	
	$("#board-viewport").append("<div class='path-select' id='" + this.id + "-select'></div>");
	$("#board-viewport").append("<div class='path' id='" + this.id + "'></div>");
}

Path.prototype.draw = function(transX, transY, scale) {
	// Move road to correct section of board
	var cartesianStart = hexToCartesian(this.start);
	var cartesianEnd = hexToCartesian(this.end);
	var x = transX + cartesianStart.x * scale + Math.sqrt(3) * scale / 4;
	var y = transY + cartesianStart.y * scale + scale / 4;
	
	var cartesianStart = hexToCartesian(this.start);
	var cartesianEnd = hexToCartesian(this.end);
	
	var deltaX = cartesianEnd.x - cartesianStart.x;
	var deltaY = cartesianEnd.y - cartesianStart.y;
	
	// Find angle of road
	var angle = Math.atan(deltaY / deltaX);
	if (deltaX < 0) {
		angle = angle + Math.PI;
	}
	
	// Find exact size of road div
	var length = scale / Math.sqrt(3) * ROAD_LENGTH_SCALE;
	var height = scale * ROAD_WIDTH_SCALE;
	
	// Offset road to be centered based on its angle
	x = x + 0.04 * scale;
	x = x + (scale / Math.sqrt(3)) - (length / 2);
	
	if (Math.abs(angle) < 0.0001 || Math.abs(angle - Math.PI) < 0.0001
			|| Math.abs(angle + Math.PI) < 0.001) {
		x = x - ((scale / Math.sqrt(3)) - (length / 2));
		x = x + (1 - ROAD_LENGTH_SCALE) * scale / (2 * Math.sqrt(3));
		y = y + 0.015 * scale - height / 2;
	} else if (Math.abs(angle - Math.PI / 3) < 0.0001) {
		x = x - scale * Math.sqrt(3) / 4;
		y = y - 0.015 * scale + scale / 4;
	} else if (Math.abs(angle + Math.PI / 3) < 0.0001) {
		x = x - scale * Math.sqrt(3) / 4;
		y = y - 0.015 * scale - scale / 4;
	}
		
	var element = $("#" + this.id);
	
	// Draw road if this path contains a road
	if (this.containsRoad) {
		element.css("transform", "translate(" + x + "px, " + y + "px) "
				+ "rotate(" + angle + "rad)");
		element.css("width", length);
		element.css("height", height);
		element.css("background-color", this.player.color);
	}

	// Add selectable area to intersection
	var select = $("#" + this.id + "-select");
	select.css("transform", "translate(" + x + "px, " + y + "px) "
			+ "rotate(" + angle + "rad)");
	select.css("width", length);
	select.css("height", height);
}

Path.prototype.addRoad = function(player) {
	this.containsRoad = true;
	this.player = player;
}

Path.prototype.createPathClickHandler = function() {
	var that = this;
	return function(event) {
		sendBuildRoadAction(that.originalStart, that.originalEnd);
		exitBuildMode();
	};
}

Path.prototype.highlight = function() {
	if (!(this.highlighted)) {
		this.highlighted = true;
		
		var select = $("#" + this.id + "-select");
		select.addClass("highlighted-path");
	
		select.click(this.createPathClickHandler());
	}
}

Path.prototype.unHighlight = function() {
	if (this.highlighted) {
		this.highlighted = false;
		
		var select = $("#" + this.id + "-select");
		select.removeClass("highlighted-path");
	
		var that = this;
		select.off("click");
	}
}

function parsePath(pathData) {
	var start = pathData.start;
	var end = pathData.end;
	var path = new Path(parseHexCoordinates(start.coord1), parseHexCoordinates(start.coord2),
			parseHexCoordinates(start.coord3), parseHexCoordinates(end.coord1),
			parseHexCoordinates(end.coord2), parseHexCoordinates(end.coord3));

	if (pathData.hasOwnProperty("road")) {
		path.addRoad(playersById[pathData.road.player]);
	}

	return path;
}
