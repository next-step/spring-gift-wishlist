package gift.controller;

import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.entity.WishList;
import gift.service.JwtAuthService;
import gift.service.WishListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<WishList> getWishList(){
        //jwtAuthService.checkValidation();
        return null;
    }

    //TODO: 위시리스트에 상품을 추가
    @PostMapping("/wishList/{memberId}")
    public ResponseEntity<WishResponseDto> addToWishList(
            @RequestBody WishRequestDto wishRequestDto,
            @PathVariable Long memberId
    ){
        //jwtAuthService.checkValidation(); // 검증로직 확인
        WishResponseDto wishResponseDto = wishListService.addToWishList(memberId, wishRequestDto);
        return new ResponseEntity<>(wishResponseDto, HttpStatus.CREATED);
    }

    //TODO: 위시 리스트에 담긴 상품을 삭제

}
