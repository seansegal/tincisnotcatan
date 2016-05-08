package edu.brown.cs.actions;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.google.gson.JsonObject;

import edu.brown.cs.catan.GameSettings;
import edu.brown.cs.catan.MasterReferee;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Referee.GameStatus;
import edu.brown.cs.catan.Resource;

public class BuyDevelopmentCardTest {
  
  @Test
  public void testBadPlayerID() {
    Referee ref = new MasterReferee();
    try {
      new BuyDevelopmentCard(ref, -2);
      assertTrue(false);
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testEmptyDeck() {
    JsonObject set = new JsonObject();
    set.addProperty("numPlayers", 1);
    Referee ref = new MasterReferee(new GameSettings(set));
    ref.addPlayer("Sean", "Red");
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getDevCard();
    ref.getPlayerByID(0).addResource(Resource.ORE);
    ref.getPlayerByID(0).addResource(Resource.SHEEP);
    ref.getPlayerByID(0).addResource(Resource.WHEAT);
    Map<Integer, ActionResponse> response = new BuyDevelopmentCard(ref, 0)
        .execute();
    assertTrue(response.get(0).getSuccess() == false);
  }

  @Test
  public void testCantAfford() {
    JsonObject set = new JsonObject();
    set.addProperty("numPlayers", 1);
    Referee ref = new MasterReferee(new GameSettings(set));
    ref.addPlayer("Sean", "Red");
    ;
    Map<Integer, ActionResponse> response = new BuyDevelopmentCard(ref, 0)
        .execute();
    assertTrue(response.get(0).getSuccess() == false);
  }

  @Test
  public void testBasicBuy() {
    JsonObject set = new JsonObject();
    set.addProperty("numPlayers", 1);
    Referee ref = new MasterReferee(new GameSettings(set));
    ref.addPlayer("Sean", "Red");
    ref.setGameStatus(GameStatus.PROGRESS);
    ref.getPlayerByID(0).addResource(Resource.ORE);
    ref.getPlayerByID(0).addResource(Resource.SHEEP);
    ref.getPlayerByID(0).addResource(Resource.WHEAT);
    Map<Integer, ActionResponse> response = new BuyDevelopmentCard(ref, 0)
        .execute();
    assertTrue(response.get(0).getSuccess());
    assertTrue(ref.getPlayerByID(0).getNumDevelopmentCards() == 1);
  }

  
}
