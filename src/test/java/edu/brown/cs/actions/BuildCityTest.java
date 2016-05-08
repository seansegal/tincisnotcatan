package edu.brown.cs.actions;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.board.IntersectionCoordinate;
import edu.brown.cs.catan.MasterReferee;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class BuildCityTest {

  @Test
  public void testBadPlayerID() {
    Referee ref = new MasterReferee();
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(1, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 1, 0);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    try {
      new BuildSettlement(ref, -2, i, false);
      assertTrue(false);
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

  }

  @Test
  public void testBadIntersection() {
    Referee ref = new MasterReferee();
    ref.addPlayer("Sean", "Red");
    HexCoordinate h1 = new HexCoordinate(0, 3, 0);
    HexCoordinate h2 = new HexCoordinate(-1, 2, 0);
    HexCoordinate h3 = new HexCoordinate(-1, 3, 0);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    try {
      new BuildCity(ref, 0, i);
      assertTrue(false);
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testNoCitiesLeft() {
    Referee ref = new MasterReferee();
    Player p = ref.getPlayerByID(ref.addPlayer("Sean", "Red"));
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 1, 1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    p.useSettlement();
    p.useCity();
    p.useSettlement();
    p.useCity();
    p.useSettlement();
    p.useCity();
    p.useSettlement();
    p.useCity();
    Map<Integer, ActionResponse> response = new BuildCity(ref, 0, i).execute();
    assertTrue(response.get(0).getSuccess() == false);
  }

  @Test
  public void testBuildBasicCity() {
    Referee ref = new MasterReferee();
    ref.addPlayer("Sean", "Red");
    ref.getPlayerByID(0).addResource(Resource.BRICK);
    ref.getPlayerByID(0).addResource(Resource.SHEEP);
    ref.getPlayerByID(0).addResource(Resource.WOOD);
    ref.getPlayerByID(0).addResource(Resource.WHEAT);
    ref.getPlayerByID(0).addResource(Resource.WHEAT);
    ref.getPlayerByID(0).addResource(Resource.WHEAT);
    ref.getPlayerByID(0).addResource(Resource.ORE);
    ref.getPlayerByID(0).addResource(Resource.ORE);
    ref.getPlayerByID(0).addResource(Resource.ORE);

    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 1, 1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    new BuildSettlement(ref, 0, i, true).execute();
    new BuildCity(ref, 0, i).execute();
    assertTrue(ref.getPlayerByID(0).getResources().get(Resource.WHEAT) == 0);
    assertTrue(ref.getPlayerByID(0).getResources().get(Resource.BRICK) == 0);
    assertTrue(ref.getPlayerByID(0).getResources().get(Resource.WOOD) == 0);
    assertTrue(ref.getPlayerByID(0).getResources().get(Resource.SHEEP) == 0);
    assertTrue(ref.getPlayerByID(0).getResources().get(Resource.ORE) == 0);
    assertTrue(ref.getBoard().getIntersections().get(i).getBuilding() != null);
  }

}
