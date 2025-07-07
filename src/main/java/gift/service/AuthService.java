package gift.service;

import gift.dto.TokenResponseDto;
import gift.dto.UserRequestDto;
import gift.dto.UserResponseDto;
import gift.entity.User;
import gift.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final String jwtKey;

    public AuthService(UserRepository userRepository, @Value("${jwt_key}") String jwtKey) {
        this.userRepository = userRepository;
        this.jwtKey = jwtKey;
    }

    public UserResponseDto userSignUp(UserRequestDto userRequestDto) {

        // Base64 인코딩 변환
        User user = new User(
                Base64.getEncoder().encodeToString(userRequestDto.email().getBytes(StandardCharsets.UTF_8)),
                Base64.getEncoder().encodeToString(userRequestDto.password().getBytes(StandardCharsets.UTF_8)));

        return new UserResponseDto(userRepository.createUser(user));
    }

    public TokenResponseDto userLogin(UserRequestDto userRequestDto) {

        // Base64 인코딩 변환
        User user = new User(
                Base64.getEncoder().encodeToString(userRequestDto.email().getBytes(StandardCharsets.UTF_8)),
                Base64.getEncoder().encodeToString(userRequestDto.password().getBytes(StandardCharsets.UTF_8)));

        userRepository.checkUser(user); // 일치하는 단 1개의 회원정보가 있는지 체크, 없을시 예외

        return new TokenResponseDto(Jwts.builder()
                .setSubject(user.email())
                .signWith(Keys.hmacShaKeyFor(jwtKey.getBytes()))
                .compact());
    }
}
