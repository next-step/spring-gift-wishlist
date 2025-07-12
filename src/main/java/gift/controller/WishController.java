package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.CreateWishRequestDto;
import gift.dto.UpdateWishQuantityRequstDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.service.WishService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PostMapping
    public ResponseEntity<WishResponseDto> createWish(
            @RequestBody CreateWishRequestDto requestDto,
            @LoginMember Member member) {
        return new ResponseEntity<>(wishService.createWish(requestDto, member.getId()),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> findMemberWishes(@LoginMember Member member) {
        return new ResponseEntity<>(wishService.findMemberWishes(member.getId()), HttpStatus.OK);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<WishResponseDto> updateMemberWishQuantityByProductId(
            @Valid @RequestBody UpdateWishQuantityRequstDto requestDto,
            @PathVariable Long productId,
            @LoginMember Member member) {

        if (requestDto.quantity().equals(0L)) {
            wishService.deleteMemberWishByProductId(productId, member.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(
                wishService.updateMemberWishQuantityByProductId(requestDto.quantity(), productId,
                        member.getId()),
                HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteMemberWishByProductId(
            @PathVariable Long productId,
            @LoginMember Member member) {
        wishService.deleteMemberWishByProductId(productId, member.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}