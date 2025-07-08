package gift.service;

import gift.dto.*;
import gift.entity.User;
import gift.exception.ProductNotFoundException;
import gift.exception.UserNotFoundException;
import gift.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<UserResponseDto> findAllUsers(){
        return userRepository.findAllUsers().stream().map(UserResponseDto::new).collect(Collectors.toList());
    }

    public UserResponseDto findUserById(Long id) {
        User user = userRepository.findUserById(id);
        return new UserResponseDto(user);
    }

    public void deleteUser(Long id){
        boolean flag = userRepository.deleteUser(id);
        if(!flag) {
            throw new ProductNotFoundException(id);
        }
    }

    public UserResponseDto updateUser(Long id, UserRequestDto requestDto){
        boolean flag = userRepository.updateUser(id, new User(requestDto.email(), requestDto.password()));

        // 수정됐는지 검증
        if(!flag) {
            throw new UserNotFoundException(id);
        }

        User user = userRepository.findUserById(id);
        return new UserResponseDto(user);
    }
}
