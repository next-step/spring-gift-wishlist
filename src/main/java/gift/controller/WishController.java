package gift.controller;

import gift.annotation.UserValid;
import gift.dto.UserInfoRequestDto;
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
    public ResponseEntity<List<WishResponseDto>> findUserWishes(@UserValid UserInfoRequestDto userInfoRequestDto) {
        System.out.println(userInfoRequestDto.id());
        return ResponseEntity.ok(wishService.findUserWishes(userInfoRequestDto));
    }

    @PostMapping("/add")
    public ResponseEntity<WishResponseDto> addWish(@UserValid UserInfoRequestDto userInfoRequestDto, @RequestBody WishRequestDto wishRequestDto) {
        return new ResponseEntity<>(wishService.addWish(userInfoRequestDto, wishRequestDto), HttpStatus.CREATED);
    }

    @PatchMapping("/patch")
    public ResponseEntity<Void> updateWish(@UserValid UserInfoRequestDto userInfoRequestDto, @RequestBody WishRequestDto wishrequestDto) {
        wishService.updateWish(userInfoRequestDto, wishrequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteWish(@UserValid UserInfoRequestDto userInfoRequestDto, @RequestBody WishRequestDto wishRequestDto) {
        wishService.deleteWish(userInfoRequestDto, wishRequestDto);
        return ResponseEntity.noContent().build();
    }
}
