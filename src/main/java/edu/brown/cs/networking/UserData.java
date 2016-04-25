package edu.brown.cs.networking;


public interface UserData {


  boolean setField(String field, Object value);


  Object getField(String field);


  boolean isValid();
}
