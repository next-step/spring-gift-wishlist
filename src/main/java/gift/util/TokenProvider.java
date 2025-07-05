package gift.util;

public interface TokenProvider {

    String createToken(Long userId);
    Long getUserId(String token);
}
