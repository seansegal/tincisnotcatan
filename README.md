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

### The Networking Library
The Networking Library is specifically an abstraction for using persistent notions of sessions with websockets. While Jetty provides a `org.eclipse.jetty.websocket.api.Session`, the implementation fails to maintain persistence like HttpSession objects. To solve this problem for user management, this library sets a cookie for all connecting sessions, called "USER_ID", which is an alphanumeric string, 16 characters long. When a session connects to our server side websocket, there are three cases.

    1) The connecting session has no USER_ID cookie.
    2) The connecting session has a USER_ID cookie, and an existing User object holds a reference to a session object whose USER_ID matches the connecting session's USER_ID.
    3) The connecting session has a USER_ID cookie, but we have no "memory" of it.

In 1), we consider this a brand new end-user. We create a new `User` object, assign the `User` a new USER_ID, and send a command to the session's endpoint to set the appropriate cookie.

In 2), we already have a `User` object associated with this USER_ID. This means that we have a new `org.eclipse.jetty.websocket.api.Session` object that connected, but in reality, this represents the same end user that temporarily disconnected. (Due to refresh, page close, websocket error, etc). The package assigns the `User` object the newly connected session, discarding the old reference. This happens without any alert to the user of the library, to create the illusion of persistent websocket sessions.

In 3), either the end user has attempted to maliciously fake a USER_ID for one reason or another, or the USER_ID represents a user that connected before the most recent server reboot. In this case, we behave exactly like 1), and reassign the USER_ID.

The important classes in this package are:

    GCT - (Grand Central Terminal) The top-level Group manager directly instantiated by the end-developer. Using the Builder pattern, the GCT can be configured with: 
``` java
    new GCTBuilder("/action") // the websocket route to enable with Spark
        // optional, how to choose groups for new users.
        .withGroupSelector(<Your GroupSelector>) 
        // optional, a place to put a dynamically updating websocket showing current group information.
        .withGroupViewRoute("/groups") 
        .build();
```
    The GCT provides management of all of the Groups that are active at a given time. 
    
    User - a representation of a single end-user, not to be confused with a Session.
    
    Group - (Interface) A collection of Users that generally have the permission to send messages that affect other Users in the group (Conceptually, people in the same game). A Group must be able to say if it isFull(), or isEmpty() (among other things). A valid implementation of a Group could be a group that is never full, and is the sole Group for the whole server, where all Users end up. In Catan, Groups represent single instances of a game of Catan.

    UserGroup - A concrete implementation of a Group that provides a builder pattern for modular construction by the end-developer. The role of the GroupSelector (below) is to either choose the most appropriate currently-open group, or create a new group that fits the User or developer's specifications. UserGroup allows a great deal of customization and field access to tailor the Group to the specific needs of the game or web app.

    GroupSelector - (Interface) Used to choose the ideal Group (selected from a list of non-full Groups that have at least one User in them in the GCT). The GroupSelector can access any field of the Groups in making this determination, including the consideration of unique identifiers that may have been requested by the end user. (In the case of joining an existing game). The GroupSelector's sole method, selectFor(User u, Collection<Group> c), is intended to find the best Group in c that u should be placed in.

    DistinctRandom - A simple helper class that provides a static method getString(), which provides a guaranteed-unique alphanumeric string for user or group identifiers. 

    RequestProcessor - (Interface) The GCT makes no assumptions about the format of messages that the developer intends to receive from the front end. A RequestProcessor allows the end-developer to programmatically define what messages to accept and how to handle said messages. RequestProcessor provides two method signatures : 

    boolean match(JsonObject j) and
    boolean run(User user, Group g, JsonObject json, API api)

    match() allows the RequestProcessor to indicate if the JsonObject is in such a format that it can be handled. It might check for certain fields, and in turn check if those fields are valid. If match() returns true, then the message sent should be handled by run(...).

    This pattern allows the configuration of the Group to include a collection of RequestProcessors, so each Group, can programmatically define what it's allowed and able to handle. In Catan, the request processors do not vary between instances of Groups (they all handle a game of Catan). But, it's conceivable that as games expand and rules become more complicated, more RequestProcessors with more specific parameters might be needed. Further, while this feature isn't used in Catan as of this writing, it's possible to easily change what RequestProcessors are "active" or listening for messages programmatically, to therefore disable actions not at the API level, but at the Group level. (Say, if a user disconnected).












