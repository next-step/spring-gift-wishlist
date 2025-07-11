package gift.wish.controller;

import gift.wish.dto.WishRequestDto;
import gift.wish.dto.WishResponseDto;
import gift.wish.service.WishService;
import gift.exception.GlobalExceptionHandler.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/wishlists")
public class WishController {

    private final WishService wishService;
    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    //위시리스트 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<WishResponseDto>>> getWishlist(@RequestBody WishRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse<>(200,"조회에 성공했습니다", wishService.getWishlist(dto)));
    }

    //위시리스트 추가
    @PostMapping
    public ResponseEntity<ApiResponse<WishResponseDto>> addWish(@RequestBody WishRequestDto dto) {
        return ResponseEntity.ok(new ApiResponse<>(200,"추가에 성공했습니다", wishService.addWish(dto)));
    }

    //위시리스트 삭제
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteWish(@RequestBody WishRequestDto dto) {
        wishService.deleteWish(dto);
        return ResponseEntity.ok(new ApiResponse<>(200,"삭제에 성공했습니다", null));
    }

}
