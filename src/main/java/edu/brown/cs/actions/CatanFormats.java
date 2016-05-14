package edu.brown.cs.actions;

/**
 * Class responsible for formatting for decimal trade.
 *
 * @author anselvahle
 *
 */
public abstract class CatanFormats {

  private static final double ROUND_TO = 100.0;

  public synchronized static double round(double raw) {
    return Math.round(raw * ROUND_TO) / ROUND_TO;
  }

}
