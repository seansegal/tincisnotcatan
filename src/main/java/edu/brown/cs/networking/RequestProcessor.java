package edu.brown.cs.networking;

import java.util.Collection;

import com.google.gson.JsonObject;

public interface RequestProcessor {

  boolean run(User<?> user, Collection<User<?>> group, JsonObject json);


  String identifier();


}
