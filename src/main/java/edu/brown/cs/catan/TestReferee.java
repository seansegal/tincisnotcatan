package edu.brown.cs.catan;

import edu.brown.cs.actions.BuildSettlement;
import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.board.IntersectionCoordinate;


public class TestReferee extends MasterReferee {

  public TestReferee(){
    super();
    Player sean = this.getPlayerByID(this.addPlayer("Sean", "#FF0000"));
    Player nick = this.getPlayerByID(this.addPlayer("Nick", "#00FF00"));
    Player hans = this.getPlayerByID(this.addPlayer("Hans", "#00FFFF"));
    Player ansel = this.getPlayerByID(this.addPlayer("Ansel", "#FF00FF"));
    sean.addResource(Resource.BRICK);
    sean.addResource(Resource.BRICK);
    sean.addResource(Resource.BRICK);
    sean.addResource(Resource.ORE);
    sean.addResource(Resource.ORE);
    sean.addResource(Resource.ORE);
    sean.addResource(Resource.ORE);
    sean.addResource(Resource.WHEAT);
    sean.addResource(Resource.WHEAT);
    hans.addResource(Resource.WHEAT);
    hans.addResource(Resource.SHEEP);
    hans.addResource(Resource.ORE);
    nick.addResource(Resource.WOOD);
    nick.addResource(Resource.WOOD);
    ansel.addResource(Resource.SHEEP);

    // Adding HexCoordinates:

    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(1, 1, 0);
    IntersectionCoordinate inter = new IntersectionCoordinate(h1, h2, h3);
    new BuildSettlement(this, sean.getID(), inter, false).execute();

    HexCoordinate a1 = new HexCoordinate(0, 0, 0);
    HexCoordinate a2 = new HexCoordinate(0,0,1);
    HexCoordinate a3 = new HexCoordinate(0,1,1);
    IntersectionCoordinate inter2 = new IntersectionCoordinate(a1, a2, a3);
    new BuildSettlement(this, ansel.getID(), inter2, false).execute();

    HexCoordinate b1 = new HexCoordinate(0, 0, 0);
    HexCoordinate b2 = new HexCoordinate(1,0,1);
    HexCoordinate b3 = new HexCoordinate(1,0,0);
    IntersectionCoordinate inter3 = new IntersectionCoordinate(b1, b2, b3);
    new BuildSettlement(this, nick.getID(), inter3, false).execute();

//    HexCoordinate c1 = new HexCoordinate(0, 0, 0);
//    HexCoordinate c2 = new HexCoordinate(0, 1, 0);
//    HexCoordinate c3 = new HexCoordinate(1, 1, 0);
//    IntersectionCoordinate inter4 = new IntersectionCoordinate(c1, c2, c3);
//    new BuildSettlement(this, hans.getID(), inter4, false).execute();
//
//    HexCoordinate d1 = new HexCoordinate(0, 0, 0);
//    HexCoordinate d2 = new HexCoordinate(0, 1, 0);
//    HexCoordinate d3 = new HexCoordinate(1, 1, 0);
//    IntersectionCoordinate inter5 = new IntersectionCoordinate(d1, d2, d3);
//    new BuildSettlement(this, sean.getID(), inter5, false).execute();

  }


}
