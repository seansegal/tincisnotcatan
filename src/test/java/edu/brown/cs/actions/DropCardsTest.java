package edu.brown.cs.actions;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import com.google.gson.JsonObject;

import edu.brown.cs.catan.MasterReferee;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class DropCardsTest {

  @Test
  public void testBadPlayerID() {
    Referee ref = new MasterReferee();
    JsonObject set = new JsonObject();
    JsonObject toDrop = new JsonObject();
    toDrop.addProperty("wood", 0.0);
    toDrop.addProperty("brick", 0.0);
    toDrop.addProperty("sheep", 0.0);
    toDrop.addProperty("ore", 2.0);
    toDrop.addProperty("wheat", 1.0);
    set.add("toDrop", toDrop);
    try {
      FollowUpAction drop = new DropCards(-2, 2);
      drop.setupAction(ref, -2, set);
      assertTrue(false);
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testNotSetUp() {
    Referee ref = new MasterReferee();
    ref.addPlayer("Sean", "Red");
    JsonObject set = new JsonObject();
    JsonObject toDrop = new JsonObject();
    toDrop.addProperty("wood", 0.0);
    toDrop.addProperty("brick", 0.0);
    toDrop.addProperty("sheep", 0.0);
    toDrop.addProperty("ore", 2.0);
    toDrop.addProperty("wheat", 1.0);
    set.add("toDrop", toDrop);
    try {
      FollowUpAction drop = new DropCards(0, 2);
      drop.execute();
      assertTrue(false);
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testDroppingBadCards() {
    Referee ref = new MasterReferee();
    Player p = ref.getPlayerByID(ref.addPlayer("Sean", "Red"));
    JsonObject set = new JsonObject();
    JsonObject toDrop = new JsonObject();
    toDrop.addProperty("wood", 0.0);
    toDrop.addProperty("brick", 0.0);
    toDrop.addProperty("sheep", 0.0);
    toDrop.addProperty("ore", 2.0);
    toDrop.addProperty("wheat", 1.0);
    set.add("toDrop", toDrop);
    p.addResource(Resource.WOOD, 3.0);
    FollowUpAction drop = new DropCards(0, 3.0);
    drop.setupAction(ref, 0, set);
    Map<Integer, ActionResponse> response = drop.execute();
    assertTrue(response.get(0).getSuccess() == false);
  }
  
  @Test
  public void testDroppingImproperNumber() {
    Referee ref = new MasterReferee();
    Player p = ref.getPlayerByID(ref.addPlayer("Sean", "Red"));
    JsonObject set = new JsonObject();
    JsonObject toDrop = new JsonObject();
    toDrop.addProperty("wood", 0.0);
    toDrop.addProperty("brick", 0.0);
    toDrop.addProperty("sheep", 0.0);
    toDrop.addProperty("ore", 1.0);
    toDrop.addProperty("wheat", 1.0);
    set.add("toDrop", toDrop);
    p.addResource(Resource.WOOD, 3.0);
    FollowUpAction drop = new DropCards(0, 3.0);
    drop.setupAction(ref, 0, set);
    Map<Integer, ActionResponse> response = drop.execute();
    assertTrue(response.get(0).getSuccess() == false);
  }

  @Test
  public void testBasicDrop() {
    Referee ref = new MasterReferee();
    Player p = ref.getPlayerByID(ref.addPlayer("Sean", "Red"));
    JsonObject set = new JsonObject();
    JsonObject toDrop = new JsonObject();
    toDrop.addProperty("wood", 0.0);
    toDrop.addProperty("brick", 0.0);
    toDrop.addProperty("sheep", 0.0);
    toDrop.addProperty("ore", 2.0);
    toDrop.addProperty("wheat", 1.0);
    set.add("toDrop", toDrop);
    p.addResource(Resource.ORE, 3.0);
    p.addResource(Resource.WHEAT, 4.0);
    FollowUpAction drop = new DropCards(0, 3.0);
    Collection<FollowUpAction> follow = new ArrayList<>();
    follow.add(drop);
    ref.addFollowUp(follow);
    drop.setupAction(ref, 0, set);
    Map<Integer, ActionResponse> response = drop.execute();
    assertTrue(response.get(0).getSuccess() == true);
    assertTrue(p.getResources().get(Resource.ORE) == 1.0);
    assertTrue(p.getResources().get(Resource.WHEAT) == 3.0);
  }
}
