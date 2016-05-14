var ROAD_WIDTH_SCALE = 0.055;
var ROAD_LENGTH_SCALE = 0.95;

/*
 * Construct a new intersection.
 * @param start1 - the coordinate of the first adjacent tile to the start intersection
 * @param start2 - the coordinate of the second adjacent tile to the start intersection
 * @param start3 - the coordinate of the third adjacent tile to the start intersection
 * @param end1 - the coordinate of the first adjacent tile to the end intersection
 * @param end2 - the coordinate of the second adjacent tile to the end intersection
 * @param end3 - the coordinate of the third adjacent tile to the end intersection 
 */
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

	this.canBuildRoad = false;
	this.highlighted = false;
	
	$("#board-viewport").append("<div class='path-select' id='" + this.id + "-select'></div>");
	$("#board-viewport").append("<div class='path' id='" + this.id + "'></div>");
}

/*
 * Redraws this path.
 * @param transX - the x translation of the board
 * @param transY - the y translation of the board
 * @param scale - the scale to draw at
 */
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

	if (Math.abs(angle) < 0.001) {
		angle = 0.0;
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

/*
 * Adds a road to this path.
 * @param player - the player who owns this road
 */
Path.prototype.addRoad = function(player) {
	this.containsRoad = true;
	this.player = player;
}

/*
 * Creates a path click handler.
 */
Path.prototype.createPathClickHandler = function() {
	var that = this;
	return function(event) {
		if (inPlaceRoadMode) {
			sendPlaceRoadAction(that.originalStart, that.originalEnd);
			exitPlaceRoadMode();
		} else {
			sendBuildRoadAction(that.originalStart, that.originalEnd);
			exitBuildMode();
		}
	};
}

/*
 * Highlights this path.
 */
Path.prototype.highlight = function() {
	if (!(this.highlighted)) {
		this.highlighted = true;
		
		var select = $("#" + this.id + "-select");
		select.addClass("highlighted-path");
	
		select.click(this.createPathClickHandler());
	}
}

/*
 * Unhighlights this path.
 */
Path.prototype.unHighlight = function() {
	if (this.highlighted) {
		this.highlighted = false;
		
		var select = $("#" + this.id + "-select");
		select.removeClass("highlighted-path");
	
		var that = this;
		select.off("click");
	}
}

/*
 * Creates a path from the given data.
 * @param pathData - the path data
 */
function parsePath(pathData) {
	var start = pathData.start;
	var end = pathData.end;
	var path = new Path(parseHexCoordinates(start.coord1), parseHexCoordinates(start.coord2),
			parseHexCoordinates(start.coord3), parseHexCoordinates(end.coord1),
			parseHexCoordinates(end.coord2), parseHexCoordinates(end.coord3));

	path.canBuildRoad = pathData.canBuildRoad;

	if (pathData.hasOwnProperty("road")) {
		path.addRoad(playersById[pathData.road.player]);
	}

	return path;
}
