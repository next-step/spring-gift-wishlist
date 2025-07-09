package gift.controller.view;

import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.service.AuthServiceJWTandCookie;
import gift.service.JwtAuthService;
import gift.service.WishListService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/view")
@Controller
public class WishListViewController {

    //precondition : 유효한 회원인지를 확인해야함
    private final AuthServiceJWTandCookie authServiceJWTandCookie;
    private final WishListService wishListService;

    public WishListViewController(AuthServiceJWTandCookie authServiceJWTandCookie, WishListService wishListService){
        this.authServiceJWTandCookie = authServiceJWTandCookie;
        this.wishListService = wishListService;
    }

    //WishList에 담긴 상품 목록을 조회
    @GetMapping("/wishlist")
    public String getWishList(
            @CookieValue String token,
            Model model
    ){
        authServiceJWTandCookie.checkValidation(token);
        Long memberId = authServiceJWTandCookie.getMemberId(token);
        List<WishResponseDto> myWishList = wishListService.getList(memberId);
        model.addAttribute("wishlist", myWishList);
        return "/view/wishlist";
    }

    //위시리스트에 상품을 추가
    @PostMapping("/wishlist")
    public String addToWishList(
            @ModelAttribute @Valid WishRequestDto wishRequestDto,//상품ID, 수량
            BindingResult bindingResult,
            @CookieValue String token
    ){
        authServiceJWTandCookie.checkValidation(token); // 검증로직 확인
        if(bindingResult.hasErrors()){
            return "redirect:/view/products/list";
        }
        Long memberId = authServiceJWTandCookie.getMemberId(token);
        wishListService.addToWishList(memberId, wishRequestDto);
        return "redirect:/view/wishlist";
    }

    //위시 리스트에 담긴 상품을 삭제
    @PostMapping("/wishlist/delete/{wishListId}")
    public String removeWishList(
            @PathVariable Long wishListId,
            @CookieValue String token
    ){
        authServiceJWTandCookie.checkValidation(token);
        wishListService.removeFromWishList(wishListId);
        return "redirect:/view/wishlist";
    }

    //동일한 상품을 추가하는 경우 (장바구니 내 물품 수량 조절 + )
    @PostMapping("/wishlist/add/{wishListId}")
    public String addItem(
            @PathVariable Long wishListId,
            @CookieValue String token
    ){
        authServiceJWTandCookie.checkValidation(token);
        Long memberId = authServiceJWTandCookie.getMemberId(token);
        wishListService.changeQuantity(memberId, wishListId, 1);
        return "redirect:/view/wishlist";
    }


    //동일한 상품을 제거하는 경우 (장바구니 내 물품 수량 조절 -)
    @PostMapping("/wishlist/subtract/{wishListId}")
    public String subtractItem(
            @PathVariable Long wishListId,
            @CookieValue String token
    ){
        authServiceJWTandCookie.checkValidation(token);
        Long memberId = authServiceJWTandCookie.getMemberId(token);
        wishListService.changeQuantity(memberId, wishListId, -1);
        return "redirect:/view/wishlist";
    }

    //결제창 이동
    @GetMapping("/payment")
    public String toPay(){
        return "view/pay";
    }

}
