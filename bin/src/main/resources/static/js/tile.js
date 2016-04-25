var X_UNIT_VEC = {x: Math.sqrt(3) / 2, y: 0.5};
var Y_UNIT_VEC = {x: 0, y: -1};
var Z_UNIT_VEC = {x: -Math.sqrt(3) / 2, y: 0.5};

var TILE_SCALE = 0.95;
var NUMBER_SCALE = 0.175;
var NUMBER_CIRCLE_SCALE = 0.3;
var NUMBER_CIRCLE_DOTS_SCALE = 0.025;

var TILE_TYPE = {
	BRICK: 1,
	WOOD: 2,
	ORE: 3,
	WHEAT: 4,
	SHEEP: 5,
	DESERT: 6,
	SEA: 7
}

function Tile(coordinates, tileType, number, hasRobber) {
	this.coordinates = coordinates;
	this.tileType = tileType;
	this.number = number;
	this.numDots = 6 - Math.abs(this.number - 7);
	this.hasRobber = hasRobber;

	this.id = "tile-x-" + this.coordinates.x + "y-" + this.coordinates.y + "z-" + this.coordinates.z;

	$("#board-viewport").append("<div class='hexagon-wrapper' id='" + this.id + "-wrapper'>"
			+ "<div class='hexagon' id='" + this.id + "'></div></div>");
	
	if (!(this.tileType === TILE_TYPE.DESERT || this.tileType === TILE_TYPE.SEA)) {
		$("#" + this.id + "-wrapper").append("<div class='circle number-circle'>"
				+ "<span class='unselectable'>" + this.number + "</span>"
				+ "<br><div class='dots-container'></div></div>");
	} else {
		$("#" + this.id + "-wrapper").append("<div class='circle number-circle desert-circle'></div>");
	}
}

Tile.prototype.draw = function(transX, transY, scale) {
	var displacement = hexToCartesian(this.coordinates);
	var tileX = transX + displacement.x * scale;
	var tileY = transY + displacement.y * scale;
	
	tileY = tileY - scale * (1 - 1 / Math.sqrt(3)) / 2;

	var wrapper = $("#" + this.id + "-wrapper");
	var element = $("#" + this.id);
	var numberCircle = wrapper.children(".number-circle");

	// Color board
	var color = "";
	switch (this.tileType) {
		case TILE_TYPE.BRICK:
			element.addClass("brick-color");
			break;
		case TILE_TYPE.WOOD:
			element.addClass("wood-color");
			break;
		case TILE_TYPE.ORE:
			element.addClass("ore-color");
			break;
		case TILE_TYPE.WHEAT:
			element.addClass("wheat-color");
			break;
		case TILE_TYPE.SHEEP:
			element.addClass("sheep-color");
			break;
		case TILE_TYPE.DESERT:
			element.addClass("desert-color");
			break;
		case TILE_TYPE.SEA:
			element.addClass("sea-color");
		default:
			break;
	}

	// element.css("background-color", color);
	
	// Translate and scale tile
	wrapper.css("transform", "translate(" + tileX + "px , " + tileY + "px)");
	wrapper.css("width", TILE_SCALE * scale);
	wrapper.css("height", TILE_SCALE * scale);
		
	// Translate number circle to correct location
	var circleX = -(Math.sqrt(Math.pow(scale / Math.sqrt(3), 2)
			+ Math.pow(scale, 2)) - scale) / 2 - 0.015 * scale;
	var circleY = -scale / (2 * Math.sqrt(3));
		
	// Center number circle inside tile
	circleX = circleX - NUMBER_CIRCLE_SCALE * scale / 2 + scale / Math.sqrt(3);
	circleY = circleY - NUMBER_CIRCLE_SCALE * scale / 2 - scale * (1 - 1 / Math.sqrt(3)) / 2;
		
	numberCircle.css("transform", "translate(" + circleX + "px , " + circleY + "px)");
		
	// Scale number circle
	numberCircle.css("width", (scale * NUMBER_CIRCLE_SCALE) + "px");
	numberCircle.css("height", (scale * NUMBER_CIRCLE_SCALE) + "px");
		
	if (!(this.tileType === TILE_TYPE.DESERT || this.tileType === TILE_TYPE.SEA)) {
		// Scale and center tile number
		numberCircle.css("font-size", (scale * NUMBER_SCALE) + "px");
		numberCircle.children("span").css("line-height", (scale * NUMBER_CIRCLE_SCALE) + "px");		
	
		// Add dots
		var dotsContainer = numberCircle.children(".dots-container");
		dotsContainer.empty();
		
		for (var i = 0; i < this.numDots; i++) {
			dotsContainer.append("<div class='circle number-dot'></div>");
		}
		
		// Move dots below number
		dotsContainer.css("transform", "translate(0px, " + (-scale * NUMBER_CIRCLE_SCALE) * 3 / 4 + "px)");
		
		// Set size of dots
		dotsContainer.children().css("height", scale * NUMBER_CIRCLE_DOTS_SCALE);
		dotsContainer.children().css("width", scale * NUMBER_CIRCLE_DOTS_SCALE);
		dotsContainer.children().not(":first").css("margin-left", scale * NUMBER_CIRCLE_DOTS_SCALE / 3);
	}

	// Draw robber
	if (this.hasRobber) {
		numberCircle.empty();
		numberCircle.append("<img src='images/icon-robber.svg' alt='Robber' class='robber-icon'>");
	}
}

function parseTile(tileData) {
	var coords = parseHexCoordinates(tileData.hexCoordinate);
	var type = parseTileType(tileData.type);
	return new Tile(coords, type, tileData.number, tileData.hasRobber);
}

function parseTileType(tileType) {
	switch (tileType) {
		case "BRICK":
			return TILE_TYPE.BRICK;
		case "WOOD":
			return TILE_TYPE.WOOD;
		case "ORE":
			return TILE_TYPE.ORE;
		case "WHEAT":
			return TILE_TYPE.WHEAT;
		case "SHEEP":
			return TILE_TYPE.SHEEP;
		case "DESERT":
			return TILE_TYPE.DESERT;
		case "SEA":
			return TILE_TYPE.SEA;
		default:
			return;
	}
}

function hexToCartesian(hexCoordinates) {
	var x = X_UNIT_VEC.x * hexCoordinates.x + Y_UNIT_VEC.x * hexCoordinates.y 
			+ Z_UNIT_VEC.x * hexCoordinates.z;
	var y = X_UNIT_VEC.y * hexCoordinates.x + Y_UNIT_VEC.y * hexCoordinates.y 
			+ Z_UNIT_VEC.y * hexCoordinates.z;
	return {x: x, y: y};
}

function parseHexCoordinates(hexCoordinates) {
	return {x: hexCoordinates.x, y: hexCoordinates.y, z: hexCoordinates.z};
}


