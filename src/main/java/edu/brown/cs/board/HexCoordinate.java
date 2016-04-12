package edu.brown.cs.board;

import static java.lang.Math.sqrt;

public class HexCoordinate {
  private int _x;
  private int _y;
  private int _z;
  
  public HexCoordinate(int x, int y, int z) {
    _x = x;
    _y = y;
    _z = z;
  }

  public int getX() {
    return _x;
  }

  public int getY() {
    return _y;
  }

  public int getZ() {
    return _z;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    HexCoordinate other = (HexCoordinate) obj;
    if ((sqrt(3) / 2 * _x + _y + sqrt(3) / 2 * _z) != (sqrt(3) / 2
        * other.getX() + other.getY() + sqrt(3) / 2 * other.getZ()))
      return false;
    if ((1 / 2 * _x + 1 / 2 * _z) != 
        (1 / 2 * other.getX() + 1 / 2 * other.getZ()))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + _x;
    result = prime * result + _y;
    result = prime * result + _z;
    return result;
  }
}
