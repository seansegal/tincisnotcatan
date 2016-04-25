package edu.brown.cs.networking;

import org.eclipse.jetty.websocket.api.Session;

class User<D extends UserData> {

  private Session session;
  private D       data;
  private int     userID;


  public User(Session s, int userID, Class<D> data) {
    this.session = s;
    this.userID = userID;
    try {
      this.data = data.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      System.out.println("Illegal user data type : " + data);
      assert false;
      e.printStackTrace();
    }
  }


  public Object getField(String field) {
    return data.getField(field);
  }


  public boolean setField(String field, Object value) {
    return data.setField(field, value);
  }


  public boolean isValid() {
    return session.isOpen() && data.isValid();
  }


  public Session session() {
    return session;
  }


  public int userID() {
    return userID;
  }

}
