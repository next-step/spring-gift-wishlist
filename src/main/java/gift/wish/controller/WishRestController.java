package gift.wish.controller;

import gift.security.LoginUser;
import gift.user.domain.User;
import gift.wish.dto.WishResponseDto;
import gift.wish.service.WishService;
import gift.wish.dto.WishRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WishRestController {

  private final WishService wishService;

  public WishRestController(WishService wishService) {
    this.wishService = wishService;
  }

  @PostMapping("/api/wishes")
  public ResponseEntity<WishResponseDto> createWish(
      @Valid @RequestBody WishRequestDto wishRequestDto,
      @LoginUser User user
  ) {
    WishResponseDto wishResponseDto = wishService.createWish(user.getId(), wishRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(wishResponseDto);
  }


}
