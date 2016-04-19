package edu.brown.cs.board;

public class PathCoordinate {
  private final IntersectionCoordinate _startCoord;
  private final IntersectionCoordinate _endCoord;

  public PathCoordinate(IntersectionCoordinate startCoord,
      IntersectionCoordinate endCoord) {
    _startCoord = startCoord;
    _endCoord = endCoord;
  }

  public IntersectionCoordinate get_startCoord() {
    return _startCoord;
  }

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

}
