package gift.controller;

import gift.config.LoginMember;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/wishes")
@RestController
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping("")
    public ResponseEntity<List<WishResponseDto>> getWishList(@LoginMember Member member) {
        return ResponseEntity.ok(
                wishService.getWishList(member.id())
        );
    }

    @PostMapping("/{productId}")
    public ResponseEntity<String> saveWish(
            @LoginMember Member member,
            @PathVariable Long productId,
            @RequestBody WishRequestDto wishRequestDto
            ) {
        wishService.saveWish(
                member.id(),
                productId,
                wishRequestDto.count()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Wish Successfully Created");
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<String> patchWish(
            @LoginMember Member member,
            @PathVariable Long productId,
            @RequestBody WishRequestDto wishRequestDto
    ) {
        wishService.updateWishCount(
                member.id(),
                productId,
                wishRequestDto.count()
        );

        return ResponseEntity
                .ok("Wish Successfully patched");
    }
}
