package edu.brown.cs.gamestats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLStatsHandler implements StatsHandler {

  private Connection _conn;
  private static final String FOREIGN_KEYS_ON = "PRAGMA foreign_keys = ON;";
  private static final String DB = "data/catan.sqlite3";


  public SQLStatsHandler() throws ClassNotFoundException, SQLException{
    String url = "jdbc:sqlite:" + DB;
    Class.forName("org.sqlite.JDBC");
    _conn = DriverManager.getConnection(url);
    try (Statement stat = _conn.createStatement()) {
      stat.executeUpdate(FOREIGN_KEYS_ON);
    }
    //TODO: check if correct tables are available
  }

  @Override
  public void storeGameStats(GameStats stats) {

  }

}
