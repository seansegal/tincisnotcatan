package edu.brown.cs.networking;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


public class GroupView implements Group {

  @SerializedName("group")
  private Group inner;


  public GroupView(Group g) {
    this.inner = g;
  }


  @Override
  public boolean add(User u) {
    throw new UnsupportedOperationException("Can't add to view of group");
  }


  @Override
  public boolean remove(User u) {
    throw new UnsupportedOperationException("Can't remove from view of group");
  }


  @Override
  public boolean handleMessage(User u, JsonObject message) {
    throw new UnsupportedOperationException(
        "Can't send message to view of group");
  }


  @Override
  public String identifier() {
    return inner.identifier();
  }


  @Override
  public int maxSize() {
    return inner.maxSize();
  }


  @Override
  public int currentSize() {
    return inner.currentSize();
  }


  @Override
  public boolean isFull() {
    return inner.isFull();
  }


  @Override
  public boolean isEmpty() {
    return inner.isEmpty();
  }


  @Override
  public String groupName() {
    return inner.groupName();
  }


  @Override
  public void userDisconnected(User u, long expiresAt) {
    throw new UnsupportedOperationException("Can't modify a GroupView");
  }


  @Override
  public void userReconnected(User u) {
    throw new UnsupportedOperationException("Can't modify a GroupView");
  }


  @Override
  public boolean afkTick() {
    throw new UnsupportedOperationException(
        "Illegal action for a GroupView : afkTick()");
  }

  @Override
  public boolean hasUserWithID(String uuid) {
    return inner.hasUserWithID(uuid);
  }

}
