package gift.wish.controller;

import gift.member.entity.Member;
import gift.wish.annotation.LoginMember;
import gift.wish.dto.WishCreateRequestDto;
import gift.wish.dto.WishCreateResponseDto;
import gift.wish.dto.WishGetRequestDto;
import gift.wish.dto.WishPageResponseDto;
import gift.wish.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // /api/wishes?page=0&size=10&sort=createdDate,desc
    // TODO: sort 추가 -> 테스트 필요
    @GetMapping
    public ResponseEntity<WishPageResponseDto> getWishes(@LoginMember Member member,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdDate,desc") String sort) {

        WishGetRequestDto wishGetRequestDto = new WishGetRequestDto(page, size, sort);

        return new ResponseEntity<>(wishService.getWishes(member, wishGetRequestDto),
            HttpStatus.OK);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(@LoginMember Member member, @PathVariable Long wishId) {
        wishService.deleteWish(member, wishId);
        return ResponseEntity.noContent().build();
    }

}

// TODO: 수량 변경 필요?