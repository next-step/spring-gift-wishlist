package gift.controller;

import gift.auth.LoginMember;
import gift.domain.Member;
import gift.dto.SaveWishRequestDto;
import gift.dto.WishSummaryResponseDto;
import gift.service.WishListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishes")
public class WishListController {
    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    // 위시 리스트 상품 조회
    @GetMapping
    public ResponseEntity<List<WishSummaryResponseDto>> findAllByMemberId(@LoginMember Member member) {
        List<WishSummaryResponseDto> list = wishListService.findAllWishSummaryByMemberId(member.getId());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 위시 리스트 상품 등록
    @PostMapping
    public ResponseEntity<Void> saveWish(@Valid @RequestBody SaveWishRequestDto requestDto, @LoginMember Member member) {
        wishListService.saveWish(member.getId(), requestDto.productId());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
