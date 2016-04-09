var X_UNIT_VEC = {x: Math.sqrt(3) / 2, y: -0.5};
var Y_UNIT_VEC = {x: 0, y: 1};
var Z_UNIT_VEC = {x: -Math.sqrt(3) / 2, y: -0.5};

function Tile(coordinates) {
	this.coordinates = coordinates;

	this.id = "tile-x-" + this.coordinates.x + "y-" + this.coordinates.y + "z-" + this.coordinates.z;

	$("#board-viewport").append("<div class='hexagon' id='" + this.id + "'></div>");
}

Tile.prototype.draw = function(transX, transY, scale) {
	var displacement = this.calcDisplacement();
	var x = transX + displacement.x * scale;
	var y = transY + displacement.y * scale;
	$("#" + this.id).css("transform", "translate(" + x + "px , " + y + "px) rotate(30deg)");
}

Tile.prototype.calcDisplacement = function() {
	var x = X_UNIT_VEC.x * this.coordinates.x + Y_UNIT_VEC.x * this.coordinates.y 
			+ Z_UNIT_VEC.x * this.coordinates.z;
	var y = X_UNIT_VEC.y * this.coordinates.x + Y_UNIT_VEC.y * this.coordinates.y 
			+ Z_UNIT_VEC.y * this.coordinates.z;
	return {x: x, y: y};
}


