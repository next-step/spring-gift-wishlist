package gift.controller;

import gift.config.LoginMember;
import gift.dto.request.WishRequestDto;
import gift.dto.response.WishResponseDto;
import gift.entity.Member;
import gift.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<Void> addWish(@RequestBody WishRequestDto wishRequestDto, @LoginMember Member member) {
        wishService.addWish(member.getId(), wishRequestDto.getProductId(),wishRequestDto.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getWishes(@LoginMember Member member) {
        List<WishResponseDto> wishes = wishService.getWishes(member.getId());

        return ResponseEntity.status(HttpStatus.OK).body(wishes); // 200
    }
}
