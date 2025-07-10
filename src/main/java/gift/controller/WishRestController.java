package gift.controller;

import gift.config.JwtTokenProvider;
import gift.config.UnAuthorizationException;
import gift.domain.Product;
import gift.dto.CreateWishRequest;
import gift.dto.WishResponse;
import gift.service.WishService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishRestController {
    private final JwtTokenProvider jwtTokenProvider;
    private final WishService service;


    public WishRestController(JwtTokenProvider jwtTokenProvider, WishService service) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.service = service;
    }

    @GetMapping("/products")
    public HttpEntity<List<Product>> getProducts(@RequestHeader("Authorization") String authHeader) {
        if (!jwtTokenProvider.validateToken(authHeader)) {
            throw new UnAuthorizationException("인증되지 않은 사용자입니다.");
        }
        List<Product> productList = service.productList();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @PostMapping()
    public HttpEntity<Void> addWishList(@RequestBody CreateWishRequest request,
                                        @RequestHeader("Authorization") String authHeader) {
        if (!jwtTokenProvider.validateToken(authHeader)) {
            throw new UnAuthorizationException("인증되지 않은 사용자입니다.");
        }
        service.addWishProduct(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{memberId}")
    public HttpEntity<List<WishResponse>> getWishList(@RequestHeader("Authorization") String authHeader, @PathVariable Long memberId) {
        if (!jwtTokenProvider.validateToken(authHeader)) {
            throw new UnAuthorizationException("인증되지 않은 사용자입니다.");
        }
        List<WishResponse> wishList = service.getMeberWishList(memberId);
        return new ResponseEntity<>(wishList, HttpStatus.OK);
    }
}
