package edu.brown.cs.catan;

import edu.brown.cs.actions.BuildCity;
import edu.brown.cs.actions.BuildRoad;
import edu.brown.cs.actions.BuildSettlement;
import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.board.IntersectionCoordinate;

/**
 * Used for Testing purposes.
 *
 */
public class TestReferee extends MasterReferee {

  public TestReferee() {
    super();
    Player sean = this.getPlayerByID(this.addPlayer("Sean"));
    Player nick = this.getPlayerByID(this.addPlayer("Nick"));
    Player hans = this.getPlayerByID(this.addPlayer("Hans"));
    Player ansel = this.getPlayerByID(this.addPlayer("Ansel"));
    sean.addResource(Resource.BRICK, 6);
    sean.addResource(Resource.WOOD, 4);
    sean.addResource(Resource.ORE, 4);
    sean.addResource(Resource.WHEAT, 3);
    hans.addResource(Resource.SHEEP);
    hans.addResource(Resource.ORE, 3);
    hans.addResource(Resource.WHEAT, 2);
    nick.addResource(Resource.WOOD, 2);
    ansel.addResource(Resource.SHEEP);

    // Adding Settlements & Cities:
    HexCoordinate h1 = new HexCoordinate(1, 2, 0);
    HexCoordinate h2 = new HexCoordinate(1, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 1, 0);
    IntersectionCoordinate inter = new IntersectionCoordinate(h1, h2, h3);
    new BuildSettlement(this, sean.getID(), inter, false).execute();
    new BuildCity(this, sean.getID(), inter).execute();
    HexCoordinate r1 = new HexCoordinate(1, 2, 0);
    HexCoordinate r2 = new HexCoordinate(0, 2, 0);
    HexCoordinate r3 = new HexCoordinate(0, 1, 0);
    IntersectionCoordinate roadEnd = new IntersectionCoordinate(r1, r2, r3);
    new BuildRoad(this, sean.getID(), inter, roadEnd, false).execute();

    HexCoordinate a1 = new HexCoordinate(0, 2, 2);
    HexCoordinate a2 = new HexCoordinate(0, 2, 1);
    HexCoordinate a3 = new HexCoordinate(0, 1, 1);
    IntersectionCoordinate inter2 = new IntersectionCoordinate(a1, a2, a3);
    new BuildSettlement(this, sean.getID(), inter2, false).execute();

    HexCoordinate c1 = new HexCoordinate(2, 1, 0);
    HexCoordinate c2 = new HexCoordinate(1, 0, 0);
    HexCoordinate c3 = new HexCoordinate(1, 1, 0);
    IntersectionCoordinate inter4 = new IntersectionCoordinate(c1, c2, c3);
    new BuildSettlement(this, hans.getID(), inter4, false).execute();
    new BuildCity(this, hans.getID(), inter4).execute();

    HexCoordinate d1 = new HexCoordinate(0, 0, 0);
    HexCoordinate d2 = new HexCoordinate(0, 1, 0);
    HexCoordinate d3 = new HexCoordinate(1, 1, 0);
    IntersectionCoordinate inter5 = new IntersectionCoordinate(d1, d2, d3);
    new BuildSettlement(this, hans.getID(), inter5, false).execute();
    HexCoordinate s1 = new HexCoordinate(0, 0, 0);
    HexCoordinate s2 = new HexCoordinate(1, 0, 0);
    HexCoordinate s3 = new HexCoordinate(1, 1, 0);
    IntersectionCoordinate roadEnd5 = new IntersectionCoordinate(s1, s2, s3);
    new BuildRoad(this, hans.getID(), inter5, roadEnd5, false).execute();

    HexCoordinate b1 = new HexCoordinate(1, -1, 0);
    HexCoordinate b2 = new HexCoordinate(1, 0, 0);
    HexCoordinate b3 = new HexCoordinate(1, 0, 1);
    IntersectionCoordinate inter3 = new IntersectionCoordinate(b1, b2, b3);
    new BuildSettlement(this, nick.getID(), inter3, false).execute();

    HexCoordinate e1 = new HexCoordinate(0, 0, 0);
    HexCoordinate e2 = new HexCoordinate(0, 1, 0);
    HexCoordinate e3 = new HexCoordinate(0, 1, 1);
    IntersectionCoordinate inter6 = new IntersectionCoordinate(e1, e2, e3);
    new BuildSettlement(this, nick.getID(), inter6, false).execute();

    HexCoordinate f1 = new HexCoordinate(1, 0, 1);
    HexCoordinate f2 = new HexCoordinate(0, 0, 0);
    HexCoordinate f3 = new HexCoordinate(0, 0, 1);
    IntersectionCoordinate inter7 = new IntersectionCoordinate(f1, f2, f3);
    new BuildSettlement(this, nick.getID(), inter7, false).execute();

    HexCoordinate g1 = new HexCoordinate(0, 0, 1);
    HexCoordinate g2 = new HexCoordinate(0, 1, 1);
    HexCoordinate g3 = new HexCoordinate(0, 1, 2);
    IntersectionCoordinate inter8 = new IntersectionCoordinate(g1, g2, g3);
    new BuildSettlement(this, nick.getID(), inter8, false).execute();
  }
}
