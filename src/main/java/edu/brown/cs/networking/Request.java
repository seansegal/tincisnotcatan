package edu.brown.cs.networking;

import java.util.Map;

class Request {

  private RequestType type;
  private Object content;
  private boolean isValid;

  private static final String REQUEST_IDENTIFIER = "requestType";
  private static final String CONTENT_IDENTIFIER = "content";

  public Request(Map<String, Object> map) {
    isValid = true;
    if (map.containsKey(REQUEST_IDENTIFIER)) {
      String key = map.get(REQUEST_IDENTIFIER).toString();
      try {
        type = RequestType.valueOf(key.toUpperCase());
      } catch (IllegalArgumentException e) {
        System.out.println("Received invalid request identifier : " + key);
        isValid = false;
      }
    } else {
      System.out.println("Received no request identifier.");
      isValid = false;
    }
    if(map.containsKey(CONTENT_IDENTIFIER)){
      content = map.get(CONTENT_IDENTIFIER);
    } else {
      System.out.println("Received no content in request.");
      isValid = false;
    }

  }

  public RequestType type() {
    return type;
  }

  public boolean isValid(){
    return isValid;
  }

  public Object content() {
    return content;
  }

}
