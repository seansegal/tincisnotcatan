package edu.brown.cs.api;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

// System Tests
public class CatanAPITest {

  @Test
  public void testCreation() {
    CatanAPI api = new CatanAPI();
    // TODO test with settings;
    assertTrue(api != null);
  }

}
