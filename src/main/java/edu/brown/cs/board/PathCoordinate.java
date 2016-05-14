package edu.brown.cs.board;

/**
 * System for maintaing and describing location of paths on the board.
 *
 * @author anselvahle
 *
 */
public class PathCoordinate {
  private final IntersectionCoordinate _startCoord;
  private final IntersectionCoordinate _endCoord;

  /**
   * Constructor for the class.
   *
   * @param startCoord
   *          Coordinate of the intersection where a path ends.
   * @param endCoord
   *          Coordiante of the intersection where a path ends.
   */
  public PathCoordinate(IntersectionCoordinate startCoord,
      IntersectionCoordinate endCoord) {
    _startCoord = startCoord;
    _endCoord = endCoord;
  }

  /**
   * Gets the coordinate of the intersection where the path begins.
   *
   * @return Intersection coordinate of the intersection at the beginning of the
   *         path.
   */
  public IntersectionCoordinate get_startCoord() {
    return _startCoord;
  }

  /**
   * Gets the coordinate of the intersection where the path ends.
   *
   * @return Intersection coordinate of the intersection at the end of the path.
   */
  public IntersectionCoordinate get_endCoord() {
    return _endCoord;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof PathCoordinate)) {
      return false;
    }

    PathCoordinate other = (PathCoordinate) obj;
    if (this._startCoord.equals(other.get_startCoord())
        && this._endCoord.equals(other.get_endCoord())) {
      return true;
    }
    if (this._startCoord.equals(other.get_endCoord())
        && this._endCoord.equals(other.get_startCoord())) {
      return true;
    }

    return false;
  }

  @Override
  public int hashCode() {
    return _startCoord.hashCode() + _endCoord.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder toRet = new StringBuilder();
    toRet.append("START: ");
    toRet.append(_startCoord.toString());
    toRet.append(" END: ");
    toRet.append(_endCoord.toString());
    return toRet.toString();
  }

}
