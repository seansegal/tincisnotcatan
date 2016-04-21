package edu.brown.cs.board;

import static java.lang.Math.sqrt;

public class HexCoordinate {
  private static final double TOLERANCE = .001;
  private final int _x;
  private final int _y;
  private final int _z;

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
    if (!(Math.abs(this.cartesianX() - other.cartesianX()) < TOLERANCE)) {
      return false;
    }
    if (!(Math.abs(this.cartesianY() - other.cartesianY()) < TOLERANCE)) {
      return false;
    }
    return true;
  }

  public double cartesianX() {
    return (-0.5) * _x + _y + (-0.5) * _z;
  }

  public double cartesianY() {
    return -(sqrt(3)/ 2) * _x + (sqrt(3)/ 2) * _z;
  }

  @Override
  public int hashCode() {
    // double[] coords = { this.cartesianX(), this.cartesianY() };
    // return Arrays.hashCode(coords);
    return 1;
  }

  @Override
  public String toString() {
    return String.format("( %d, %d, %d)", _x, _y, _z);
  }
}
