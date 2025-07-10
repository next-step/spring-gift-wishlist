package gift.controller;

import gift.auth.LoginMember;
import gift.domain.Member;
import gift.dto.WishSummaryResponseDto;
import gift.service.WishListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wishes")
public class WishListController {
    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    public ResponseEntity<List<WishSummaryResponseDto>> findAllByMemberId(@LoginMember Member member) {
        List<WishSummaryResponseDto> list = wishListService.findAllWishSummaryByMemberId(member.getId());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
