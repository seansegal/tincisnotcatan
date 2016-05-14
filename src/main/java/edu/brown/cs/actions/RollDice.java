package edu.brown.cs.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;

import edu.brown.cs.board.Tile;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;
import edu.brown.cs.catan.Settings;

/**
 * Action responsible for rolling the dice.
 * 
 * @author anselvahle
 *
 */
public class RollDice implements FollowUpAction {

  private Player _player;
  private final int _playerID;
  private Referee _ref;
  private static final String VERB = "start the next turn.";
  private static final String ID = "rollDice";
  private boolean _isSetUp = false;

  public RollDice(Referee ref, int playerID) {
    assert ref != null;
    _ref = ref;
    _playerID = playerID;
    _player = _ref.getPlayerByID(playerID);
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
    if (!ref.currentPlayer().equals(_player)) {
      throw new IllegalArgumentException();
    }
    _isSetUp = true;
  }

  public RollDice(int playerID) {
    _playerID = playerID;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    if (!_isSetUp) {
      throw new UnsupportedOperationException(
          "A FollowUpAction must be setup before executed.");
    }
    Random r = new Random();
    PrimitiveIterator.OfInt rolls = r.ints(1, 7).iterator();
    int diceRoll = rolls.nextInt() + rolls.nextInt();
    _ref.getGameStats().addRoll(diceRoll);
    Map<Integer, Map<Resource, Integer>> playerResourceCount = new HashMap<>();
    Map<Integer, ActionResponse> toRet = new HashMap<>();

    if (diceRoll != 7) {
      Collection<Tile> tiles = _ref.getBoard().getTiles();
      // Iterate through tiles on the board
      for (Tile t : tiles) {
        // If the tile matches the roll and does not have the robber
        if (t.getRollNumber() == diceRoll && !t.hasRobber()) {
          // Find out who should collect what from the intersections
          Map<Integer, Map<Resource, Integer>> fromTile = t
              .notifyIntersections();
          // Iterate through this and consolidate collections for each person
          for (int playerID : fromTile.keySet()) {
            if (!playerResourceCount.containsKey(playerID)) {
              playerResourceCount.put(playerID,
                  new HashMap<Resource, Integer>());
            }
            Map<Resource, Integer> resourceCount = fromTile.get(playerID);
            Map<Resource, Integer> playerCount = playerResourceCount
                .get(playerID);
            for (Resource res : resourceCount.keySet()) {
              if (playerCount.containsKey(res)) {
                // Update the count
                playerCount.replace(res,
                    playerCount.get(res) + resourceCount.get(res));
              } else {
                playerCount.put(res, resourceCount.get(res));
              }
              // Make sure the player collects the resource
              _ref.getPlayerByID(playerID).addResource(res,
                  resourceCount.get(res), _ref.getBank());
            }
          }
        }
      }
      for (Integer playerID : playerResourceCount.keySet()) {
        StringBuilder message = new StringBuilder();
        message.append(String.format("%d was rolled", diceRoll));
        Map<Resource, Integer> resourceCount = playerResourceCount
            .get(playerID);
        for (Resource res : resourceCount.keySet()) {
          switch (res) {
          case WHEAT:
            message.append(String.format(", you received %d wheat",
                resourceCount.get(res)));
            break;
          case SHEEP:
            message.append(String.format(", you received %d sheep",
                resourceCount.get(res)));
            break;
          case ORE:
            message.append(String.format(", you received %d ore",
                resourceCount.get(res)));
            break;
          case BRICK:
            message.append(String.format(", you received %d brick",
                resourceCount.get(res)));
            break;
          case WOOD:
            message.append(String.format(", you received %d wood",
                resourceCount.get(res)));
            break;
          default:
            message.append(".");
            break;
          }
        }
        message.append(".");
        ActionResponse toAdd = new ActionResponse(true, message.toString(),
            resourceCount);
        toRet.put(playerID, toAdd);
      }
      for (Player p : _ref.getPlayers()) {
        if (!toRet.containsKey(p.getID())) {
          ActionResponse toAdd = new ActionResponse(true, String.format(
              "%d was rolled.", diceRoll), new HashMap<Resource, Integer>());
          toRet.put(p.getID(), toAdd);
        }
      }
    } else {
      // 7 is rolled:
      Map<Integer, Double> playersToDrop = new HashMap<>();
      Map<Integer, JsonObject> jsonToSend = new HashMap<>();
      String message = "7 was rolled.";
      for (Player p : _ref.getPlayers()) {
        if (p.getNumResourceCards() > Settings.DROP_CARDS_THRESH) {
          double numToDrop = p.getNumResourceCards() / 2.0;
          if (!_ref.getGameSettings().isDecimal) {
            numToDrop = Math.floor(numToDrop);
          }
          playersToDrop.put(p.getID(), numToDrop);
          message += String.format(" %s must discard cards", p.getName());
          JsonObject jsonForPlayer = new JsonObject();
          jsonForPlayer.addProperty("numToDrop", numToDrop);
          jsonToSend.put(p.getID(), jsonForPlayer);
        }
      }
      if (playersToDrop.size() > 0) {
        Collection<FollowUpAction> followUps = new ArrayList<>();
        message += ".";
        for (Player p : _ref.getPlayers()) {
          if (playersToDrop.containsKey(p.getID())) {
            followUps
                .add(new DropCards(p.getID(), playersToDrop.get(p.getID())));
            toRet.put(p.getID(),
                new ActionResponse(true,
                    "7 was rolled. You must drop half of your cards.",
                    jsonToSend.get(p.getID())));
          } else {
            toRet.put(p.getID(), new ActionResponse(true, message, null));
          }
        }
        _ref.addFollowUp(followUps);
      } else {
        ActionResponse respToAll = new ActionResponse(true,
            "7 was rolled. No one has more than 7 cards.", null);
        ActionResponse respToPlayer = new ActionResponse(true,
            "7 was rolled. You get to move the Robber.", null);
        for (Player p : _ref.getPlayers()) {
          if (p.equals(_player)) {
            toRet.put(p.getID(), respToPlayer);
          } else {
            toRet.put(p.getID(), respToAll);
          }
        }
      }
      // Follow up MoveRobber action:
      _ref.addFollowUp(ImmutableList.of(new MoveRobber(_player.getID(), false, true)));
    }
    _ref.removeFollowUp(this);
    return toRet;
  }

  @Override
  public JsonObject getData() {
    JsonObject toRet = new JsonObject();
    toRet.addProperty("message", "Please roll the dice");
    return toRet;
  }

  @Override
  public String getID() {
    return ID;
  }

  @Override
  public int getPlayerID() {
    return _playerID;
  }

  @Override
  public void setupAction(Referee ref, int playerID, JsonObject params) {
    if (playerID != _playerID) {
      throw new IllegalArgumentException();
    }
    assert ref != null;
    _ref = ref;
    _player = _ref.getPlayerByID(playerID);
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
    if (!ref.currentPlayer().equals(_player)) {
      throw new IllegalArgumentException();
    }
    _isSetUp = true;
  }

  @Override
  public String getVerb() {
    return VERB;
  }
}
