package edu.brown.cs.reflection;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.brown.cs.networking.RequestType;

// UNFINISHED - THIS IS A CONCEPT I NEED TO FLESH OUT
public class ActionRequest {

  private static final String REQUEST_IDENTIFIER   = "typeOfRequest";
  private static final String ARGUMENTS_IDENTIFIER = "args";
  private static final String TYPES_IDENTIFIER     = "ofType";
  private static final String CONTENT_IDENTIFIER   = "content";


  private RequestType         requestType;
  private List<Object>        content;
  private boolean             isValid              = true;


  public ActionRequest(Map<String, Object> map) {

    this.isValid = validateMap(map);

  }


  private boolean validateMap(Map<String, Object> map) {
    if (map.containsKey(REQUEST_IDENTIFIER)) {
      String key = map.get(REQUEST_IDENTIFIER).toString().toUpperCase();
      try {
        requestType = RequestType.valueOf(key);
      } catch (IllegalArgumentException e) {
        return false;
      }
      return validateArguments(map);
    }
    return false;
  }


  private boolean validateArguments(Map<String, Object> map) {
    if (map.containsKey(ARGUMENTS_IDENTIFIER)
        && map.containsKey(TYPES_IDENTIFIER)) {
      List<String> argList = new ArrayList<>();
      List<String> typeList = new ArrayList<>();

      List<?> argMaybe = (List<?>) map.get(ARGUMENTS_IDENTIFIER);
      for (Object a : argMaybe) {
        try {
          argList.add((String) a);
        } catch (ClassCastException e) {
          System.out.println("Class cast");
          return false;
        }
      }
      List<?> typeMaybe = (List<?>) map.get(TYPES_IDENTIFIER);
      for (Object t : typeMaybe) {
        try {
          typeList.add((String) t);
        } catch (ClassCastException e) {
          System.out.println("Class cast");
          return false;
        }
      }
      System.out.println(argList);
      System.out.println(typeList);
      if (argList.size() != typeList.size()) {
        return false;
      }
      content = Arguments.cast(argList, typeList);
      return true;
    } else if (map.containsKey(CONTENT_IDENTIFIER)){
      return true; // unfinished
    }
    return true; // unfinished
  }


  public boolean isValid() {
    return isValid;
  }


  public RequestType requestType() {
    return requestType;
  }

}
