# catan
For CS032 : Introduction to Software Engineering. A modeling of Settlers of Catan in Java.

# coordinates
http://homepages.inf.ed.ac.uk/rbf/CVonline/LOCAL_COPIES/AV0405/MARTIN/Hex.pdf

# Google Form!
https://docs.google.com/forms/u/0/d/1O8lxl-nhlunTfGOnSRq8g2ccZLpLsR5VHq3jRVlTUEs/edit

# Icons
https://thenounproject.com/term/catan/

# CatanAPI JSON Documentation

### CatanAPI Overview
The CatanAPI provides a simple way to keep track of one game of Settlers of Catan. The API will keep track of all aspects of the game and provide enough JSON formatted information about the game so that it can be represented graphically. The section below outlines how to create a game, set the games settings and then send/receive information about a game. 

### Creating a game
To create a game of Catan, simply instantiate a CatanAPI object. For simplicity, CatanAPI contains only one empty constructor which creates a game with default settings. If you would like to customize with game with various settings, you should immediately call the setSettings method to set the game settings. See more details on the settings object below.

### Game Settings Object
Directly after creating a game, a user has the option to set the game settings using the API setSettings method. This settings function takes in a JsonObject with the following format: 
```javascript
{numPlayers: numPlayers, victoryPoints: victoryPoints, isDecimal: isDecimal, isDynamic: isDynamic, isStandard: isStandard}
```
where:
`numPlayers` is the number of players, currently only 2,3,4 players are supported. The default it 4 players. 
`victoryPoints` is the number of victory points to play to. The default is 10 points.
`isDecimal` indicates whether the game should use decimal resource values. The default is `false`. 
`isDynamic` indicates whether the game should use dynamic exchange rates. This parameter is ignored if `isDecimal = false`. The default is `false`. 
`isStandard` indicates whether the board should be constructed using the Offical Standard Catan board. The default is `false`. When `false`, the game board is constructed randomly.  

### Adding and removing players
Once a game is created and the settings are set, the user should proceed to register players with the API. This is done using the `addPlayer` method which takes a JsonObject `playerAttributes` as a parameter. Currently, the format of this object is:
```javascript
{userName: "player username here"}
```
If this field is missing, an `IllegalArgumentExpception` will be thrown. Upon success, this method returns an integer corresponding to the player's ID. This will be used continually to refer to this player.

To remove a player, use the `removePlayer(int id)` method. This function only succeeds if the game has not yet started. If this method is called when a game is in progress an `UnsupportedOperationException` is thrown. This method returns `true` on success, indicating that the player has, in fact, been removed from the game. 

### Getting the game state
In order to get information about the current game state, call the `getGameState` method which returns a GameState JsonObject. The GameState object has the following structure: 
```javascript
    {playerID: playerID, 
    turnOrder: [1,3,2,4], 
    currentTurn: currentTurn,
    winner: winner, 
    settings: {Settings object},
    followUp: {FollowUp object},
    stats: {Stats object},
    players: [{Player object}, {Player object}, {Player object}, {Player object}],
    hand: {Hand object}, 
    board: {Board object}}
```
where 
`playerID` is the player ID of the player whose data we are sending
`turnOrder` is an array corresponding to the turn order (using player ids to refer to players)
`currentTurn` is the player ID of the current players turn
`winner` that exists if and only if there is a game winner, in which case it will be set to the winner's ID
`settings` is the same settings object passed into setSettings() will default fields now filled in with their default values. 

`followUp` is an object of the form:
``` javascript
{actionName: actionName, actionData: {}}
```
where `actionName` is a predefined FollowUpAction with `actionData` as its corresponding data object

`stats` is an object of the form:
```javascript
{turn: currentTurn, rolls: [1,2,3,4,5,6,5,4,3,2,1]}
```
where `turn` is the current turn number and `rolls` is an array of size 11 corresponding to the roll distribution of the current game

`players` is an array of player objects corresponding to each player. The `player` object has the form: 
``` javascript
{name: name, id: playerID, color: hexColor, 
numSettlements: numSettlementsRemaining, numCities: numCitiesRemaining, 
numPlayedKnights: numPlayedKnights, numRoads: numRoads, longestRoad: true, largestArmy: false, 
victoryPoints: publicVictoryPoints, numResourceCards: numResourceCards, numDevelopmentCards: numDevelopmentCards, 
rates: {wheat: 2, sheep: 4, ore: 4, wood: 4, brick: 4}}
```
This is each players public data.

`hand` is a private object that should be sent to each player individually. It is of the form
``` javascript
{resources: {wheat: 2, brick: 1, ore:  0, ...},
devCards: {monopoly: 0, ...},
canBuildSettlement: true/false,
canBuildRoad: true/false,
canBuildCity: true/false,
canBuyDevCard: true/false}
```

`board` is a public object that represents the current boards state. It is of the form: 
``` javascript
```

### Actions
Actions are the only way to change the game state. They are all called by using the CatanAPI's performAction method. Both Actions and FollowUpActions are performed using this function. Currently, the API supports the follow Actions and FollowUpActions:


