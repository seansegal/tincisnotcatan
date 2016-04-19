var X_UNIT_VEC = {x: Math.sqrt(3) / 2, y: 0.5};
var Y_UNIT_VEC = {x: 0, y: -1};
var Z_UNIT_VEC = {x: -Math.sqrt(3) / 2, y: 0.5};

var NUMBER_SCALE = 0.2;
var NUMBER_CIRCLE_SCALE = 0.3;

var TILE_TYPE = {
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
var DESERT_COLOR = "#C5BA96"

function Tile(coordinates, tileType, number) {
	this.coordinates = coordinates;
	this.tileType = tileType;
	this.number = number;

	this.id = "tile-x-" + this.coordinates.x + "y-" + this.coordinates.y + "z-" + this.coordinates.z;

	$("#board-viewport").append("<div class='hexagon-wrapper' id='" + this.id + "-wrapper'>"
			+ "<div class='hexagon' id='" + this.id + "'></div>"
			+ "<div class='circle number-circle'><span class='unselectable'>" + this.number + "</span></div></div>");
}

Tile.prototype.draw = function(transX, transY, scale) {
	var displacement = hexToCartesian(this.coordinates);
	var tileX = transX + displacement.x * scale;
	var tileY = transY + displacement.y * scale;

	var wrapper = $("#" + this.id + "-wrapper");
	var element = $("#" + this.id);
	var numberCircle = wrapper.children(".number-circle");

	// Color board
	var color = "";
	switch (this.tileType) {
		case TILE_TYPE.BRICK:
			color = BRICK_COLOR;
			break;
		case TILE_TYPE.WOOD:
			color = WOOD_COLOR;
			break;
		case TILE_TYPE.ORE:
			color = ORE_COLOR;
			break;
		case TILE_TYPE.WHEAT:
			color = WHEAT_COLOR;
			break;
		case TILE_TYPE.SHEEP:
			color = SHEEP_COLOR;
			break;
		case TILE_TYPE.DESERT:
			color = DESERT_COLOR;
		default:
			break;
	}

	element.css("background-color", color);
	wrapper.css("border-color", color);
	
	// Translate tile to correct location
	element.css("transform", "translate(" + tileX + "px , " + tileY + "px) rotate(-30deg)");
	
	if (!(this.tileType === TILE_TYPE.DESERT)) {
		// Translate number circle to correct location
		var circleX = tileX - (Math.sqrt(Math.pow(scale / Math.sqrt(3), 2)
				+ Math.pow(scale, 2)) - scale) / 2 - 0.025 * scale;
		var circleY = tileY + scale / (2 * Math.sqrt(3)) - 0.025 * scale;
		
		// Center number circle inside tile
		circleX = circleX - NUMBER_CIRCLE_SCALE * scale / 2 + scale / Math.sqrt(3);
		circleY = circleY - NUMBER_CIRCLE_SCALE * scale / 2;
		
		numberCircle.css("transform", "translate(" + circleX + "px , " + circleY + "px)");
		numberCircle.css("font-size", (scale * NUMBER_SCALE) + "px");
		numberCircle.css("width", (scale * NUMBER_CIRCLE_SCALE) + "px");
		numberCircle.css("height", (scale * NUMBER_CIRCLE_SCALE) + "px");
		numberCircle.children("span").css("line-height", (scale * NUMBER_CIRCLE_SCALE) + "px");
	}
}

function hexToCartesian(hexCoordinates) {
	var x = X_UNIT_VEC.x * hexCoordinates.x + Y_UNIT_VEC.x * hexCoordinates.y 
			+ Z_UNIT_VEC.x * hexCoordinates.z;
	var y = X_UNIT_VEC.y * hexCoordinates.x + Y_UNIT_VEC.y * hexCoordinates.y 
			+ Z_UNIT_VEC.y * hexCoordinates.z;
	return {x: x, y: y};
}


