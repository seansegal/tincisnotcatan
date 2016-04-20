var ROAD_WIDTH_SCALE = 0.055;
var ROAD_LENGTH_SCALE = 0.95;

function Road(start1, start2, start3, end1, end2, end3) {
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

	this.id = ("road-x-" + this.start.x + "y-" + this.start.y + "z-" + this.start.z
				+ "-to-x-" + this.end.x + "y-" + this.end.y + "z-" + this.end.z).replace(/[.]/g, "_");
	this.containsRoad = false;
	this.player = null;
	
	$("#board-viewport").append("<div class='road' id='" + this.id + "'></div>");
}

Road.prototype.draw = function(transX, transY, scale) {
	if (this.containsRoad) {
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
		
		if (angle == 0 || angle == Math.PI || angle == -Math.PI) {
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
		
		element.css("transform", "translate(" + x + "px, " + y + "px) "
				+ "rotate(" + angle + "rad)");
		element.css("width", length);
		element.css("height", height);
		element.css("background-color", this.player.color);
	}
}

Road.prototype.addRoad = function(player) {
	this.containsRoad = true;
	this.player = player;
}
