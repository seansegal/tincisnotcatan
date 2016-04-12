package edu.brown.cs.catan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.brown.cs.board.Board;

public class MasterReferee implements Referee {

  private Board _board; //TODO make final
  private final List<Player> _players; // Map players to Color?
  private int _turn;
  private final Bank _bank;
  private final List<DevelopmentCard> _devCardDeck;
  private Set<Player> _hasDiscarded;
  private boolean _devHasBeenPlayed;

  public MasterReferee(int numPlayers, boolean smartBank) {
    //Board _board = new Board();
    _players = initializePlayers(numPlayers);
    _turn = 1;
    _bank = initializeBank(smartBank);
    _devCardDeck = initializeDevDeck();
    _hasDiscarded = new HashSet<>();

  }

  @Override
  public void startNextTurn(){
    _turn++;
    _devHasBeenPlayed = false;
    _hasDiscarded = new HashSet<>();
  }

  @Override
  public boolean devHasBeenPlayed() {
    return _devHasBeenPlayed;
  }

  @Override
  public DevelopmentCard getDevCard(){
    return _devCardDeck.remove(0);
  }

  private List<Player> initializePlayers(int numPlayers){
    List<Player> toReturn = new ArrayList<>();
    for(int i=0; i< numPlayers; i++){
      toReturn.add(new HumanPlayer());
    }
    return Collections.unmodifiableList(toReturn);
  }

  private Bank initializeBank(boolean isSmart){
    if(isSmart){
      assert false; //Not yet implemented
      return null;
    }
    else{
      return new SimpleBank();
    }
  }

  private List<DevelopmentCard> initializeDevDeck(){
    List<DevelopmentCard> toReturn = new ArrayList<>();
    for(int i=0; i<Settings.NUM_KNIGHTS; i++){
      toReturn.add(DevelopmentCard.KNIGHT);
    }
    for(int i=0; i<Settings.NUM_POINTS; i++){
      toReturn.add(DevelopmentCard.POINT);
    }
    for(int i=0; i<Settings.NUM_ROADBUILDING; i++){
      toReturn.add(DevelopmentCard.ROAD_BUILDING);
    }
    for(int i=0; i<Settings.NUM_YOP; i++){
      toReturn.add(DevelopmentCard.YEAR_OF_PLENTY);
    }
    for(int i=0; i<Settings.NUM_MONOPOLY; i++){
      toReturn.add(DevelopmentCard.MONOPOLY);
    }
    Collections.shuffle(toReturn);
    return toReturn;
  }

  @Override
  public Referee getReadOnlyReferee() {
    return new ReadOnlyReferee(this);
  }

  @Override
  public int getTurn() {
    return _turn;
  }

  @Override
  public boolean playerHasDiscarded(Player player) {
    return _hasDiscarded.contains(player);
  }

  @Override
  public Board getBoard() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Player getPlayerByID(String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Player> getPlayers() {
    // TODO Auto-generated method stub
    return null;
  }


  private class ReadOnlyReferee implements Referee {

    private final Referee _referee;

    protected ReadOnlyReferee(Referee referee){
      _referee = referee;
    }

    @Override
    public void startNextTurn() {
      throw new UnsupportedOperationException("A ReadOnlyReferee cannot change the turn.");
    }

    @Override
    public boolean devHasBeenPlayed() {
      return _referee.devHasBeenPlayed();
    }

    @Override
    public DevelopmentCard getDevCard() {
      throw new UnsupportedOperationException("A ReadOnlyReferee cannot draw from the dev card deck.");
    }

    @Override
    public Referee getReadOnlyReferee() {
      return this;
    }

    @Override
    public int getTurn() {
      return _referee.getTurn();
    }

    @Override
    public boolean playerHasDiscarded(Player player) {
      return _referee.playerHasDiscarded(player);
    }

    @Override
    public Board getBoard() {
      //TODO: return ReadOnly board?
      return null;
    }

    @Override
    public Player getPlayerByID(String id) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public List<Player> getPlayers() {
      List<Player> list = new ArrayList<>();
      for(Player p: _referee.getPlayers()){
          list.add(p.getImmutableCopy());
      }
      return Collections.unmodifiableList(list);
    }
  }
}
