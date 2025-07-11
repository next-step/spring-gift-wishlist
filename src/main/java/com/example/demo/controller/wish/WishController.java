package com.example.demo.controller.wish;

import com.example.demo.dto.wish.WishRequestDto;
import com.example.demo.dto.wish.WishResponseDto;
import com.example.demo.entity.User;
import com.example.demo.service.wish.WishService;
import com.example.demo.validation.LoginMember;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wishes")
public class WishController {

  private final WishService wishService;

  public WishController(WishService wishService) {
    this.wishService = wishService;
  }


  @PostMapping
  public ResponseEntity<Void> addWish(@LoginMember User user, @RequestBody WishRequestDto dto) {
    wishService.saveWishProduct(user.getId(), dto.getProductId());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping
  public ResponseEntity<List<WishResponseDto>> getWishList(@LoginMember User user) {
    return ResponseEntity.ok(wishService.getWishProductList(user.getId()));
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<Void> deleteWish(@LoginMember User user, @PathVariable Long productId) {
    wishService.deleteWishProduct(user.getId(), productId);
    return ResponseEntity.noContent().build();
  }
}