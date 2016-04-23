package edu.brown.cs.board;

public class IntersectionCoordinate {
  private final HexCoordinate coord1;
  private final HexCoordinate coord2;
  private final HexCoordinate coord3;

  public IntersectionCoordinate(HexCoordinate coord1, HexCoordinate coord2,
      HexCoordinate coord3) {
    this.coord1 = coord1;
    this.coord2 = coord2;
    this.coord3 = coord3;
  }

  public HexCoordinate getCoord1() {
    return coord1;
  }

  public HexCoordinate getCoord2() {
    return coord2;
  }

  public HexCoordinate getCoord3() {
    return coord3;
  }

  @Override
  public int hashCode() {
    int result;
    result = ((coord1 == null) ? 0 : coord1.hashCode());
    result += ((coord2 == null) ? 0 : coord2.hashCode());
    result += ((coord3 == null) ? 0 : coord3.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof IntersectionCoordinate)) {
      return false;
    }
    IntersectionCoordinate toComp = (IntersectionCoordinate) obj;
    if (coord1.equals(toComp.getCoord1())
        && coord2.equals(toComp.getCoord2())
        && coord3.equals(toComp.getCoord3())) {
      return true;
    }
    if (coord1.equals(toComp.getCoord1())
        && coord2.equals(toComp.getCoord3())
        && coord3.equals(toComp.getCoord2())) {
      return true;
    }
    if (coord1.equals(toComp.getCoord2())
        && coord2.equals(toComp.getCoord1())
        && coord3.equals(toComp.getCoord3())) {
      return true;
    }
    if (coord1.equals(toComp.getCoord3())
        && coord2.equals(toComp.getCoord2())
        && coord3.equals(toComp.getCoord1())) {
      return true;
    }
    if (coord1.equals(toComp.getCoord3())
        && coord2.equals(toComp.getCoord1())
        && coord3.equals(toComp.getCoord2())) {
      return true;
    }
    if (coord1.equals(toComp.getCoord2())
        && coord2.equals(toComp.getCoord3())
        && coord3.equals(toComp.getCoord1())) {
      return true;
    }

    return false;
  }

  @Override
  public String toString() {
    StringBuilder toRet = new StringBuilder();
    toRet.append(coord1.toString());
    toRet.append(" ");
    toRet.append(coord2.toString());
    toRet.append(" ");
    toRet.append(coord3.toString());
    toRet.append("\n");
    return toRet.toString();
  }

}
