var INITIAL_HEX_SIZE = 130;
var MIN_SCALE = 100;
var MAX_SCALE = 300;

function Board() {
	this.transX = $(window).width() / 2 - 100;
	this.transY = $(window).height() / 2 - 100;
	this.scaleFactor = INITIAL_HEX_SIZE;

	this.tiles = [];
	this.intersections = [];
	this.roads = [];
}

Board.prototype.translate = function(deltaX, deltaY) {
	this.transX = this.transX + deltaX;
	this.transY = this.transY + deltaY;
	
	// Cap translation to bounding box
	this.transX = Math.max(-this.scaleFactor / 2, this.transX);
	this.transX = Math.min($("#board-viewport").width() * 5 / 6 - this.scaleFactor / 2, this.transX);
	this.transY = Math.max(-this.scaleFactor / (2 * Math.sqrt(3)), this.transY);
	this.transY = Math.min($("#board-viewport").height() - this.scaleFactor / (2 * Math.sqrt(3)), this.transY);
	
	this.draw();
}

Board.prototype.scale = function(deltaScale) {
	this.scaleFactor = this.scaleFactor + deltaScale;
	
	// Cap scale factor between MIN_SCALE and MAX_SCALE
	this.scaleFactor = Math.max(MIN_SCALE, this.scaleFactor);
	this.scaleFactor = Math.min(MAX_SCALE, this.scaleFactor);

	// Clip edges if they go too far over border
	this.translate(0, 0);
	
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

Board.prototype.addTile = function(coordinates, resource, number) {
	this.tiles.push(new Tile(coordinates, resource, number));
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
