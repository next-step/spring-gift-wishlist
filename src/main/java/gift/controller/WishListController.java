package gift.controller;

import gift.LoginMember;
import gift.dto.WishListRequestDto;
import gift.dto.WishListResponseDto;
import gift.entity.Member;
import gift.service.WishListService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/wish-list")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    public ResponseEntity<List<WishListResponseDto>> getWishList(@LoginMember Member member) {
        List<WishListResponseDto> wishList = wishListService.getWishListByMemberId(member.getId());
        return ResponseEntity.ok(wishList);
    }

    @PostMapping
    public ResponseEntity<String> createWishList(@LoginMember Member member, @RequestBody WishListRequestDto wishListRequestDto) {
        wishListService.addWishList(member.getId(), wishListRequestDto);
        return ResponseEntity.ok("위시리스트에 상품이 담겼습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWishList(@LoginMember Member member, @PathVariable Long id) {
        wishListService.deleteWishList(member.getId(), id);
        return ResponseEntity.noContent().build();

    }
}
