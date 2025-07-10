package gift.wish.controller;

import gift.member.entity.Member;
import gift.wish.annotation.LoginMember;
import gift.wish.dto.WishCreateRequestDto;
import gift.wish.dto.WishCreateResponseDto;
import gift.wish.service.WishService;
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

    @PostMapping
    public ResponseEntity<WishCreateResponseDto> addWish(@LoginMember Member member,
        @RequestBody WishCreateRequestDto wishCreateRequestDto) {

        return new ResponseEntity<>(wishService.addWish(member, wishCreateRequestDto),
            HttpStatus.CREATED);
    }
//
//    @GetMapping
//    public ResponseEntity<> getWishes() {
//
//    }
//
//    @DeleteMapping("/{wishId}")
//    public ResponseEntity<Void> deleteWish(@PathVariable Long wishId) {
//
//        return ResponseEntity.noContent().build();
//    }

}
