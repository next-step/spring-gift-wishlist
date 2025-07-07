package gift.controller;

import gift.dto.request.ModifyUserRequest;
import gift.dto.response.UserResponse;
import gift.exception.token.TokenExpiredException;
import gift.exception.token.TokenTypeException;
import gift.service.TokenService;
import gift.service.UserService;
import gift.utils.JwtParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gift.status.TokenErrorStatus.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @GetMapping("/info")
    public ResponseEntity<String> getUserInfo(@RequestHeader HttpHeaders headers){
        if(headers.get("Authorization") == null || !JwtParser.isValidTokenType(headers.get("Authorization"))){
            throw new TokenTypeException(INVALID_TOKEN_TYPE.getErrorMessage());
        }
        String token = JwtParser.getToken(headers.get("Authorization"));
        if(tokenService.isTokenExpired(token)) {
            throw new TokenExpiredException(TOKEN_EXPIRED.getErrorMessage());
        }
        return ResponseEntity.ok().body(tokenService.extractEmail(token));
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<String> modifyUser(
            @RequestHeader HttpHeaders headers,
            @PathVariable Long userId,
            @RequestBody ModifyUserRequest modifyUserRequest
    ){
        if(headers.get("Authorization") == null || !JwtParser.isValidTokenType(headers.get("Authorization"))){
            throw new TokenTypeException(INVALID_TOKEN_TYPE.getErrorMessage());
        }
        String token = JwtParser.getToken(headers.get("Authorization"));
        if(tokenService.isTokenExpired(token)){
            throw new TokenTypeException(TOKEN_EXPIRED.getErrorMessage());
        }
        return ResponseEntity.ok().body(userService.modifyUserInfo(userId, modifyUserRequest));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(
            @RequestHeader HttpHeaders headers,
            @PathVariable Long userId
    ){
        if(headers.get("Authorization") == null || !JwtParser.isValidTokenType(headers.get("Authorization"))){
            throw new TokenTypeException(INVALID_TOKEN_TYPE.getErrorMessage());
        }
        String token = JwtParser.getToken(headers.get("Authorization"));
        if(tokenService.isTokenExpired(token)){
            throw new TokenTypeException(TOKEN_EXPIRED.getErrorMessage());
        }
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
