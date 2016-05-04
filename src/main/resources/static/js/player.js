function Player(id, name, color) {
	this.id = id;
	this.name = name;
	this.color = color;
	this.rgbColor = hexToRgb(color);

	this.victoryPoints = 0;
	this.resourceCards = 0;
	this.developmentCards = 0;
	this.playedKnights = 0;
	this.roads = 0;
	this.settlements = 0;
	this.cities = 0;
	this.largestArmy = false;
	this.longestRoad = false;

	this.hand = {};
}

Player.prototype.addPlayerTab = function() {
	$("#player-tabs").append("<li role='presentation' id='p" + this.id + "-tab-tab'><a href='#p" + this.id + "-tab' aria-controls='home' "
			+ "role='tab' data-toggle='tab'>P" + this.id + "</a></li>");
	$("#player-tabs-content").append("<div role='tabpanel' class='tab-pane player-tab-pane' id='p" + this.id + "-tab'></div>");

	var tabTab = $("#p" + this.id + "-tab-tab");
	tabTab.children().css("background-color", this.color);
	tabTab.children().css("color", this.color);

	var tab = $("#p" + this.id + "-tab");
	tab.empty();

	var victoryPointsToDisplay = this.victoryPoints;
	if (this.hand.hasOwnProperty("victoryPoint")) {
		victoryPointsToDisplay = victoryPointsToDisplay + this.hand.victoryPoint;
	}
	
	tab.append("<div class='player-name text-center'><h4>" + this.name + "</h4></div>");
	tab.append("<h4 class='text-center'>" + victoryPointsToDisplay
			+ "<img class='player-tab-vp-icon' src='images/icon-victory-point.svg' alt='Victory Point'></h4>");
	tab.append("<div class='panel panel-default'><div class='panel-heading'>"
			+ "<h5 class='panel-title-small'>Hand</h5></div><div class='panel-body'>"
			+ "<p><strong>Resource Cards:</strong> " + this.resourceCards + "</p>"
			+ "<p><strong>Development Cards:</strong> " + this.developmentCards + "</p>"
			+ "<p><strong>Played Knights:</strong> " + this.playedKnights + "</p></div></div>");
	tab.append("<div class='panel panel-default'><div class='panel-heading'>"
			+ "<h5 class='panel-title-small'>Remaining Buildings</h5></div><div class='panel-body'"
			+ "<p><strong>Roads:</strong> " + this.roads + "</p>"
			+ "<p><strong>Settlements:</strong> " + this.settlements + "</p>"
			+ "<p><strong>Cities:</strong> " + this.cities + "</p></div></div>");

	// Add longest road banner if applicable
	if (this.longestRoad) {
		tab.append("<div class='longest-road-banner text-center'><h4>Longest Road"
				+ "<img src='images/icon-road-building.svg' alt='Road Building'></h4></div>");
	}

	// Add largest army banner if applicable
	if (this.largestArmy) {
		tab.append("<div class='largest-army-banner text-center'><h4>Largest Army"
				+ "<img src='images/icon-knight.svg' alt='Knight'></h4></div>");
	}

	// Modify color scheme to fit this player's color
	var rgb = this.rgbColor.r + "," + this.rgbColor.g + "," + this.rgbColor.b;
	tab.css("background-color", "rgba(" + rgb + ",0.2)");

	var panels = $("#p" + this.id + "-tab .panel");
	panels.css("border-color", "rgba(" + rgb + ",0.6)");

	var panelHeadings = panels.children(".panel-heading");
	panelHeadings.css("background-color", "rgba(" + rgb + ",0.4)");
	panelHeadings.css("border-color", "rgba(" + rgb + ",0.6)");

	var longestRoad = $("#p" + this.id + "-tab .longest-road-banner");
	longestRoad.css("background-color", "rgba(" + rgb  + ",0.4)");
	longestRoad.css("border-color", "rgba(" + rgb + ",0.6)");

	var largestArmy = $("#p" + this.id + "-tab .largest-army-banner");
	largestArmy.css("background-color", "rgba(" + rgb + ",0.4)");
	largestArmy.css("border-color", "rgba(" + rgb + ",0.6)");
}

Player.prototype.fillTurnDisplay = function() {
	var displayContainer = $("#turn-display-container");
	displayContainer.append("<div id='" + this.id + "-turn-square' class='turn-square'></div>");

	var width = $("#end-turn-btn").outerWidth() / players.length;
	var height = $("#end-turn-btn").outerWidth() / 4;

	var turnSquare = $("#" + this.id + "-turn-square");
	turnSquare.css("width", width);
	turnSquare.css("height", height);
	turnSquare.css("background-color", this.color);

	if (currentPlayerTurn === this.id) {
		turnSquare.addClass("selected-turn-square");
	}
}

function parsePlayers(playersData) {
	var players = [];

	for (var i = 0; i < playersData.length; i++) {
		var playerData = playersData[i];

		// Construct player from playerData object
		var player = new Player(playerData.id, playerData.name, playerData.color);
		player.victoryPoints = playerData.victoryPoints;
		player.playedKnights = playerData.numPlayedKnights;
		player.roads = playerData.numRoads;
		player.settlements = playerData.numSettlements;
		player.cities = playerData.numCities;
		player.largestArmy = playerData.largestArmy;
		player.longestRoad = playerData.longestRoad;
		player.resourceCards = playerData.numResourceCards;
		player.developmentCards = playerData.numDevelopmentCards;

		players.push(player);
	}

	return players;
}

function fillPlayerHand(handData) {
	var player = playersById[playerId];

	// Add resource cards to this player's hand
	$("#brick-number").text(handData.resources.brick);
	player.hand.brick = handData.resources.brick;

	$("#wood-number").text(handData.resources.wood);
	player.hand.wood = handData.resources.wood;

	$("#ore-number").text(handData.resources.ore);
	player.hand.ore = handData.resources.ore;

	$("#wheat-number").text(handData.resources.wheat);
	player.hand.wheat = handData.resources.wheat;

	$("#sheep-number").text(handData.resources.sheep);
	player.hand.sheep = handData.resources.sheep;

	// Add dev cards to this player's hand
	$("#knight-number").text(handData.devCards["Knight"]);
	player.hand.knight = handData.devCards["Knight"];

	$("#year-of-plenty-number").text(handData.devCards["Year of Plenty"]);
	player.hand.yearOfPlenty = handData.devCards["Year of Plenty"];

	$("#monopoly-number").text(handData.devCards["Monopoly"]);
	player.hand.monopoly = handData.devCards["Monopoly"];

	$("#road-building-number").text(handData.devCards["Road Building"]);
	player.hand.roadBuilding = handData.devCards["Road Building"];

	$("#victory-point-number").text(handData.devCards["Victory Point"]);
	player.hand.victoryPoint = handData.devCards["Victory Point"];
}

function fillPlayerBuyOptions(handData) {
	if (handData.canBuildSettlement) {
		$("#settlement-build-btn").prop("disabled", false);
	} else {
		$("#settlement-build-btn").prop("disabled", true);
	}

	if (handData.canBuildCity) {
		$("#city-build-btn").prop("disabled", false);
	} else {
		$("#city-build-btn").prop("disabled", true);
	}

	if (handData.canBuildRoad) {
		$("#road-build-btn").prop("disabled", false);
	} else {
		$("#road-build-btn").prop("disabled", true);
	}

	if (handData.canBuyDevCard) {
		$("#buy-dev-card-modal-open").prop("disabled", false);
	} else {
		$("#buy-dev-card-modal-open").prop("disabled", true);
	}
}

function createTradeRateText(number) {
	return number + ":1";
}

function fillPlayerTradeRates(rates) {
	$("#brick-trade-rate").text(createTradeRateText(rates.brick));
	$("#wood-trade-rate").text(createTradeRateText(rates.wood));
	$("#ore-trade-rate").text(createTradeRateText(rates.ore));
	$("#wheat-trade-rate").text(createTradeRateText(rates.wheat));
	$("#sheep-trade-rate").text(createTradeRateText(rates.sheep));
	$("#wildcard-trade-rate").text(createTradeRateText(rates.wildcard));
}

function hexToRgb(hex) {
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    var r = parseInt(result[1], 16);
    var g = parseInt(result[2], 16);
    var b = parseInt(result[3], 16);
    return { r: r, g: g, b: b };
}

