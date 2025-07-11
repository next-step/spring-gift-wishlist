package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> findWishList(@LoginMember Member member) {

        return ResponseEntity.ok(wishService.findWishList(member.getId()));
    }

    @PostMapping
    public ResponseEntity<WishResponseDto> createWish(
            @LoginMember Member member,
            @Valid @RequestBody WishRequestDto dto
            ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(wishService.saveWish(member.getId(), dto));
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
            @PathVariable Long wishId,
            @LoginMember Member member
    ) {
        wishService.deleteWish(wishId, member);
        return ResponseEntity.noContent().build();
    }

}
