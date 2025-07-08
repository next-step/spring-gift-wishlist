package gift.controller;

import gift.entity.WishList;
import gift.service.JwtAuthService;
import gift.service.WishListService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
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
        jwtAuthService.checkValidation();

    }

    //TODO: 위시리스트에 상품을 추가

    //TODO: 위시 리스트에 담긴 상품을 삭제

}
