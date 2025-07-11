package gift.controller;

import gift.dto.WishListCreateRequestDto;
import gift.dto.WishListResponseDto;
import gift.dto.WishListUpdateRequestDto;
import gift.service.WishListService;
import jakarta.servlet.http.HttpServletRequest;
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

  private Long getMemberIdFromRequest(HttpServletRequest request) {
    Object memberIdAttr = request.getAttribute("memberId");
    if (memberIdAttr == null) {
      throw new IllegalStateException("Member ID not found in request attributes");
    }
    return (Long) memberIdAttr;
  }

  @PostMapping
  public ResponseEntity<WishListResponseDto> addToWishList(
      HttpServletRequest request,
      @RequestBody @Valid WishListCreateRequestDto wishListCreateRequestDto) {

    Long memberId = getMemberIdFromRequest(request);
    WishListResponseDto response = wishListService.addToWishList(memberId, wishListCreateRequestDto);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<WishListResponseDto>> getWishList(HttpServletRequest request) {
    Long memberId = getMemberIdFromRequest(request);
    List<WishListResponseDto> responseList = wishListService.getWishList(memberId);
    return ResponseEntity.ok(responseList);
  }

  @PatchMapping
  public ResponseEntity<WishListResponseDto> updateQuantity(
      HttpServletRequest request,
      @RequestBody @Valid WishListUpdateRequestDto wishListUpdateRequestDto) {

    Long memberId = getMemberIdFromRequest(request);
    WishListResponseDto response = wishListService.updateQuantity(memberId, wishListUpdateRequestDto);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping
  public ResponseEntity<Void> removeFromWishList(
      HttpServletRequest request,
      @RequestParam("productId") Long productId) {

    Long memberId = getMemberIdFromRequest(request);
    wishListService.removeFromWishList(memberId, productId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/all")
  public ResponseEntity<Void> clearWishList(HttpServletRequest request) {
    Long memberId = getMemberIdFromRequest(request);
    wishListService.clearWishList(memberId);
    return ResponseEntity.noContent().build();
  }
}
