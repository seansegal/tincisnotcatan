package edu.brown.cs.catan;

import java.util.HashMap;
import java.util.Map;

public class SimpleBank implements Bank {

   Map<Resource,Integer> _supply;

   public SimpleBank(){
     _supply = new HashMap<>();
     for(Resource resource: Resource.values()){
       _supply.put(resource, 0);
     }
   }

  @Override
  public void getResource(Resource resource) {
    int newVal = _supply.get(resource) + 1;
    _supply.put(resource, newVal);
  }

  @Override
  public void discardResource(Resource resource) {
    int newVal = _supply.get(resource) -1;
    assert newVal >= 0;
    _supply.put(resource, newVal);
  }

  @Override
  public double getBankRate() {
    return Settings.BANK_RATE;
  }
}
