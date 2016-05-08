package edu.brown.cs.actions;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.board.IntersectionCoordinate;
import edu.brown.cs.board.PathCoordinate;
import edu.brown.cs.catan.MasterReferee;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class BuildRoadTest {

  @Test
  public void testBadPlayerID() {
    Referee ref = new MasterReferee();
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(1, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 1, 0);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    HexCoordinate h4 = new HexCoordinate(0, 0, 0);
    HexCoordinate h5 = new HexCoordinate(0, 1, 1);
    HexCoordinate h6 = new HexCoordinate(0, 1, 0);
    IntersectionCoordinate i2 = new IntersectionCoordinate(h4, h5, h6);
    try {
      new BuildRoad(ref, -2, i, i2, false);
      assertTrue(false);
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

  }

  @Test
  public void testBadIntersectionInput() {
    Referee ref = new MasterReferee();
    ref.addPlayer("Sean", "Red");
    HexCoordinate h1 = new HexCoordinate(0, 3, 0);
    HexCoordinate h2 = new HexCoordinate(-1, 2, 0);
    HexCoordinate h3 = new HexCoordinate(-1, 3, 0);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    HexCoordinate h4 = new HexCoordinate(0, 0, 0);
    HexCoordinate h5 = new HexCoordinate(0, 1, 1);
    HexCoordinate h6 = new HexCoordinate(0, 1, 0);
    IntersectionCoordinate i2 = new IntersectionCoordinate(h4, h5, h6);
    try {
      new BuildRoad(ref, 0, null, i2, false);
      assertTrue(false);
    } catch (AssertionError e) {
      assertTrue(true);
    } catch (IllegalArgumentException e2) {
      assertTrue(true);
    }
    try {
      new BuildRoad(ref, 0, i, null, false);
      assertTrue(false);
    } catch (AssertionError e) {
      assertTrue(true);
    } catch (IllegalArgumentException e2) {
      assertTrue(true);
    }
    try {
      new BuildRoad(ref, 0, i, i2, false);
      assertTrue(false);
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testBadPath() {
    Referee ref = new MasterReferee();
    ref.addPlayer("Sean", "Red");
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(1, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 1, 0);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    HexCoordinate h4 = new HexCoordinate(0, 0, 0);
    HexCoordinate h5 = new HexCoordinate(1, 0, 0);
    HexCoordinate h6 = new HexCoordinate(1, 0, 1);
    IntersectionCoordinate i2 = new IntersectionCoordinate(h4, h5, h6);
    try {
      new BuildRoad(ref, 0, i, i2, false);
      assertTrue(false);
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testNoSettlement() {
    Referee ref = new MasterReferee();
    Player p = ref.getPlayerByID(ref.addPlayer("Sean", "Red"));
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 1, 1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    HexCoordinate h4 = new HexCoordinate(0, 0, 0);
    HexCoordinate h5 = new HexCoordinate(0, 1, 0);
    HexCoordinate h6 = new HexCoordinate(1, 1, 0);
    IntersectionCoordinate i2 = new IntersectionCoordinate(h4, h5, h6);
    Map<Integer, ActionResponse> response = new BuildRoad(ref, 0, i, i2, false)
        .execute();
    assertTrue(response.get(0).getSuccess() == false);
  }

  @Test
  public void testNoRoadsLeft() {
    Referee ref = new MasterReferee();
    Player p = ref.getPlayerByID(ref.addPlayer("Sean", "Red"));
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 1, 1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    HexCoordinate h4 = new HexCoordinate(0, 0, 0);
    HexCoordinate h5 = new HexCoordinate(0, 1, 0);
    HexCoordinate h6 = new HexCoordinate(1, 1, 0);
    IntersectionCoordinate i2 = new IntersectionCoordinate(h4, h5, h6);
    new BuildSettlement(ref, 0, i, false).execute();
    p.useRoad();
    p.useRoad();
    p.useRoad();
    p.useRoad();
    p.useRoad();
    p.useRoad();
    p.useRoad();
    p.useRoad();
    p.useRoad();
    p.useRoad();
    p.useRoad();
    p.useRoad();
    p.useRoad();
    p.useRoad();
    p.useRoad();
    Map<Integer, ActionResponse> response = new BuildRoad(ref, 0, i, i2, false)
        .execute();
    assertTrue(response.get(0).getSuccess() == false);
  }

  @Test
  public void testBuildBasicRoad() {
    Referee ref = new MasterReferee();
    ref.addPlayer("Sean", "Red");
    ref.getPlayerByID(0).addResource(Resource.BRICK);
    ref.getPlayerByID(0).addResource(Resource.SHEEP);
    ref.getPlayerByID(0).addResource(Resource.WOOD);
    ref.getPlayerByID(0).addResource(Resource.WHEAT);
    ref.getPlayerByID(0).addResource(Resource.BRICK);
    ref.getPlayerByID(0).addResource(Resource.WOOD);
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 1, 1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    HexCoordinate h4 = new HexCoordinate(0, 0, 0);
    HexCoordinate h5 = new HexCoordinate(0, 1, 0);
    HexCoordinate h6 = new HexCoordinate(1, 1, 0);
    IntersectionCoordinate i2 = new IntersectionCoordinate(h4, h5, h6);
    new BuildSettlement(ref, 0, i, true).execute();
    Map<Integer, ActionResponse> response = new BuildRoad(ref, 0, i, i2, true)
        .execute();
    assertTrue(response.get(0).getSuccess() == true);
    assertTrue(ref.getPlayerByID(0).getResources().get(Resource.WHEAT) == 0);
    assertTrue(ref.getPlayerByID(0).getResources().get(Resource.BRICK) == 0);
    assertTrue(ref.getPlayerByID(0).getResources().get(Resource.WOOD) == 0);
    assertTrue(ref.getPlayerByID(0).getResources().get(Resource.SHEEP) == 0);
    assertTrue(ref.getBoard().getPaths().get(new PathCoordinate(i, i2))
        .getRoad() != null);
  }

}
