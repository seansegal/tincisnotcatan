package edu.brown.cs.catan;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleBankTest {

  @Test
  public void testConstruction(){
    Bank bank = new SimpleBank();
    assertTrue(bank != null);
    // Test other initial properties properties here
  }

  @Test
  public void testBankRate(){
    assertTrue(new SimpleBank().getBankRate(Resource.BRICK) == Settings.BANK_RATE);
  }


}
