package edu.brown.cs.board;

import static java.lang.Math.sqrt;

public class HexCoordinate {
  private static final double TOLERANCE = .001;
  private final int x;
  private final int y;
  private final int z;

  public HexCoordinate(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
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
    return (-0.5) * x + y + (-0.5) * z;
  }

  public double cartesianY() {
    return -(sqrt(3) / 2) * x + (sqrt(3) / 2) * z;
  }

  @Override
  public int hashCode() {
    // double[] coords = { this.cartesianX(), this.cartesianY() };
    // return Arrays.hashCode(coords);
    return 1;
  }

  @Override
  public String toString() {
    return String.format("( %d, %d, %d)", x, y, z);
  }
}
