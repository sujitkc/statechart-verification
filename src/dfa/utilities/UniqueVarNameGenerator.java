package utilities;

import java.util.HashSet;

public class UniqueVarNameGenerator {
    HashSet<String> varNames;

    public UniqueVarNameGenerator() {
        this.varNames = new HashSet<>();
    }

    public String generate() {
        String varName = UniqueVarNameGenerator.generateRandomString();
        while (this.varNames.contains(varName)) {
            varName = UniqueVarNameGenerator.generateRandomString();
        }
        return varName;
    }

    static String validCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static int length = validCharacters.length();

    protected static String generateRandomString() {
        int len = 6;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(validCharacters.charAt((int) (Math.random() * length)));
        }
        return builder.toString();
    }
}
