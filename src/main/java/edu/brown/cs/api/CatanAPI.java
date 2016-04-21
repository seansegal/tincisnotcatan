package edu.brown.cs.api;

import edu.brown.cs.catan.MasterReferee;
import edu.brown.cs.catan.Referee;

public class CatanAPI {

  public static void main(String[] args) {
    System.out.println(new CatanAPI().getHand(0));
    System.out.println(new CatanAPI().getBoard());
    System.out.println(new CatanAPI().getPlayers());
  }

  private Referee _referee;
  private CatanConverter _converter;

  public CatanAPI() {
    _referee = new MasterReferee();
    _converter = new CatanConverter();
  }

  public String getBoard() {
    return _converter.getBoard(_referee);
  }

  public String getHand(int playerID) {
    return _converter.getHand(playerID, _referee);
  }

  public String getPlayers() {
    return _converter.getPlayers(_referee);
  }

  public int addPlayer(String name, String color) {
    return _referee.addPlayer(name, color);
  }

  public void preformAction(String action) {
    // TODO: implement & what does this return, String?
  }

}
