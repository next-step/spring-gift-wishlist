package gift.service;

import gift.exception.token.TokenExpiredException;
import gift.exception.token.TokenTypeException;
import gift.utils.JwtParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static gift.status.TokenErrorStatus.INVALID_TOKEN_TYPE;

@Service
public class TokenService {
    @Value("${jwt-secret-key}")
    private String secretKey;

    @Value("${jwt-access-token-expire-time}")
    private Integer accessTokenTTL;

    public boolean isTokenExpired(List<String> tokens) throws TokenExpiredException {
        if(!JwtParser.isValidTokenType(tokens)){
            throw new TokenTypeException(INVALID_TOKEN_TYPE.getErrorMessage());
        }
        return extractExpiration(JwtParser.getToken(tokens)).before(new Date());
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenTTL))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractEmail(String token){
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
