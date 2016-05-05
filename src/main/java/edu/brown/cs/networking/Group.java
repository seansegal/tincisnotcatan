package edu.brown.cs.networking;

import com.google.gson.JsonObject;

public interface Group {

  boolean add(User u);


  boolean remove(User u);


  boolean handleMessage(User u, JsonObject message);


  String identifier();


  String groupName();


  int maxSize();


  int currentSize();


  boolean isFull();


  boolean isEmpty();


  void userDisconnected(User u, long expiresAt);


  void userReconnected(User u);


  boolean afkTick();

  boolean hasUserWithID(String uuid);

}
