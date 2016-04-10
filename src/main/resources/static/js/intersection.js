var BUILDING = {
	EMPTY_INTERSECTION: 1,
	SETTLEMENT: 2,
	CITY: 3
}

function Intersection(coord1, coord2, coord3) {
	this.coordinates = this.findCenter(coord1, coord2, coord3);
	this.id = ("intersection-x-" + this.coordinates.x + "y-" + this.coordinates.y + "z-" + this.coordinates.z).replace(/[.]/g, "_");
	this.building = BUILDING.SETTLEMENT;
	
	$("#board-viewport").append("<div class='intersection' id='" + this.id  + "'></div>");
}

Intersection.prototype.draw = function(transX, transY, scale) {
	var displacement = hexToCartesian(this.coordinates);
	var x = transX + displacement.x * scale + Math.sqrt(3) * scale / 4;
	var y = transY + displacement.y * scale + scale / 4;
	
	console.log(transY + "  " + (displacement.y * scale) + "  " + (scale / 4));
	
	var element = $("#" + this.id);
	element.empty();
			
	switch(this.building) {
	case BUILDING.SETTLEMENT:
		element.append("<svg><path class='path1' d='M16 9.5l-3-3v-4.5h-2v2.5l-3-3-8 8v0.5h2v5h5v-3h2v3h5v-5h2z'></path></svg>");
		element.css("transform", "translate(" + x + "px , " + y + "px)");
		var svg = $("#" + this.id).children().first();
		break;
	case BUILDING.CITY:
		break;
	default:
		break;
	}
	
}

Intersection.prototype.findCenter = function(c1, c2, c3) {
	var x = (c1.x + c2.x + c3.x) / 3;
	var y = (c1.y + c2.y + c3.y) / 3;
	var z = (c1.z + c2.z + c3.z) / 3;
	
	return {x: x, y: y, z: z};
}
