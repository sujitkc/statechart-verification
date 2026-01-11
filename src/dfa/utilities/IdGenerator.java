package utilities;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IdGenerator {
  private static Set<String> ids = new HashSet<String>();
  public static String generateId(String type) {    
    while (true) {
      Random random = new Random ();
      int integer = random.nextInt();
      if (integer < 0) {
        integer = -1 * integer;
      }
      String id = type + Integer.toString(integer);
      if (!IdGenerator.ids.contains(id)) {
        IdGenerator.ids.add(id);
        return id;
      }
    }
  }
  
  public static boolean hasId(String id) {
    return IdGenerator.ids.contains(id);
  }
  
  public static void addId(String id) {
    IdGenerator.ids.add(id);
  }

  public static void printAllIds() {
    for(String id : IdGenerator.ids) {
      System.out.println(id);
    }
  }
}
