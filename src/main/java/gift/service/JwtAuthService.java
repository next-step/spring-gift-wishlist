package gift.service;

import gift.dto.MemberRequestDto;
import groovy.util.logging.Slf4j;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

//JWT와 관련된 서비스 :
@Slf4j
@Service
public class JwtAuthService {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthService.class);
    private final String secretKey = "ComeOnYouGunnersNorthLondonisRedNorthLondonFOREVER";

    //payload의 정보를 추출하는 함수
    public Long getMemberId(String bearerToken){
        String token = bearerToken.split(" ")[1]; //접두사 제거
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberId", Long.class);
    }

    //TODO: 토큰 생성
    public String createJwt(String email, Long memberId){
        return Jwts.builder()
                .claim("email", email)
                .claim("memberId", memberId)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    //TODO: 토큰을 검증 -> 로그인 이후의 동작 (wishList -> 사용자별 wishList 존재)
    public void checkValidation(String bearerToken) {

        if(!bearerToken.startsWith("Bearer ")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }

        try{
            String token = bearerToken.split(" ")[1]; //Bearer 접두사 제거
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseSignedClaims(token);
        }
        catch (Exception e){
            //Authorization 헤더가 유효하지 않거나 토큰이 유효하지 않은 경우 401 Unauthorized 반환
            log.info("토큰검증에 실패했습니다.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }

}