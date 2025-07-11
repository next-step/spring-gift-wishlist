package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.User;
import gift.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getAllWishes(@LoginUser User user) {
        return new ResponseEntity<>(wishService.getAllWishes(user.getId()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WishResponseDto> postWish(
            @RequestBody WishRequestDto wishRequestDto,
            @LoginUser User user) {
        return new ResponseEntity<>(wishService.createWish(user.getId(), wishRequestDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<WishResponseDto> updateWish(
            @RequestBody WishRequestDto wishRequestDto,
            @LoginUser User user) {
        return new ResponseEntity<>(wishService.updateWish(user.getId(), wishRequestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteWish(
            @PathVariable Long productId,
            @LoginUser User user) {
        wishService.deleteWish(user.getId(), productId);
        return ResponseEntity.notFound().build();
    }
}
