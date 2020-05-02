# Catan
An online version of Settlers of Catan with the option to play with Advanced Economic features. Originally built for CS32: Introduction to Software Engineering final project.  

Visit http://tinc.herokuapp.com/ to try out the game!

## Contributing
Contributions are welcome! To run locally, build the Docker image with 
`docker build -t catan .`

Then run the application with, 
`docker run --rm --name catan -d -p 4567:4567 --cpus=4 catan`

Once running, the application will be available at `localhost:4567`.

## Deployment
The latest code on `master` automatically deployed to our staging site: `http://stormy-mesa-37166.herokuapp.com/home`.

The staging site is pushed to production (`tinc.herokuapp.com`) manually. It will only be updated when there are no users playing Catan.

In the future, CI and CD should be setup to automatically test new builds and deploy upgrades.

## End-User Documentation

### Home Screen
The first thing you will see is a home screen with a single text input for your username. Once a username has been input, you will be brought to a screen where you have the option to create a new game or join an existing game. To join an existing game, simply click the `Join Game` button next to the game that you wish to enter. To create a new game, first enter the name of the game to display to other players, the number of players to allow in the game, the number of victory points to play to, and which advanced economic features to enable.

The decimal resources option enables resource amounts accurate to two decimal places. This allows players to trade fractional resource amounts with each other and the bank.

The dynamic exchange rates option is only available if the decimal resources option is selected. When dynamic exchange rates are enabled, the bank trade rates will vary based off the current resources in players's hands.

### Beginning the Game
Once a player has created a game or joined an existing game, they are taken to the game board. Once all players have joined, a random turn order is generated and the game begins. As per the official rules, the first player must place a settlement and road, then then the second player, and so on until the last player places their first settlement and road. Then, the last player will place their second settlement and road, then the second to last player, and so on until the first player places their second settlement and road.

### Turn Flow
Once all players have placed their two initial settlements, the first players turn begins. On a player's turn, the player has the option of building a road, settlement, city, or development card, proposing a trade with other players, trading with the bank, or playing a development card. The player can take as many of these actions as they choose on a single turn, with the restriction that only one development card can be played per turn. When a player is ready to end their turn, they should press the 'End Turn' button to move to the next player's turn.

### Player Tab
The first tab on the right is the `Player Tab`. This tab displays all player information, including number of victory points, total number of resource and development cards, and number of buildings left. If a player has longest road or largest army, a banner will be displayed in their player tab.

### Build Tab
The second tab on the right is the `Build Tab`. This tab displays the costs for roads, settlements, cities, and development cards. If the player does not have enough resources to buy a building, the build button will be disabled. When a build button is clicked, the available locations on the board are highlighted. Clicking on one of these highlighted locations will build the appropriate building at that location.

### Trade Tab
The third tab on the right is the `Trade Tab`. This tab has two subtabs - one for trading with other players, and one for trading with the bank. If you would like to trade with other players, enter your desired resources to give and desired resources to receive. Entering a negative number indicates a resource to give and entering a positive number indicates a resource to receive. When you are ready to propose the trade to all players, you should hit the `Propose Trade` button.

At this point, all other players will see the proposed trade and are given the option to accept or reject the trade. As other players accept or reject the trade, you will see their response. If a player accepts the trade, you will be given the option to finalize the trade with them, at which points the appropriate resources will be added and removed from your hand. If you would like to cancel the trade at any point, simply press the `Cancel Trade` button and all other players will be notified that the trade has been canceled.

To trade with the bank, select the resources that you would like to give and receive. The trade rate between these resources is displayed directly below the resource to give. Enter the number of resources that you would like to receive, and the number of resources you must give will be automatically calculated based off this trade rate. If you would like to trade, click the `Trade` button and resources will automatically be added to and removed from your hand.

### Development Cards
The number of resource and development cards in your hand are displayed in the bar at the bottom of the screen. Hovering over a development card displays instructions on how to use it. To play a development card, click the appropriate development card icon.

If a Knight is played, all tiles that the robber can move to will be highlighted. Clicking on a highlighted tile will move the robber to that tile. If a player has a building adjacent to this tile, a dialogue will pop up and the current player can select a player to take a random card from.

If Year of Plenty is played, the player can select exactly two resources to add to their hand. If the decimal resources option is enabled, fractional amounts of resources can be selected, as long as the total addes up to exactly two.

If Monopoly is played, the player can select a resource to steal from all other players.

If Road Building is played, the player can build two roads in the same manner as when the `Build Road` button is pressed.

Victory points are automatically added to the victory point count in your player tab, but are not visible to other players.

## Documentation: The CatanAPI

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
    {intersections: [Intersection object],
     tiles: [Tile object],
     paths: [Path object]}

     intersection: {canBuildSettlement: true/false,
                    coordinate: {hex coordinate}
                    building: 0 // Player id & exists only if there is a building}

    tile: {hasRobber: true/false, 
           number: 5, //number to roll
            type: "WHEAT" //resource type}

    path: { canBuildRoad: true/false,
            road: 2 // Player id & exists only if there is a building
            start: {hex cooridnate},
            end: {hex coordinate}}         
```

### Actions
Actions are the only way to change the game state. They are all called by using the CatanAPI's performAction method. Both Actions and FollowUpActions are performed using this function. Currently, the API supports the follow Actions and FollowUpActions:

#### Actions: Can be sent on a players turn, as long as there are no pending FollowUpActions that must be performed first. 
* buildRoad
* buildSettlement
* buildCity
* buyDevCard
* playMonopoly
* playKnight
* playYearOfPlenty
* playRoadBuilding
* tradeWithBank
* proposeTrade (used for interplayer trading)
* startGame (called when the game should be started)
* endTurn


#### FollowUpActions: Can only be sent when the API is currently waiting of this FollowUpAction:
* moveRobber
* takeCard
* dropCards
* rollDice
* knightOrDice (sent when a player must choose between a Knight or Dice)
* reviewTrade (used to accept or decline a propsed trade)
* tradeResponse (used to finalize a trade)




## Documentation: The Networking Library
The Networking Library is specifically an abstraction for using persistent notions of sessions with websockets. While Jetty provides a `org.eclipse.jetty.websocket.api.Session`, the implementation fails to maintain persistence like `HttpSession` objects. To solve this problem for user management, this library sets a cookie for all connecting sessions, called "USER\_ID", which is an alphanumeric string, 16 characters long. When a session connects to our server side websocket, there are three cases.

    1. The connecting session has no USER\_ID cookie.
    2. The connecting session has a USER\_ID cookie, and an existing User object holds a reference to a session object whose USER\_ID matches the connecting session's USER\_ID.
    3. The connecting session has a USER_ID cookie, but we have no "memory" of it.

In 1), we consider this a brand new end-user. We create a new `User` object, assign the `User` a new USER\_ID, and send a command to the session's endpoint to set the appropriate cookie.

In 2), we already have a `User` object associated with this USER\_ID. This means that we have a new `org.eclipse.jetty.websocket.api.Session` object that connected, but in reality, this represents the same end user that temporarily disconnected. (Due to refresh, page close, websocket error, etc). The package assigns the `User` object the newly connected session, discarding the old reference. This happens without any alert to the user of the library, to create the illusion of persistent websocket sessions.

In 3), either the end user has attempted to maliciously fake a USER\_ID for one reason or another, or the USER\_ID represents a user that connected before the most recent server reboot. In this case, we behave exactly like 1), and reassign the USER\_ID.

The important classes in this package are:

`GCT` - (Grand Central Terminal) The top-level Group manager directly instantiated by the end-developer. Using the Builder pattern, the GCT can be configured with: 
    
``` java
    new GCTBuilder("/action") // the websocket route to enable with Spark
        // optional, how to choose groups for new users.
        .withGroupSelector(<Your GroupSelector>) 
        // optional, a place to put a dynamically updating websocket showing current group information.
        .withGroupViewRoute("/groups") 
        .build();
```

The `GCT` provides management of all of the `Group`s that are active at the current time. 
    
`User` - a representation of a single end-user, not to be confused with a `Session`.
    
`Group` - (Interface) A collection of `User`s that generally have the permission to send messages that affect other `User`s in the `Group` (Conceptually, people in the same game). A `Group` must be able to say if it `isFull()`, or `isEmpty()` (among other things). A valid implementation of a `Group` could be a group that is never full, and is the sole `Group` for the whole server, where all Users end up. In Catan, `Group`s represent single instances of a game of Catan.

`UserGroup` - A concrete implementation of a `Group` that provides a builder pattern for modular construction by the end-developer. The role of the `GroupSelector` (below) is to either choose the most appropriate currently-open group, or create a new group that fits the User or developer's specifications. `UserGroup` allows a great deal of customization and field access to tailor the `Group` to the specific needs of the game or web app.

`GroupSelector` - (Interface) Used to choose the ideal `Group` (selected from a list of non-full `Group`s that have at least one `User` in them in the `GCT`). The `GroupSelector` can access any field of the `Group`s in making this determination, including the consideration of unique identifiers that may have been requested by the end user. (In the case of joining an existing game). The `GroupSelector`'s sole method, `selectFor(User u, Collection<Group> c)`, is intended to find the best `Group` in `c` that `u` should be placed in.

`DistinctRandom` - A simple helper class that provides a static method `getString()`, which provides a guaranteed-unique alphanumeric string for user or group identifiers. 

`RequestProcessor` - (Interface) The `GCT` makes no assumptions about the format of messages that the developer intends to receive from the front end. A `RequestProcessor` allows the end-developer to programmatically define what messages to accept and how to handle said messages. `RequestProcessor` provides two method signatures : 
``` java
boolean match(JsonObject j) and
boolean run(User user, Group g, JsonObject json, API api)
```
`match()` allows the RequestProcessor to indicate if the `JsonObject` is in such a format that it can be handled. It might check for certain fields, and in turn check if those fields are valid. If `match()` returns true, then the message sent should be handled by `run()`.

This pattern allows the configuration of the `Group` to include a collection of `RequestProcessors`, so each `Group`, can programmatically define what it's allowed and able to handle. In Catan, the processors do not vary between instances of `Group`s (they all handle a game of Catan). But, it's conceivable that as games expand and rules become more complicated, more RequestProcessors with more specific parameters might be needed. Further, while this feature isn't used in Catan as of this writing, it's possible to easily change what `RequestProcessors` are "active" or listening for messages programmatically, to therefore disable actions not at the API level, but at the `Group` level. (Say, if a user disconnected).


# Helpful links 

#### Coordinates
http://homepages.inf.ed.ac.uk/rbf/CVonline/LOCAL_COPIES/AV0405/MARTIN/Hex.pdf

#### Google Form for Requirements
https://docs.google.com/forms/u/0/d/1O8lxl-nhlunTfGOnSRq8g2ccZLpLsR5VHq3jRVlTUEs/edit

#### Icons Source
https://thenounproject.com/term/catan/









