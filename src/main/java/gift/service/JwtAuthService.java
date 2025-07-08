package gift.service;

import gift.dto.MemberRequestDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

//JWT와 관련된 서비스 :
@Service
public class JwtAuthService {

    private String secretKey = "ComeOnYouGunnersNorthLondonisRedNorthLondonFOREVER";

    //TODO: 토큰 생성
    public String createJwt(MemberRequestDto memberRequestDto){
        return Jwts.builder()
                .claim("email", memberRequestDto.email())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    //TODO: 토큰을 검증 -> 로그인 이후의 동작 (wishList -> 사용자별 wishList 존재)
    public void checkValidation(String token) {
        //헤더가 유효하지 않거나 토큰이 유효하지 않은 경우 : 401
    }

}