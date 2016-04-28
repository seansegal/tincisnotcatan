function Player(id, name, color) {
	this.id = id;
	this.name = name;
	this.color = color;

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
	$("#player-tabs").append("<li role='presentation'><a href='#p" + this.id + "-tab' aria-controls='home' "
			+ "role='tab' data-toggle='tab'>P" + this.id + "</a></li>");
	$("#player-tabs-content").append("<div role='tabpanel' class='tab-pane' id='p" + this.id + "-tab'></div>");

	var tab = $("#p" + this.id + "-tab");
	tab.empty();
	
	tab.append("<p><strong>Name:</strong> " + this.name + "</p>");
	tab.append("<div class='circle player-circle' style='background-color: " + this.color + "'></div>");
	tab.append("<p><strong>Victory Points:</strong> " + this.victoryPoints + "</p>");
	tab.append("<p><strong>Resource Cards:</strong> " + this.resourceCards + "</p>");
	tab.append("<p><strong>Development Cards:</strong> " + this.developmentCards + "</p>");
	tab.append("<p><strong>Played Knights:</strong> " + this.playedKnights + "</p>");
	tab.append("<p><strong>Roads:</strong> " + this.roads + "</p>");
	tab.append("<p><strong>Settlements:</strong> " + this.settlements + "</p>");
	tab.append("<p><strong>Cities:</strong> " + this.cities + "</p>");
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

	$("#knight-number").text(handData.devCards["Knight"]);
	$("#year-of-plenty-number").text(handData.devCards["Year of Plenty"]);
	$("#monopoly-number").text(handData.devCards["Monopoly"]);
	$("#road-building-number").text(handData.devCards["Road Building"]);
	$("#victory-point-number").text(handData.devCards["Victory Point"]);
}

function fillPlayerBuyOptions(handData) {
	// if (handData.canBuildSettlement) {
	// 	$("#settlement-build-btn").prop("disabled", false);
	// } else {
	// 	$("#settlement-build-btn").prop("disabled", true);
	// }

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

