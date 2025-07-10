package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.CreateWishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<WishResponseDto> createWish(
            @RequestBody CreateWishRequestDto requestDto,
            @LoginMember Member member) {
        return new ResponseEntity<>(wishService.createWish(requestDto, member.getId()), HttpStatus.CREATED);
    }
}