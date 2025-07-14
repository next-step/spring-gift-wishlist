package gift.controller;

import gift.config.LoginMember;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    // 위시리스트 추가
    @PostMapping
    public ResponseEntity<WishResponseDto> addWish(
            @LoginMember Member member,
            @Valid @RequestBody WishRequestDto requestDto) {
        WishResponseDto response = wishService.addWish(member, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
} 