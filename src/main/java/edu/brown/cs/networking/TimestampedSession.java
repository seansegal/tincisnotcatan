package edu.brown.cs.networking;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.SuspendToken;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;


class TimestampedSession
    implements Session, Comparable<TimestampedSession> {

  private Session inner;
  private long createdAt;


  public TimestampedSession(Session s) {
    inner = s;
    createdAt = System.currentTimeMillis();
  }


  @Override
  public void close() {
    inner.close();
  }


  @Override
  public void close(CloseStatus arg0) {
    inner.close(arg0);
  }


  @Override
  public void close(int arg0, String arg1) {
    inner.close(arg0, arg1);
  }


  @Override
  public void disconnect() throws IOException {
    inner.disconnect();
  }


  @Override
  public long getIdleTimeout() {
    return inner.getIdleTimeout();
  }


  @Override
  public InetSocketAddress getLocalAddress() {
    return inner.getLocalAddress();
  }


  @Override
  public WebSocketPolicy getPolicy() {
    return inner.getPolicy();
  }


  @Override
  public String getProtocolVersion() {
    return inner.getProtocolVersion();
  }


  @Override
  public RemoteEndpoint getRemote() {
    return inner.getRemote();
  }


  @Override
  public InetSocketAddress getRemoteAddress() {
    return inner.getRemoteAddress();
  }


  @Override
  public UpgradeRequest getUpgradeRequest() {
    return inner.getUpgradeRequest();
  }


  @Override
  public UpgradeResponse getUpgradeResponse() {
    return inner.getUpgradeResponse();
  }


  @Override
  public boolean isOpen() {
    return inner.isOpen();
  }


  @Override
  public boolean isSecure() {
    return inner.isSecure();
  }


  @Override
  public void setIdleTimeout(long arg0) {
    inner.setIdleTimeout(arg0);
  }


  @Override
  public SuspendToken suspend() {
    return inner.suspend();
  }


  @Override
  public int compareTo(TimestampedSession o) {
    if(createdAt < o.createdAt) {
      return -1;
    }
    if(createdAt > o.createdAt) {
      return 1;
    }
    return 0;
  }

}
