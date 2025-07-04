package gift.service;

import gift.dto.Member;
import gift.dto.MemberRequestDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.stereotype.Service;

//JWT와 관련된 서비스 :
@Service
public class JwtAuthService {

    private String secretKey = "ComeOnYouGunnersNorthLondonisRed";

    //TODO: 토큰 생성
    public String createJwt(MemberRequestDto memberRequestDto){
        return Jwts.builder()
                .claim("email", memberRequestDto.email())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    //TODO: 토큰을 검증
    public void checkValidation(String token) {

    }

}