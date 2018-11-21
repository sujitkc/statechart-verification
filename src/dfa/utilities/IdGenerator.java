package utilities;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IdGenerator {
	private static Set<String> mIds = new HashSet<String>();
	public static String generateId(String type) {		
		while (true) {
			Random random = new Random ();
			int integer = random.nextInt();
			if (integer < 0) {
				integer = -1 * integer;
			}
			String id = type + Integer.toString(integer);
			if (!IdGenerator.mIds.contains(id)) {
				IdGenerator.mIds.add(id);
				return id;
			}
		}
	}
	
	public static boolean hasId(String id) {
		return IdGenerator.mIds.contains(id);
	}
	
	public static void addId(String id) {
		IdGenerator.mIds.add(id);
	}

	public static void printAllIds() {
		for(String id : IdGenerator.mIds) {
			System.out.println(id);
		}
	}
}
