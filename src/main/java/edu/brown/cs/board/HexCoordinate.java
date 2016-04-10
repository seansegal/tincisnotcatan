package edu.brown.cs.board;

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
    if (_x != other._x)
      return false;
    if (_y != other._y)
      return false;
    if (_z != other._z)
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
