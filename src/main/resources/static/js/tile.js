var X_UNIT_VEC = {x: Math.sqrt(3) / 2, y: 0.5};
var Y_UNIT_VEC = {x: 0, y: -1};
var Z_UNIT_VEC = {x: -Math.sqrt(3) / 2, y: 0.5};

var TILE_SCALE = 0.95;
var NUMBER_SCALE = 0.175;
var NUMBER_CIRCLE_SCALE = 0.3;
var NUMBER_CIRCLE_DOTS_SCALE = 0.025;
var PORT_SCALE = 0.55;

var TILE_TYPE = {
	BRICK: 1,
	WOOD: 2,
	ORE: 3,
	WHEAT: 4,
	SHEEP: 5,
	DESERT: 6,
	SEA: 7
}

var PORT = {
	NONE: 0,
	BRICK: 1,
	WOOD: 2,
	WHEAT: 3,
	ORE: 4,
	SHEEP: 5,
	WILDCARD: 6
}

function Tile(coordinates, tileType, number, hasRobber, port) {
	this.coordinates = coordinates;
	this.tileType = tileType;
	this.number = number;
	this.numDots = 6 - Math.abs(this.number - 7);
	this.hasRobber = hasRobber;

	this.port = port;
	this.portLocations = [];

	this.id = "tile-x-" + this.coordinates.x + "y-" + this.coordinates.y + "z-" + this.coordinates.z;

	$("#board-viewport").append("<div class='hexagon-wrapper' id='" + this.id + "-wrapper'>"
			+ "<div class='hexagon' id='" + this.id + "'></div></div>");

	if (this.tileType === TILE_TYPE.DESERT) {
		$("#" + this.id + "-wrapper").append("<div class='circle number-circle desert-circle'></div>");
	} else if (this.tileType === TILE_TYPE.SEA) {
		$("#" + this.id + "-wrapper").append("<div class='circle number-circle sea-circle'></div>");
	} else {
		$("#" + this.id + "-wrapper").append("<div class='circle number-circle number-circle-color'>"
				+ "<span class='unselectable'>" + this.number + "</span>"
				+ "<br><div class='dots-container'></div></div>");
	}

	if (this.port !== PORT.NONE) {
		$("#board-viewport").append("<div class='circle port-circle' id='" + this.id + "-port'></div>");
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
			element.css("background", "none");
			// element.addClass("sea-color");
		default:
			break;
	}
	
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

		// Set number to red if 6 or 8
		if (this.number === 6 || this.number === 8) {
			numberCircle.children("span").addClass("red-number");
			numberCircle.find(".number-dot").addClass("red-number-dot");
		}
	}

	// Draw robber
	if (this.hasRobber) {
		numberCircle.empty();
		numberCircle.append("<img src='images/icon-robber.svg' alt='Robber' class='robber-icon'>");
	}

	// Draw port
	if (this.port !== PORT.NONE) {
		var type = null;
		switch (this.port) {
			case PORT.BRICK:
				type = "brick";
				break;
			case PORT.WOOD:
				type = "wood";
				break;
			case PORT.ORE:
				type = "ore";
				break;
			case PORT.WHEAT:
				type = "wheat";
				break;
			case PORT.SHEEP:
				type = "sheep";
				break;
			case PORT.WILDCARD:
				type = "wildcard";
				break;
			default:
				break;
		}

		var center = {x: (this.portLocations[0].x + this.portLocations[1].x) / 2,
					  y: (this.portLocations[0].y + this.portLocations[1].y) / 2,
					  z: (this.portLocations[0].z + this.portLocations[1].z) / 2}
		var cartCenter = hexToCartesian(center);

		var size = PORT_SCALE * scale;

		var x = transX + cartCenter.x * scale + Math.sqrt(3) * scale / 4 - size / 2 + 0.045 * scale;
		var y = transY + cartCenter.y * scale + scale / 4 - size / 2 + 0.01 * scale;

		// Move port circle to correct location and set size
		var portCircle = $("#" + this.id + "-port");
		portCircle.css("transform", "translate(" + x + "px, " + y + "px)");
		portCircle.css("height", size);
		portCircle.css("width", size);

		// Add port resource icon
		numberCircle.empty();
		numberCircle.append("<img src='images/icon-" + type + ".svg' alt='" + type + "' class='port-icon'>");
		numberCircle.css("border", "solid 1px black");
		numberCircle.addClass(type + "-color");
	}

	// Highlight if in move robber mode
	numberCircle.off("click");

	if (moveRobberMode && this.isRobbable()) {
		numberCircle.addClass("number-circle-highlighted");
		var that = this;
		numberCircle.off("click");
		numberCircle.click(function(event) {
			moveRobberMode = false;
			sendMoveRobberAction(that.coordinates);
			board.draw();
		});
	} else {
		numberCircle.removeClass("number-circle-highlighted");
	}
}

Tile.prototype.isRobbable = function() {
	return !(this.tileType === TILE_TYPE.SEA || this.hasRobber); 
}

function parseTile(tileData) {
	var coords = parseHexCoordinates(tileData.hexCoordinate);
	var type = parseTileType(tileData.type);
	var port = PORT.NONE;

	if (tileData.hasOwnProperty("portType")) {
		switch (tileData.portType) {
			case "BRICK":
				port = PORT.BRICK;
				break;
			case "WOOD":
				port = PORT.WOOD;
				break;
			case "ORE":
				port = PORT.ORE;
				break;
			case "WHEAT":
				port = PORT.WHEAT;
				break;
			case "SHEEP":
				port = PORT.SHEEP;
				break;
			case "WILDCARD":
				port = PORT.WILDCARD;
				break;
			default:
				break;
		}
	}

	var tile = new Tile(coords, type, tileData.number, tileData.hasRobber, port);

	if (tile.port !== PORT.NONE) {
		var portLocs = tileData.portLocations;
		var portLoc1 = findCenter(portLocs[0].coord1, portLocs[0].coord2, portLocs[0].coord3);
		var portLoc2 = findCenter(portLocs[1].coord1, portLocs[1].coord2, portLocs[1].coord3);

		tile.portLocations = [portLoc1, portLoc2];
	}

	return tile;
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

function findCenter(c1, c2, c3) {
	var x = (c1.x + c2.x + c3.x) / 3;
	var y = (c1.y + c2.y + c3.y) / 3;
	var z = (c1.z + c2.z + c3.z) / 3;
	
	return {x: x, y: y, z: z};
}


