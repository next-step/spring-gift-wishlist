package gift.controller;

import gift.config.LoginMember;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.service.WishService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    // 상품 추가
    @PostMapping
    public ResponseEntity<WishResponseDto> addWish(
            @RequestBody WishRequestDto wishRequestDto,
            @LoginMember Member member) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(wishService.addWish(member.getId(), wishRequestDto.productId()));
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getWishlist(@LoginMember Member member) {

        return ResponseEntity.ok(wishService.getWishlist(member.getId()));
    }

    // 상품 삭제
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
            @PathVariable Long wishId,
            @LoginMember Member member) {
        wishService.deleteWish(member.getId(), wishId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
