package gift.controller;

import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.service.WishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wish")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<WishResponseDto>> findAllWishes() {
        return ResponseEntity.ok(wishService.findAllWishes());
    }

    @PostMapping("/add")
    public ResponseEntity<WishResponseDto> addWish(@RequestBody WishRequestDto requestDto) {
        return ResponseEntity.created(wishService.addWish(requestDto));
    }

    @PatchMapping("/patch")
    public ResponseEntity<WishResponseDto> updateWish(@RequestBody WishRequestDto requestDto) {
        return ResponseEntity.ok(wishService.updateWish(requestDto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteWish(@RequestBody WishRequestDto requestDto) {
        return ResponseEntity.noContent().build();
    }
}
