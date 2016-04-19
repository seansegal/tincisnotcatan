var ROAD_WIDTH_SCALE = 0.20;
var ROAD_LENGTH_SCALE = 1.0;

function Road(start1, start2, start3, end1, end2, end3) {
	this.start = findCenter(start1, start2, start3);
	this.end = findCenter(end1, end2, end3);
	this.id = ("road-x-" + this.start.x + "y-" + this.start.y + "z-" + this.start.z
				+ "-to-x-" + this.end.x + "y-" + this.end.y + "z-" + this.end.z).replace(/[.]/g, "_");
	this.containsRoad = true;
	this.player = null;
	
	$("#board-viewport").append("<div class='road' id='" + this.id + "'></div>");
}

Road.prototype.draw = function(transX, transY, scale) {
	if (this.containsRoad) {
		var cartesianStart = hexToCartesian(this.start);
		var cartesianEnd = hexToCartesian(this.end);
		var x = transX + cartesianStart.x * scale + Math.sqrt(3) * scale / 4;
		var y = transY + cartesianStart.y * scale + scale / 4;
		
		var cartesianStart = hexToCartesian(this.start);
		var cartesianEnd = hexToCartesian(this.end);
		
		var deltaX = cartesianEnd.x - cartesianStart.x;
		var deltaY = cartesianEnd.y - cartesianStart.y;
		
		var angle = Math.atan(deltaY / deltaX);
		if (deltaX < 0) {
			angle = angle + Math.PI;
		}
		
		console.log(angle);
		
		var length = scale / Math.sqrt(3) * ROAD_LENGTH_SCALE;
		var height = scale * ROAD_WIDTH_SCALE;
		
		// Account for rotation
		x = x - (length / 2) * (1 - Math.cos(angle));
		y = y + (length / 2) * Math.sin(angle);
		
		// Center between hexagons
//		x = x + (height * Math.sqrt(3) / 2);
		
		var element = $("#" + this.id);
		
		element.css("transform", "translate(" + x + "px, " + y + "px) "
				+ "rotate(" + angle + "rad)");
		element.css("width", length);
		element.css("height", height);
	}
}