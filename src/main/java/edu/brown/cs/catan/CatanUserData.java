package edu.brown.cs.catan;

import edu.brown.cs.networking.UserData;


public class CatanUserData implements UserData {

  private String  userName;
  private String  color;   // should be a color object in the future.
  private Integer gameSize;


  @Override
  public boolean setField(String field, Object value) {
    Object f = findField(field);
    if (field.equals(f)) {
      return false;
    }
    f = (f.getClass().cast(value));
    return true;
  }


  @Override
  public Object getField(String field) {
    return findField(field);
  }


  @Override
  public boolean isValid() {
    return userName != null
        && color != null
        && gameSize != null;
  }


  private Object findField(String field) {
    switch (field) {
      case "userName":
        return userName;
      case "color":
        return color;
      case "gameSize":
        return gameSize;
      default:
        throw new IllegalArgumentException(
            "Field " + field + " is not supported by CatanUserData");
    }
  }


}
