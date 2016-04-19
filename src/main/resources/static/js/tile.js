var X_UNIT_VEC = {x: Math.sqrt(3) / 2, y: 0.5};
var Y_UNIT_VEC = {x: 0, y: -1};
var Z_UNIT_VEC = {x: -Math.sqrt(3) / 2, y: 0.5};

var RESOURCE = {
	BRICK: 1,
	WOOD: 2,
	ORE: 3,
	WHEAT: 4,
	SHEEP: 5,
	DESERT: 6
}

var BRICK_COLOR = "#945D3F";
var WOOD_COLOR = "#408754";
var WHEAT_COLOR = "#C9BA56";
var ORE_COLOR = "#A1AFDA";
var SHEEP_COLOR = "#83B94E";
var DESERT_COLOR = "#AEA57F"

function Tile(coordinates, resource) {
	this.coordinates = coordinates;
	this.resource = resource;

	this.id = "tile-x-" + this.coordinates.x + "y-" + this.coordinates.y + "z-" + this.coordinates.z;

	$("#board-viewport").append("<div class='hexagon' id='" + this.id + "'></div>");
}

Tile.prototype.draw = function(transX, transY, scale) {
	var displacement = hexToCartesian(this.coordinates);
	var x = transX + displacement.x * scale;
	var y = transY + displacement.y * scale;

	var element = $("#" + this.id);

	element.css("transform", "translate(" + x + "px , " + y + "px) rotate(-30deg)");

	var color = "";
	switch (this.resource) {
		case RESOURCE.BRICK:
			color = BRICK_COLOR;
			break;
		case RESOURCE.WOOD:
			color = WOOD_COLOR;
			break;
		case RESOURCE.ORE:
			color = ORE_COLOR;
			break;
		case RESOURCE.WHEAT:
			color = WHEAT_COLOR;
			break;
		case RESOURCE.SHEEP:
			color = SHEEP_COLOR;
			break;
		case RESOURCE.DESERT:
			color = DESERT_COLOR;
		default:
			break;
	}

	element.css("background-color", color);
}

function hexToCartesian(hexCoordinates) {
	var x = X_UNIT_VEC.x * hexCoordinates.x + Y_UNIT_VEC.x * hexCoordinates.y 
			+ Z_UNIT_VEC.x * hexCoordinates.z;
	var y = X_UNIT_VEC.y * hexCoordinates.x + Y_UNIT_VEC.y * hexCoordinates.y 
			+ Z_UNIT_VEC.y * hexCoordinates.z;
	return {x: x, y: y};
}


