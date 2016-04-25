package edu.brown.cs.catan;

import edu.brown.cs.networking.UserData;


public class CatanUserData implements UserData {

  // need to assign default illegal values to get the class for casting.
  private String  userName = "";
  private String  color    = ""; // should be a color object in the future.
  private Integer gameSize = -1;


  @Override
  public boolean setField(String field, Object value) {
    switch (field.toLowerCase()) {
      case "username":
        userName = (String) value;
        return true;
      case "color":
        color = (String) value;
        return true;
      case "gamesize":
        gameSize = (Integer) value;
        return true;
      default:
        throw new IllegalArgumentException(
            "Field " + field + " is not supported by CatanUserData");
    }
  }


  @Override
  public Object getField(String field) {
    switch (field.toLowerCase()) {
      case "username":
        return userName;
      case "color":
        return color;
      case "gamesize":
        return gameSize;
      default:
        throw new IllegalArgumentException(
            "Field " + field + " is not supported by CatanUserData");
    }
  }


  @Override
  public boolean isValid() {
    return !userName.equals("")
        && !color.equals("")
        && !gameSize.equals(-1);
  }


}
