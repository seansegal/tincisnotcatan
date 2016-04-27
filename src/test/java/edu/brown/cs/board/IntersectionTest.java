package edu.brown.cs.board;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.catan.HumanPlayer;
import edu.brown.cs.catan.MasterReferee;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class IntersectionTest {

  @Test
  public void InitializationTest() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    Intersection inter = new Intersection(i);

    assertTrue(inter != null);
    assertTrue(inter.getBuilding() == null);
    assertTrue(inter.getPort() == null);
    assertTrue(inter.getPaths().size() == 0);
    assertTrue(inter.getPosition().equals(i));
  }

  @Test
  public void EqualityTest() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    Intersection inter = new Intersection(i);

    HexCoordinate m1 = new HexCoordinate(1, 1, 1);
    HexCoordinate m2 = new HexCoordinate(2, 2, 1);
    HexCoordinate m3 = new HexCoordinate(-1, 0, -1);
    IntersectionCoordinate i2 = new IntersectionCoordinate(m1, m2, m3);
    Intersection inter2 = new Intersection(i2);

    assertTrue(i.equals(i2));
    assertTrue(inter.equals(inter2));
  }

  @Test
  public void HashTest() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    Intersection inter = new Intersection(i);

    HexCoordinate m1 = new HexCoordinate(1, 1, 1);
    HexCoordinate m2 = new HexCoordinate(2, 2, 1);
    HexCoordinate m3 = new HexCoordinate(-1, 0, -1);
    IntersectionCoordinate i2 = new IntersectionCoordinate(m1, m2, m3);
    Intersection inter2 = new Intersection(i2);

    assertTrue(inter.hashCode() == inter2.hashCode());
    assertTrue(inter.hashCode() == inter.hashCode());

  }

  @Test
  public void PathTest() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    Intersection inter = new Intersection(i);

    HexCoordinate m1 = new HexCoordinate(1, 1, 1);
    HexCoordinate m2 = new HexCoordinate(2, 2, 1);
    HexCoordinate m3 = new HexCoordinate(-1, 0, -1);
    IntersectionCoordinate i2 = new IntersectionCoordinate(m1, m2, m3);
    Intersection inter2 = new Intersection(i2);

    Path p = new Path(inter, inter2);

    assertTrue(inter.getPaths().size() == 1);
    assertTrue(inter.getPaths().contains(p));
    assertTrue(inter2.getPaths().size() == 1);
    assertTrue(inter2.getPaths().contains(p));
  }

  @Test
  public void PortTest() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    Intersection inter = new Intersection(i);
    Port p = new Port(Resource.WHEAT);

    assertTrue(inter.getPort() == null);
    inter.setPort(p);
    assertTrue(inter.getPort() != null);
    assertTrue(inter.getPort().getResource() == Resource.WHEAT);
  }

  @Test
  public void BuildingTest() {
    Referee ref = new MasterReferee();
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    Intersection inter = new Intersection(i);
    Player p = new HumanPlayer(1, "name", "000000");

    assertTrue(inter.canPlaceSettlement(ref));
    assertTrue(!inter.canPlaceCity(p));
    inter.placeSettlement(p);
    assertTrue(inter.getBuilding().getPlayer().equals(p));
    assertTrue(inter.canPlaceCity(p));
    assertTrue(!inter.canPlaceSettlement(ref));
    inter.placeCity(p);
    assertTrue(inter.getBuilding().getPlayer().equals(p));
    assertTrue(!inter.canPlaceCity(p));
  }
}
