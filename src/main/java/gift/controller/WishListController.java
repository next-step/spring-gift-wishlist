package gift.controller;

import gift.dto.WishListCreateRequestDto;
import gift.dto.WishListResponseDto;
import gift.dto.WishListUpdateRequestDto;
import gift.security.LoginMemberId;
import gift.service.WishListService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

  private final WishListService wishListService;

  public WishListController(WishListService wishListService) {
    this.wishListService = wishListService;
  }

  @PostMapping
  public ResponseEntity<WishListResponseDto> addToWishList(
      @LoginMemberId Long memberId,
      @RequestBody @Valid WishListCreateRequestDto wishListCreateRequestDto) {
    WishListResponseDto response = wishListService.addToWishList(memberId, wishListCreateRequestDto);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<WishListResponseDto>> getWishList(@LoginMemberId Long memberId) {
    List<WishListResponseDto> responseList = wishListService.getWishList(memberId);
    return ResponseEntity.ok(responseList);
  }

  @PatchMapping
  public ResponseEntity<WishListResponseDto> updateQuantity(
      @LoginMemberId Long memberId,
      @RequestBody @Valid WishListUpdateRequestDto wishListUpdateRequestDto) {
    WishListResponseDto response = wishListService.updateQuantity(memberId, wishListUpdateRequestDto);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping
  public ResponseEntity<Void> removeFromWishList(
      @LoginMemberId Long memberId,
      @RequestParam("productId") Long productId) {
    wishListService.removeFromWishList(memberId, productId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/all")
  public ResponseEntity<Void> clearWishList(@LoginMemberId Long memberId) {
    wishListService.clearWishList(memberId);
    return ResponseEntity.noContent().build();
  }
}
