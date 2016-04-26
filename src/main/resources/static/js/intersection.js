var SETTLEMENT_SCALE = 0.2;
var CITY_SCALE = 0.25;
var SELECTABLE_AREA_SCALE = 0.25;
var SETTLEMENT_SVG_WIDTH = 16;
var CITY_SVG_WIDTH = 100;

var SETTLEMENT_SVG = "<svg class='settlement-icon'><path "
	+ "d='M16 9.5l-3-3v-4.5h-2v2.5l-3-3-8 8v0.5h2v5h5v-3h2v3h5v-5h2z'></path></svg>";
var CITY_SVG = '<svg class="city-icon">'
	+ '<path d="M95.359,55.907H100l-5.063-8.122H54.429V34.388h7'
	+ '.173L30.801,12.026l-10.548,7.658v-4.638H13.22v9.744L0,34.388h7.173 v47.996H2.'
	+ '532v5.591h45.147h3.797h47.258v-5.591h-3.375V55.907z"/></svg>';

var BUILDING = {
	NONE: 1,
	SETTLEMENT: 2,
	CITY: 3
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

function Intersection(coord1, coord2, coord3) {
	this.intersectCoordinates = {coord1: coord1, coord2: coord2, coord3: coord3};
	this.coordinates = findCenter(coord1, coord2, coord3);
	this.id = ("intersection-x-" + this.coordinates.x + "y-" 
			+ this.coordinates.y + "z-" + this.coordinates.z).replace(/[.]/g, "_");

	this.building = BUILDING.NONE;
	this.player;

	this.port = PORT.NONE;
	
	this.highlighted = false;
	this.canBuildSettlement;
	
	$("#board-viewport").append("<div class='intersection-select circle' id='" + this.id + "-select'></div>");
	$("#board-viewport").append("<div class='intersection' id='" + this.id  + "'></div>");
}

Intersection.prototype.draw = function(transX, transY, scale) {
	var displacement = hexToCartesian(this.coordinates);
		
	var element = $("#" + this.id);
	element.empty();
		
	switch(this.building) {
	case BUILDING.SETTLEMENT:
		var size = scale * SETTLEMENT_SCALE;
		var x = transX + displacement.x * scale + Math.sqrt(3) * scale / 4 - size / 4;
		var y = transY + displacement.y * scale + scale / 4 - size / 2;

		// Move intersection to correct location and set size
		element.append(SETTLEMENT_SVG);
		element.css("transform", "translate(" + x + "px, " + y + "px)");
		element.attr("height", size);
		element.attr("width", size);
		
		// Scale svg element
		var svg = element[0].getElementsByTagName("svg")[0];
		svg.setAttribute("viewBox", "0 0 " + SETTLEMENT_SVG_WIDTH + " " + SETTLEMENT_SVG_WIDTH);
		svg.setAttribute("height", size);
		svg.setAttribute("width", size);
		
		// Set fill of svg element
		var svg2 = $("#" + this.id).children().first();
		svg2.css("fill", this.player.color);
		break;
	case BUILDING.CITY:
		var size = scale * CITY_SCALE;
		var x = transX + displacement.x * scale + Math.sqrt(3) * scale / 4 - size / 4;
		var y = transY + displacement.y * scale + scale / 4 - size / 2;
		
		element.append(CITY_SVG);
		element.css("transform", "translate(" + x + "px, " + y + "px)");
		element.attr("height", size);
		element.attr("width", size);
		
		var svg = element[0].getElementsByTagName("svg")[0];
		svg.setAttribute("viewBox", "0 0 " + CITY_SVG_WIDTH + " " + CITY_SVG_WIDTH);
		svg.setAttribute("height", size);
		svg.setAttribute("width", size);
		
		var svg2 = $("#" + this.id).children().first();
		svg2.css("fill", this.player.color);
		
		break;
	default:
		break;
	}
	
	// Calculate size and displacement of selectable area
	var size = scale * SELECTABLE_AREA_SCALE;
	var x = transX + displacement.x * scale + Math.sqrt(3) * scale / 4 - size / 4 - 0.020 * scale;
	var y = transY + displacement.y * scale + scale / 4 - size / 2;
	
	if (this.building === BUILDING.SETTLEMENT) {
		x = x + 0.0075 * scale;
		y = y + 0.015 * scale;
	}

	// Add selectable area to intersection
	var select = $("#" + this.id + "-select");
	select.css("transform", "translate(" + x + "px, " + y + "px)");
	select.css("height", size);
	select.css("width", size);
}

Intersection.prototype.addSettlement = function(player) {
	this.building = BUILDING.SETTLEMENT;
	this.player = player;
}

Intersection.prototype.addCity = function(player) {
	this.building = BUILDING.CITY;
	this.player = player;
}

Intersection.prototype.createIntersectionClickHandler = function() {
	var that = this;
	return function(event) {
		console.log(that);
		if (that.building === BUILDING.NONE) {
			sendBuildSettlementAction(that.intersectCoordinates);
		} else if (that.building === BUILDING.SETTLEMENT) {
			sendBuildCityAction(that.intersectCoordinates);
		}
	};
}

Intersection.prototype.highlight = function() {
	if (!(this.highlighted)) {
		this.highlighted = true;
		
		var select = $("#" + this.id + "-select");
		select.addClass("highlighted");
	
		select.click(this.createIntersectionClickHandler());
	}
}

Intersection.prototype.unHighlight = function() {
	if (this.highlighted) {
		this.highlighted = false;
		
		var select = $("#" + this.id + "-select");
		select.removeClass("highlighted");
	
		var that = this;
		select.off("click");
	}
}

findCenter = function(c1, c2, c3) {
	var x = (c1.x + c2.x + c3.x) / 3;
	var y = (c1.y + c2.y + c3.y) / 3;
	var z = (c1.z + c2.z + c3.z) / 3;
	
	return {x: x, y: y, z: z};
}

function parseIntersection(data) {
	var position = data.coordinate;
	var intersect = new Intersection(parseHexCoordinates(position.coord1),
			parseHexCoordinates(position.coord2),
			parseHexCoordinates(position.coord3))

	if (data.hasOwnProperty("building")) {
		var player = playersById[data.building.player];
		if (data.building.type === "settlement") {
			intersect.addSettlement(player);
		} else if (data.building.type === "city") {
			intersect.addCity(player);
		}
	}

	if (data.hasOwnProperty("port")) {
		switch (data.port._resource) {
			case "BRICK":
				this.port = PORT.BRICK;
				break;
			case "WOOD":
				this.port = PORT.WOOD;
				break;
			case "ORE":
				this.port = PORT.ORE;
				break;
			case "WHEAT":
				this.port = PORT.WHEAT;
				break;
			case "SHEEP":
				this.port = PORT.SHEEP;
				break;
			case "WILDCARD":
				this.port = PORT.WILDCARD;
				break;
			default:
				break;
		}
	}

	intersect.canBuildSettlement = data.canBuildSettlement;

	return intersect;
}
