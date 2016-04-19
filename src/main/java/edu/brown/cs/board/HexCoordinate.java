package edu.brown.cs.board;

import static java.lang.Math.sqrt;

import java.util.Arrays;

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
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    HexCoordinate other = (HexCoordinate) obj;
    if (this.cartesianX() != other.cartesianX()) {
      return false;
    }
    if (this.cartesianY() != other.cartesianY()) {
      return false;
    }
    return true;
  }

  public double cartesianX() {
    return -1 / 2 * _x + _y + -1 / 2 * _z;
  }
  
  public double cartesianY() {
    return -sqrt(3) / 2 * _x + sqrt(3) / 2 * _z;
  }

  @Override
  public int hashCode() {
    double[] coords = { this.cartesianX(), this.cartesianY() };
    return Arrays.hashCode(coords);
  }

  @Override
  public String toString() {
    return String.format("( %d, %d, %d)", _x, _y, _z);
  }
}
