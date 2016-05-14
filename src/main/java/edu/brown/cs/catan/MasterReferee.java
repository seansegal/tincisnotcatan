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

import edu.brown.cs.actions.FollowUpAction;
import edu.brown.cs.board.Board;
import edu.brown.cs.board.Intersection;
import edu.brown.cs.gamestats.CatanStats;
import edu.brown.cs.gamestats.GameStats;

/**
 * An implementation of Referee that can read and write data. There should be one
 * MasterReferee per game. Contains all Catan game data.
 *
 */
public class MasterReferee implements Referee {

  private final Board _board;
  private final Map<Integer, Player> _players;
  private final List<Integer> _turnOrder;
  private Turn _turn;
  private final Bank _bank;
  private final List<DevelopmentCard> _devCardDeck;
  private final GameSettings _gameSettings;
  private Player _largestArmy = null;
  private Player _longestRoad = null;
  private GameStatus _gameStatus;
  private final Setup _setup;
  private GameStats _gameStats;

  /**
   * Creates a MasterReferee. Contains all Catan game data with default game
   * settings.
   */
  public MasterReferee() {
    _gameSettings = new GameSettings(); // Use default settings.
    _board = new Board(_gameSettings);
    _players = new HashMap<Integer, Player>();
    _turnOrder = initializeTurnOrder(_gameSettings.numPlayers);
    _bank = initializeBank(false);
    _devCardDeck = initializeDevDeck();
    _turn = new Turn(1, Collections.emptyMap());
    _gameStatus = GameStatus.WAITING;
    _setup = new Setup(getSetupOrder());
    _gameStats = CatanStats.getGameStatsObject();
  }

  /**
   * Creates a MasterReferee. Contains all Catan game data with inputted
   * GameSettings.
   *
   * @param gameSettings
   *          A GameSettings object to use as the gameSettings.
   */
  public MasterReferee(GameSettings gameSettings) {
    _gameSettings = gameSettings;
    _board = new Board(_gameSettings);
    _players = new HashMap<Integer, Player>();
    _turnOrder = initializeTurnOrder(_gameSettings.numPlayers);
    _bank = initializeBank(_gameSettings.isDynamic);
    _devCardDeck = initializeDevDeck();
    _turn = new Turn(1, Collections.emptyMap());
    _gameStatus = GameStatus.WAITING;
    _setup = new Setup(getSetupOrder());
    _gameStats = CatanStats.getGameStatsObject();
  }

  private List<Integer> getSetupOrder() {
    List<Integer> toRet = new ArrayList<>();
    toRet.addAll(_turnOrder);
    List<Integer> copyTurnOrder = new ArrayList<>(_turnOrder);
    Collections.reverse(copyTurnOrder);
    toRet.addAll(copyTurnOrder);
    return toRet;
  }

  @Override
  public Setup getSetup() {
    return _setup;
  }

  private List<Integer> initializeTurnOrder(int numFullPlayers) {
    List<Integer> toReturn = new ArrayList<>();
    for (int i = 0; i < _gameSettings.numPlayers; i++) {
      toReturn.add(i);
    }
    Collections.shuffle(toReturn);
    return toReturn;
  }

  @Override
  public void startNextTurn() {
    Player nextPlayer = _players.get(_turnOrder.get((_turn.getTurnNum())
        % _gameSettings.numPlayers));

    if (_gameStatus == GameStatus.PROGRESS) {
      _turn = new Turn(_turn.getTurnNum() + 1, nextPlayer.getDevCards());
    } else {
      _turn = new Turn(_turn.getTurnNum() + 1, Collections.emptyMap());
    }

  }

  @Override
  public Player currentPlayer() {
    if (_players.size() != _gameSettings.numPlayers) {
      return null;
    }
    if (_gameStatus == GameStatus.PROGRESS) {
      return _players.get(_turnOrder.get((_turn.getTurnNum() - 1)
          % _gameSettings.numPlayers));
    } else {
      return getPlayerByID(_setup.getCurrentPlayerID());
    }
  }

  @Override
  public DevelopmentCard getDevCard() {
    return _devCardDeck.remove(0);
  }

  @Override
  public void addFollowUp(Collection<FollowUpAction> actions) {
    _turn.addFollowUp(actions);
  }

  @Override
  public FollowUpAction getNextFollowUp(int playerID) {
    return _turn.getNextFollowUp(playerID);
  }

  private Bank initializeBank(boolean isSmart) {
    if (isSmart) {
      return new DynamicBank();
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
  public boolean hasLongestRoad(int id) {
    if (_longestRoad == null) {
      int max = 0;
      for (Player p : _players.values()) {
        int longestPath = _board.longestPath(p);
        if (longestPath > max && longestPath >= Settings.LONGEST_ROAD_THRESH) {
          max = longestPath;
          _longestRoad = p;
        }
      }
    } else {
      int toBeat = _board.longestPath(_longestRoad);
      for (Player p : _players.values()) {
        int longestPath = _board.longestPath(p);
        if (longestPath > toBeat) {
          toBeat = longestPath;
          _longestRoad = p;
        }
      }
    }
    return _longestRoad != null ? _longestRoad.getID() == id : false;
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
  public Player getWinner() {
    for (Player p : _players.values()) {
      if (getNumTotalPoints(p.getID()) >= _gameSettings.winningPointCount) {
        return p;
      }
    }
    return null;
  }

  @Override
  public Map<Resource, Double> getBankRates(int id) {
    Player player = getPlayerByID(id);
    Map<Resource, Double> rates = new HashMap<>();
    for (Resource r : Resource.values()) {
      rates.put(r, _bank.getBankRate(r));
    }
    for (Intersection i : _board.getIntersections().values()) {
      if (i.getPort() != null && i.getBuilding() != null
          && i.getBuilding().getPlayer().equals(player)) {
        if (i.getPort().getResource() == Resource.WILDCARD) {
          for (Resource r : Resource.values()) {
            double rate = Math.min(rates.get(r), _bank.getWildCardRate(r));
            rates.put(r, rate);
          }
        } else {
          Resource r = i.getPort().getResource();
          double rate = Math.min(rates.get(r), _bank.getPortRate(r));
          rates.put(r, rate);
        }
      }
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
  public Bank getBank() {
    return _bank;
  }

  @Override
  public GameSettings getGameSettings() {
    return _gameSettings;
  }

  @Override
  public void removeFollowUp(FollowUpAction action) {
    _turn.removeFollowUp(action);

  }

  @Override
  public List<Integer> getTurnOrder() {
    return Collections.unmodifiableList(_turnOrder);
  }

  @Override
  public GameStatus getGameStatus() {
    return _gameStatus;
  }

  @Override
  public void setGameStatus(GameStatus state) {
    _gameStatus = state;
  }

  @Override
  public boolean devCardDeckIsEmpty() {
    return _devCardDeck.isEmpty();
  }

  @Override
  public GameStats getGameStats() {
    return _gameStats;
  }

  @Override
  public boolean removePlayer(int id) {
    return _players.remove(id) != null;
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
    public Bank getBank() {
      return _referee.getBank();
    }

    @Override
    public GameSettings getGameSettings() {
      return _referee.getGameSettings();
    }

    @Override
    public FollowUpAction getNextFollowUp(int playerID) {
      throw new UnsupportedOperationException(
          "A ReadOnlyReferee cannot getNextFollowup");
    }

    @Override
    public void addFollowUp(Collection<FollowUpAction> actions) {
      throw new UnsupportedOperationException(
          "A ReadOnlyReferee cannot addFollowUp.");
    }

    @Override
    public void removeFollowUp(FollowUpAction action) {
      throw new UnsupportedOperationException(
          "A ReadOnlyReferee cannot removeFollowUp.");

    }

    @Override
    public List<Integer> getTurnOrder() {
      return Collections.unmodifiableList(_turnOrder);
    }

    @Override
    public GameStatus getGameStatus() {
      return _referee.getGameStatus();
    }

    @Override
    public void setGameStatus(GameStatus state) {
      throw new UnsupportedOperationException(
          "A ReadOnlyReferee cannot setGameState.");

    }

    @Override
    public Setup getSetup() {
      return _referee.getSetup();
    }

    @Override
    public Player getWinner() {
      return _referee.getWinner() != null ? _referee.getWinner()
          .getImmutableCopy() : null;
    }

    @Override
    public boolean devCardDeckIsEmpty() {
      return _referee.devCardDeckIsEmpty();
    }

    @Override
    public GameStats getGameStats() {
      return _referee.getGameStats();
    }

    @Override
    public boolean removePlayer(int id) {
      return _referee.removePlayer(id);
    }

  }

}
