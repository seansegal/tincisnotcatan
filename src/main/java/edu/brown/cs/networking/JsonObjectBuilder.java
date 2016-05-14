package edu.brown.cs.networking;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

class JsonObjectBuilder {

  private JsonObject j;


  public JsonObjectBuilder() {
    j = new JsonObject();
  }


  public JsonObjectBuilder addProperty(String key, String value) {
    j.addProperty(key, value);
    return this;
  }


  public JsonObjectBuilder addProperty(String key, int value) {
    j.addProperty(key, value);
    return this;
  }


  public JsonObjectBuilder add(String key, JsonElement element) {
    j.add(key, element);
    return this;
  }


  public JsonObject build() {
    return j;
  }

}
