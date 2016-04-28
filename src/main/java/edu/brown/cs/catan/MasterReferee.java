package edu.brown.cs.catan;

import static edu.brown.cs.catan.Settings.CITY_POINT_VAL;
import static edu.brown.cs.catan.Settings.INITIAL_CITIES;
import static edu.brown.cs.catan.Settings.INITIAL_SETTLEMENTS;
import static edu.brown.cs.catan.Settings.LARGEST_ARMY_POINT_VAL;
import static edu.brown.cs.catan.Settings.LONGEST_ROAD_POINT_VAL;
import static edu.brown.cs.catan.Settings.SETTLEMENT_POINT_VAL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.board.Board;

public class MasterReferee implements Referee {

  private final Board _board;
  private final Map<Integer, Player> _players;
  private final Map<Integer, Integer> _turnOrder;
  private Turn _turn;
  private final Bank _bank;
  private final List<DevelopmentCard> _devCardDeck;
  private final GameSettings _gameSettings;
  private Player _largestArmy = null;
  private boolean _isSetUp = true;

  public MasterReferee() {
    _board = new Board();
    _gameSettings = new GameSettings(); // TODO customize
    _players = new HashMap<Integer, Player>();
    _turnOrder = initializeTurnOrder(_gameSettings.NUM_PLAYERS);
    _turn = new Turn(1);
    _bank = initializeBank(false);
    _devCardDeck = initializeDevDeck();
  }

  private Map<Integer, Integer> initializeTurnOrder(int numFullPlayers) {
    Map<Integer, Integer> toReturn = new HashMap<>();
    List<Integer> players = new ArrayList<>(_players.keySet());
    for (int i = 0; i < numFullPlayers; i++) {
      players.add(i);
    }
    Collections.shuffle(players);
    for (int i = 1; i <= numFullPlayers; i++) {
      toReturn.put(i, players.get(i - 1));
    }
    return toReturn;
  }

  @Override
  public void startNextTurn() {
    _turn = new Turn(_turn.getTurnNum() + 1);
  }

  @Override
  public Player currentPlayer() {
    if (_players.size() != _gameSettings.NUM_PLAYERS) {
      return null;
    }
    return _players.get(_turnOrder.get(_turn.getTurnNum()
        % _gameSettings.NUM_PLAYERS));
  }

  @Override
  public DevelopmentCard getDevCard() {
    return _devCardDeck.remove(0);
  }

  private Bank initializeBank(boolean isSmart) {
    if (isSmart) {
      assert false; // TODO: Not yet implemented
      return null;
    } else {
      return new SimpleBank();
    }
  }

  private List<DevelopmentCard> initializeDevDeck() {
    List<DevelopmentCard> toReturn = new ArrayList<>();
    for (int i = 0; i < Settings.NUM_KNIGHTS; i++) {
      toReturn.add(DevelopmentCard.KNIGHT);
    }
    for (int i = 0; i < Settings.NUM_POINTS; i++) {
      toReturn.add(DevelopmentCard.POINT);
    }
    for (int i = 0; i < Settings.NUM_ROADBUILDING; i++) {
      toReturn.add(DevelopmentCard.ROAD_BUILDING);
    }
    for (int i = 0; i < Settings.NUM_YOP; i++) {
      toReturn.add(DevelopmentCard.YEAR_OF_PLENTY);
    }
    for (int i = 0; i < Settings.NUM_MONOPOLY; i++) {
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
  public Turn getTurn() {
    return _turn.getCopy();
  }

  @Override
  public Board getBoard() {
    return _board;
  }

  @Override
  public Player getPlayerByID(int id) {
    return _players.get(id);
  }

  @Override
  public Collection<Player> getPlayers() {
    return Collections.unmodifiableCollection(_players.values());
  }

  @Override
  public void playDevCard() {
    _turn.setDevCardHasBeenPlayed();
  }

  @Override
  public void playerMustDiscard(int id, double amount) {
    _turn.setMustDiscard(id, amount);
  }

  @Override
  public boolean hasLongestRoad(int id) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean hasLargestArmy(int id) {
    Player player = getPlayerByID(id);
    int maxArmy = _largestArmy != null ? _largestArmy.numPlayedKnights() : 0;
    Player maxPlayer = null;
    for (Player p : _players.values()) {
      if (p.numPlayedKnights() > maxArmy) {
        maxArmy = p.numPlayedKnights();
        maxPlayer = p;
      }
    }
    if (maxArmy >= Settings.LARGEST_ARMY_THRESH && maxPlayer != null
        && maxPlayer.equals(player)) {
      _largestArmy = maxPlayer;
      return true;
    }
    if (_largestArmy != null && player.equals(_largestArmy)) {
      return true;
    }
    return false;
  }

  @Override
  public int getNumPublicPoints(int id) {
    Player player = getPlayerByID(id);
    int settlementPoints = SETTLEMENT_POINT_VAL
        * (INITIAL_SETTLEMENTS - player.numSettlements());
    int cityPoints = CITY_POINT_VAL * (INITIAL_CITIES - player.numCities());
    int roadArmyPts = hasLargestArmy(id) ? LARGEST_ARMY_POINT_VAL : 0;
    roadArmyPts += hasLongestRoad(id) ? LONGEST_ROAD_POINT_VAL : 0;
    return settlementPoints + cityPoints + roadArmyPts;
  }

  @Override
  public int getNumTotalPoints(int id) {
    int publicPoints = getNumPublicPoints(id);
    publicPoints += getPlayerByID(id).numVictoryPoints();
    return publicPoints;
  }

  @Override
  public Map<Resource, Double> getBankRates(int id) {
    // Player player = getPlayerByID(id);
    Map<Resource, Double> rates = new HashMap<>();
    for (Resource r : Resource.values()) {
      rates.put(r, _bank.getBankRate());
    }
    for (Map.Entry<Resource, Double> r : _bank.getPortRates().entrySet()) {
      // TODO: check if player has port
    }
    return rates;
  }

  @Override
  public int addPlayer(String name) {
    return addPlayer(name, _gameSettings.COLORS[_players.size()]);
  }

  @Override
  public int addPlayer(String name, String color) {
    if (_turn.getTurnNum() == 1) {
      int id = _players.size();
      _players.put(id, new HumanPlayer(id, name, color));
      return id;
    }
    throw new UnsupportedOperationException(
        "Cannot currently add players during a game.");
  }

  @Override
  public boolean gameIsFull() {
    return _gameSettings.NUM_PLAYERS == _players.size();
  }

  @Override
  public Bank getBank() {
    return _bank;
  }

  @Override
  public boolean isSetUp() {
    return _isSetUp;
  }

  private class ReadOnlyReferee implements Referee {

    private final Referee _referee;

    public ReadOnlyReferee(Referee referee) {
      _referee = referee;
    }

    @Override
    public void startNextTurn() {
      throw new UnsupportedOperationException(
          "A ReadOnlyReferee cannot change the turn.");
    }

    @Override
    public DevelopmentCard getDevCard() {
      throw new UnsupportedOperationException(
          "A ReadOnlyReferee cannot draw from the dev card deck.");
    }

    @Override
    public Referee getReadOnlyReferee() {
      return this;
    }

    @Override
    public Turn getTurn() {
      return _referee.getTurn();
    }

    @Override
    public Board getBoard() {
      return _referee.getBoard(); // Change to unmodifiable?
    }

    @Override
    public Player getPlayerByID(int id) {
      return _referee.getPlayerByID(id).getImmutableCopy();
    }

    @Override
    public List<Player> getPlayers() {
      List<Player> list = new ArrayList<>();
      for (Player p : _referee.getPlayers()) {
        list.add(p.getImmutableCopy());
      }
      return Collections.unmodifiableList(list);
    }

    @Override
    public void playDevCard() {
      throw new UnsupportedOperationException(
          "ReadOnlyReferee cannot play development cards.");

    }

    @Override
    public Player currentPlayer() {
      return _referee.currentPlayer().getImmutableCopy();
    }

    @Override
    public boolean hasLongestRoad(int player) {
      return _referee.hasLongestRoad(player);
    }

    @Override
    public boolean hasLargestArmy(int player) {
      return _referee.hasLargestArmy(player);
    }

    @Override
    public int getNumPublicPoints(int player) {
      return _referee.getNumPublicPoints(player);
    }

    @Override
    public int getNumTotalPoints(int player) {
      return _referee.getNumTotalPoints(player);
    }

    @Override
    public Map<Resource, Double> getBankRates(int player) {
      return _referee.getBankRates(player);
    }

    @Override
    public int addPlayer(String name) {
      throw new UnsupportedOperationException(
          "A ReadOnlyReferee cannot add players.");
    }

    @Override
    public int addPlayer(String name, String color) {
      throw new UnsupportedOperationException(
          "A ReadOnlyReferee cannot add players.");
    }

    @Override
    public boolean gameIsFull() {
      return _referee.gameIsFull();
    }

    @Override
    public Bank getBank() {
      return _referee.getBank();
    }

    @Override
    public boolean isSetUp() {
      return _referee.isSetUp();
    }

    @Override
    public void playerMustDiscard(int player, double amount) {
      throw new UnsupportedOperationException(
          "A ReadOnlyReferee cannot change playerMustDiscard.");
    }

  }

}
