package edu.brown.cs.networking;


class ExpiredUserException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = 399002437494062502L;


  public ExpiredUserException() {
    super();
  }


  public ExpiredUserException(Throwable cause) {
    super(cause);
  }


  public ExpiredUserException(String message) {
    super(message);
  }


  public ExpiredUserException(String message, Throwable cause) {
    super(message, cause);
  }

}
