package edu.brown.cs.networking;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

class Request {

  private RequestType type;
  private JSONObject content;
  private boolean isValid;

  private static final String REQUEST_IDENTIFIER = "requestType";
  private static final String CONTENT_IDENTIFIER = "content";

  private static final Gson GSON = new Gson();

  public Request(JSONObject json) {
    isValid = true;

    if (json.has(REQUEST_IDENTIFIER)) {

      try {
        String key = json.get(REQUEST_IDENTIFIER).toString();
        type = RequestType.valueOf(key.toUpperCase());
      } catch (IllegalArgumentException e) {
        System.out.println("Received invalid request identifier");
        isValid = false;
      } catch (JSONException j) {
        j.printStackTrace();
      }
    } else {
      System.out.println("Received no request identifier.");
      isValid = false;
    }
    if(json.has(CONTENT_IDENTIFIER)){
      try {
        content = json.getJSONObject(CONTENT_IDENTIFIER);
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
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

  public JSONObject content() {
    return content;
  }

}
