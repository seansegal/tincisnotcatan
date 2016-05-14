package edu.brown.cs.board;

import static java.lang.Math.sqrt;

/**
 * Hexagonal Coordinate System representation of a point.
 *
 * @author anselvahle
 *
 */
public class HexCoordinate {
  private static final double TOLERANCE = .001;
  private final int x;
  private final int y;
  private final int z;

  /**
   * Constructor for the class.
   *
   * @param x
   *          Value of the x position.
   * @param y
   *          Value of the y position.
   * @param z
   *          Value of the z position.
   */
  public HexCoordinate(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Gets the x coordinate.
   * 
   * @return Value of the x coordinate.
   */
  public int getX() {
    return x;
  }

  /**
   * Gets the y coordinate.
   * 
   * @return Value of the y coordinate.
   */
  public int getY() {
    return y;
  }

  /**
   * Gets the z coordinate.
   * 
   * @return Value of the z coordinate.
   */
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

  /**
   * Converts the the coordinate into cartesian and gives the x value.
   *
   * @return Double that is the x value of this point in the cartesian plane.
   */
  public double cartesianX() {
    return (-0.5) * x + y + (-0.5) * z;
  }

  /**
   * Converts the the coordinate into cartesian and gives the y value.
   *
   * @return Double that is the y value of this point in the cartesian plane.
   */
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
