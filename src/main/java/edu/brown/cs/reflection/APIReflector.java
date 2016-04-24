package edu.brown.cs.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

// UNFINISHED
public class APIReflector {

  public static JSONObject invoke(String methodName, Object on,
      List<Object> params) {

    for (Object o : params) {
      System.out.format("Got an argument %s of type %s%n", o, o.getClass());
    }

    List<Method> candidates =
        Arrays.stream(on.getClass().getMethods())
            .filter(m -> m.getName().equals(methodName))
            .filter(m -> m.getParameterCount() == params.size())
            .filter(m -> {
              Class<?>[] parameterTypes = m.getParameterTypes();
              for (int i = 0; i < parameterTypes.length; i++) {
                if (!parameterTypes[i].equals(params.get(i).getClass())) {
                  return false;
                }
              }
              return true;
            }).collect(Collectors.toList());

    if (candidates.size() > 1) {
      throw new IllegalStateException(
          "Found two methods with allegedly identical signatures");
    } else if (candidates.isEmpty()) {
      System.out.format(
          "APIReflector failed to find a method named \"%s\" with the supplied args",
          methodName);
    } else {
      try {

        Object o = candidates.get(0).invoke(on, params.toArray());
        System.out.println(o);

      } catch (IllegalAccessException | IllegalArgumentException
          | InvocationTargetException e) {
        System.out.println("Something went wrong!");
        e.printStackTrace();
      }
    }

    return null;

  }


}
