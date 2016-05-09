package edu.brown.cs.actions;

public abstract class CatanFormats {

  private static final double ROUND_TO = 100.0;

  public synchronized static double round(double raw) {
    return Math.round(raw * ROUND_TO) / ROUND_TO;
  }

}
