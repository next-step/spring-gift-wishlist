package gift.utils;

import java.util.List;

public class JwtParser {
    public static boolean isValidTokenType(List<String> token){
        return token.getFirst().split(" ")[0].equals("Bearer");
    }

    public static String getToken(List<String> token){
        return token.getFirst().split(" ")[1];
    }
}
