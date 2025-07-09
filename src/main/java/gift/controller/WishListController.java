package gift.controller;

import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.service.JwtAuthService;
import gift.service.WishListService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WishListController {

    //precondition : 유효한 회원인지를 확인해야함
    private final JwtAuthService jwtAuthService;
    private final WishListService wishListService;

    public WishListController(JwtAuthService jwtAuthService, WishListService wishListService){
        this.jwtAuthService = jwtAuthService;
        this.wishListService = wishListService;
    }

    //TODO: WishList에 담긴 상품 목록을 조회
    @GetMapping("/wishList")
    public ResponseEntity<List<WishResponseDto>> getWishList(
            @RequestHeader(value = "Authorization") String token
    ){
        jwtAuthService.checkValidation(token);
        Long memberId = jwtAuthService.getMemberId(token);
        List<WishResponseDto> myWishList = wishListService.getList(memberId);
        return ResponseEntity.ok(myWishList);
    }

    //TODO: 위시리스트에 상품을 추가
    @PostMapping("/wishList")
    public ResponseEntity<WishResponseDto> addToWishList(
            @RequestBody WishRequestDto wishRequestDto, //상품ID, 수량
            @RequestHeader(value = "Authorization") String token
    ){
        jwtAuthService.checkValidation(token); // 검증로직 확인
        Long memberId = jwtAuthService.getMemberId(token);

        WishResponseDto wishResponseDto = wishListService.addToWishList(memberId, wishRequestDto);
        return new ResponseEntity<>(wishResponseDto, HttpStatus.CREATED);
    }

    //TODO: 위시 리스트에 담긴 상품을 삭제
    @DeleteMapping("/wishList/{wishListId}")
    public ResponseEntity<Void> removeWishList(
            @PathVariable Long wishListId,
            @RequestHeader(value = "Authorization") String token
    ){
        jwtAuthService.checkValidation(token);
        wishListService.removeFromWishList(wishListId);
        return ResponseEntity.noContent().build();
    }

    //동일한 상품을 추가하는 경우 (장바구니 내 물품 수량 조절)
    @PatchMapping("/wishList/add/{wishListId}")
    public ResponseEntity<List<WishResponseDto>> addItem(
            @PathVariable Long wishListId,
            @RequestHeader(value = "Authorization") String token
    ){
        jwtAuthService.checkValidation(token);
        Long memberId = jwtAuthService.getMemberId(token);

        System.out.println("memberId = " + memberId);

        List<WishResponseDto> myWishList = wishListService.changeQuantity(memberId, wishListId, 1);
        return ResponseEntity.ok(myWishList);
    }


    //동일한 상품을 제거하는 경우 (장바구니 내 물품 수량 조절)
    @PatchMapping("/wishList/subtract/{wishListId}")
    public ResponseEntity<List<WishResponseDto>> subtractItem(
            @PathVariable Long wishListId,
            @RequestHeader(value = "Authorization") String token
    ){
        System.out.println("wishListId = " + wishListId);
        jwtAuthService.checkValidation(token);
        Long memberId = jwtAuthService.getMemberId(token);

        System.out.println("memberId = " + memberId);

        List<WishResponseDto> myWishList = wishListService.changeQuantity(memberId, wishListId, -1);
        return ResponseEntity.ok(myWishList);
    }

}
