package edu.brown.cs.board;

public class IntersectionCoordinate {
  private final HexCoordinate _coord1;
  private final HexCoordinate _coord2;
  private final HexCoordinate _coord3;

  public IntersectionCoordinate(HexCoordinate coord1, HexCoordinate coord2,
      HexCoordinate coord3) {
    _coord1 = coord1;
    _coord2 = coord2;
    _coord3 = coord3;
  }

  public HexCoordinate getCoord1() {
    return _coord1;
  }

  public HexCoordinate getCoord2() {
    return _coord2;
  }

  public HexCoordinate getCoord3() {
    return _coord3;
  }

  @Override
  public int hashCode() {
    int result;
    result = ((_coord1 == null) ? 0 : _coord1.hashCode());
    result += ((_coord2 == null) ? 0 : _coord2.hashCode());
    result += ((_coord3 == null) ? 0 : _coord3.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof IntersectionCoordinate)) {
      return false;
    }
    IntersectionCoordinate toComp = (IntersectionCoordinate) obj;
    if (_coord1.equals(toComp.getCoord1())
        && _coord2.equals(toComp.getCoord2())
        && _coord3.equals(toComp.getCoord3())) {
      return true;
    }
    if (_coord1.equals(toComp.getCoord1())
        && _coord2.equals(toComp.getCoord3())
        && _coord3.equals(toComp.getCoord2())) {
      return true;
    }
    if (_coord1.equals(toComp.getCoord2())
        && _coord2.equals(toComp.getCoord1())
        && _coord3.equals(toComp.getCoord3())) {
      return true;
    }
    if (_coord1.equals(toComp.getCoord3())
        && _coord2.equals(toComp.getCoord2())
        && _coord3.equals(toComp.getCoord1())) {
      return true;
    }
    if (_coord1.equals(toComp.getCoord3())
        && _coord2.equals(toComp.getCoord1())
        && _coord3.equals(toComp.getCoord2())) {
      return true;
    }
    if (_coord1.equals(toComp.getCoord2())
        && _coord2.equals(toComp.getCoord3())
        && _coord3.equals(toComp.getCoord1())) {
      return true;
    }

    return false;
  }

  @Override
  public String toString() {
    StringBuilder toRet = new StringBuilder();
    toRet.append(_coord1.toString());
    toRet.append(" ");
    toRet.append(_coord2.toString());
    toRet.append(" ");
    toRet.append(_coord3.toString());
    toRet.append("\n");
    return toRet.toString();
  }

}
