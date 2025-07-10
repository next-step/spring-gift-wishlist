package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.CreateProductRequestDto;
import gift.dto.CreateWishRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.UpdateWishQuantityRequstDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.service.WishService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        return new ResponseEntity<>(wishService.createWish(requestDto, member.getId()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> findMemberWishes(@LoginMember Member member) {
        return new ResponseEntity<>(wishService.findMemberWishes(member.getId()), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<WishResponseDto> updateMemberWishQuantityByProductId(
            @Valid @RequestBody UpdateWishQuantityRequstDto requestDto,
            @LoginMember Member member) {

        if (requestDto.quantity().equals(0L)) {
            //wishService.deleteMemberWishByProductId(member.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(wishService.updateMemberWishQuantityByProductId(requestDto, member.getId()),
                HttpStatus.OK);
    }

}