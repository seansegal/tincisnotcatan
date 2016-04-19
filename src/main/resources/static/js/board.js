var INITIAL_HEX_SIZE = 100;
var TILE_SCALE = 0.95;
var MIN_SCALE = 50;
var MAX_SCALE = 300;

function Board() {
	this.transX = $(window).width() / 2 - 100;
	this.transY = $(window).height() / 2 - 100;
	this.scaleFactor = INITIAL_HEX_SIZE;

	this.tiles = [];
	this.intersections = [];
	this.roads = [];

	setupHexagonSizes(this.scaleFactor);
}

Board.prototype.translate = function(deltaX, deltaY) {
	this.transX = this.transX + deltaX;
	this.transY = this.transY + deltaY;
	this.draw();
}

Board.prototype.scale = function(deltaScale) {
	this.scaleFactor = this.scaleFactor + deltaScale;
	this.scaleFactor = Math.max(MIN_SCALE, this.scaleFactor);
	this.scaleFactor = Math.min(MAX_SCALE, this.scaleFactor);
	setHexagonSizes(this.scaleFactor);
	this.draw();
}

Board.prototype.draw = function() {
	for (var i = 0; i < this.tiles.length; i++) {
		this.tiles[i].draw(this.transX, this.transY, this.scaleFactor);
	}
	
	for (var i = 0; i < this.intersections.length; i++) {
		this.intersections[i].draw(this.transX, this.transY, this.scaleFactor);
	}
	
	for (var i = 0; i < this.roads.length; i++) {
		this.roads[i].draw(this.transX, this.transY, this.scaleFactor);
	}
}

Board.prototype.addTile = function(coordinates) {
	this.tiles.push(new Tile(coordinates));
	this.draw();
}

Board.prototype.addIntersection = function(c1, c2, c3) {
	this.intersections.push(new Intersection(c1, c2, c3));
	this.draw();
}

Board.prototype.addRoad = function(s1, s2, s3, e1, e2, e3) {
	this.roads.push(new Road(s1, s2, s3, e1, e2, e3));
	this.draw();
}

function setupHexagonSizes(scale) {
	document.styleSheets[0].insertRule(".hexagon { color: #fff; }", 0);
	document.styleSheets[0].insertRule(".hexagon { color: #fff; }", 0);
	document.styleSheets[0].insertRule(".hexagon { color: #fff; }", 0);
	document.styleSheets[0].insertRule(".hexagon { color: #fff; }", 0);
	setHexagonSizes(scale);
}

function setHexagonSizes(scale) {
	document.styleSheets[0].deleteRule(0);
	document.styleSheets[0].deleteRule(0);
	document.styleSheets[0].deleteRule(0);
	document.styleSheets[0].deleteRule(0);

	var width = TILE_SCALE * scale;

	document.styleSheets[0].insertRule(".hexagon { width: " + width + "px; height: " 
		+ (width * 1 / Math.sqrt(3)) + "px; margin: " +  (width * 0.5 / Math.sqrt(3)) + "px }", 0);
	document.styleSheets[0].insertRule(".hexagon:before, .hexagon:after { border-left: " 
		+ (width / 2) + "px solid transparent; border-right: " + (width / 2) + "px solid transparent; }", 0);
	document.styleSheets[0].insertRule(".hexagon:before { border-bottom: " + (width * 0.5 / Math.sqrt(3)) 
		+ "px solid #64C7CC; }", 0);
	document.styleSheets[0].insertRule(".hexagon:after { border-top: " + (width * 0.5 / Math.sqrt(3)) 
		+ "px solid #64C7CC; }", 0);
}
