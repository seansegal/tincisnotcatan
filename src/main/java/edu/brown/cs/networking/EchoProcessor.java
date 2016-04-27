package edu.brown.cs.networking;

import java.util.Collection;

import com.google.gson.JsonObject;

// a stupid simple processor that just prints out what's sent by the user.
class EchoProcessor implements RequestProcessor {

  @Override
  public boolean run(User user, Collection<User> group, JsonObject json, API api) {
    System.out.println(json.toString() + " FROM " + user.toString());
    return true;
  }


  @Override
  public boolean match(JsonObject j) {
    return true;
  }

}
