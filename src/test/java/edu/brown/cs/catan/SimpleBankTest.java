package edu.brown.cs.catan;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleBankTest {

  @Test
  public void testConstruction(){
    Bank bank = new SimpleBank();
    assertTrue(bank != null);
  }

  @Test
  public void testBankRate(){
    assertTrue(new SimpleBank().getBankRate(Resource.BRICK) == Settings.BANK_RATE);
  }

  @Test
  public void testPortRateSheep(){
    assertTrue(new SimpleBank().getPortRate(Resource.SHEEP) == Settings.PORT_RATES.get(Resource.SHEEP));
  }

  @Test
  public void testPortRateWood(){
    assertTrue(new SimpleBank().getPortRate(Resource.WOOD) == Settings.PORT_RATES.get(Resource.WOOD));
  }

  @Test
  public void testPortRateWheat(){
    assertTrue(new SimpleBank().getPortRate(Resource.WHEAT) == Settings.PORT_RATES.get(Resource.WHEAT));
  }

  @Test
  public void testPortRateBrick(){
    assertTrue(new SimpleBank().getPortRate(Resource.BRICK) == Settings.PORT_RATES.get(Resource.BRICK));
  }

  @Test
  public void testPortRateOre(){
    assertTrue(new SimpleBank().getPortRate(Resource.ORE) == Settings.PORT_RATES.get(Resource.ORE));
  }


}
