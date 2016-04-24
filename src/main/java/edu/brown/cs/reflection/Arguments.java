package edu.brown.cs.reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// UNFINISHED
public class Arguments {

  public static List<Object> cast(List<String> args, List<String> types) {
    List<Object> toReturn = new ArrayList<>();
    if(args.size() != types.size()) {
      System.out.println("Arguments.cast() received lists of different lengths");
      return Collections.emptyList();
    }
    try{
      for(int i = 0; i < args.size(); i++) {
        switch(types.get(i).toUpperCase()) {
          case "INT":
          case "INTEGER":
            toReturn.add(Integer.parseInt(args.get(i)));
            break;
          case "STRING":
          case "STR":
            toReturn.add(args.get(i));
            break;
          case "BOOLEAN":
          case "BOOL":
            toReturn.add(Boolean.parseBoolean(args.get(i)));
            break;
          case "DOUBLE":
            toReturn.add(Double.parseDouble(args.get(i)));
            break;
          default:
            throw new UnsupportedOperationException("This class doesn't support casting " + types.get(i));
        }
      }
    } catch (NumberFormatException e) {
      System.out.println("Arguments.cast() got number format exception");
      return Collections.emptyList();
    }
    return toReturn;
  }

}
