package gift.controller.api;


import gift.dto.user.UserCreateRequest;
import gift.dto.user.UserResponse;
import gift.entity.User;
import gift.model.CustomPage;
import gift.service.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public ResponseEntity<CustomPage<UserResponse>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0")
            @Min(value = 0, message = "페이지 번호는 0 이상이여야 합니다.") Integer page,
            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1, message = "페이지 크기는 양수여야 합니다.") Integer size
    ) {
        var pageResponse =  CustomPage.convert(
            userService.getBy(page, size),UserResponse::fromEntity
        );
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @NotNull(message = "사용자 ID는 필수입니다.")
            @PathVariable Long id
    ) {
        var user = userService.getById(id);
        return new ResponseEntity<>(UserResponse.fromEntity(user), HttpStatus.OK);
    }

    @PostMapping
    public UserResponse createUser(
            @Valid  @RequestBody UserCreateRequest request
    ) {
        log.info("사용자 생성 요청: {}", request);
        var user = userService.create(request.toEntity());
        log.info("사용자 생성 완료: {}", user);
        return UserResponse.fromEntity(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable @NotNull(message = "사용자 ID는 필수입니다.") Long id,
            @Valid @RequestBody UserCreateRequest request
    ) {
        log.info("사용자 업데이트 요청: {}", request);
        User updateRequest = request.toEntity();
        updateRequest.setId(id);
        var user = userService.update(updateRequest);
        log.info("사용자 업데이트 완료: {}", user);
        return new ResponseEntity<>(UserResponse.fromEntity(user), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> patchUser(
            @PathVariable @NotNull(message = "사용자 ID는 필수입니다.") Long id,
            @Valid @RequestBody UserCreateRequest request
    ) {
        log.info("사용자 패치 요청: {}", request);
        User patchRequest = request.toEntity();
        patchRequest.setId(id);
        var user = userService.patch(patchRequest);
        log.info("사용자 패치 완료: {}", user);
        return new ResponseEntity<>(UserResponse.fromEntity(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable @NotNull(message = "사용자 ID는 필수입니다.") Long id
    ) {
        log.info("사용자 삭제 요청: ID={}", id);
        userService.deleteById(id);
        log.info("사용자 삭제 완료: ID={}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
