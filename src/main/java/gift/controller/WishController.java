package gift.controller;

import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
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

    // 상품 추가
    @PostMapping
    public ResponseEntity<WishResponseDto> addWish(
            @RequestBody WishRequestDto wishRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                // memberId 임시 지정
                .body(wishService.addWish(1L, wishRequestDto.productId()));
    }
}
