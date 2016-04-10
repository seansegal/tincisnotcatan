function Player(id, color) {
	this.id = id;
	this.color = color;
	
	this.victoryPoints = 4;
	this.resourceCards = 6;
	this.developmentCards = 3;
	this.playedKnights = 3;
	this.roads = 3;
	this.settlements = 2;
	this.cities = 0;
}

Player.prototype.fillPlayerTab = function() {
	var tab = $("#p" + this.id + "-tab");
	tab.empty();
	
	tab.append("<div class='circle player-circle' style='background-color: " + this.color + "'></div>");
	tab.append("<p><strong>Victory Points:</strong> " + this.victoryPoints + "</p>");
	tab.append("<p><strong>Resource Cards:</strong> " + this.resourceCards + "</p>");
	tab.append("<p><strong>Development Cards:</strong> " + this.developmentCards + "</p>");
	tab.append("<p><strong>Played Knights:</strong> " + this.playedKnights + "</p>");
	tab.append("<p><strong>Roads:</strong> " + this.roads + "</p>");
	tab.append("<p><strong>Settlements:</strong> " + this.settlements + "</p>");
	tab.append("<p><strong>Cities:</strong> " + this.cities + "</p>");
}

