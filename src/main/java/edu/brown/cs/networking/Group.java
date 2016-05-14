package edu.brown.cs.networking;

import java.util.Collection;
import java.util.List;

import com.google.gson.JsonObject;

/**
 * Abstraction for a Group of Users
 *
 * @author ndemarco
 */
public interface Group {

  /**
   * Add a user
   *
   * @param u
   *          user to add
   * @return boolean indicating success.
   */
  boolean add(User u);


  /**
   * Remove a user
   *
   * @param u
   *          user to remove
   * @return boolean indicating success.
   */
  boolean remove(User u);


  /**
   * Handle a message from a user.
   *
   * @param u
   *          the sending user
   * @param message
   *          the message from the user
   * @return boolean indicating success
   */
  boolean handleMessage(User u, JsonObject message);


  /**
   * @return the unique identifier for this group
   */
  String identifier();


  /**
   * @return the user-defined name of this group, not necessarily unique.
   */
  String groupName();


  /**
   * @return the number of users required to fill this group.
   */
  int maxSize();


  /**
   * @return how many users are currently in the group.
   */
  int currentSize();


  /**
   * @return boolean indicating if this group is full
   */
  boolean isFull();


  /**
   * @return boolean indicating if this group is empty
   */
  boolean isEmpty();


  /**
   * @return boolean indicating if this group has a User that has the ID
   *         {@code uuid}
   */
  boolean hasUser(String uuid);


  /**
   * @return boolean indicating if this group contains {@code u}
   */
  boolean hasUser(User u);


  /**
   * Remove all users and clean up this group.
   */
  void clear();


  /**
   * @return all of the users that have currently connected sessions.
   */
  Collection<User> connectedUsers();


  /**
   * optionally record a message from the user.
   *
   * @param m
   *          the message
   */
  void logMessage(Message m);


  /**
   * @return a list of the messages that have been logged.
   */
  List<Message> getMessageLog();

}
