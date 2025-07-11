package gift.controller;

import gift.dto.wishRequestDto;
import gift.dto.wishResponseDto;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wish")
public class WishController {

    private final WishListService wishListService;

    public WishController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping("/list")
    public ResponseEntity<wishResponseDto> findAllWishes() {
    }

    @PostMapping("/add")
    public ResponseEntity<wishResponseDto> addWish(@RequestBody wishRequestDto requestDto) {
    }

    @PatchMapping("/patch")
    public ResponseEntity<wishResponseDto> updateWish(@RequestBody wishRequestDto requestDto) {
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteWish(@RequestBody wishRequestDto requestDto) {
    }
}
