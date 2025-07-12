package gift.controller;


import gift.dto.request.WishRequestDto;
import gift.dto.response.WishResponseDto;
import gift.entity.User;
import gift.service.UserService;
import gift.service.WishService;
import gift.wishPreProcess.LoginMember;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final UserService userService;
    private final WishService wishService;

    public WishController(UserService userService, WishService wishService) {
        this.userService = userService;
        this.wishService = wishService;
    }

    @PostMapping("")
    public ResponseEntity<WishResponseDto> addToWish(
        @RequestBody @Valid WishRequestDto wishRequestDto,
        @LoginMember User currentUser
    ) {

    }


}
