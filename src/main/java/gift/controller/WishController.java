package gift.controller;

import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.service.WishService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<WishResponseDto>> findUserWishes() {
        return ResponseEntity.ok(wishService.findUserWishes());
    }

    @PostMapping("/add")
    public ResponseEntity<WishResponseDto> addWish(@RequestBody WishRequestDto requestDto) {
        return new ResponseEntity<>(wishService.addWish(requestDto), HttpStatus.CREATED);
    }

    @PatchMapping("/patch")
    public ResponseEntity<Void> updateWish(@RequestBody WishRequestDto requestDto) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteWish(@RequestBody WishRequestDto requestDto) {
        return ResponseEntity.noContent().build();
    }
}
