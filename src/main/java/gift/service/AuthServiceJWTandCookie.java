package gift.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

//JWT와 관련된 서비스 :
@Service
public class AuthServiceJWTandCookie {

    private final String secretKey = "ComeOnYouGunnersNorthLondonisRedNorthLondonFOREVER";

    //payload의 정보를 추출하는 함수
    public Long getMemberId(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberId", Long.class);
    }

    //토큰 생성
    public String createJwt(String email, Long memberId){
        return Jwts.builder()
                .claim("email", email)
                .claim("memberId", memberId)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    //토큰을 검증 -> 로그인 이후의 동작
    public void checkValidation(String token) {

        try{
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseSignedClaims(token);
        }
        catch (Exception e){
            //Authorization 헤더가 유효하지 않거나 토큰이 유효하지 않은 경우 401 Unauthorized 반환
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }

}