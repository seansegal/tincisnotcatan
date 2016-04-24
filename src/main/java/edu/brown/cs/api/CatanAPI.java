package edu.brown.cs.api;

import java.util.Map;

import edu.brown.cs.actions.ActionResponse;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.TestReferee;

public class CatanAPI {

  public static void main(String[] args) {
    System.out.println(new CatanAPI().getHand(0));
    System.out.println(new CatanAPI().getPlayers());
    System.out.println(new CatanAPI().getBoard());
  }


  private Referee        _referee;
  private CatanConverter _converter;
  private ActionFactory  _actionFactory;


  public CatanAPI() {
    _referee = new TestReferee();
    _converter = new CatanConverter();
  }


  public String getBoard() {
    return _converter.getBoard(_referee.getBoard());
  }


  public String getHand(int playerID) {
    return _converter.getHand(_referee.getPlayerByID(playerID));
  }


  public String getPlayers() {
    return _converter.getPlayers(_referee.getReadOnlyReferee());
  }


  public int addPlayer(String name, String color) {
    if (name == null || color == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    return _referee.addPlayer(name, color);
  }


  /**
   * Performs Catan Actions.
   *
   * @param action
   *          A JSON String with parameters for a given Catan action.
   * @return A Map from Player IDs to JSON Strings that should be sent as a
   *         response.
   */
  public Map<Integer, String> performAction(String action) {
    if (action == null) {
      throw new IllegalArgumentException("Input cannot be null.");
    }
    Map<Integer, ActionResponse> response =
        _actionFactory.createAction(action).execute();
    return _converter.responseToJSON(response);
  }


}
