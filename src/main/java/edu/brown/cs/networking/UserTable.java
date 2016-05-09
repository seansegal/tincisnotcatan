package edu.brown.cs.networking;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UserTable {

  private final Set<User>       users;
  private final Map<User, Long> afk;


  public UserTable() {
    this.users = new HashSet<>();
    this.afk = new HashMap<>();
  }


  public boolean addUser(User u) {
    synchronized (this) {
      return users.add(u);
    }
  }


  public boolean removeUser(User u) {
    synchronized (this) {
      return users.remove(u);
    }
  }


  public boolean userAFK(User u, long expiresAt) {
    synchronized (this) {
      if (!users.contains(u)) {
        return false;
      }
      afk.put(u, expiresAt);
      System.out.println("AFK set: Keyset : " + afk.keySet());
      return true;
    }
  }


  public boolean userNotAFK(User u) {
    synchronized (this) {
      if (!afk.containsKey(u)) {
        return false;
      }
      afk.remove(u);
      System.out.println("AFK Unset: Keyset: " + afk.keySet());
      return true;
    }
  }


  public boolean contains(User u) {
    synchronized (this) {
      return users.contains(u);
    }
  }


  public boolean contains(String id) {
    System.out.println(users);
    synchronized (this) {
      return users.stream()
          .anyMatch(u -> u.hasField(Networking.USER_IDENTIFIER)
              && u.getField(Networking.USER_IDENTIFIER).equals(id));
    }
  }


  public boolean isEmpty() {
    synchronized (this) {
      return users.isEmpty();
    }
  }


  public Collection<User> users() {
    return Collections.unmodifiableCollection(users);
  }


  public Collection<User> onlyConnectedUsers() {
    return Collections.unmodifiableCollection(users.stream()
        .filter(u -> !afk.containsKey(u)).collect(Collectors.toList()));
  }


  public Collection<User> onlyDisconnectedUsers() {
    return Collections.unmodifiableCollection(users.stream()
        .filter(u -> afk.containsKey(u)).collect(Collectors.toList()));
  }


  public Map<User, Long> afkMap() {
    return Collections.unmodifiableMap(afk);
  }


  public boolean isAfk(User u) {
    synchronized (this) {
      System.out.println("Checking afk : keyset : " + afk.keySet());
      return afk.keySet().contains(u);
    }
  }


  public int size() {
    synchronized (this) {
      return users.size();
    }
  }


  public boolean expired(User u) throws ExpiredUserException {
    synchronized (this) {
      if (!afk.containsKey(u)) {
        throw new ExpiredUserException("User not in AFK representation");
      }
      return afk.get(u) < System.currentTimeMillis();
    }

  }


  public boolean allUsersConnected() {
    synchronized (this) {
      return afk.isEmpty();
    }
  }


  public void clear() {
    synchronized (this) {
      users.clear();
      afk.clear();
    }
  }


}
